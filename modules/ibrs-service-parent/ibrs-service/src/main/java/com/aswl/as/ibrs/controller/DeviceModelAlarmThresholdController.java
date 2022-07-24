package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceModelAlarmThresholdDto;
import com.aswl.as.ibrs.api.dto.MetadataStatusOperationDto;
import com.aswl.as.ibrs.api.module.DeviceModelAlarmThreshold;
import com.aswl.as.ibrs.service.DeviceModelAlarmThresholdService;
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
import com.aswl.as.common.core.utils.PageUtil;

/**
 * 设备型号区间报警controller
 *
 * @author dingfei
 * @date 2019-11-16 17:05
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceModelAlarmThreshold", tags = "设备型号区间报警")
@RestController
@RequestMapping("/v1/deviceModelAlarmThreshold")
public class DeviceModelAlarmThresholdController extends BaseController {

    private final DeviceModelAlarmThresholdService deviceModelAlarmThresholdService;

    /**
     * 根据ID获取设备型号区间报警
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备型号区间报警ID查询设备型号区间报警")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备型号区间报警ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceModelAlarmThreshold> findById(@PathVariable("id") String id) {
        DeviceModelAlarmThreshold deviceModelAlarmThreshold = new DeviceModelAlarmThreshold();
        deviceModelAlarmThreshold.setId(id);
        return new ResponseBean<>(deviceModelAlarmThresholdService.get(deviceModelAlarmThreshold));
    }

    /**
     * 查询所有设备型号区间报警
     *
     * @param deviceModelAlarmThreshold
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备型号区间报警列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceModelAlarmThreshold", value = "设备型号区间报警对象", paramType = "path", required = true, dataType = "deviceModelAlarmThreshold"))
    @GetMapping(value = "deviceModelAlarmThresholds")
    public ResponseBean
            <List<DeviceModelAlarmThreshold>> findAll(DeviceModelAlarmThreshold deviceModelAlarmThreshold) {
        deviceModelAlarmThreshold.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelAlarmThresholdService.findAllList(deviceModelAlarmThreshold));
    }

    /**
     * 分页查询设备型号区间报警列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceModelAlarmThreshold
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备型号区间报警列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceModelAlarmThreshold", value = "设备型号区间报警信息", dataType = "deviceModelAlarmThreshold")
    })

    @GetMapping("deviceModelAlarmThresholdList")
    public ResponseBean<PageInfo<DeviceModelAlarmThreshold>> deviceModelAlarmThresholdList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                                           DeviceModelAlarmThreshold deviceModelAlarmThreshold) {
        deviceModelAlarmThreshold.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelAlarmThresholdService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), deviceModelAlarmThreshold));
    }

    /**
     * 新增设备型号区间报警
     *
     * @param deviceModelAlarmThresholdDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备型号区间报警", notes = "新增设备型号区间报警")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceModelAlarmThresholdDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceModelAlarmThresholdDto"))
    @PostMapping
    @Log("新增设备型号区间报警")
    public ResponseBean
            <Boolean> insertDeviceModelAlarmThreshold(@RequestBody @Valid DeviceModelAlarmThresholdDto deviceModelAlarmThresholdDto) {
        DeviceModelAlarmThreshold deviceModelAlarmThreshold = new DeviceModelAlarmThreshold();
        BeanUtils.copyProperties(deviceModelAlarmThresholdDto, deviceModelAlarmThreshold);
        deviceModelAlarmThreshold.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelAlarmThresholdService.insert(deviceModelAlarmThreshold) > 0);
    }

    /**
     * 修改设备型号区间报警
     *
     * @param deviceModelAlarmThresholdDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备型号区间报警信息", notes = "根据设备型号区间报警ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceModelAlarmThresholdDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceModelAlarmThresholdDto"))
    @Log("修改设备型号区间报警")
    @PutMapping
    public ResponseBean
            <Boolean> updateDeviceModelAlarmThreshold(@RequestBody @Valid DeviceModelAlarmThresholdDto deviceModelAlarmThresholdDto) {
        DeviceModelAlarmThreshold deviceModelAlarmThreshold = new DeviceModelAlarmThreshold();
        BeanUtils.copyProperties(deviceModelAlarmThresholdDto, deviceModelAlarmThreshold);
        deviceModelAlarmThreshold.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelAlarmThresholdService.update(deviceModelAlarmThreshold) > 0);
    }

    /**
     * 根据设备型号区间报警ID删除设备型号区间报警信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备型号区间报警信息", notes = "根据设备型号区间报警ID删除设备型号区间报警信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备型号区间报警ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteDeviceModelAlarmThresholdById(@PathVariable("id") String id) {
        DeviceModelAlarmThreshold deviceModelAlarmThreshold = new DeviceModelAlarmThreshold();
        deviceModelAlarmThreshold.setId(id);
        deviceModelAlarmThreshold.setNewRecord(false);
        deviceModelAlarmThreshold.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceModelAlarmThresholdService.delete(deviceModelAlarmThreshold) > 0);
    }

    /**
     * 批量删除设备型号区间报警信息
     *
     * @param deviceModelAlarmThreshold
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备型号区间报警", notes = "根据设备型号区间报警ID批量删除设备型号区间报警")
    @ApiImplicitParam(name = "deviceModelAlarmThreshold", value = "设备型号区间报警信息", dataType = "deviceModelAlarmThreshold")
    @Log("批量删除设备型号区间报警")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllDeviceModelAlarmThreshold(@RequestBody DeviceModelAlarmThreshold deviceModelAlarmThreshold) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceModelAlarmThreshold.getIdString()))
                success = deviceModelAlarmThresholdService.deleteAll(deviceModelAlarmThreshold.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备型号区间报警失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 批量新增设备型号区间报警数据
     * @param list 集合
     * @return ResponseBean
     */

    @ApiOperation(value = "批量新增设备型号区间报警数据", notes = "批量新增设备型号区间报警数据")
    @ApiImplicitParam(name = "list", value = "区间报警列表集合", dataType = "list")
    @PostMapping("insertBath")
    public ResponseBean<Boolean> insertBatch(@RequestBody List<DeviceModelAlarmThresholdDto> list) {
         return new ResponseBean<>(deviceModelAlarmThresholdService.insertBatch(list) > 0);
        }
    }
