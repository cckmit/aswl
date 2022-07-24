package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventSfpDto;
import com.aswl.as.ibrs.api.module.EventSfp;
import com.aswl.as.ibrs.service.EventSfpService;
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
 * 设备事件-光口controller
 *
 * @author zgl
 * @date 2019-11-01 14:04
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventSfp", tags = "设备事件-光口")
@RestController
@RequestMapping("/v1/eventSfp")
public class EventSfpController extends BaseController {

    private final EventSfpService eventSfpService;

    /**
     * 根据ID获取设备事件-光口
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-光口ID查询设备事件-光口")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-光口ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventSfp> findById(@PathVariable("id") String id) {
        EventSfp eventSfp = new EventSfp();
        eventSfp.setId(id);
        return new ResponseBean<>(eventSfpService.get(eventSfp));
    }

    /**
     * 查询所有设备事件-光口
     *
     * @param eventSfp
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-光口列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventSfp", value = "设备事件-光口对象", paramType = "path", required = true, dataType = "eventSfp"))
    @GetMapping(value = "eventSfps")
    public ResponseBean
            <List<EventSfp>> findAll(EventSfp eventSfp) {
        eventSfp.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpService.findAllList(eventSfp));
    }

    /**
     * 分页查询设备事件-光口列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventSfp
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-光口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventSfp", value = "设备事件-光口信息", dataType = "eventSfp")
    })

    @GetMapping("eventSfpList")
    public ResponseBean<PageInfo<EventSfp>> eventSfpList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                           EventSfp eventSfp) {
        eventSfp.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventSfp));
    }

    /**
     * 新增设备事件-光口
     *
     * @param eventSfpDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-光口", notes = "新增设备事件-光口")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventSfpDto", value = "设备dto", required = true, paramType = "body", dataType = "eventSfpDto"))
    @PostMapping
    @Log("新增设备事件-光口")
    public ResponseBean
            <Boolean> insertEventSfp(@RequestBody @Valid EventSfpDto eventSfpDto) {
        EventSfp eventSfp = new EventSfp();
        BeanUtils.copyProperties(eventSfpDto, eventSfp);
        eventSfp.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpService.insert(eventSfp) > 0);
    }

    /**
     * 修改设备事件-光口
     *
     * @param eventSfpDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-光口信息", notes = "根据设备事件-光口ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventSfpDto", value = "设备dto", required = true, paramType = "body", dataType = "eventSfpDto"))
    @Log("修改设备事件-光口")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventSfp(@RequestBody @Valid EventSfpDto eventSfpDto) {
        EventSfp eventSfp = new EventSfp();
        BeanUtils.copyProperties(eventSfpDto, eventSfp);
        eventSfp.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpService.update(eventSfp) > 0);
    }

    /**
     * 根据设备事件-光口ID删除设备事件-光口信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-光口信息", notes = "根据设备事件-光口ID删除设备事件-光口信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-光口ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventSfpById(@PathVariable("id") String id) {
        EventSfp eventSfp = new EventSfp();
        eventSfp.setId(id);
        eventSfp.setNewRecord(false);
        eventSfp.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpService.delete(eventSfp) > 0);
    }

    /**
     * 批量删除设备事件-光口信息
     *
     * @param eventSfp
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-光口", notes = "根据设备事件-光口ID批量删除设备事件-光口")
    @ApiImplicitParam(name = "eventSfp", value = "设备事件-光口信息", dataType = "eventSfp")
    @Log("批量删除设备事件-光口")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventSfp(@RequestBody EventSfp eventSfp) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventSfp.getIdString()))
                success = eventSfpService.deleteAll(eventSfp.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-光口失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
