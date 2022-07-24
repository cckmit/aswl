package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventEoutletDto;
import com.aswl.as.ibrs.api.module.EventEoutlet;
import com.aswl.as.ibrs.service.EventEoutletService;
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
 * 设备事件-电源电口controller
 *
 * @author zgl
 * @date 2019-11-01 14:01
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventEoutlet", tags = "设备事件-电源电口")
@RestController
@RequestMapping("/v1/eventEoutlet")
public class EventEoutletController extends BaseController {

    private final EventEoutletService eventEoutletService;

    /**
     * 根据ID获取设备事件-电源电口
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-电源电口ID查询设备事件-电源电口")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电源电口ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventEoutlet> findById(@PathVariable("id") String id) {
        EventEoutlet eventEoutlet = new EventEoutlet();
        eventEoutlet.setId(id);
        return new ResponseBean<>(eventEoutletService.get(eventEoutlet));
    }

    /**
     * 查询所有设备事件-电源电口
     *
     * @param eventEoutlet
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-电源电口列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEoutlet", value = "设备事件-电源电口对象", paramType = "path", required = true, dataType = "eventEoutlet"))
    @GetMapping(value = "eventEoutlets")
    public ResponseBean
            <List<EventEoutlet>> findAll(EventEoutlet eventEoutlet) {
        eventEoutlet.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventEoutletService.findAllList(eventEoutlet));
    }

    /**
     * 分页查询设备事件-电源电口列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventEoutlet
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-电源电口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventEoutlet", value = "设备事件-电源电口信息", dataType = "eventEoutlet")
    })

    @GetMapping("eventEoutletList")
    public PageInfo<EventEoutlet> eventEoutletList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                   EventEoutlet eventEoutlet) {
        eventEoutlet.setTenantCode(SysUtil.getTenantCode());
        return eventEoutletService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventEoutlet);
    }

    /**
     * 新增设备事件-电源电口
     *
     * @param eventEoutletDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-电源电口", notes = "新增设备事件-电源电口")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEoutletDto", value = "设备dto", required = true, paramType = "body", dataType = "eventEoutletDto"))
    @PostMapping
    @Log("新增设备事件-电源电口")
    public ResponseBean
            <Boolean> insertEventEoutlet(@RequestBody @Valid EventEoutletDto eventEoutletDto) {
        EventEoutlet eventEoutlet = new EventEoutlet();
        BeanUtils.copyProperties(eventEoutletDto, eventEoutlet);
        eventEoutlet.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEoutletService.insert(eventEoutlet) > 0);
    }

    /**
     * 修改设备事件-电源电口
     *
     * @param eventEoutletDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-电源电口信息", notes = "根据设备事件-电源电口ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEoutletDto", value = "设备dto", required = true, paramType = "body", dataType = "eventEoutletDto"))
    @Log("修改设备事件-电源电口")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventEoutlet(@RequestBody @Valid EventEoutletDto eventEoutletDto) {
        EventEoutlet eventEoutlet = new EventEoutlet();
        BeanUtils.copyProperties(eventEoutletDto, eventEoutlet);
        eventEoutlet.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEoutletService.update(eventEoutlet) > 0);
    }

    /**
     * 根据设备事件-电源电口ID删除设备事件-电源电口信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-电源电口信息", notes = "根据设备事件-电源电口ID删除设备事件-电源电口信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电源电口ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventEoutletById(@PathVariable("id") String id) {
        EventEoutlet eventEoutlet = new EventEoutlet();
        eventEoutlet.setId(id);
        eventEoutlet.setNewRecord(false);
        eventEoutlet.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEoutletService.delete(eventEoutlet) > 0);
    }

    /**
     * 批量删除设备事件-电源电口信息
     *
     * @param eventEoutlet
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-电源电口", notes = "根据设备事件-电源电口ID批量删除设备事件-电源电口")
    @ApiImplicitParam(name = "eventEoutlet", value = "设备事件-电源电口信息", dataType = "eventEoutlet")
    @Log("批量删除设备事件-电源电口")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventEoutlet(@RequestBody EventEoutlet eventEoutlet) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventEoutlet.getIdString()))
                success = eventEoutletService.deleteAll(eventEoutlet.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-电源电口失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
