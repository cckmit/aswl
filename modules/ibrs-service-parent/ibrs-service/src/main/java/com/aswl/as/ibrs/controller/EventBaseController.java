package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventBaseDto;
import com.aswl.as.ibrs.api.module.EventBase;
import com.aswl.as.ibrs.service.EventBaseService;
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
 * 设备事件-基础controller
 *
 * @author zgl
 * @date 2019-11-01 11:59
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventBase", tags = "设备事件-基础")
@RestController
@RequestMapping("/v1/eventBase")
public class EventBaseController extends BaseController {

    private final EventBaseService eventBaseService;

    /**
     * 根据ID获取设备事件-基础
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备事件-基础ID查询设备事件-基础")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-基础ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventBase> findById(@PathVariable("id") String id) {
        EventBase eventBase = new EventBase();
        eventBase.setId(id);
        return new ResponseBean<>(eventBaseService.get(eventBase));
    }

    /**
     * 查询所有设备事件-基础
     *
     * @param eventBase
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备事件-基础列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventBase", value = "设备事件-基础对象", paramType = "path", required = true, dataType = "eventBase"))
    @GetMapping(value = "eventBases")
    public ResponseBean
            <List<EventBase>> findAll(EventBase eventBase) {
        eventBase.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventBaseService.findAllList(eventBase));
    }

    /**
     * 分页查询设备事件-基础列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventBase
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备事件-基础列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventBase", value = "设备事件-基础信息", dataType = "eventBase")
    })

    @GetMapping("eventBaseList")
    public PageInfo<EventBase> eventBaseList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                             @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                             @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                             EventBase eventBase) {
        eventBase.setTenantCode(SysUtil.getTenantCode());
        return eventBaseService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventBase);
    }

    /**
     * 新增设备事件-基础
     *
     * @param eventBaseDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备事件-基础", notes = "新增设备事件-基础")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventBaseDto", value = "设备dto", required = true, paramType = "body", dataType = "eventBaseDto"))
    @PostMapping
    @Log("新增设备事件-基础")
    public ResponseBean
            <Boolean> insertEventBase(@RequestBody @Valid EventBaseDto eventBaseDto) {
        EventBase eventBase = new EventBase();
        BeanUtils.copyProperties(eventBaseDto, eventBase);
        eventBase.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventBaseService.insert(eventBase) > 0);
    }

    /**
     * 修改设备事件-基础
     *
     * @param eventBaseDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备事件-基础信息", notes = "根据设备事件-基础ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventBaseDto", value = "设备dto", required = true, paramType = "body", dataType = "eventBaseDto"))
    @Log("修改设备事件-基础")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventBase(@RequestBody @Valid EventBaseDto eventBaseDto) {
        EventBase eventBase = new EventBase();
        BeanUtils.copyProperties(eventBaseDto, eventBase);
        eventBase.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventBaseService.update(eventBase) > 0);
    }

    /**
     * 根据设备事件-基础ID删除设备事件-基础信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备事件-基础信息", notes = "根据设备事件-基础ID删除设备事件-基础信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备事件-基础ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventBaseById(@PathVariable("id") String id) {
        EventBase eventBase = new EventBase();
        eventBase.setId(id);
        eventBase.setNewRecord(false);
        eventBase.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventBaseService.delete(eventBase) > 0);
    }

    /**
     * 批量删除设备事件-基础信息
     *
     * @param eventBase
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备事件-基础", notes = "根据设备事件-基础ID批量删除设备事件-基础")
    @ApiImplicitParam(name = "eventBase", value = "设备事件-基础信息", dataType = "eventBase")
    @Log("批量删除设备事件-基础")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventBase(@RequestBody EventBase eventBase) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventBase.getIdString()))
                success = eventBaseService.deleteAll(eventBase.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备事件-基础失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
