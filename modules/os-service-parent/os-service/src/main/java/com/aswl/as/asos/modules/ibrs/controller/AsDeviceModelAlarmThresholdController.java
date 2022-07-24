package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.dto.DeviceModelAlarmThresholdDto;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModelAlarmThreshold;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceModelAlarmThresholdService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 设备型号区间报警 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@RestController
@RequestMapping("/ibrs/asDeviceModelAlarmThreshold")
@Api("设备型号区间报警")
public class AsDeviceModelAlarmThresholdController extends AbstractController {

    @Autowired
    IAsDeviceModelAlarmThresholdService iAsDeviceModelAlarmThresholdService;

    /**
    * 设备型号区间报警分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("设备型号区间报警分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsDeviceModelAlarmThresholdService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 设备型号区间报警列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("设备型号区间报警列表")
    public R findList(@RequestBody AsDeviceModelAlarmThreshold entity){
        List<AsDeviceModelAlarmThreshold> list= iAsDeviceModelAlarmThresholdService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 设备型号区间报警信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceModel:info")
    @ApiOperation("设备型号区间报警信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsDeviceModelAlarmThreshold data = iAsDeviceModelAlarmThresholdService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存设备型号区间报警")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceModel:save")
    @ApiOperation("保存设备型号区间报警")
    public R save(@RequestBody AsDeviceModelAlarmThreshold entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsDeviceModelAlarmThresholdService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新设备型号区间报警")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceModel:update")
    @ApiOperation("更新设备型号区间报警")
    public R update(@RequestBody AsDeviceModelAlarmThreshold entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsDeviceModelAlarmThresholdService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除设备型号区间报警
    */
    @SysLog("删除设备型号区间报警")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceModel:delete")
    @ApiOperation("删除设备型号区间报警")
    public R delete(@RequestBody String[] ids){
        iAsDeviceModelAlarmThresholdService.deleteEntityByIds(ids);
        return R.ok();
    }


    @ApiOperation(value = "批量新增设备型号区间报警数据", notes = "批量新增设备型号区间报警数据")
    @ApiImplicitParam(name = "list", value = "区间报警列表集合", dataType = "list")
    @RequiresPermissions("os:deviceModel:save")
    @PostMapping("insertBath")
    public R insertBatch(@RequestBody List<AsDeviceModelAlarmThreshold> list) {

        //批量插入
        try
        {
            iAsDeviceModelAlarmThresholdService.insertBatch(list);
        }
        catch (Exception e)
        {
            return R.error(e.getMessage());
        }

        return R.ok();
    }

    private String verifyForm(AsDeviceModelAlarmThreshold e)
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
