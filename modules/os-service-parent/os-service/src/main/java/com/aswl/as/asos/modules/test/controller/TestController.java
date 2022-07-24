package com.aswl.as.asos.modules.test.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.test.entity.Test;
import com.aswl.as.asos.modules.test.service.ITestService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * <p>
 * 测试表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
@RestController

@RequestMapping("/test/test")
@Api("测试表")
public class TestController extends AbstractController {

    @Autowired
    ITestService iTestService;

    /**
    * 测试表列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:test:list")
    @ApiOperation("测试列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iTestService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
    * 测试表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:test:info")
    @ApiOperation("测试表信息")
    public R info(@PathVariable("entityId") Long entityId){

        Test role = iTestService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存测试表")
    @PostMapping("/save")
    @RequiresPermissions("os:test:save")
    @ApiOperation("保存测试表")
    public R save(@RequestBody Test entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setCreatorId(getUserId().toString());
//        entity.setCreateAt(LocalDateTime.now());
        iTestService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新测试表")
    @PostMapping("/update")
    @RequiresPermissions("os:test:save")
    @ApiOperation("更新测试表")
    public R update(@RequestBody Test entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setUpdaterId(getUserId().toString());
//        entity.setUpdateAt(LocalDateTime.now());
        iTestService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除测试表
    */
    @SysLog("删除测试表")
    @PostMapping("/delete")
    @RequiresPermissions("os:test:delete")
    @ApiOperation("删除测试表")
    public R delete(@RequestBody String[] ids){
        iTestService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(Test e)
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
