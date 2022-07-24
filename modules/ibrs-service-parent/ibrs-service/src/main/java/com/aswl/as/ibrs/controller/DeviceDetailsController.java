package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.service.DeviceDetailsService;
import com.aswl.as.ibrs.service.DeviceService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备controller
 *
 * @author liuliepan
 * @date 2019-09-27 14:17
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/devicedetails", tags = "设备详情")
@RestController
@RequestMapping("/v1/devicedetails")
public class DeviceDetailsController extends BaseController {

    private final DeviceDetailsService deviceDetailsService;

    /**
     * 根据ID获取设备
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备ID查询设备详情")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备ID",  paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<DeviceVo> findById(@PathVariable("id") String id) {
        return new ResponseBean<>(deviceDetailsService.findById(id));
    }


    /**
     * 获取上级设备信息
     *
     * @param parentId
     * @return ResponseBean
     */
    @GetMapping(value = "/getSuperiorDevice")
    @ApiOperation(value = "获取上级设备信息", notes = "根据父级ID获取上级设备信息")
    @ApiImplicitParam(name = "parentId", value = "父ID", required = true)
    public ResponseBean<DeviceVo> getSuperiorDevice(@RequestParam("parentId") String parentId) {
        return new ResponseBean<>(deviceDetailsService.getSuperiorDevice(parentId));
    }

    /**
     * 获取下级设备信息
     *
     * @param id
     * @return ResponseBean
     */
    @GetMapping(value = "/getSubordinateDevice")
    @ApiOperation(value = "获取下级设备信息", notes = "根据设备ID获取下级设备信息")
    @ApiImplicitParam(name = "id", value = "设备ID", required = true)
    public ResponseBean<List<DeviceVo>> getSubordinateDevice(@RequestParam("id") String id) {
        return new ResponseBean<>(deviceDetailsService.getSubordinateDevice(id));
    }



    /**
     * 分页查询设备列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param device
     * @return PageInfo
     */
    @GetMapping("deviceList")
    @ApiOperation(value = "分页查询设备列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT,paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "Device", value = "设备信息", dataType = "Device")
    })
    public PageInfo<Device> deviceList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                       @RequestParam(value = "regionId",defaultValue = "") String regionId,
                                       Device device) {
        device.setRegionId(regionId);
        device.setTenantCode(SysUtil.getTenantCode());
        return deviceDetailsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), device);
    }
}
