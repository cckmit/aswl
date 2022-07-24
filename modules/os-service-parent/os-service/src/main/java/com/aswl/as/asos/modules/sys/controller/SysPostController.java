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
import com.aswl.as.asos.modules.sys.entity.SysPostEntity;
import com.aswl.as.asos.modules.sys.service.SysPostService;

import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 工作岗位 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@RestController

@RequestMapping("/sys/post")
@Api("工作岗位")
public class SysPostController extends AbstractController {

    @Autowired
    SysPostService sysPostService;

    /**
    * 工作岗位分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:post:list")
    @ApiOperation("工作岗位分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page= sysPostService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 工作岗位列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:post:list")
    @ApiOperation("工作岗位列表")
    public R findList(@RequestBody SysPostEntity entity){
        List<SysPostEntity> list= sysPostService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 工作岗位信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:post:info")
    @ApiOperation("工作岗位信息")
    public R info(@PathVariable("entityId") Long entityId){

        SysPostEntity role = sysPostService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
    * 保存
    */
    @SysLog("保存工作岗位")
    @PostMapping("/save")
    @RequiresPermissions("os:post:save")
    @ApiOperation("保存工作岗位")
    public R save(@RequestBody SysPostEntity entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        sysPostService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新工作岗位")
    @PostMapping("/update")
    @RequiresPermissions("os:post:update")
    @ApiOperation("更新工作岗位")
    public R update(@RequestBody SysPostEntity entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        sysPostService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除工作岗位
    */
    @SysLog("删除工作岗位")
    @PostMapping("/delete")
    @RequiresPermissions("os:post:delete")
    @ApiOperation("删除工作岗位")
    public R delete(@RequestBody String[] ids){
        sysPostService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(SysPostEntity e)
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
