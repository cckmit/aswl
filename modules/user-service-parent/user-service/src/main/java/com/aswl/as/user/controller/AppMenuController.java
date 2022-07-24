package com.aswl.as.user.controller;

import cn.hutool.core.collection.CollUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.properties.SysProperties;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.core.vo.MenuVo;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.common.security.utils.SecurityUtil;
import com.aswl.as.user.api.constant.MenuConstant;
import com.aswl.as.user.api.dto.AppMenuDto;
import com.aswl.as.user.api.dto.MenuDto;
import com.aswl.as.user.api.module.AppMenu;
import com.aswl.as.user.api.module.Menu;
import com.aswl.as.user.api.module.Role;
import com.aswl.as.user.service.AppMenuService;
import com.aswl.as.user.service.MenuService;
import com.aswl.as.user.utils.MenuUtil;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 菜单controller
 *
 * @author aswl.com
 * @date 2018/8/26 22:48
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/appMenu", tags = "APP菜单信息管理")
@RestController
@RequestMapping("/v1/appMenu")
public class AppMenuController extends BaseController {

    private final AppMenuService appMenuService;

    private final SysProperties sysProperties;

    /**
     * 返回当前用户的树形菜单集合
     *
     * @return 当前用户的树形菜单
     */
    @GetMapping(value = "userAppMenu")
    @ApiOperation(value = "获取当前用户的APP菜单列表")
    public ResponseBean<List<AppMenuDto>> userAppMenu() {
        List<AppMenuDto> menuDtoList = new ArrayList<>();
        String tenantCode = SysUtil.getTenantCode(), identifier = SysUtil.getUser();
        List<AppMenu> userMenus;
        // 超级管理员
        if (identifier.equals(sysProperties.getAdminUser())) {
            // 获取角色的菜单
            AppMenu menu = new AppMenu();
            menu.setTenantCode(tenantCode);
            menu.setApplicationCode(SysUtil.getSysCode());
            menu.setType(MenuConstant.MENU_TYPE_MENU);
            userMenus = appMenuService.findAllList(menu);
        } else {
            List<Role> roleList = SecurityUtil.getCurrentAuthentication().getAuthorities().stream()
                    // 按角色过滤
                    .filter(authority -> authority.getAuthority() != null && authority.getAuthority().startsWith("role_"))
                    .map(authority -> {
                        Role role = new Role();
                        role.setRoleCode(authority.getAuthority());
                        return role;
                    }).collect(Collectors.toList());
            // 根据角色code批量查找菜单
            userMenus = appMenuService.findAppMenuByRoleList(roleList, tenantCode);
        }
        if (CollectionUtils.isNotEmpty(userMenus)) {
            userMenus.stream()
                    // 菜单类型
                    .filter(menu -> MenuConstant.MENU_TYPE_MENU.equals(menu.getType()))
                    // dto封装
                    .map(AppMenuDto::new)
                    // 去重
                    .distinct()
                    .forEach(menuDtoList::add);
            // 排序、构建树形关系
            return new ResponseBean<>(TreeUtil.buildTree(CollUtil.sort(menuDtoList, Comparator.comparingInt(AppMenuDto::getSort)), "-1"));
        }
        return new ResponseBean<>(Lists.newArrayList());
    }

