package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceModelDto;
import com.aswl.as.ibrs.api.module.DeviceModel;
import com.aswl.as.ibrs.api.module.DeviceType;
import com.aswl.as.ibrs.api.module.EventUcMetadataModel;
import com.aswl.as.ibrs.api.vo.DeviceModelVo;
import com.aswl.as.ibrs.api.vo.EventUcMetadataVo;
import com.aswl.as.ibrs.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 设备型号controller
 *
 * @author dingfei
 * @date 2019-09-26 15:22
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceModel", tags = "设备型号")
@RestController
@RequestMapping("/v1/deviceModel")
public class DeviceModelController extends BaseController {
    private final DeviceModelService deviceModelService;
    private final EventUcMetadataModelService eventUcMetadataModelService;
    private final EventUcMetadataModelOperationService eventUcMetadataModelOperationService;
    private final DeviceModelAlarmThresholdService deviceModelAlarmThresholdService;
    private final EventUcStatusGroupModelService eventUcStatusGroupModelService;
    

    /**
     * 根据ID获取设备型号
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据设备型号ID查询设备型号")
    public ResponseBean<DeviceModel> findById(@PathVariable("id") String id) {
        DeviceModel deviceModel = new DeviceModel();
        deviceModel.setId(id);
        return new ResponseBean<>(deviceModelService.get(deviceModel));
    }

    /**
     * 查询所有设备型号
     *
     * @param deviceModel
     * @return ResponseBean
     */
    @GetMapping(value = "deviceModels")
    @ApiOperation(value = "查询所有设备型号列表")
    public ResponseBean
            <List<DeviceModel>> findAll(DeviceModel deviceModel) {
        deviceModel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelService.findAllList(deviceModel));
    }


    /**
     * 根据设备种类查询设备型号
     *
     * @param deviceModelDto
     * @return ResponseBean
     */
    @GetMapping(value = "deviceModelsByKind")
    @ApiOperation(value = "根据设备种类查询设备型号")
    public ResponseBean
            <List<DeviceModelVo>> findDeviceModelsByKind(DeviceModelDto deviceModelDto) {
        return new ResponseBean<>(deviceModelService.findDeviceModelsByKind(deviceModelDto));
    }

    /**
     * 分页查询设备型号列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceModelDto
     * @return PageInfo
     */
    @GetMapping("deviceModelList")
    @ApiOperation(value = "分页查询设备型号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "deviceModel", value = "设备型号信息", dataType = "deviceModel")
    })
    public ResponseBean<PageInfo<DeviceModelVo>> deviceModelList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                               DeviceModelDto deviceModelDto) {
        return new ResponseBean<>(deviceModelService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), deviceModelDto));
    }

    /**
     * 新增设备型号
     *
     * @param deviceModelDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备型号", notes = "新增设备型号")
    @Log("新增设备型号")
    public ResponseBean
            <Boolean> insertDeviceModel(@RequestBody @Valid DeviceModelDto deviceModelDto) {
        DeviceModel deviceModel = new DeviceModel();
        BeanUtils.copyProperties(deviceModelDto, deviceModel);
        if (deviceModelService.findByDeviceModelName(deviceModelDto.getDeviceModelName()) != null) {
            return new ResponseBean<>(false);
        }
        deviceModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelService.insert(deviceModel) > 0);
    }

    /**
     * 修改设备型号
     *
     * @param deviceModelDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备型号信息", notes = "根据设备型号ID更新职位信息")
    @Log("修改设备型号")
    public ResponseBean
            <Boolean> updateDeviceModel(@RequestBody @Valid DeviceModelDto deviceModelDto) {
        DeviceModel deviceModel = new DeviceModel();
        BeanUtils.copyProperties(deviceModelDto, deviceModel);
        deviceModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelService.update(deviceModel) > 0);
    }

    /**
     * 根据设备型号ID删除设备型号信息
     *
     * @param id
     * @return ResponseBean
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备型号信息", notes = "根据设备型号ID删除设备型号信息")
    public ResponseBean
            <Boolean> deleteDeviceModelById(@PathVariable("id") String id) {
        DeviceModel deviceModel = new DeviceModel();
        deviceModel.setId(id);
        deviceModel.setNewRecord(false);
        deviceModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelService.delete(deviceModel) > 0);
    }

    /**
     * 批量删除设备型号信息
     *
     * @param deviceModel
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备型号", notes = "根据设备型号ID批量删除设备型号")
    @ApiImplicitParam(name = "deviceModel", value = "设备型号信息", dataType = "deviceModel")
    @Log("批量删除设备型号")
    public ResponseBean
            <Boolean> deleteAllDeviceModel(@RequestBody DeviceModel deviceModel) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceModel.getIdString())) {
                String []  ids = deviceModel.getIdString().split(",");
                for (int i = 0; i <ids.length ; i++) {
                    String modelId = ids[i];
                    //根据型号ID查询事件元数据-设备型号表数据
                    List<EventUcMetadataModel> list = eventUcMetadataModelService.findByUcMetadataByModelId(modelId);
                    for (EventUcMetadataModel metadataModel : list) {
                        //删除设备型号事件元数据-状态操作
                        eventUcMetadataModelOperationService.deleteByEventMetadataModelId(metadataModel.getId());
                        //删除设备型号区间报警数据
                        deviceModelAlarmThresholdService.deleteByEventMetadataModelId(metadataModel.getId());
                    }
                    //删除事件元数据-设备型号
                    eventUcMetadataModelService.deleteByDeviceModelId(modelId);

                    //删除事件状态组-设备型号
                    eventUcStatusGroupModelService.deleteByDeviceModelId(modelId);
                }
                success = deviceModelService.deleteAll(deviceModel.getIdString().split(",")) > 0;
            } 
            }catch(Exception e){
            log.error("删除设备型号失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据设备型号查询设备型号是否存在
     *
     * @param deviceModelName
     * @return ResponseBean
     */
    @GetMapping(value = "checkDeviceModelName")
    @ApiOperation(value = "根据设备型号查询设备型号是否存在", notes = "根据设备型号查询设备型号是否存在")
    @Log("根据设备型号查询设备型号是否存在")
    public ResponseBean<Boolean> checkDeviceModelName(@RequestParam("deviceModelName") @Valid String deviceModelName) {
        return new ResponseBean<>(deviceModelService.findByDeviceModelName(deviceModelName) != null);


    }

    /**
     * 返回当前用户下的设备型号
     */
    @GetMapping(value = "getDeviceModelByUser")
    @ApiOperation(value = "根据用户返回当前用户下的设备型号", notes = "根据用户返回当前用户下的设备型号")
    public ResponseBean<List<String>> getDeviceModelByUser(){
        //获取当前登录用户名
        String userName = SysUtil.getUser();
        return new ResponseBean<>(deviceModelService.getDeviceModelByUser(userName));
    }

    /**
     * 设备id查询设备型号
     */
    @GetMapping("getByDeviceId")
    @ApiOperation(value = "根据设备ID查询设备型号")
    public ResponseBean<DeviceModel> getByDeviceId(@RequestParam("id") String id){
       return new ResponseBean<>(deviceModelService.getByDeviceId(id));
    }
}
