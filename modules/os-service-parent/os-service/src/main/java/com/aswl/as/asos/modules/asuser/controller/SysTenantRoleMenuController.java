package com.aswl.as.asos.modules.asuser.controller;

import cn.hutool.core.collection.CollUtil;
import com.aswl.as.common.core.enums.BusinessType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.TreeUtil;
import com.aswl.as.user.api.dto.MenuDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.Menu;
import com.aswl.as.user.api.module.Role;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import com.aswl.as.asos.modules.asuser.entity.SysTenantRoleMenu;
import com.aswl.as.asos.modules.asuser.service.ISysTenantRoleMenuService;
import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 租户角色菜单表 前端控制器
 * </p>
 *
 * @author df
 * @since 2020-11-19
 */
@RestController

@RequestMapping("/asuser/sys-tenant-role-menu")
@Api(tags ="租户角色菜单表" )
public class SysTenantRoleMenuController extends AbstractController {

    @Autowired
    ISysTenantRoleMenuService iSysTenantRoleMenuService;
    @Autowired
    UserServiceClient userServiceClient;

    /**
     * 租户角色菜单表分页列表
     */
    @GetMapping("/list")
    @ApiOperation("租户角色菜单表分页列表")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = iSysTenantRoleMenuService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 租户角色菜单表列表
     */
    @PostMapping("/findList")
    @ApiOperation("租户角色菜单表列表")
    public R findList(@RequestBody SysTenantRoleMenu entity) {
        List<SysTenantRoleMenu> list = iSysTenantRoleMenuService.findList(entity);
        return R.ok().put("list", list);
    }

    /**
     * 租户角色菜单表信息
     */
    @GetMapping("/info/{entityId}")
    @ApiOperation("租户角色菜单表信息")
    public R info(@PathVariable("entityId") String entityId) {

        SysTenantRoleMenu data = iSysTenantRoleMenuService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
     * 保存
     */
    @SysLog("保存租户角色菜单表")
    @PostMapping("/save")
    @ApiOperation("保存租户角色菜单表")
    public R save(@RequestBody SysTenantRoleMenu entity) {
        iSysTenantRoleMenuService.saveEntity(entity);
        return R.ok();
    }

    /**
     * 更新
     */
    @SysLog("更新租户角色菜单表")
    @PostMapping("/update")
    @ApiOperation("更新租户角色菜单表")
    public R update(@RequestBody SysTenantRoleMenu entity) {
        iSysTenantRoleMenuService.updateEntityById(entity);

        return R.ok();
    }

    /**
     * 删除租户角色菜单表
     */
    @SysLog("删除租户角色菜单表")
    @PostMapping("/delete")
    @ApiOperation("删除租户角色菜单表")
    public R delete(@RequestBody String[] ids) {
        iSysTenantRoleMenuService.deleteEntityByIds(ids);
        return R.ok();
    }

    /**
     * 获取树形菜单
     */
    @SysLog("获取树形菜单")
    @GetMapping("/tree")
    @ApiOperation("获取树形菜单")
    public ResponseBean<List<MenuDto>> getTree() {
        // 获取基础数据
        String tenantCode ="osDemo";
        ResponseBean<List<Menu>> menus = userServiceClient.findAllMenu(tenantCode);
        if (menus.getStatus() == 200) {
            if (Optional.ofNullable(menus.getData().stream()).isPresent()) {
                // 转成MenuDto
                List<MenuDto> menuDtoList = menus.getData().stream().map(MenuDto::new).collect(Collectors.toList());
                // 排序、构建树形关系
                return new ResponseBean<>(TreeUtil.buildTree(CollUtil.sort(menuDtoList, Comparator.comparingInt(MenuDto::getSort)), "-1"));
            }
            return new ResponseBean<>(new ArrayList<>());
        }
        return null;
    }
}
