package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventEcurrentDto;
import com.aswl.as.ibrs.api.module.EventEcurrent;
import com.aswl.as.ibrs.service.EventEcurrentService;
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
 * 设备事件-电源电流controller
 *
 * @author zgl
 * @date 2019-11-01 13:48
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventEcurrent", tags = "设备事件-电源电流")
@RestController
@RequestMapping("/v1/eventEcurrent")
public class EventEcurrentController extends BaseController {

    private final EventEcurrentService eventEcurrentService;

    /**
     * 根据ID获取设备事件-电源电流
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-电源电流ID查询设备事件-电源电流")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电源电流ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventEcurrent> findById(@PathVariable("id") String id) {
        EventEcurrent eventEcurrent = new EventEcurrent();
        eventEcurrent.setId(id);
        return new ResponseBean<>(eventEcurrentService.get(eventEcurrent));
    }

    /**
     * 查询所有设备事件-电源电流
     *
     * @param eventEcurrent
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-电源电流列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEcurrent", value = "设备事件-电源电流对象", paramType = "path", required = true, dataType = "eventEcurrent"))
    @GetMapping(value = "eventEcurrents")
    public ResponseBean
            <List<EventEcurrent>> findAll(EventEcurrent eventEcurrent) {
        eventEcurrent.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventEcurrentService.findAllList(eventEcurrent));
    }

    /**
     * 分页查询设备事件-电源电流列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventEcurrent
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-电源电流列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventEcurrent", value = "设备事件-电源电流信息", dataType = "eventEcurrent")
    })

    @GetMapping("eventEcurrentList")
    public PageInfo<EventEcurrent> eventEcurrentList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                     EventEcurrent eventEcurrent) {
        eventEcurrent.setTenantCode(SysUtil.getTenantCode());
        return eventEcurrentService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventEcurrent);
    }

    /**
     * 新增设备事件-电源电流
     *
     * @param eventEcurrentDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-电源电流", notes = "新增设备事件-电源电流")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEcurrentDto", value = "设备dto", required = true, paramType = "body", dataType = "eventEcurrentDto"))
    @PostMapping
    @Log("新增设备事件-电源电流")
    public ResponseBean
            <Boolean> insertEventEcurrent(@RequestBody @Valid EventEcurrentDto eventEcurrentDto) {
        EventEcurrent eventEcurrent = new EventEcurrent();
        BeanUtils.copyProperties(eventEcurrentDto, eventEcurrent);
        eventEcurrent.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEcurrentService.insert(eventEcurrent) > 0);
    }

    /**
     * 修改设备事件-电源电流
     *
     * @param eventEcurrentDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-电源电流信息", notes = "根据设备事件-电源电流ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEcurrentDto", value = "设备dto", required = true, paramType = "body", dataType = "eventEcurrentDto"))
    @Log("修改设备事件-电源电流")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventEcurrent(@RequestBody @Valid EventEcurrentDto eventEcurrentDto) {
        EventEcurrent eventEcurrent = new EventEcurrent();
        BeanUtils.copyProperties(eventEcurrentDto, eventEcurrent);
        eventEcurrent.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEcurrentService.update(eventEcurrent) > 0);
    }

    /**
     * 根据设备事件-电源电流ID删除设备事件-电源电流信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-电源电流信息", notes = "根据设备事件-电源电流ID删除设备事件-电源电流信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电源电流ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventEcurrentById(@PathVariable("id") String id) {
        EventEcurrent eventEcurrent = new EventEcurrent();
        eventEcurrent.setId(id);
        eventEcurrent.setNewRecord(false);
        eventEcurrent.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEcurrentService.delete(eventEcurrent) > 0);
    }

    /**
     * 批量删除设备事件-电源电流信息
     *
     * @param eventEcurrent
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-电源电流", notes = "根据设备事件-电源电流ID批量删除设备事件-电源电流")
    @ApiImplicitParam(name = "eventEcurrent", value = "设备事件-电源电流信息", dataType = "eventEcurrent")
    @Log("批量删除设备事件-电源电流")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventEcurrent(@RequestBody EventEcurrent eventEcurrent) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventEcurrent.getIdString()))
                success = eventEcurrentService.deleteAll(eventEcurrent.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-电源电流失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
