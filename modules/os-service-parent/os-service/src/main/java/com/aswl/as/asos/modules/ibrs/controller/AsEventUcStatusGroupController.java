package com.aswl.as.asos.modules.ibrs.controller;


import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadata;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroupModel;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusOperation;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.ibrs.api.vo.EventUcMetadataVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
//import com.aswl.as.ibrs.api.vo.eventUcStatusGroupModelVo;
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
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcStatusGroup;
import com.aswl.as.asos.modules.ibrs.service.IAsEventUcStatusGroupService;

import org.springframework.web.bind.annotation.RestController;
import com.aswl.as.asos.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 事件状态组 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@RestController

@RequestMapping("/ibrs/asEventUcStatusGroup")
@Api("事件状态组")
public class AsEventUcStatusGroupController extends AbstractController {

    @Autowired
    IAsEventUcStatusGroupService iAsEventUcStatusGroupService;

    /**
    * 事件状态组分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("事件状态组分页列表")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page=iAsEventUcStatusGroupService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 事件状态组列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:deviceModel:list")
    @ApiOperation("事件状态组列表")
    public R findList(@RequestBody AsEventUcStatusGroup entity){
        List<AsEventUcStatusGroup> list= iAsEventUcStatusGroupService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 事件状态组信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:deviceModel:info")
    @ApiOperation("事件状态组信息")
    public R info(@PathVariable("entityId") Long entityId){

        AsEventUcStatusGroup data = iAsEventUcStatusGroupService.getEntityById(String.valueOf(entityId));
        return R.ok().put("data", data);
    }

    /**
    * 保存
    */
    @SysLog("保存事件状态组")
    @PostMapping("/save")
    @RequiresPermissions("os:deviceModel:save")
    @ApiOperation("保存事件状态组")
    public R save(@RequestBody AsEventUcStatusGroup entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsEventUcStatusGroupService.saveEntity(entity);

        return R.ok();
    }

    /**
    * 更新
    */
    @SysLog("更新事件状态组")
    @PostMapping("/update")
    @RequiresPermissions("os:deviceModel:update")
    @ApiOperation("更新事件状态组")
    public R update(@RequestBody AsEventUcStatusGroup entity){
        //数据校验
        String vStr=verifyForm(entity);
        if(vStr!=null)
        {
            return R.error(vStr);
        }

        iAsEventUcStatusGroupService.updateEntityById(entity);

        return R.ok();
    }

    /**
    * 删除事件状态组
    */
    @SysLog("删除事件状态组")
    @PostMapping("/delete")
    @RequiresPermissions("os:deviceModel:delete")
    @ApiOperation("删除事件状态组")
    public R delete(@RequestBody String[] ids){
        iAsEventUcStatusGroupService.deleteEntityByIds(ids);
        return R.ok();
    }


    private String verifyForm(AsEventUcStatusGroup e)
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

    //TODO
    @ApiOperation(value = "根据设备型号ID已选择操作列表", notes = "根据设备型号ID已选择操作列表")
    @GetMapping("osExtendSelectStatusGroupOperationList")
    public R osExtendSelectStatusGroupOperationList(@RequestParam("eventMetadataModelId") String eventMetadataModelId) {
        return R.ok().put("data",iAsEventUcStatusGroupService.osGetselectExtendStatusGroupOperationList(eventMetadataModelId));
    }

    /**
     *根据元数据ID查询状态操作列表
     * @param eventMetadataId
     * @return
     */
    @ApiOperation(value = "根据元数据ID查询状态操作列表", notes = "根据元数据ID查询状态操作列表")
//    @SysLog("根据元数据ID查询状态操作列表")
    @GetMapping("extendStatusGroupOperationList")
    public ResponseBean<List<AsEventUcStatusOperation>> osEvent1(@RequestParam("eventMetadataId") String eventMetadataId)
    {
        // 修改成从本地数据库查 extendStatusGroupOperationList
        return iAsEventUcStatusGroupService.osEvent1(eventMetadataId);
    }

    /**
     * 根据设备型号ID查询时间状态组数据
     * @param deviceModelId
     * @return
     */
    @ApiOperation(value = "根据设备型号ID查询时间状态组数据", notes = "根据设备型号ID查询时间状态组数据")
//    @SysLog("根据设备型号ID查询时间状态组数据")
    @ApiImplicitParam(name = "deviceModelId", value = "型号ID", dataType = "String")
    @GetMapping("extendStatusGroup")
    public ResponseBean<List<AsEventUcStatusGroupModel>> getExtendStatusGroup(@RequestParam("deviceModelId") String deviceModelId) {

        // 修改成本地数据库查 getExtendStatusGroup

        return  iAsEventUcStatusGroupService.osEvent2(deviceModelId);
    }

    @ApiOperation(value = "根据设备型号ID和状态组ID查询事件状态组属性", notes = "根据设备型号ID查询事件状态组属性")
//    @SysLog("根据设备型号ID和状态组ID查询事件状态组属性")
    @GetMapping("extendStatusGroupAttribute")
    public ResponseBean<List<AsEventUcMetadata>> osEvent3(@RequestParam("deviceModelId") String deviceModelId, @RequestParam("groupId") String groupId) {

        // 改为从本地数据库查 getExtendStatusGroupAttribute 因为sql应该不会变动

        return  iAsEventUcStatusGroupService.osEvent3(deviceModelId,groupId);
    }

}
