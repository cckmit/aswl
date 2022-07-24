package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventElectricityDto;
import com.aswl.as.ibrs.api.module.EventElectricity;
import com.aswl.as.ibrs.service.EventElectricityService;
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
 * 设备事件-电量controller
 *
 * @author hzj
 * @date 2021/06/01 19:05
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventElectricity", tags = "设备事件-电量")
@RestController
@RequestMapping("/v1/eventElectricity")
public class EventElectricityController extends BaseController {

    private final EventElectricityService eventElectricityService;

    /**
     * 根据ID获取设备事件-电量
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-电量ID查询设备事件-电量")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电量ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventElectricity> findById(@PathVariable("id") String id) {
        EventElectricity eventElectricity = new EventElectricity();
        eventElectricity.setId(id);
        return new ResponseBean<>(eventElectricityService.get(eventElectricity));
    }

    /**
     * 查询所有设备事件-电量
     *
     * @param eventElectricity
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-电量列表")
    @ApiImplicitParam(name = "eventElectricity", value = "设备事件-电量对象", paramType = "path", required = true, dataType = "eventElectricity")
    @GetMapping(value = "eventElectricitys")
    public ResponseBean
            <List<EventElectricity>> findAll(EventElectricity eventElectricity) {
        eventElectricity.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventElectricityService.findList(eventElectricity));
    }

    /**
     * 分页查询设备事件-电量列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventElectricity
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-电量列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventElectricity", value = "设备事件-电量信息", dataType = "eventElectricity")
    })

    @GetMapping("eventElectricityList")
    public ResponseBean
            <PageInfo<EventElectricity>> eventElectricityList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                              @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                              @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                              @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                              EventElectricity eventElectricity) {
        eventElectricity.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventElectricityService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventElectricity));
    }

    /**
     * 新增设备事件-电量
     *
     * @param eventElectricityDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-电量", notes = "新增设备事件-电量")
    @PostMapping
    @Log("新增设备事件-电量")
    public ResponseBean
            <Boolean> insertEventElectricity(@RequestBody @Valid EventElectricityDto eventElectricityDto) {
        EventElectricity eventElectricity = new EventElectricity();
        BeanUtils.copyProperties(eventElectricityDto, eventElectricity);
        eventElectricity.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventElectricityService.insert(eventElectricity) > 0);
    }

    /**
     * 修改设备事件-电量
     *
     * @param eventElectricityDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-电量信息", notes = "根据设备事件-电量ID更新设备事件-电量信息")
    @Log("修改设备事件-电量")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventElectricity(@RequestBody @Valid EventElectricityDto eventElectricityDto) {
        EventElectricity eventElectricity = new EventElectricity();
        BeanUtils.copyProperties(eventElectricityDto, eventElectricity);
        eventElectricity.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventElectricityService.update(eventElectricity) > 0);
    }

    /**
     * 根据设备事件-电量ID删除设备事件-电量信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-电量信息", notes = "根据设备事件-电量ID删除设备事件-电量信息")
    @ApiImplicitParam(name = "id", value = "设备事件-电量ID", paramType = "path", required = true, dataType =
            "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventElectricityById(@PathVariable("id") String id) {
        EventElectricity eventElectricity = new EventElectricity();
        eventElectricity.setId(id);
        eventElectricity.setNewRecord(false);
        eventElectricity.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventElectricityService.delete(eventElectricity) > 0);
    }

    /**
     * 批量删除设备事件-电量信息
     *
     * @param eventElectricity
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-电量", notes = "根据设备事件-电量ID批量删除设备事件-电量")
    @ApiImplicitParam(name = "eventElectricity", value = "设备事件-电量信息", dataType = "eventElectricity")
    @Log("批量删除设备事件-电量")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventElectricity(@RequestBody EventElectricity eventElectricity) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventElectricity.getIdString()))
                success = eventElectricityService.deleteAll(eventElectricity.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-电量失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
