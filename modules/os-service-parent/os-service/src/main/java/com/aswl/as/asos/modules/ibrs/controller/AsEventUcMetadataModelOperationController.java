package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.dto.OsMetadataModelOperationDto;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.ibrs.api.dto.metadataModelOperationDto;
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
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadataModelOperation;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcMetadataModelOperationService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 设备型号事件元数据-状态操作 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
@RestController

@RequestMapping("/ibrs/asEventUcMetadataModelOperation")
@Api("设备型号事件元数据-状态操作")
public class AsEventUcMetadataModelOperationController extends AbstractController {

    @Autowired
    IAsEventUcMetadataModelOperationService iAsEventUcMetadataModelOperationService;

    /**
    * 设备型号事件元数据-状态操作分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("设备型号事件元数据-状态操作分页列表")
    public R list(@RequestParam Map<String, Object> params){

        /*
        //如果不是超级管理员，则只查询自己创建的角色列表
        if(getUserId() != Constant.SUPER_ADMIN){
             params.put("createUserId", getUserId());
        }
        */

        PageUtils page=iAsEventUcMetadataModelOperationService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 设备型号事件元数据-状态操作列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("设备型号事件元数据-状态操作列表")
    public R findList(@RequestBody AsEventUcMetadataModelOperation entity){
        List<AsEventUcMetadataModelOperation> list= iAsEventUcMetadataModelOperationService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 设备型号事件元数据-状态操作信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceModel:info")
    @ApiOperation("设备型号事件元数据-状态操作信息")
    public R info(@PathVariable("entityId") String entityId){

        AsEventUcMetadataModelOperation data = iAsEventUcMetadataModelOperationService.getEntityById(entityId);
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存设备型号事件元数据-状态操作")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceModel:save")
    @ApiOperation("保存设备型号事件元数据-状态操作")
    public R save(@RequestBody AsEventUcMetadataModelOperation entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsEventUcMetadataModelOperationService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新设备型号事件元数据-状态操作")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceModel:update")
    @ApiOperation("更新设备型号事件元数据-状态操作")
    public R update(@RequestBody AsEventUcMetadataModelOperation entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsEventUcMetadataModelOperationService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除设备型号事件元数据-状态操作
    */
    @SysLog("删除设备型号事件元数据-状态操作")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceModel:delete")
    @ApiOperation("删除设备型号事件元数据-状态操作")
    public R delete(@RequestBody String[] ids){
        iAsEventUcMetadataModelOperationService.deleteEntityByIds(ids);
        return R.ok();
    }


    /**
     * 修改状态操作列表
     * @param dto 集合
     * @return ResponseBean
     */

    @ApiOperation(value = "修改扩展状态组属性操作列表", notes = "修改扩展状态组属性操作列表")
    @ApiImplicitParam(name = "list", value = "状态操作列表集合", dataType = "list")
    @PostMapping("updateExtendModelAttributeOperation")
    public R updateExtendModelAttributeOperation(@RequestBody OsMetadataModelOperationDto dto) {
        // 先删除原数据
        iAsEventUcMetadataModelOperationService.osDeleteByEventMetadataModelId(dto.getEventMetadataModelId());

        if (dto.getModelOperationDtoList()==null || dto.getModelOperationDtoList().size()==0){
            return R.ok();
        }else {
            // 新增数据
            if(iAsEventUcMetadataModelOperationService.osInsertBatch(dto.getModelOperationDtoList()) > 0)
            {
                return R.ok();
            }
            else
            {
                return R.error();
            }
        }
    }

    private String verifyForm(AsEventUcMetadataModelOperation e)
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
