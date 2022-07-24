package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceExtTypeDto;
import com.aswl.as.ibrs.api.module.DeviceExtType;
import com.aswl.as.ibrs.service.DeviceExtTypeService;
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
 * 外接设备类型表controller
 *
 * @author dingfei
 * @date 2019-11-09 10:10
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceExtType", tags = "外接设备类型表")
@RestController
@RequestMapping("/v1/deviceExtType")
public class DeviceExtTypeController extends BaseController {

    private final DeviceExtTypeService deviceExtTypeService;

    /**
     * 根据ID获取外接设备类型表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据外接设备类型表ID查询外接设备类型表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "外接设备类型表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceExtType> findById(@PathVariable("id") String id) {
        DeviceExtType deviceExtType = new DeviceExtType();
        deviceExtType.setId(id);
        return new ResponseBean<>(deviceExtTypeService.get(deviceExtType));
    }

    /**
     * 查询所有外接设备类型表
     *
     * @param deviceExtType
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有外接设备类型表列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceExtType", value = "外接设备类型表对象", paramType = "path", required = true, dataType = "deviceExtType"))
    @GetMapping(value = "deviceExtTypes")
    public ResponseBean
            <List<DeviceExtType>> findAll(DeviceExtType deviceExtType) {
        deviceExtType.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtTypeService.findAllList(deviceExtType));
    }

    /**
     * 分页查询外接设备类型表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceExtType
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询外接设备类型表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceExtType", value = "外接设备类型表信息", dataType = "deviceExtType")
    })

    @GetMapping("deviceExtTypeList")
    public ResponseBean<PageInfo<DeviceExtType>> deviceExtTypeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     DeviceExtType deviceExtType) {
        deviceExtType.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtTypeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), deviceExtType));
    }

    /**
     * 新增外接设备类型表
     *
     * @param deviceExtTypeDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增外接设备类型表", notes = "新增外接设备类型表")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceExtTypeDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceExtTypeDto"))
    @PostMapping
    @Log("新增外接设备类型表")
    public ResponseBean
            <Boolean> insertDeviceExtType(@RequestBody @Valid DeviceExtTypeDto deviceExtTypeDto) {
        DeviceExtType deviceExtType = new DeviceExtType();
        BeanUtils.copyProperties(deviceExtTypeDto, deviceExtType);
        deviceExtType.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtTypeService.insert(deviceExtType) > 0);
    }

    /**
     * 修改外接设备类型表
     *
     * @param deviceExtTypeDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新外接设备类型表信息", notes = "根据外接设备类型表ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceExtTypeDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceExtTypeDto"))
    @Log("修改外接设备类型表")
    @PutMapping
    public ResponseBean
            <Boolean> updateDeviceExtType(@RequestBody @Valid DeviceExtTypeDto deviceExtTypeDto) {
        DeviceExtType deviceExtType = new DeviceExtType();
        BeanUtils.copyProperties(deviceExtTypeDto, deviceExtType);
        deviceExtType.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtTypeService.update(deviceExtType) > 0);
    }

    /**
     * 根据外接设备类型表ID删除外接设备类型表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除外接设备类型表信息", notes = "根据外接设备类型表ID删除外接设备类型表信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "外接设备类型表ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteDeviceExtTypeById(@PathVariable("id") String id) {
        DeviceExtType deviceExtType = new DeviceExtType();
        deviceExtType.setId(id);
        deviceExtType.setNewRecord(false);
        deviceExtType.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtTypeService.delete(deviceExtType) > 0);
    }

    /**
     * 批量删除外接设备类型表信息
     *
     * @param deviceExtType
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除外接设备类型表", notes = "根据外接设备类型表ID批量删除外接设备类型表")
    @ApiImplicitParam(name = "deviceExtType", value = "外接设备类型表信息", dataType = "deviceExtType")
    @Log("批量删除外接设备类型表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllDeviceExtType(@RequestBody DeviceExtType deviceExtType) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceExtType.getIdString()))
                success = deviceExtTypeService.deleteAll(deviceExtType.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除外接设备类型表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
