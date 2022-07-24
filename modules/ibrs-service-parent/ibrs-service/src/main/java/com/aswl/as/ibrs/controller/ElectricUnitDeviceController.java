package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.ElectricUnitDeviceDto;
import com.aswl.as.ibrs.api.module.ElectricUnitDevice;
import com.aswl.as.ibrs.service.ElectricUnitDeviceService;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.enums.BusinessType;

/**
 * 用电单位设备关联表controller
 *
 * @author df
 * @date 2021/06/01 20:56
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/electricUnitDevice", tags = "用电单位设备关联表")
@RestController
@RequestMapping("/v1/electricUnitDevice")
public class ElectricUnitDeviceController extends BaseController {

    private final ElectricUnitDeviceService electricUnitDeviceService;

    /**
     * 根据ID获取用电单位设备关联表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据用电单位设备关联表ID查询用电单位设备关联表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "用电单位设备关联表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ElectricUnitDevice> findById(@PathVariable("id") String id) {
        ElectricUnitDevice electricUnitDevice = new ElectricUnitDevice();
        electricUnitDevice.setId(id);
        return new ResponseBean<>(electricUnitDeviceService.get(electricUnitDevice));
    }

    /**
     * 查询所有用电单位设备关联表
     *
     * @param electricUnitDevice
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有用电单位设备关联表列表")
    @ApiImplicitParam(name = "electricUnitDevice", value = "用电单位设备关联表对象", paramType = "path", required = true, dataType = "electricUnitDevice")
    @GetMapping(value = "electricUnitDevices")
    public ResponseBean
            <List<ElectricUnitDevice>> findAll(ElectricUnitDevice electricUnitDevice) {
        electricUnitDevice.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(electricUnitDeviceService.findList(electricUnitDevice));
    }

    /**
     * 分页查询用电单位设备关联表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param electricUnitDevice
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询用电单位设备关联表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "electricUnitDevice", value = "用电单位设备关联表信息", dataType = "electricUnitDevice")
    })

    @GetMapping("electricUnitDeviceList")
    public ResponseBean
            <PageInfo<ElectricUnitDevice>> electricUnitDeviceList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                  @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                  @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                  @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                  ElectricUnitDevice electricUnitDevice) {
        electricUnitDevice.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(electricUnitDeviceService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), electricUnitDevice));
    }

    /**
     * 新增用电单位设备关联表
     *
     * @param electricUnitDeviceDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增用电单位设备关联表", notes = "新增用电单位设备关联表")
    @Log(value = "新增用电单位设备关联表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertElectricUnitDevice(@RequestBody @Valid ElectricUnitDeviceDto electricUnitDeviceDto) {
        ElectricUnitDevice electricUnitDevice = new ElectricUnitDevice();
        BeanUtils.copyProperties(electricUnitDeviceDto, electricUnitDevice);
        electricUnitDevice.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricUnitDeviceService.insert(electricUnitDevice) > 0);
    }

    /**
     * 修改用电单位设备关联表
     *
     * @param electricUnitDeviceDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新用电单位设备关联表信息", notes = "根据用电单位设备关联表ID更新用电单位设备关联表信息")
    @Log(value = "更新用电单位设备关联表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateElectricUnitDevice(@RequestBody @Valid ElectricUnitDeviceDto electricUnitDeviceDto) {
        ElectricUnitDevice electricUnitDevice = new ElectricUnitDevice();
        BeanUtils.copyProperties(electricUnitDeviceDto, electricUnitDevice);
        electricUnitDevice.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricUnitDeviceService.update(electricUnitDevice) > 0);
    }

    /**
     * 根据用电单位设备关联表ID删除用电单位设备关联表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除用电单位设备关联表信息", notes = "根据用电单位设备关联表ID删除用电单位设备关联表信息")
    @ApiImplicitParam(name = "id", value = "用电单位设备关联表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除用电单位设备关联表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteElectricUnitDeviceById(@PathVariable("id") String id) {
        ElectricUnitDevice electricUnitDevice = new ElectricUnitDevice();
        electricUnitDevice.setId(id);
        electricUnitDevice.setNewRecord(false);
        electricUnitDevice.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricUnitDeviceService.delete(electricUnitDevice) > 0);
    }

    /**
     * 批量删除用电单位设备关联表信息
     *
     * @param electricUnitDevice
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除用电单位设备关联表", notes = "根据用电单位设备关联表ID批量删除用电单位设备关联表")
    @ApiImplicitParam(name = "electricUnitDevice", value = "用电单位设备关联表信息", dataType = "electricUnitDevice")
    @Log(value = "删除用电单位设备关联表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllElectricUnitDevice(@RequestBody ElectricUnitDevice electricUnitDevice) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(electricUnitDevice.getIdString()))
                success = electricUnitDeviceService.deleteAll(electricUnitDevice.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除用电单位设备关联表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量保存用电单位设备关联表信息
     *
     * @param electricUnitDevice
     * @return ResponseBean
     */
    @PostMapping("bathSave")
    @ApiOperation(value = "批量保存用电单位设备关联表信息", notes = "批量保存用电单位设备关联表信息")
    @ApiImplicitParam(name = "electricUnitDevice", value = "用电单位设备关联表信息", dataType = "electricUnitDevice")
    @Log(value = "批量保存用电单位设备关联表信息", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertBatch(@RequestBody ElectricUnitDevice electricUnitDevice) {
        boolean success = false;
        String deviceIds = electricUnitDevice.getDeviceIds();
        success = electricUnitDeviceService.insertBatch(electricUnitDevice.getUnitId(), Stream.of(deviceIds.split(",")).collect(Collectors.toList())) > 0;
        return new ResponseBean<>(success);
    }
}