    /**
     * 返回树形App菜单集合
     *
     * @return 树形菜单集合
     */
    @GetMapping(value = "appMenus")
    @ApiOperation(value = "获取树形APP菜单列表")
    public ResponseBean<List<AppMenuDto>> appMenus() {
        // 查询所有菜单
        AppMenu condition = new AppMenu();
        condition.setApplicationCode(SysUtil.getSysCode());
        condition.setTenantCode(SysUtil.getTenantCode());
        Stream<AppMenu> menuStream = appMenuService.findAllList(condition).stream();
        if (Optional.ofNullable(menuStream).isPresent()) {
            // 转成MenuDto
            List<AppMenuDto> menuDtoList = menuStream.map(AppMenuDto::new).collect(Collectors.toList());
            // 排序、构建树形关系
            return new ResponseBean<>(TreeUtil.buildTree(CollUtil.sort(menuDtoList, Comparator.comparingInt(AppMenuDto::getSort)), "-1"));
        }
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 新增App菜单
     *
     * @param appMenu appMenu
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/8/27 16:12
     */
    @PostMapping
    @PreAuthorize("hasAuthority('sys:menu:add') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "创建APP菜单", notes = "创建APP菜单")
    @ApiImplicitParam(name = "appMenu", value = "角色实体menu", required = true, dataType = "appMenu")
    @Log(value = "新增APP菜单",businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> addAppMenu(@RequestBody @Valid AppMenu appMenu) {
        appMenu.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(appMenuService.insert(appMenu) > 0);
    }

    /**
     * 更新App菜单
     *
     * @param appMenu appMenu
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/10/24 16:34
     */
    @PutMapping
    @PreAuthorize("hasAuthority('sys:menu:edit') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "更新APP菜单信息", notes = "根据菜单id更新APP菜单的基本信息")
    @ApiImplicitParam(name = "appMenu", value = "角色实体menu", required = true, dataType = "AppMenu")
    @Log(value = "更新APP菜单",businessType = BusinessType.UPDATE)
    public ResponseBean<Boolean> updateAppMenu(@RequestBody @Valid AppMenu appMenu) {
        appMenu.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(appMenuService.update(appMenu) > 0);
    }

    /**
     * 根据id删除
     *
     * @param id id
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/8/27 16:19
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('sys:menu:del') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "删除APP菜单", notes = "根据ID删除APP菜单")
    @ApiImplicitParam(name = "id", value = "APP菜单ID", required = true, paramType = "path")
    @Log(value = "删除APP菜单",businessType = BusinessType.DELETE)
    public ResponseBean<Boolean> deleteAppMenu(@PathVariable String id) {
        AppMenu menu = new AppMenu();
        menu.setId(id);
        return new ResponseBean<>(appMenuService.delete(menu) > 0);
    }

    /**
     * 根据id查询菜单
     *
     * @param id
     * @return Menu
     * @author aswl.com
     * @date 2018/8/27 16:11
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取APP菜单信息", notes = "根据APP菜单id获取APP菜单详细信息")
    @ApiImplicitParam(name = "id", value = "APP菜单ID", required = true, dataType = "String", paramType = "path")
    public ResponseBean<AppMenu> menu(@PathVariable String id) {
        AppMenu menu = new AppMenu();
        menu.setId(id);
        return new ResponseBean<>(appMenuService.get(menu));
    }

    /**
     * 获取菜单分页列表
     *
     * @param pageNum  pageNum
     * @param pageSize pageSize
     * @param sort     sort
     * @param order    order
     * @param appMenu     appMenu
     * @return PageInfo
     * @author aswl.com
     * @date 2018/8/26 23:17
     */
    @GetMapping("appMenuList")
    @ApiOperation(value = "获取APP菜单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "appMenu", value = "APP菜单信息", dataType = "AppMenu")
    })
    public ResponseBean<PageInfo<AppMenu>> appMenuList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                 AppMenu appMenu) {
        // 租户标识过滤条件
        appMenu.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>( appMenuService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), appMenu));
    }

    /**
     * 根据角色查找菜单
     *
     * @param role       角色标识
     * @param tenantCode 租户标识
     * @return List
     * @author aswl.com
     * @date 2018/8/27 15:58
     */
    @GetMapping("findAppMenuByRole/{role}")
    @ApiOperation(value = "根据角色查找APP菜单", notes = "根据角色id获取角色APP菜单")
    @ApiImplicitParam(name = "role", value = "角色名称", required = true, dataType = "String", paramType = "path")
    public ResponseBean<List<AppMenu>> findAppMenuByRole(@PathVariable String role, @RequestParam @NotBlank String tenantCode) {
        return new ResponseBean<>(appMenuService.findMenuByRole(role, tenantCode));
    }

