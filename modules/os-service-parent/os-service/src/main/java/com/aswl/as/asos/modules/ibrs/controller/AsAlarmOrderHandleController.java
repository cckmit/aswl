package com.aswl.as.asos.modules.ibrs.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.ibrs.entity.AsAlarmOrderHandle;
import com.aswl.as.asos.modules.ibrs.service.IAsAlarmOrderHandleService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 派单处理工单设置 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-03-23
 */
@RestController

@RequestMapping("/ibrs/as-alarm-order-handle")
@Api("派单处理工单设置")
public class AsAlarmOrderHandleController extends AbstractController {

    @Autowired
    IAsAlarmOrderHandleService iAsAlarmOrderHandleService;

    /**
    * 派单处理工单设置分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:role:list")
    @ApiOperation("派单处理工单设置分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsAlarmOrderHandleService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 派单处理工单设置列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:role:list")
    @ApiOperation("派单处理工单设置列表")
    public R findList(@RequestBody AsAlarmOrderHandle entity){
        List<AsAlarmOrderHandle> list= iAsAlarmOrderHandleService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 派单处理工单设置信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:role:info")
    @ApiOperation("派单处理工单设置信息")
    public R info(@PathVariable("entityId") String entityId){

        AsAlarmOrderHandle data = iAsAlarmOrderHandleService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存派单处理工单设置")
    @PostMapping("/save")
    @RequiresPermissions("os:role:save")
    @ApiOperation("保存派单处理工单设置")
    public R save(@RequestBody AsAlarmOrderHandle entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsAlarmOrderHandleService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新派单处理工单设置")
    @PostMapping("/update")
    @RequiresPermissions("os:role:update")
    @ApiOperation("更新派单处理工单设置")
    public R update(@RequestBody AsAlarmOrderHandle entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(LocalDateTime.now());
        iAsAlarmOrderHandleService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除派单处理工单设置
    */
    @SysLog("删除派单处理工单设置")
    @PostMapping("/delete")
    @RequiresPermissions("os:role:delete")
    @ApiOperation("删除派单处理工单设置")
    public R delete(@RequestBody String[] ids){
        iAsAlarmOrderHandleService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsAlarmOrderHandle e)
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
