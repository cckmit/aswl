package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.common.utils.LRUMap;
import com.aswl.as.asos.common.utils.OsGlobalData;
import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceKind;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModel;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceKindService;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceType;
import com.aswl.as.asos.modules.ibrs.service.IAsDeviceTypeService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
@RestController

@RequestMapping("/ibrs/as-device-type")
@Api("设备类型")
public class AsDeviceTypeController extends AbstractController {

    @Autowired
    IAsDeviceTypeService iAsDeviceTypeService;

    @Autowired
    IAsDeviceKindService iAsDeviceKindService;

    /**
    * 设备类型分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceType:list")
    @ApiOperation("设备类型分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsDeviceTypeService.queryPage(params);

//        List<AsDeviceType> list=(List<AsDeviceType>)page.getList();
//        for(AsDeviceType type: list)
//        {
//            type.setDeviceKindName(getNameByKindId(type.getDeviceKindId()));
//        }

        return R.ok().put("page", page);
    }

    /**
     * 设备型号列表
     */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceType:list")
    @ApiOperation("设备类型列表")
    public R findList(@RequestBody AsDeviceType entity){
        List<AsDeviceType> list= iAsDeviceTypeService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 设备类型信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceType:info")
    @ApiOperation("设备类型信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsDeviceType data = iAsDeviceTypeService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存设备类型")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceType:save")
    @ApiOperation("保存设备类型")
    public R save(@RequestBody AsDeviceType entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setCreatorId(getUserId().toString());
//        entity.setCreateAt(LocalDateTime.now());
        entity.setCreator(getUserName());
        entity.setCreateDate(new Date());
        iAsDeviceTypeService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新设备类型")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceType:update")
    @ApiOperation("更新设备类型")
    public R update(@RequestBody AsDeviceType entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

//        entity.setUpdaterId(getUserId().toString());
//        entity.setUpdateAt(LocalDateTime.now());
        entity.setModifier(getUserName());
        entity.setModifyDate(new Date());
        iAsDeviceTypeService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除设备类型
    */
    @SysLog("删除设备类型")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceType:delete")
    @ApiOperation("删除设备类型")
    public R delete(@RequestBody String[] ids){
        iAsDeviceTypeService.deleteEntityByIds(ids);
        return R.ok();
    }

    /**
     * 更新
     */
//    @SysLog("根据设备类型查询设备类型是否存在")
    @PostMapping("/checkDeviceType")
    @RequiresPermissions("os:deviceType:info")
    @ApiOperation("根据设备类型查询设备类型是否存在")
    public R checkDeviceType(@RequestBody AsDeviceType entity){
        return R.ok().put("hasDeviceType",iAsDeviceTypeService.checkDeviceType(entity));
    }

    private String verifyForm(AsDeviceType e)
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

//    private String getNameByKindId(String deviceKindId)
//    {
//        LRUMap<String, String> map= OsGlobalData.COMMON_KIND_NAME_MAP;
//
//        if(!org.apache.commons.lang.StringUtils.isEmpty(deviceKindId))
//        {
//            if(!map.containsKey(deviceKindId))
//            {
//                //到数据库查对应的资料，放入 COMMON_TENANT_NAME_MAP
//                AsDeviceKind kind=iAsDeviceKindService.getEntityById(deviceKindId);
//                if(kind==null)
//                {
//                    return null;
//                }
//                map.put(kind.getId(),kind.getDeviceKindName());
//            }
//            return (String)map.get(deviceKindId);
//        }
//        return null;
//    }


}
