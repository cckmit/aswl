package com.aswl.as.asos.modules.asuser.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysPosition;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysPositionService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 职位表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
//@RestController
//@RequestMapping("/asuser/sys-position")
//@Api("职位表")
public class AsUserSysPositionController extends AbstractController {

    @Autowired
    IAsUserSysPositionService iAsUserSysPositionService;

    /**
    * 职位表列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:sysPosition:list")
    @ApiOperation("职位列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= iAsUserSysPositionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * 职位表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:sysPosition:info")
    @ApiOperation("职位表信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsUserSysPosition role = iAsUserSysPositionService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存职位表")
    @PostMapping("/save")
    @RequiresPermissions("os:sysPosition:save")
    @ApiOperation("保存职位表")
    public R save(@RequestBody AsUserSysPosition entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iAsUserSysPositionService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新职位表")
    @PostMapping("/update")
    @RequiresPermissions("os:sysPosition:save")
    @ApiOperation("更新职位表")
    public R update(@RequestBody AsUserSysPosition entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        iAsUserSysPositionService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除职位表
    */
    @SysLog("删除职位表")
    @PostMapping("/delete")
    @RequiresPermissions("os:sysPosition:delete")
    @ApiOperation("删除职位表")
    public R delete(@RequestBody String[] ids){
        iAsUserSysPositionService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsUserSysPosition e)
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
