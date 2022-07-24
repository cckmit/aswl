package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventEswitchDto;
import com.aswl.as.ibrs.api.module.EventEswitch;
import com.aswl.as.ibrs.service.EventEswitchService;
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
 * 设备事件-电源开关controller
 *
 * @author zgl
 * @date 2019-11-01 14:02
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventEswitch", tags = "设备事件-电源开关")
@RestController
@RequestMapping("/v1/eventEswitch")
public class EventEswitchController extends BaseController {

    private final EventEswitchService eventEswitchService;

    /**
     * 根据ID获取设备事件-电源开关
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-电源开关ID查询设备事件-电源开关")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电源开关ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventEswitch> findById(@PathVariable("id") String id) {
        EventEswitch eventEswitch = new EventEswitch();
        eventEswitch.setId(id);
        return new ResponseBean<>(eventEswitchService.get(eventEswitch));
    }

    /**
     * 查询所有设备事件-电源开关
     *
     * @param eventEswitch
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-电源开关列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEswitch", value = "设备事件-电源开关对象", paramType = "path", required = true, dataType = "eventEswitch"))
    @GetMapping(value = "eventEswitchs")
    public ResponseBean
            <List<EventEswitch>> findAll(EventEswitch eventEswitch) {
        eventEswitch.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventEswitchService.findAllList(eventEswitch));
    }

    /**
     * 分页查询设备事件-电源开关列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventEswitch
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-电源开关列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventEswitch", value = "设备事件-电源开关信息", dataType = "eventEswitch")
    })

    @GetMapping("eventEswitchList")
    public ResponseBean<PageInfo<EventEswitch>> eventEswitchList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                   EventEswitch eventEswitch) {
        eventEswitch.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventEswitchService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventEswitch));
    }

    /**
     * 新增设备事件-电源开关
     *
     * @param eventEswitchDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-电源开关", notes = "新增设备事件-电源开关")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEswitchDto", value = "设备dto", required = true, paramType = "body", dataType = "eventEswitchDto"))
    @PostMapping
    @Log("新增设备事件-电源开关")
    public ResponseBean
            <Boolean> insertEventEswitch(@RequestBody @Valid EventEswitchDto eventEswitchDto) {
        EventEswitch eventEswitch = new EventEswitch();
        BeanUtils.copyProperties(eventEswitchDto, eventEswitch);
        eventEswitch.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEswitchService.insert(eventEswitch) > 0);
    }

    /**
     * 修改设备事件-电源开关
     *
     * @param eventEswitchDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-电源开关信息", notes = "根据设备事件-电源开关ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventEswitchDto", value = "设备dto", required = true, paramType = "body", dataType = "eventEswitchDto"))
    @Log("修改设备事件-电源开关")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventEswitch(@RequestBody @Valid EventEswitchDto eventEswitchDto) {
        EventEswitch eventEswitch = new EventEswitch();
        BeanUtils.copyProperties(eventEswitchDto, eventEswitch);
        eventEswitch.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEswitchService.update(eventEswitch) > 0);
    }

    /**
     * 根据设备事件-电源开关ID删除设备事件-电源开关信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-电源开关信息", notes = "根据设备事件-电源开关ID删除设备事件-电源开关信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-电源开关ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventEswitchById(@PathVariable("id") String id) {
        EventEswitch eventEswitch = new EventEswitch();
        eventEswitch.setId(id);
        eventEswitch.setNewRecord(false);
        eventEswitch.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventEswitchService.delete(eventEswitch) > 0);
    }

    /**
     * 批量删除设备事件-电源开关信息
     *
     * @param eventEswitch
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-电源开关", notes = "根据设备事件-电源开关ID批量删除设备事件-电源开关")
    @ApiImplicitParam(name = "eventEswitch", value = "设备事件-电源开关信息", dataType = "eventEswitch")
    @Log("批量删除设备事件-电源开关")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventEswitch(@RequestBody EventEswitch eventEswitch) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventEswitch.getIdString()))
                success = eventEswitchService.deleteAll(eventEswitch.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-电源开关失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
