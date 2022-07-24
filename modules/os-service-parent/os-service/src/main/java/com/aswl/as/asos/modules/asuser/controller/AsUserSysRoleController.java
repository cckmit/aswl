package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysRole;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysRoleService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
//@RestController
//@RequestMapping("/asuser/sys-role")
//@Api("asUser角色表")
public class AsUserSysRoleController extends AbstractController {

    @Autowired
    IAsUserSysRoleService iAsUserSysRoleService;

    /**
    * 角色表列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:sysRole:list")
    @ApiOperation("角色列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= iAsUserSysRoleService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * 角色表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:sysRole:info")
    @ApiOperation("角色表信息")
    public R info(@PathVariable("entityId") Long entityId){
        AsUserSysRole role = iAsUserSysRoleService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存角色表")
    @PostMapping("/save")
    @RequiresPermissions("os:sysRole:save")
    @ApiOperation("保存角色表")
    public R save(@RequestBody AsUserSysRole entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iAsUserSysRoleService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新角色表")
    @PostMapping("/update")
    @RequiresPermissions("os:sysRole:save")
    @ApiOperation("更新角色表")
    public R update(@RequestBody AsUserSysRole entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        iAsUserSysRoleService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除角色表
    */
    @SysLog("删除角色表")
    @PostMapping("/delete")
    @RequiresPermissions("os:sysRole:delete")
    @ApiOperation("删除角色表")
    public R delete(@RequestBody String[] ids){
        iAsUserSysRoleService.deleteEntityByIds(ids);
        return R.ok();
    }

    private String verifyForm(AsUserSysRole e)
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
