package com.aswl.as.asos.modules.asuser.controller;


import com.aswl.as.asos.common.utils.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import com.aswl.as.asos.modules.asuser.entity.SysTenantLog;
import com.aswl.as.asos.modules.asuser.service.ISysTenantLogService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * SAAS租户信息记录表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-02
 */
@RestController

@RequestMapping("/asuser/sys-tenant-log")
@Api("SAAS租户信息记录表")
public class SysTenantLogController extends AbstractController {

    @Autowired
    ISysTenantLogService iSysTenantLogService;

    /**
    * SAAS租户信息记录表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:tenant:list")
    @ApiOperation("SAAS租户信息记录表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iSysTenantLogService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * SAAS租户信息记录表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:tenant:list")
    @ApiOperation("SAAS租户信息记录表列表")
    public R findList(@RequestBody SysTenantLog entity){
        List<SysTenantLog> list= iSysTenantLogService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * SAAS租户信息记录表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:tenant:info")
    @ApiOperation("SAAS租户信息记录表信息")
    public R info(@PathVariable("entityId") String entityId){

        SysTenantLog data = iSysTenantLogService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存SAAS租户信息记录表")
    @PostMapping("/save")
    @RequiresPermissions("os:tenant:save")
    @ApiOperation("保存SAAS租户信息记录表")
    public R save(@RequestBody SysTenantLog entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
        iSysTenantLogService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新SAAS租户信息记录表")
    @PostMapping("/update")
    @RequiresPermissions("os:tenant:update")
    @ApiOperation("更新SAAS租户信息记录表")
    public R update(@RequestBody SysTenantLog entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setModifier(getUserName());
//        entity.setModifyDate(LocalDateTime.now());
        iSysTenantLogService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除SAAS租户信息记录表
    */
    @SysLog("删除SAAS租户信息记录表")
    @PostMapping("/delete")
    @RequiresPermissions("os:tenant:delete")
    @ApiOperation("删除SAAS租户信息记录表")
    public R delete(@RequestBody String[] ids){
        iSysTenantLogService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(SysTenantLog e)
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
