package com.aswl.as.asos.modules.sys.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.sys.entity.SysBlackListEntity;
import com.aswl.as.asos.modules.sys.service.SysBlackListService;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 系统IP黑名单 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@RestController

@RequestMapping("/sys/black-list")
@Api("系统IP黑名单")
public class SysBlackListController extends AbstractController {

    @Autowired
    SysBlackListService sysBlackListService;

    /**
    * 系统IP黑名单分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:blackList:list")
    @ApiOperation("系统IP黑名单分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= sysBlackListService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 系统IP黑名单列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:blackList:list")
    @ApiOperation("系统IP黑名单列表")
    public R findList(@RequestBody SysBlackListEntity entity){
        List<SysBlackListEntity> list= sysBlackListService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 系统IP黑名单信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:blackList:info")
    @ApiOperation("系统IP黑名单信息")
    public R info(@PathVariable("entityId") Long entityId){

        SysBlackListEntity role = sysBlackListService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存系统IP黑名单")
    @PostMapping("/save")
    @RequiresPermissions("os:blackList:save")
    @ApiOperation("保存系统IP黑名单")
    public R save(@RequestBody SysBlackListEntity entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreateName(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        sysBlackListService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新系统IP黑名单")
    @PostMapping("/update")
    @RequiresPermissions("os:blackList:update")
    @ApiOperation("更新系统IP黑名单")
    public R update(@RequestBody SysBlackListEntity entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        sysBlackListService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除系统IP黑名单
    */
    @SysLog("删除系统IP黑名单")
    @PostMapping("/delete")
    @RequiresPermissions("os:blackList:delete")
    @ApiOperation("删除系统IP黑名单")
    public R delete(@RequestBody String[] ids){
        sysBlackListService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(SysBlackListEntity e)
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
