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
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroupModel;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcStatusGroupModelService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 事件状态组-设备型号 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@RestController

@RequestMapping("/ibrs/asEventUcStatusGroupModel")
@Api("事件状态组-设备型号")
public class AsEventUcStatusGroupModelController extends AbstractController {

    @Autowired
    IAsEventUcStatusGroupModelService iAsEventUcStatusGroupModelService;

    //TODO 还没设置权限

    /**
    * 事件状态组-设备型号分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("事件状态组-设备型号分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsEventUcStatusGroupModelService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 事件状态组-设备型号列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("事件状态组-设备型号列表")
    public R findList(@RequestBody AsEventUcStatusGroupModel entity){
        List<AsEventUcStatusGroupModel> list= iAsEventUcStatusGroupModelService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 事件状态组-设备型号信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceModel:info")
    @ApiOperation("事件状态组-设备型号信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsEventUcStatusGroupModel data = iAsEventUcStatusGroupModelService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存事件状态组-设备型号")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceModel:save")
    @ApiOperation("保存事件状态组-设备型号")
    public R save(@RequestBody AsEventUcStatusGroupModel entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreateBy(String.valueOf(getUserId()));
        entity.setCreateName(getUserName());
        entity.setCreateDate(LocalDateTime.now());
        iAsEventUcStatusGroupModelService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新事件状态组-设备型号")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceModel:update")
    @ApiOperation("更新事件状态组-设备型号")
    public R update(@RequestBody AsEventUcStatusGroupModel entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setUpdateBy(String.valueOf(getUserId()));
        entity.setUpdateName(getUserName());
        entity.setUpdateDate(LocalDateTime.now());
        iAsEventUcStatusGroupModelService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除事件状态组-设备型号
    */
    @SysLog("删除事件状态组-设备型号")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceModel:delete")
    @ApiOperation("删除事件状态组-设备型号")
    public R delete(@RequestBody String[] ids){
        iAsEventUcStatusGroupModelService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsEventUcStatusGroupModel e)
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
