package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysRoleMenu;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysRoleMenuService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * <p>
 * 角色权限表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
//@RestController
//@RequestMapping("/asuser/sys-role-menu")
//@Api("角色权限表")
public class AsUserSysRoleMenuController extends AbstractController {

    @Autowired
    IAsUserSysRoleMenuService iAsUserSysRoleMenuService;

    /**
    * 角色权限表列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:sysRoleMenu:list")
    @ApiOperation("角色权限列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= iAsUserSysRoleMenuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * 角色权限表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:sysRoleMenu:info")
    @ApiOperation("角色权限表信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsUserSysRoleMenu role = iAsUserSysRoleMenuService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存角色权限表")
    @PostMapping("/save")
    @RequiresPermissions("os:sysRoleMenu:save")
    @ApiOperation("保存角色权限表")
    public R save(@RequestBody AsUserSysRoleMenu entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsUserSysRoleMenuService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新角色权限表")
    @PostMapping("/update")
    @RequiresPermissions("os:sysRoleMenu:save")
    @ApiOperation("更新角色权限表")
    public R update(@RequestBody AsUserSysRoleMenu entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsUserSysRoleMenuService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除角色权限表
    */
    @SysLog("删除角色权限表")
    @PostMapping("/delete")
    @RequiresPermissions("os:sysRoleMenu:delete")
    @ApiOperation("删除角色权限表")
    public R delete(@RequestBody String[] ids){
        iAsUserSysRoleMenuService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsUserSysRoleMenu e)
    {

        try
        {
            //表单校验
            ValidatorUtils.validateEntity(e);
        }
        catch (Exception tempException)
        {
            return tempException.getMessage();
        }

        return null;
    }


}
