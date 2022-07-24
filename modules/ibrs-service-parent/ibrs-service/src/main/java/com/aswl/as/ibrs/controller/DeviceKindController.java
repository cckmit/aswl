package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceKindDto;
import com.aswl.as.ibrs.api.module.DeviceKind;
import com.aswl.as.ibrs.service.DeviceKindService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

/**
 * 设备种类controller
 *
 * @author dingfei
 * @date 2019-09-26 14:33
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/deviceKind", tags = "设备种类")
@RestController
@RequestMapping("/v1/deviceKind")
public class DeviceKindController extends BaseController {

    private final DeviceKindService deviceKindService;

    /**
     * 根据ID获取设备种类
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据设备种类ID查询设备种类")
    public ResponseBean<DeviceKind> findById(@PathVariable("id") String id) {
        DeviceKind deviceKind = new DeviceKind();
        deviceKind.setId(id);
        return new ResponseBean<>(deviceKindService.get(deviceKind));
    }

    /**
     * 查询所有设备种类
     *
     * @param deviceKind
     * @return ResponseBean
     */
    @GetMapping(value = "deviceKinds")
    @ApiOperation(value = "查询所有设备种类列表")
    public ResponseBean
            <List<DeviceKind>> findAll(DeviceKind deviceKind) {
        return new ResponseBean<>(deviceKindService.findAllList(deviceKind));
    }

    /**
     * 分页查询设备种类列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param deviceKind
     * @return PageInfo
     */
    @GetMapping("deviceKindList")
    @ApiOperation(value = "分页查询设备种类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, dataType = "String"),
            @ApiImplicitParam(name = "deviceKind", value = "设备种类信息", dataType = "deviceKind")
    })
    public ResponseBean<PageInfo<DeviceKind>> deviceKindList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                               DeviceKind deviceKind) {
        return new ResponseBean<>(deviceKindService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), deviceKind));
    }

    /**
     * 新增设备种类
     *
     * @param deviceKindDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备种类", notes = "新增设备种类")
    @Log("新增设备种类")
    public ResponseBean
            <Boolean> insertDeviceKind(@RequestBody @Valid DeviceKindDto deviceKindDto) {
        DeviceKind deviceKind = new DeviceKind();
        BeanUtils.copyProperties(deviceKindDto, deviceKind);
        deviceKind.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceKindService.insert(deviceKind) > 0);
    }

    /**
     * 修改设备种类
     *
     * @param deviceKindDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备种类信息", notes = "根据设备种类ID更新职位信息")
    @Log("修改设备种类")
    public ResponseBean
            <Boolean> updateDeviceKind(@RequestBody @Valid DeviceKindDto deviceKindDto) {
        DeviceKind deviceKind = new DeviceKind();
        BeanUtils.copyProperties(deviceKindDto, deviceKind);
        deviceKind.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceKindService.update(deviceKind) > 0);
    }

    /**
     * 根据设备种类ID删除设备种类信息
     *
     * @param id
     * @return ResponseBean
     */
    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备种类信息", notes = "根据设备种类ID删除设备种类信息")
    public ResponseBean
            <Boolean> deleteDeviceKindById(@PathVariable("id") String id) {
        DeviceKind deviceKind = new DeviceKind();
        deviceKind.setId(id);
        deviceKind.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(deviceKindService.delete(deviceKind) > 0);
    }

    /**
     * 批量删除设备种类信息
     *
     * @param deviceKind
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备种类", notes = "根据设备种类ID批量删除设备种类")
    @ApiImplicitParam(name = "deviceKind", value = "设备种类信息", dataType = "deviceKind")
    @Log("批量删除设备种类")
    public ResponseBean
            <Boolean> deleteAllDeviceKind(@RequestBody DeviceKind deviceKind) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(deviceKind.getIdString()))
                success = deviceKindService.deleteAll(deviceKind.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备种类失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
