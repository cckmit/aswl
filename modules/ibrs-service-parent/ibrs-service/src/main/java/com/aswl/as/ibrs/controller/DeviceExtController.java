package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceExtDto;
import com.aswl.as.ibrs.api.module.DeviceExt;
import com.aswl.as.ibrs.service.DeviceExtService;
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
 * 外接设备表controller
 *
 * @author dingfei
 * @date 2019-11-12 15:57
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceExt", tags = "外接设备表")
@RestController
@RequestMapping("/v1/deviceExt")
public class DeviceExtController extends BaseController {

    private final DeviceExtService deviceExtService;

    /**
     * 根据ID获取外接设备表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据外接设备表ID查询外接设备类型表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "外接设备表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceExt> findById(@PathVariable("id") String id) {
        DeviceExt deviceExt = new DeviceExt();
        deviceExt.setId(id);
        return new ResponseBean<>(deviceExtService.get(deviceExt));
    }

    /**
     * 查询所有外接设备类型表
     *
     * @param deviceExt
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有外接设备表列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceExt", value = "外接设备表对象", paramType = "path", required = true, dataType = "deviceExt"))
    @GetMapping(value = "deviceExts")
    public ResponseBean
            <List<DeviceExt>> findAll(DeviceExt deviceExt) {
        deviceExt.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtService.findAllList(deviceExt));
    }

    /**
     * 分页查询外接设备类型表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceExt
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询外接设备表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceExt", value = "外接设备表信息", dataType = "deviceExt")
    })

    @GetMapping("deviceExtList")
    public PageInfo<DeviceExt> deviceExtList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                             @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                             @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                             DeviceExt deviceExt) {
        deviceExt.setTenantCode(SysUtil.getTenantCode());
        return deviceExtService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), deviceExt);
    }

    /**
     * 新增外接设备表
     *
     * @param deviceExtDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增外接设备表", notes = "新增外接设备表")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceExtDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceExtDto"))
    @PostMapping
    @Log("新增外接设备表")
    public ResponseBean
            <Boolean> insertDeviceExt(@RequestBody @Valid DeviceExtDto deviceExtDto) {
        DeviceExt deviceExt = new DeviceExt();
        BeanUtils.copyProperties(deviceExtDto, deviceExt);
        deviceExt.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtService.insert(deviceExt) > 0);
    }

    /**
     * 修改外接设备类型表
     *
     * @param deviceExtDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新外接设备表信息", notes = "根据外接设备表ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "deviceExtDto", value = "设备dto", required = true, paramType = "body", dataType = "deviceExtDto"))
    @Log("修改外接设备表")
    @PutMapping
    public ResponseBean
            <Boolean> updateDeviceExt(@RequestBody @Valid DeviceExtDto deviceExtDto) {
        DeviceExt deviceExt = new DeviceExt();
        BeanUtils.copyProperties(deviceExtDto, deviceExt);
        deviceExt.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtService.update(deviceExt) > 0);
    }

    /**
     * 根据外接设备表ID删除外接设备表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除外接设备表信息", notes = "根据外接设备表ID删除外接设备表信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "外接设备类型表ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteDeviceExtById(@PathVariable("id") String id) {
        DeviceExt deviceExt = new DeviceExt();
        deviceExt.setId(id);
        deviceExt.setNewRecord(false);
        deviceExt.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceExtService.delete(deviceExt) > 0);
    }

    /**
     * 批量删除外接设备表信息
     *
     * @param deviceExt
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除外接设备表", notes = "根据外接设备表ID批量删除外接设备表")
    @ApiImplicitParam(name = "deviceExt", value = "外接设备表信息", dataType = "deviceExt")
    @Log("批量删除外接设备表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllDeviceExt(@RequestBody DeviceExt deviceExt) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceExt.getIdString()))
                success = deviceExtService.deleteAll(deviceExt.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除外接设备表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 开启关闭外接设备
     *
     * @param status
     * @return ResponseBean
     */

    @ApiOperation(value = "开启关闭外接设备", notes = "开启关闭外接设备(0、关闭,1、开启,)")
    @Log("发送短信")
    @GetMapping("switchExternalDevice")
    public ResponseBean switchExternalDevice(@RequestParam("status") Integer status) {
        String message = "";
        if (status == 0) {
            message = "关闭外接设备操作成功";
        } else {
            message = "开启外接设备操作成功";
        }
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(200);
        responseBean.setData(message);
        responseBean.setMsg("success");
        responseBean.setStatus(200);
        return  responseBean;
    }


    /**
     * 外接设备单次动作
     *
     * @return ResponseBean
     */

    @ApiOperation(value = "外接设备单次动作", notes = "外接设备单次动作")
    @Log("单次动作")
    @GetMapping("actionExternalDevice")
    public ResponseBean actionExternalDevice() {
        String message = "外接设备操作成功";
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(200);
        responseBean.setData(message);
        responseBean.setMsg("success");
        responseBean.setStatus(200);
        return responseBean;
    }
}
