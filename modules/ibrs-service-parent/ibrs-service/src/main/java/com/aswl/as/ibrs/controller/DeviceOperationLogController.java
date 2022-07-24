package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.DeviceOperationLogDto;
import com.aswl.as.ibrs.api.module.DeviceOperationLog;
import com.aswl.as.ibrs.api.vo.DeviceOperationLogVo;
import com.aswl.as.ibrs.service.DeviceOperationLogService;
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
import com.aswl.as.common.core.enums.BusinessType;

/**
 * 设备操作记录controller
 *
 * @author df
 * @date 2021/07/06 15:44
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceOperationLog", tags = "设备操作记录")
@RestController
@RequestMapping("/v1/deviceOperationLog")
public class DeviceOperationLogController extends BaseController {

    private final DeviceOperationLogService deviceOperationLogService;

    /**
     * 根据ID获取设备操作记录
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备操作记录ID查询设备操作记录")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备操作记录ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceOperationLog> findById(@PathVariable("id") String id) {
        DeviceOperationLog deviceOperationLog = new DeviceOperationLog();
        deviceOperationLog.setId(id);
        return new ResponseBean<>(deviceOperationLogService.get(deviceOperationLog));
    }

    /**
     * 查询所有设备操作记录
     *
     * @param deviceOperationLog
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备操作记录列表")
    @ApiImplicitParam(name = "deviceOperationLog", value = "设备操作记录对象", paramType = "path", required = true, dataType = "deviceOperationLog")
    @GetMapping(value = "deviceOperationLogs")
    public ResponseBean
            <List<DeviceOperationLog>> findAll(DeviceOperationLog deviceOperationLog) {
        deviceOperationLog.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceOperationLogService.findList(deviceOperationLog));
    }

    /**
     * 分页查询设备操作记录列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param dto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备操作记录列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceOperationLog", value = "设备操作记录信息", dataType = "deviceOperationLog")
    })

    @GetMapping("deviceOperationLogList")
    public ResponseBean
            <PageInfo<DeviceOperationLogVo>> deviceOperationLogList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                    DeviceOperationLogDto dto) {
        dto.setTenantCode(SysUtil.getTenantCode());
        dto.setProjectId(SysUtil.getProjectId());
        return new ResponseBean<>(deviceOperationLogService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), dto));
    }

    /**
     * 新增设备操作记录
     *
     * @param deviceOperationLogDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备操作记录", notes = "新增设备操作记录")
    @Log(value = "新增设备操作记录", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertDeviceOperationLog(@RequestBody @Valid DeviceOperationLogDto deviceOperationLogDto) {
        DeviceOperationLog deviceOperationLog = new DeviceOperationLog();
        BeanUtils.copyProperties(deviceOperationLogDto, deviceOperationLog);
        deviceOperationLog.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceOperationLogService.insert(deviceOperationLog) > 0);
    }

    /**
     * 修改设备操作记录
     *
     * @param deviceOperationLogDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备操作记录信息", notes = "根据设备操作记录ID更新设备操作记录信息")
    @Log(value = "更新设备操作记录", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateDeviceOperationLog(@RequestBody @Valid DeviceOperationLogDto deviceOperationLogDto) {
        DeviceOperationLog deviceOperationLog = new DeviceOperationLog();
        BeanUtils.copyProperties(deviceOperationLogDto, deviceOperationLog);
        deviceOperationLog.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceOperationLogService.update(deviceOperationLog) > 0);
    }

    /**
     * 根据设备操作记录ID删除设备操作记录信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备操作记录信息", notes = "根据设备操作记录ID删除设备操作记录信息")
    @ApiImplicitParam(name = "id", value = "设备操作记录ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除设备操作记录", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteDeviceOperationLogById(@PathVariable("id") String id) {
        DeviceOperationLog deviceOperationLog = new DeviceOperationLog();
        deviceOperationLog.setId(id);
        deviceOperationLog.setNewRecord(false);
        deviceOperationLog.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceOperationLogService.delete(deviceOperationLog) > 0);
    }

    /**
     * 批量删除设备操作记录信息
     *
     * @param deviceOperationLog
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备操作记录", notes = "根据设备操作记录ID批量删除设备操作记录")
    @ApiImplicitParam(name = "deviceOperationLog", value = "设备操作记录信息", dataType = "deviceOperationLog")
    @Log(value = "删除设备操作记录", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllDeviceOperationLog(@RequestBody DeviceOperationLog deviceOperationLog) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceOperationLog.getIdString()))
                success = deviceOperationLogService.deleteAll(deviceOperationLog.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备操作记录失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
