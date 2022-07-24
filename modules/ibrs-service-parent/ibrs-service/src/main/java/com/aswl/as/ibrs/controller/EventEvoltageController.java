package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventEvoltageDto;
import com.aswl.as.ibrs.api.module.EventEvoltage;
import com.aswl.as.ibrs.service.EventEvoltageService;
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
 * 设备事件-电压controller
 *
 * @author zgl
 * @date 2019-11-01 14:03
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventEvoltage", tags = "设备事件-电压")
@RestController
@RequestMapping("/v1/eventEvoltage")
public class EventEvoltageController extends BaseController {

    private final EventEvoltageService eventEvoltageService;

    /**
     * 根据ID获取设备事件-电压
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-电压ID查询设备事件-电压")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电压ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventEvoltage> findById(@PathVariable("id") String id) {
        EventEvoltage eventEvoltage = new EventEvoltage();
        eventEvoltage.setId(id);
        return new ResponseBean<>(eventEvoltageService.get(eventEvoltage));
    }

    /**
     * 查询所有设备事件-电压
     *
     * @param eventEvoltage
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-电压列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEvoltage", value = "设备事件-电压对象", paramType = "path", required = true, dataType = "eventEvoltage"))
    @GetMapping(value = "eventEvoltages")
    public ResponseBean
            <List<EventEvoltage>> findAll(EventEvoltage eventEvoltage) {
        eventEvoltage.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventEvoltageService.findAllList(eventEvoltage));
    }

    /**
     * 分页查询设备事件-电压列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventEvoltage
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-电压列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventEvoltage", value = "设备事件-电压信息", dataType = "eventEvoltage")
    })

    @GetMapping("eventEvoltageList")
    public ResponseBean<PageInfo<EventEvoltage>> eventEvoltageList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     EventEvoltage eventEvoltage) {
        eventEvoltage.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventEvoltageService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventEvoltage));
    }

    /**
     * 新增设备事件-电压
     *
     * @param eventEvoltageDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-电压", notes = "新增设备事件-电压")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEvoltageDto", value = "设备dto", required = true, paramType = "body", dataType = "eventEvoltageDto"))
    @PostMapping
    @Log("新增设备事件-电压")
    public ResponseBean
            <Boolean> insertEventEvoltage(@RequestBody @Valid EventEvoltageDto eventEvoltageDto) {
        EventEvoltage eventEvoltage = new EventEvoltage();
        BeanUtils.copyProperties(eventEvoltageDto, eventEvoltage);
        eventEvoltage.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEvoltageService.insert(eventEvoltage) > 0);
    }

    /**
     * 修改设备事件-电压
     *
     * @param eventEvoltageDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-电压信息", notes = "根据设备事件-电压ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEvoltageDto", value = "设备dto", required = true, paramType = "body", dataType = "eventEvoltageDto"))
    @Log("修改设备事件-电压")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventEvoltage(@RequestBody @Valid EventEvoltageDto eventEvoltageDto) {
        EventEvoltage eventEvoltage = new EventEvoltage();
        BeanUtils.copyProperties(eventEvoltageDto, eventEvoltage);
        eventEvoltage.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEvoltageService.update(eventEvoltage) > 0);
    }

    /**
     * 根据设备事件-电压ID删除设备事件-电压信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-电压信息", notes = "根据设备事件-电压ID删除设备事件-电压信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电压ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventEvoltageById(@PathVariable("id") String id) {
        EventEvoltage eventEvoltage = new EventEvoltage();
        eventEvoltage.setId(id);
        eventEvoltage.setNewRecord(false);
        eventEvoltage.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEvoltageService.delete(eventEvoltage) > 0);
    }

    /**
     * 批量删除设备事件-电压信息
     *
     * @param eventEvoltage
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-电压", notes = "根据设备事件-电压ID批量删除设备事件-电压")
    @ApiImplicitParam(name = "eventEvoltage", value = "设备事件-电压信息", dataType = "eventEvoltage")
    @Log("批量删除设备事件-电压")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventEvoltage(@RequestBody EventEvoltage eventEvoltage) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventEvoltage.getIdString()))
                success = eventEvoltageService.deleteAll(eventEvoltage.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-电压失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
