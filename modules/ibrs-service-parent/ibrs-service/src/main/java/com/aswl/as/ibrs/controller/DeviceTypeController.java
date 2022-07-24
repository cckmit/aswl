package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceTypeDto;
import com.aswl.as.ibrs.api.module.DeviceType;
import com.aswl.as.ibrs.service.DeviceTypeService;
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
 * 设备类型controller
 *
 * @author dingfei
 * @date 2019-09-26 15:29
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceType", tags = "设备类型")
@RestController
@RequestMapping("/v1/deviceType")
public class DeviceTypeController extends BaseController {

    private final DeviceTypeService deviceTypeService;

    /**
     * 根据ID获取设备类型
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据设备类型ID查询设备类型")
    public ResponseBean<DeviceType> findById(@PathVariable("id") String id) {
        DeviceType deviceType = new DeviceType();
        deviceType.setId(id);
        return new ResponseBean<>(deviceTypeService.get(deviceType));
    }

    /**
     * 查询所有设备类型
     *
     * @param deviceType
     * @return ResponseBean
     */
    @GetMapping(value = "deviceTypes")
    @ApiOperation(value = "查询所有设备类型列表")
    public ResponseBean
            <List<DeviceType>> findAll(DeviceType deviceType) {
        deviceType.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceTypeService.findAllList(deviceType));
    }

    /**
     * 分页查询设备类型列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceType
     * @return PageInfo
     */
    @GetMapping("deviceTypeList")
    @ApiOperation(value = "分页查询设备类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "deviceType", value = "设备类型信息", dataType = "deviceType")
    })
    public ResponseBean<PageInfo<DeviceType>> deviceTypeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                               DeviceType deviceType) {
       deviceType.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceTypeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), deviceType));
    }

    /**
     * 新增设备类型
     *
     * @param deviceTypeDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备类型", notes = "新增设备类型")
    @Log("新增设备类型")
    public ResponseBean
            <Boolean> insertDeviceType(@RequestBody @Valid DeviceTypeDto deviceTypeDto) {
        DeviceType deviceType = new DeviceType();
        BeanUtils.copyProperties(deviceTypeDto, deviceType);
        if(deviceTypeService.findByDeviceType(deviceTypeDto.getDeviceType())!=null){
            return new ResponseBean<>(false);
        }
        deviceType.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceTypeService.insert(deviceType) > 0);
    }

    /**
     * 修改设备类型
     *
     * @param deviceTypeDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备类型信息", notes = "根据设备类型ID更新职位信息")
    @Log("修改设备类型")
    public ResponseBean
            <Boolean> updateDeviceType(@RequestBody @Valid DeviceTypeDto deviceTypeDto) {
        DeviceType deviceType = new DeviceType();
        BeanUtils.copyProperties(deviceTypeDto, deviceType);
        deviceType.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceTypeService.update(deviceType) > 0);
    }

    /**
     * 根据设备类型ID删除设备类型信息
     *
     * @param id
     * @return ResponseBean
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备类型信息", notes = "根据设备类型ID删除设备类型信息")
    public ResponseBean
            <Boolean> deleteDeviceTypeById(@PathVariable("id") String id) {
        DeviceType deviceType = new DeviceType();
        deviceType.setId(id);
        deviceType.setNewRecord(false);
        deviceType.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceTypeService.delete(deviceType) > 0);
    }

    /**
     * 批量删除设备类型信息
     *
     * @param deviceType
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备类型", notes = "根据设备类型ID批量删除设备类型")
    @ApiImplicitParam(name = "deviceType", value = "设备类型信息", dataType = "deviceType")
    @Log("批量删除设备类型")
    public ResponseBean
            <Boolean> deleteAllDeviceType(@RequestBody DeviceType deviceType) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceType.getIdString()))
                success = deviceTypeService.deleteAll(deviceType.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备类型失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 根据设备类型查询设备类型是否存在
     *
     * @param deviceType
     * @return ResponseBean
     */
    @GetMapping(value = "checkDeviceType")
    @ApiOperation(value = "根据设备类型查询设备类型是否存在", notes = "根据设备类型查询设备类型是否存在")
    @Log("根据设备类型查询设备类型是否存在")
    public ResponseBean<Boolean> checkDeviceType(@RequestParam("deviceType") @Valid String deviceType) {
        boolean success = false;
            if (deviceTypeService.findByDeviceType(deviceType)!=null) {
                success=true;
            }
            return new ResponseBean<>(success);

    }
}
