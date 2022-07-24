package com.aswl.as.asos.modules.asuser.controller;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.aswl.as.asos.modules.asuser.entity.SysTenantRole;
import com.aswl.as.asos.modules.asuser.service.ISysTenantRoleMenuService;
import com.aswl.as.asos.modules.asuser.service.ISysTenantRoleService;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.user.api.module.Menu;
import com.aswl.as.user.api.module.Role;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author df
 * @date 2020/10/19 11:24
 */

@RestController
@RequestMapping("/tenant/sys-tenant_role")
@Api(tags = "租户角色表")
public class SysTenantRoleController extends AbstractController {

    @Autowired
    ISysTenantRoleService iSysTenantRoleService;
    @Autowired
    ISysTenantRoleMenuService iSysTenantRoleMenuService;

    /**
     * 租户角色列表
     */
    @GetMapping("/list")
    @ApiOperation("租户角色列表")
    public R list(@RequestParam Map<String, Object> params) {

        PageUtils page = iSysTenantRoleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 根据ID查询
     */
    @GetMapping("/info/{entityId}")
    @ApiOperation("根据ID查询")
    public R info(@PathVariable("entityId") String entityId) {

        SysTenantRole sysTenantRole = iSysTenantRoleService.getEntityById(entityId);
        return R.ok().put("sysTenantRole", sysTenantRole);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @ApiOperation("保存租户角色信息表")
    public R save(@RequestBody SysTenantRole entity) {
        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iSysTenantRoleService.saveEntity(entity);
        return R.ok("保存租户角色成功");
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @ApiOperation("更新租户角色信息表")
    public R update(@RequestBody SysTenantRole entity) {
        iSysTenantRoleService.updateEntityById(entity);
        return R.ok();
    }

    /**
     * 删除租户角色信息表
     */
    @PostMapping("/delete")
    @ApiOperation("删除租户角色信息表")
    public R delete(@RequestBody String[] ids) {
        iSysTenantRoleService.deleteEntityByIds(ids);
        return R.ok();

    }


    /**
     * 更新角色菜单
     *
     * @param entity
     * @return
     */
    @PutMapping("roleMenuUpdate")
    @ApiOperation(value = "更新角色菜单信息", notes = "更新角色菜单信息")
    public ResponseBean<Boolean> updateTenantRoleMenu(@RequestBody SysTenantRole entity) {
        boolean success = false;
        String menuIds = entity.getMenuIds();
        if (StringUtils.isNotBlank(entity.getId())) {
            // 保存角色菜单关系
            if (entity != null && StringUtils.isNotBlank(menuIds))
                success = iSysTenantRoleMenuService.saveRoleMenus(entity.getId(), Stream.of(menuIds.split(",")).collect(Collectors.toList())) > 0;
        }
        return new ResponseBean<>(success);
    }


    /**
     * 根据角色查找菜单
     *
     * @param roleId
     * @return 属性集合
     */
    @GetMapping("roleTree/{roleId}")
    @ApiOperation(value = "根据租户角色查找菜单", notes = "根据租户角色code获取角色菜单")
    public ResponseBean<List<String>> roleTree(@PathVariable String roleId) {
        // 根据角色查找菜单
        Stream<AsUserSysMenu> menuStream = iSysTenantRoleService.findMenuByRoleId(roleId).stream();
        // 获取菜单ID
        if (Optional.ofNullable(menuStream).isPresent())
            return new ResponseBean<>(menuStream.map(AsUserSysMenu::getId).collect(Collectors.toList()));
        return new ResponseBean<>(new ArrayList<>());
    }

}
