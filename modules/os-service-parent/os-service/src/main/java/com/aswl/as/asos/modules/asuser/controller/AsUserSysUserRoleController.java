package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysUserRole;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysUserRoleService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * <p>
 * 用户角色表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
//@RestController
//@RequestMapping("/asuser/sys-user-role")
//@Api("用户角色表")
public class AsUserSysUserRoleController extends AbstractController {

    @Autowired
    IAsUserSysUserRoleService iAsUserSysUserRoleService;

    /**
    * 用户角色表列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:sysUserRole:list")
    @ApiOperation("用户角色列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= iAsUserSysUserRoleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * 用户角色表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:sysUserRole:info")
    @ApiOperation("用户角色表信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsUserSysUserRole role = iAsUserSysUserRoleService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存用户角色表")
    @PostMapping("/save")
    @RequiresPermissions("os:sysUserRole:save")
    @ApiOperation("保存用户角色表")
    public R save(@RequestBody AsUserSysUserRole entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsUserSysUserRoleService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新用户角色表")
    @PostMapping("/update")
    @RequiresPermissions("os:sysUserRole:save")
    @ApiOperation("更新用户角色表")
    public R update(@RequestBody AsUserSysUserRole entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsUserSysUserRoleService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除用户角色表
    */
    @SysLog("删除用户角色表")
    @PostMapping("/delete")
    @RequiresPermissions("os:sysUserRole:delete")
    @ApiOperation("删除用户角色表")
    public R delete(@RequestBody String[] ids){
        iAsUserSysUserRoleService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsUserSysUserRole e)
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