    /**
     * 查询所有菜单
     *
     * @param tenantCode 租户标识
     * @return List
     * @author aswl.com
     * @date 2019/04/26 11:50
     */
    @GetMapping("findAppAllMenu")
    @ApiOperation(value = "查询所有APP菜单", notes = "查询所有APP菜单")
    public ResponseBean<List<AppMenu>> findAppAllMenu(@RequestParam @NotBlank String tenantCode) {
        AppMenu menu = new AppMenu();
        menu.setTenantCode(tenantCode);
        menu.setApplicationCode(SysUtil.getSysCode());
        return new ResponseBean<>(appMenuService.findAllList(menu));
    }

    /**
     * 根据角色查找菜单
     *
     * @param roleCode 角色code
     * @return 属性集合
     */
    @GetMapping("roleTree/{roleCode}")
    @ApiOperation(value = "根据角色查找APP菜单", notes = "根据角色code获取角色APP菜单")
    @ApiImplicitParam(name = "roleCode", value = "角色code", required = true, dataType = "String", paramType = "path")
    public ResponseBean<List<String>> roleTree(@PathVariable String roleCode) {
        // 根据角色查找菜单
        Stream<AppMenu> menuStream = appMenuService.findMenuByRole(roleCode, SysUtil.getTenantCode()).stream();
        // 获取菜单ID
        if (Optional.ofNullable(menuStream).isPresent())
            return new ResponseBean<>(menuStream.map(AppMenu::getId).collect(Collectors.toList()));
        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 导出菜单
     *
     * @param menuVo menuVo
     * @author aswl.com
     * @date 2018/11/28 12:46
     */
    /*
    @PostMapping("export")
    @PreAuthorize("hasAuthority('sys:menu:export') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "导出菜单", notes = "根据菜单id导出菜单")
    @ApiImplicitParam(name = "menuVo", value = "菜单信息", required = true, dataType = "MenuVo")
    @Log(value = "导出菜单",businessType = BusinessType.EXPORT)
    public void exportMenu(@RequestBody MenuVo menuVo, HttpServletRequest request, HttpServletResponse response) {
        String tenantCode = SysUtil.getTenantCode();
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "菜单信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<AppMenu> menus;
            // 导出当前租户下的所有菜单
            if (StringUtils.isEmpty(menuVo.getIdString())) {
                AppMenu menu = new AppMenu();
                menu.setTenantCode(tenantCode);
                menus = appMenuService.findList(menu);
            } else {    // 导出选中
                AppMenu menu = new AppMenu();
                // 按逗号切割ID，流处理获取ID集合，去重，转成字符串数组
                menu.setIds(Stream.of(menuVo.getIdString().split(",")).filter(StringUtils::isNotBlank).distinct().toArray(String[]::new));
                menus = appMenuService.findListById(menu);
            }
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(menus), MenuUtil.getMenuMap());
        } catch (Exception e) {
            log.error("导出菜单数据失败！", e);
        }
    }
    */

    /**
     * 导入数据
     *
     * @param file file
     * @return ResponseBean
     * @author aswl.com
     * @date 2018/11/28 12:51
     */
    /*
    @PostMapping("import")
    @PreAuthorize("hasAuthority('sys:menu:import') or hasAnyRole('" + SecurityConstant.ROLE_ADMIN + "')")
    @ApiOperation(value = "导入菜单", notes = "导入菜单")
    @Log("导入菜单")
    public ResponseBean<Boolean> importMenu(@ApiParam(value = "要上传的文件", required = true) MultipartFile file, HttpServletRequest request) {
        try {
            log.debug("开始导入菜单数据");
            Stream<AppMenu> menuStream = MapUtil.map2Java(AppMenu.class,
                    ExcelToolUtil.importExcel(file.getInputStream(), MenuUtil.getMenuMap())).stream();
            if (Optional.ofNullable(menuStream).isPresent()) {
                menuStream.forEach(menu -> {
                    if (appMenuService.update(menu) < 1)
                        appMenuService.insert(menu);
                });
            }
            return new ResponseBean<>(Boolean.TRUE);
        } catch (Exception e) {
            log.error("导入菜单数据失败！", e);
        }
        return new ResponseBean<>(Boolean.FALSE);
    }
    */
}
