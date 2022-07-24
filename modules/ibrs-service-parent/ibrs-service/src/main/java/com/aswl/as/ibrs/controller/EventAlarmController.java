package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventAlarmDto;
import com.aswl.as.ibrs.api.module.EventAlarm;
import com.aswl.as.ibrs.service.EventAlarmService;
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
 * 设备事件-报警controller
 *
 * @author zgl
 * @date 2019-11-01 11:29
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventAlarm", tags = "设备事件-报警")
@RestController
@RequestMapping("/v1/eventAlarm")
public class EventAlarmController extends BaseController {

    private final EventAlarmService eventAlarmService;

    /**
     * 根据ID获取设备事件-报警
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-报警ID查询设备事件-报警")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-报警ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventAlarm> findById(@PathVariable("id") String id) {
        EventAlarm eventAlarm = new EventAlarm();
        eventAlarm.setId(id);
        return new ResponseBean<>(eventAlarmService.get(eventAlarm));
    }

    /**
     * 查询所有设备事件-报警
     *
     * @param eventAlarm
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-报警列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventAlarm", value = "设备事件-报警对象", paramType = "path", required = true, dataType = "eventAlarm"))
    @GetMapping(value = "eventAlarms")
    public ResponseBean
            <List<EventAlarm>> findAll(EventAlarm eventAlarm) {
        eventAlarm.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventAlarmService.findAllList(eventAlarm));
    }

    /**
     * 分页查询设备事件-报警列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventAlarm
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-报警列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventAlarm", value = "设备事件-报警信息", dataType = "eventAlarm")
    })

    @GetMapping("eventAlarmList")
    public PageInfo<EventAlarm> eventAlarmList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                               EventAlarm eventAlarm) {
        eventAlarm.setTenantCode(SysUtil.getTenantCode());
        return eventAlarmService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventAlarm);
    }

    /**
     * 新增设备事件-报警
     *
     * @param eventAlarmDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-报警", notes = "新增设备事件-报警")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventAlarmDto", value = "设备dto", required = true, paramType = "body", dataType = "eventAlarmDto"))
    @PostMapping
    @Log("新增设备事件-报警")
    public ResponseBean
            <Boolean> insertEventAlarm(@RequestBody @Valid EventAlarmDto eventAlarmDto) {
        EventAlarm eventAlarm = new EventAlarm();
        BeanUtils.copyProperties(eventAlarmDto, eventAlarm);
        eventAlarm.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventAlarmService.insert(eventAlarm) > 0);
    }

    /**
     * 修改设备事件-报警
     *
     * @param eventAlarmDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-报警信息", notes = "根据设备事件-报警ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventAlarmDto", value = "设备dto", required = true, paramType = "body", dataType = "eventAlarmDto"))
    @Log("修改设备事件-报警")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventAlarm(@RequestBody @Valid EventAlarmDto eventAlarmDto) {
        EventAlarm eventAlarm = new EventAlarm();
        BeanUtils.copyProperties(eventAlarmDto, eventAlarm);
        eventAlarm.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventAlarmService.update(eventAlarm) > 0);
    }

    /**
     * 根据设备事件-报警ID删除设备事件-报警信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-报警信息", notes = "根据设备事件-报警ID删除设备事件-报警信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-报警ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventAlarmById(@PathVariable("id") String id) {
        EventAlarm eventAlarm = new EventAlarm();
        eventAlarm.setId(id);
        eventAlarm.setNewRecord(false);
        eventAlarm.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventAlarmService.delete(eventAlarm) > 0);
    }

    /**
     * 批量删除设备事件-报警信息
     *
     * @param eventAlarm
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-报警", notes = "根据设备事件-报警ID批量删除设备事件-报警")
    @ApiImplicitParam(name = "eventAlarm", value = "设备事件-报警信息", dataType = "eventAlarm")
    @Log("批量删除设备事件-报警")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventAlarm(@RequestBody EventAlarm eventAlarm) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventAlarm.getIdString()))
                success = eventAlarmService.deleteAll(eventAlarm.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-报警失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
