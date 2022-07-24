package com.aswl.as.asos.modules.ibrs.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceKind;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceKindService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备种类 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@RestController

@RequestMapping("/ibrs/as-device-kind")
@Api("设备种类")
public class AsDeviceKindController extends AbstractController {

    @Autowired
    IAsDeviceKindService iAsDeviceKindService;

    /**
    * 设备种类分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceKind:list")
    @ApiOperation("设备种类分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsDeviceKindService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 设备种类列表
     */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceKind:list")
    @ApiOperation("设备种类列表")
    public R findList(@RequestBody AsDeviceKind entity){

        List<AsDeviceKind> list= iAsDeviceKindService.findList(entity);
        return R.ok().put("list",list);

    }

    /**
    * 设备种类信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceKind:info")
    @ApiOperation("设备种类信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsDeviceKind data = iAsDeviceKindService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存设备种类")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceKind:save")
    @ApiOperation("保存设备种类")
    public R save(@RequestBody AsDeviceKind entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iAsDeviceKindService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新设备种类")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceKind:update")
    @ApiOperation("更新设备种类")
    public R update(@RequestBody AsDeviceKind entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        iAsDeviceKindService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除设备种类
    */
    @SysLog("删除设备种类")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceKind:delete")
    @ApiOperation("删除设备种类")
    public R delete(@RequestBody String[] ids){
        iAsDeviceKindService.deleteEntityByIds(ids);
        return R.ok();
    }

    private String verifyForm(AsDeviceKind e)
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
