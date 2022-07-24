package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysUserAuths;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysUserAuthsService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 用户授权表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
//@RestController
//@RequestMapping("/asuser/sys-user-auths")
//@Api("用户授权表")
public class AsUserSysUserAuthsController extends AbstractController {

    @Autowired
    IAsUserSysUserAuthsService iAsUserSysUserAuthsService;

    /**
    * 用户授权表列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:sysUserAuths:list")
    @ApiOperation("用户授权列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= iAsUserSysUserAuthsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * 用户授权表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:sysUserAuths:info")
    @ApiOperation("用户授权表信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsUserSysUserAuths role = iAsUserSysUserAuthsService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存用户授权表")
    @PostMapping("/save")
    @RequiresPermissions("os:sysUserAuths:save")
    @ApiOperation("保存用户授权表")
    public R save(@RequestBody AsUserSysUserAuths entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iAsUserSysUserAuthsService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新用户授权表")
    @PostMapping("/update")
    @RequiresPermissions("os:sysUserAuths:save")
    @ApiOperation("更新用户授权表")
    public R update(@RequestBody AsUserSysUserAuths entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setUpdaterId(getUserId().toString());
//        entity.setUpdateAt(LocalDateTime.now());
        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        iAsUserSysUserAuthsService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除用户授权表
    */
    @SysLog("删除用户授权表")
    @PostMapping("/delete")
    @RequiresPermissions("os:sysUserAuths:delete")
    @ApiOperation("删除用户授权表")
    public R delete(@RequestBody String[] ids){
        iAsUserSysUserAuthsService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsUserSysUserAuths e)
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
