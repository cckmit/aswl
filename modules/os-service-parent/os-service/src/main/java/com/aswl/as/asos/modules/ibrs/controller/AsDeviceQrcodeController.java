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
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceQrcode;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceQrcodeService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 设备二维码 前端控制器
 * </p>
 *
 * @author df
 * @since 2020-12-16
 */
@RestController

@RequestMapping("/ibrs/as-device-qrcode")
@Api("设备二维码")
public class AsDeviceQrcodeController extends AbstractController {

    @Autowired
    IAsDeviceQrcodeService iAsDeviceQrcodeService;

    /**
    * 设备二维码分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:role:list")
    @ApiOperation("设备二维码分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsDeviceQrcodeService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 设备二维码列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:role:list")
    @ApiOperation("设备二维码列表")
    public R findList(@RequestBody AsDeviceQrcode entity){
        List<AsDeviceQrcode> list= iAsDeviceQrcodeService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 设备二维码信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:role:info")
    @ApiOperation("设备二维码信息")
    public R info(@PathVariable("entityId") String entityId){

        AsDeviceQrcode data = iAsDeviceQrcodeService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存设备二维码")
    @PostMapping("/save")
    @RequiresPermissions("os:role:save")
    @ApiOperation("保存设备二维码")
    public R save(@RequestBody AsDeviceQrcode entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }
        iAsDeviceQrcodeService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新设备二维码")
    @PostMapping("/update")
    @RequiresPermissions("os:role:update")
    @ApiOperation("更新设备二维码")
    public R update(@RequestBody AsDeviceQrcode entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }
        iAsDeviceQrcodeService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除设备二维码
    */
    @SysLog("删除设备二维码")
    @PostMapping("/delete")
    @RequiresPermissions("os:role:delete")
    @ApiOperation("删除设备二维码")
    public R delete(@RequestBody String[] ids){
        iAsDeviceQrcodeService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsDeviceQrcode e)
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
