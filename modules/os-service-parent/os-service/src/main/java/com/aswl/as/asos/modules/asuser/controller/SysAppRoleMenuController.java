package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.asuser.entity.SysAppRoleMenu;
import com.aswl.as.asos.modules.asuser.service.ISysAppRoleMenuService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * APP角色权限表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-25
 */
@RestController

@RequestMapping("/asuser/sys-app-role-menu")
@Api("APP角色权限表")
public class SysAppRoleMenuController extends AbstractController {

    @Autowired
    ISysAppRoleMenuService iSysAppRoleMenuService;

    /**
    * APP角色权限表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:role:list")
    @ApiOperation("APP角色权限表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iSysAppRoleMenuService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * APP角色权限表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:role:list")
    @ApiOperation("APP角色权限表列表")
    public R findList(@RequestBody SysAppRoleMenu entity){
        List<SysAppRoleMenu> list= iSysAppRoleMenuService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * APP角色权限表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:role:info")
    @ApiOperation("APP角色权限表信息")
    public R info(@PathVariable("entityId") String entityId){

        SysAppRoleMenu data = iSysAppRoleMenuService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存APP角色权限表")
    @PostMapping("/save")
    @RequiresPermissions("os:role:save")
    @ApiOperation("保存APP角色权限表")
    public R save(@RequestBody SysAppRoleMenu entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iSysAppRoleMenuService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新APP角色权限表")
    @PostMapping("/update")
    @RequiresPermissions("os:role:update")
    @ApiOperation("更新APP角色权限表")
    public R update(@RequestBody SysAppRoleMenu entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iSysAppRoleMenuService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除APP角色权限表
    */
    @SysLog("删除APP角色权限表")
    @PostMapping("/delete")
    @RequiresPermissions("os:role:delete")
    @ApiOperation("删除APP角色权限表")
    public R delete(@RequestBody String[] ids){
        iSysAppRoleMenuService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(SysAppRoleMenu e)
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
