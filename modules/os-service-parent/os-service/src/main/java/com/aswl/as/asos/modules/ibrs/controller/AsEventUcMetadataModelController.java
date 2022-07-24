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
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadataModel;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcMetadataModelService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 事件元数据-设备型号 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@RestController

@RequestMapping("/ibrs/asEventUcMetadataModel")
@Api("事件元数据-设备型号")
public class AsEventUcMetadataModelController extends AbstractController {

    @Autowired
    IAsEventUcMetadataModelService iAsEventUcMetadataModelService;

    //TODO 权限还没控制，到时候要看看什么权限
    /**
    * 事件元数据-设备型号分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceType:list")
    @ApiOperation("事件元数据-设备型号分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsEventUcMetadataModelService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 事件元数据-设备型号列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceType:list")
    @ApiOperation("事件元数据-设备型号列表")
    public R findList(@RequestBody AsEventUcMetadataModel entity){
        List<AsEventUcMetadataModel> list= iAsEventUcMetadataModelService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 事件元数据-设备型号信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceType:info")
    @ApiOperation("事件元数据-设备型号信息")
    public R info(@PathVariable("entityId") Long entityId){
        AsEventUcMetadataModel data = iAsEventUcMetadataModelService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存事件元数据-设备型号")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceType:save")
    @ApiOperation("保存事件元数据-设备型号")
    public R save(@RequestBody AsEventUcMetadataModel entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreateName(getUserName());
        entity.setCreateBy(String.valueOf(getUserId()));
        entity.setCreateDate(LocalDateTime.now());
        iAsEventUcMetadataModelService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新事件元数据-设备型号")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceType:update")
    @ApiOperation("更新事件元数据-设备型号")
    public R update(@RequestBody AsEventUcMetadataModel entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setUpdateBy(String.valueOf(getUserId()));
        entity.setUpdateName(getUserName());
        entity.setUpdateDate(LocalDateTime.now());
        iAsEventUcMetadataModelService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除事件元数据-设备型号
    */
    @SysLog("删除事件元数据-设备型号")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceType:delete")
    @ApiOperation("删除事件元数据-设备型号")
    public R delete(@RequestBody String[] ids){
        iAsEventUcMetadataModelService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsEventUcMetadataModel e)
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
