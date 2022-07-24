package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.enums.DeviceSettingsType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DevicePortSettingDto;
import com.aswl.as.ibrs.api.dto.DeviceSettingsDto;
import com.aswl.as.ibrs.api.dto.DeviceSettingsListDto;
import com.aswl.as.ibrs.api.module.DeviceOperationLog;
import com.aswl.as.ibrs.api.module.DeviceSettings;
import com.aswl.as.ibrs.service.DeviceSettingsService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.utils.PageUtil;

/**
 * 设备设置controller
 *
 * @author dingfei
 * @date 2019-12-18 16:24
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceSettings", tags = "设备设置")
@RestController
@RequestMapping("/v1/deviceSettings")
public class DeviceSettingsController extends BaseController {

    private final DeviceSettingsService deviceSettingsService;

    /**
     * 根据ID获取设备设置
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备设置ID查询设备设置")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备设置ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceSettings> findById(@PathVariable("id") String id) {
        DeviceSettings deviceSettings = new DeviceSettings();
        deviceSettings.setId(id);
        return new ResponseBean<>(deviceSettingsService.get(deviceSettings));
    }

    /**
     * 查询所有设备设置
     *
     * @param deviceSettings
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备设置列表")
    @ApiImplicitParam(name = "deviceSettings", value = "设备设置对象", paramType = "path", required = true, dataType = "deviceSettings")
    @GetMapping(value = "deviceSettingss")
    public ResponseBean
            <List<DeviceSettings>> findAll(DeviceSettings deviceSettings) {
        deviceSettings.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceSettingsService.findList(deviceSettings));
    }

    /**
     * 分页查询设备设置列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceSettings
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceSettings", value = "设备设置信息", dataType = "deviceSettings")
    })

    @GetMapping("deviceSettingsList")
    public ResponseBean<PageInfo<DeviceSettings>> deviceSettingsList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                     DeviceSettings deviceSettings) {
        deviceSettings.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceSettingsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), deviceSettings));
    }

    /**
     * 新增设备设置
     *
     * @param deviceSettingsDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备设置", notes = "新增设备设置")
    @PostMapping
    @Log("新增设备设置")
    public ResponseBean
            <Boolean> insertDeviceSettings(@RequestBody @Valid DeviceSettingsDto deviceSettingsDto) {
        DeviceSettings deviceSettings = new DeviceSettings();
        BeanUtils.copyProperties(deviceSettingsDto, deviceSettings);
        deviceSettings.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceSettingsService.insert(deviceSettings) > 0);
    }

    /**
     * 修改设备设置
     *
     * @param deviceSettingsDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备设置信息", notes = "根据设备设置ID更新设备设置信息")
    @Log("修改设备设置")
    @PutMapping
    public ResponseBean
            <Boolean> updateDeviceSettings(@RequestBody @Valid DeviceSettingsDto deviceSettingsDto) {
        DeviceSettings deviceSettings = new DeviceSettings();
        BeanUtils.copyProperties(deviceSettingsDto, deviceSettings);
        deviceSettings.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceSettingsService.update(deviceSettings) > 0);
    }

    /**
     * 根据设备设置ID删除设备设置信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备设置信息", notes = "根据设备设置ID删除设备设置信息")
    @ApiImplicitParam(name = "id", value = "设备设置ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteDeviceSettingsById(@PathVariable("id") String id) {
        DeviceSettings deviceSettings = new DeviceSettings();
        deviceSettings.setId(id);
        deviceSettings.setNewRecord(false);
        deviceSettings.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceSettingsService.delete(deviceSettings) > 0);
    }

    /**
     * 批量删除设备设置信息
     *
     * @param deviceSettings
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备设置", notes = "根据设备设置ID批量删除设备设置")
    @ApiImplicitParam(name = "deviceSettings", value = "设备设置信息", dataType = "deviceSettings")
    @Log("批量删除设备设置")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllDeviceSettings(@RequestBody DeviceSettings deviceSettings) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceSettings.getIdString()))
                success = deviceSettingsService.deleteAll(deviceSettings.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备设置失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 查询设置信息
     *
     * @param deviceId
     * @param type
     * @return ResponseBean
     */
    @ApiOperation(value = "设置信息", notes = "设置信息")
    @GetMapping(value = "settings")
    public ResponseBean
            <DeviceSettings> getDeviceSettings(@RequestParam("deviceId") String deviceId, @RequestParam("type") String type) {
        DeviceSettings temporary_revoke = deviceSettingsService.findByDeviceIdAndMode(deviceId, DeviceSettingsType.DOOR_TEMPORARY_REVOKE.getType());
        if (temporary_revoke != null && temporary_revoke.getValue() != null){
            //当前时间轴
            long time = new Date().getTime();
            if (Long.parseLong(temporary_revoke.getValue()) < time){
                DeviceSettings deviceSettings = new DeviceSettings();
                deviceSettings.setId(temporary_revoke.getId());
                deviceSettings.setDeviceId(deviceId);
                deviceSettings.setMode(DeviceSettingsType.DOOR_DEPLOY.getType());
                deviceSettings.setValue("");
                deviceSettingsService.update(deviceSettings);
            }
        }
        return new ResponseBean<>(deviceSettingsService.findDeviceSettings(deviceId, type));
    }


    /**
     * 设置信息
     *
     * @param dto
     * @return ResponseBean
     */
    @ApiOperation(value = "设置信息", notes = "设置信息")
    @PostMapping(value = "updateBath")
    public ResponseBean
            <Boolean> updateBath(@RequestBody DeviceSettingsListDto dto) {
        return new ResponseBean<>(deviceSettingsService.updateBath(dto) > 0);
    }




    /**
     * 设备网络口、光纤口端口设置
     *
     * @param list
     * @return ResponseBean
     */
    @ApiOperation(value = "设备网络口、光纤口端口设置", notes = "设备网络口、光纤口端口设置")
    @PostMapping(value = "updatePort")
    public ResponseBean
            <Boolean> updatePortSettings(@RequestBody List<DevicePortSettingDto> list) {
            return new ResponseBean<>(deviceSettingsService.updatePortSettings(list)>0);
    }
}
