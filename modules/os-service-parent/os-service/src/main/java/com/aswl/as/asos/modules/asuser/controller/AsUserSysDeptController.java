package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysDept;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysDeptService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
//@RestController
//@RequestMapping("/asuser/sys-dept")
//@Api("部门表")
public class AsUserSysDeptController extends AbstractController {

    @Autowired
    IAsUserSysDeptService iAsUserSysDeptService;

    /**
    * 部门表列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:sysDept:list")
    @ApiOperation("部门表列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= iAsUserSysDeptService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * 部门表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:sysDept:info")
//    @ApiOperation("部门表信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsUserSysDept role = iAsUserSysDeptService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存部门表")
    @PostMapping("/save")
    @RequiresPermissions("os:sysDept:save")
    @ApiOperation("保存部门表")
    public R save(@RequestBody AsUserSysDept entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iAsUserSysDeptService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新部门表")
    @PostMapping("/update")
    @RequiresPermissions("os:sysDept:save")
    @ApiOperation("更新部门表")
    public R update(@RequestBody AsUserSysDept entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        iAsUserSysDeptService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除部门表
    */
    @SysLog("删除部门表")
    @PostMapping("/delete")
    @RequiresPermissions("os:sysDept:delete")
    @ApiOperation("删除部门表")
    public R delete(@RequestBody String[] ids){
        iAsUserSysDeptService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsUserSysDept e)
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
