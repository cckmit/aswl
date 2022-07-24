package com.aswl.as.asos.modules.ibrs.controller;


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
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadata;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcMetadataService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 事件元数据 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@RestController

@RequestMapping("/ibrs/asEventUcMetadata")
@Api("事件元数据")
public class AsEventUcMetadataController extends AbstractController {

    @Autowired
    IAsEventUcMetadataService iAsEventUcMetadataService;

    /**
    * 事件元数据分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("事件元数据分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsEventUcMetadataService.queryPage(params);

        return R.ok().put("page", page);
    }



    /**
    * 事件元数据列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("事件元数据列表")
    public R findList(@RequestBody AsEventUcMetadata entity){
        List<AsEventUcMetadata> list= iAsEventUcMetadataService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 事件元数据信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceModel:info")
    @ApiOperation("事件元数据信息")
    public R info(@PathVariable("entityId") Long entityId){
        AsEventUcMetadata data = iAsEventUcMetadataService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存事件元数据")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceModel:save")
    @ApiOperation("保存事件元数据")
    public R save(@RequestBody AsEventUcMetadata entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreateBy(String.valueOf(getUserId()));
        entity.setCreateName(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsEventUcMetadataService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新事件元数据")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceModel:update")
    @ApiOperation("更新事件元数据")
    public R update(@RequestBody AsEventUcMetadata entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setUpdateBy(String.valueOf(getUserId()));
        entity.setUpdateName(getUserName());
        entity.setUpdateDate(LocalDateTime.now());
        iAsEventUcMetadataService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除事件元数据
    */
    @SysLog("删除事件元数据")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceModel:delete")
    @ApiOperation("删除事件元数据")
    public R delete(@RequestBody String[] ids){
        iAsEventUcMetadataService.deleteEntityByIds(ids);
        return R.ok();
    }

    //TODO 提供一个接口给前端查询


    private String verifyForm(AsEventUcMetadata e)
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


    /**
     * 根据设备型号ID获取元数据
     * @param id 型号ID
     * @return ResponseBean
     */

    @ApiOperation(value = "根据设备型号ID获取元数据", notes = "根据设备型号ID获取元数据")
    @ApiImplicitParam(name = "id", value = "型号ID", dataType = "String")
    @GetMapping("findUcMetadataByDeviceModelId")
    public R findEventUcMetadataByDeviceModelId(@RequestParam("id") String id) {
        return R.ok().put("data",iAsEventUcMetadataService.osFindEventUcMetadataByDeviceModelId(id));
    }


}
