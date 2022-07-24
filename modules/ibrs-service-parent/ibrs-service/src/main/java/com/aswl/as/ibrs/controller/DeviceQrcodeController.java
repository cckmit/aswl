package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceQrcodeDto;
import com.aswl.as.ibrs.api.module.DeviceQrcode;
import com.aswl.as.ibrs.api.vo.DeviceQrcodeVo;
import com.aswl.as.ibrs.service.DeviceQrcodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 设备二维码表controller
 *
 * @author hfx
 * @date 2020-02-26 15:44
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceQrcode", tags = "设备二维码表")
@RestController
@RequestMapping("/v1/deviceQrcode")
public class DeviceQrcodeController extends BaseController {

    private final DeviceQrcodeService deviceQrcodeService;

    /**
     * 根据ID获取设备二维码表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备二维码表ID查询设备二维码表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备二维码表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceQrcode> findById(@PathVariable("id") Integer id) {
        DeviceQrcode deviceQrcode = new DeviceQrcode();
        deviceQrcode.setQrCodeId(id);
        return new ResponseBean<>(deviceQrcodeService.get(deviceQrcode));
    }

    /**
     * 查询所有设备二维码表
     *
     * @param deviceQrcode
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备二维码表列表")
    @ApiImplicitParam(name = "deviceQrcode", value = "设备二维码表对象", paramType = "path", required = true, dataType = "deviceQrcode")
    @GetMapping(value = "deviceQrcodes")
    public ResponseBean
            <List<DeviceQrcode>> findAll(DeviceQrcode deviceQrcode) {
//        deviceQrcode.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceQrcodeService.findList(deviceQrcode));
    }

    /**
     * 分页查询设备二维码表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceQrcode
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备二维码表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "deviceQrcode", value = "设备二维码表信息", dataType = "deviceQrcode")
    })

    @GetMapping("deviceQrcodeList")
    public ResponseBean<PageInfo<DeviceQrcode>> deviceQrcodeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                 @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                 @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                 @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                 DeviceQrcode deviceQrcode) {
//        deviceQrcode.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(deviceQrcodeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), deviceQrcode));
    }

    /**
     * 新增设备二维码表
     *
     * @param deviceQrcodeDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备二维码表", notes = "新增设备二维码表")
    @PostMapping
    @Log("新增设备二维码表")
    public ResponseBean
            <Boolean> insertDeviceQrcode(@RequestBody @Valid DeviceQrcodeDto deviceQrcodeDto) {
        DeviceQrcode deviceQrcode = new DeviceQrcode();
        BeanUtils.copyProperties(deviceQrcodeDto, deviceQrcode);
//        deviceQrcode.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceQrcodeService.insert(deviceQrcode) > 0);
    }

    /**
     * 修改设备二维码表
     *
     * @param deviceQrcodeDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备二维码表信息", notes = "根据设备二维码表ID更新设备二维码表信息")
    @Log("修改设备二维码表")
    @PutMapping
    public ResponseBean
            <Boolean> updateDeviceQrcode(@RequestBody @Valid DeviceQrcodeDto deviceQrcodeDto) {
        DeviceQrcode deviceQrcode = new DeviceQrcode();
        BeanUtils.copyProperties(deviceQrcodeDto, deviceQrcode);
//        deviceQrcode.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceQrcodeService.update(deviceQrcode) > 0);
    }

    /**
     * 根据设备二维码表ID删除设备二维码表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备二维码表信息", notes = "根据设备二维码表ID删除设备二维码表信息")
    @ApiImplicitParam(name = "id", value = "设备二维码表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteDeviceQrcodeById(@PathVariable("id") Integer id) {
        DeviceQrcode deviceQrcode = new DeviceQrcode();
        deviceQrcode.setQrCodeId(id);
//        deviceQrcode.setNewRecord(false);
//        deviceQrcode.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceQrcodeService.delete(deviceQrcode) > 0);
    }

    /**
     * 批量删除设备二维码表信息
     *
     * @param deviceQrcode
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备二维码表", notes = "根据设备二维码表ID批量删除设备二维码表")
    @ApiImplicitParam(name = "deviceQrcode", value = "设备二维码表信息", dataType = "deviceQrcode")
    @Log("批量删除设备二维码表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllDeviceQrcode(@RequestBody DeviceQrcode deviceQrcode) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceQrcode.getIdString()))
                success = deviceQrcodeService.deleteAll(deviceQrcode.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备二维码表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    // 添加一个接口，用来根据Qrcde获取设备id等资料
    /**
     * 根据二维码获取设备二维码表
     *
     * @param qrCode
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备二维码表ID查询设备二维码表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备二维码表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/getDeviceIdByQrCode/{qrCode}")
    public ResponseBean<DeviceQrcodeVo> getDeviceIdByQrCode(@PathVariable("qrCode") String qrCode) {
//        DeviceQrcode deviceQrcode = new DeviceQrcode();
//        deviceQrcode.setId(id);
        return new ResponseBean<>(deviceQrcodeService.getDeviceIdByQrCode(qrCode));
    }

}
