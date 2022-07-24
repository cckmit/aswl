package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventUcStatusOperationDto;
import com.aswl.as.ibrs.api.module.EventUcStatusOperation;
import com.aswl.as.ibrs.service.EventUcStatusOperationService;
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
 * 事件状态操作controller
 *
 * @author dingfei
 * @date 2019-11-12 10:24
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventUcStatusOperation", tags = "事件状态操作")
@RestController
@RequestMapping("/v1/eventUcStatusOperation")
public class EventUcStatusOperationController extends BaseController {

    private final EventUcStatusOperationService eventUcStatusOperationService;

    /**
     * 根据ID获取事件状态操作
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据事件状态操作ID查询事件状态操作")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "事件状态操作ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventUcStatusOperation> findById(@PathVariable("id") String id) {
        EventUcStatusOperation eventUcStatusOperation = new EventUcStatusOperation();
        eventUcStatusOperation.setId(id);
        return new ResponseBean<>(eventUcStatusOperationService.get(eventUcStatusOperation));
    }

    /**
     * 查询所有事件状态操作
     *
     * @param eventUcStatusOperation
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有事件状态操作列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcStatusOperation", value = "事件状态操作对象", paramType = "path", required = true, dataType = "eventUcStatusOperation"))
    @GetMapping(value = "eventUcStatusOperations")
    public ResponseBean
            <List<EventUcStatusOperation>> findAll(EventUcStatusOperation eventUcStatusOperation) {
        eventUcStatusOperation.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusOperationService.findAllList(eventUcStatusOperation));
    }

    /**
     * 分页查询事件状态操作列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventUcStatusOperation
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询事件状态操作列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventUcStatusOperation", value = "事件状态操作信息", dataType = "eventUcStatusOperation")
    })

    @GetMapping("eventUcStatusOperationList")
    public ResponseBean<PageInfo<EventUcStatusOperation>> eventUcStatusOperationList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                       EventUcStatusOperation eventUcStatusOperation) {
        eventUcStatusOperation.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusOperationService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventUcStatusOperation));
    }

    /**
     * 新增事件状态操作
     *
     * @param eventUcStatusOperationDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增事件状态操作", notes = "新增事件状态操作")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcStatusOperationDto", value = "设备dto", required = true, paramType = "body", dataType = "eventUcStatusOperationDto"))
    @PostMapping
    @Log("新增事件状态操作")
    public ResponseBean
            <Boolean> insertEventUcStatusOperation(@RequestBody @Valid EventUcStatusOperationDto eventUcStatusOperationDto) {
        EventUcStatusOperation eventUcStatusOperation = new EventUcStatusOperation();
        BeanUtils.copyProperties(eventUcStatusOperationDto, eventUcStatusOperation);
        eventUcStatusOperation.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusOperationService.insert(eventUcStatusOperation) > 0);
    }

    /**
     * 修改事件状态操作
     *
     * @param eventUcStatusOperationDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新事件状态操作信息", notes = "根据事件状态操作ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcStatusOperationDto", value = "设备dto", required = true, paramType = "body", dataType = "eventUcStatusOperationDto"))
    @Log("修改事件状态操作")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventUcStatusOperation(@RequestBody @Valid EventUcStatusOperationDto eventUcStatusOperationDto) {
        EventUcStatusOperation eventUcStatusOperation = new EventUcStatusOperation();
        BeanUtils.copyProperties(eventUcStatusOperationDto, eventUcStatusOperation);
        eventUcStatusOperation.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusOperationService.update(eventUcStatusOperation) > 0);
    }

    /**
     * 根据事件状态操作ID删除事件状态操作信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除事件状态操作信息", notes = "根据事件状态操作ID删除事件状态操作信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "事件状态操作ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventUcStatusOperationById(@PathVariable("id") String id) {
        EventUcStatusOperation eventUcStatusOperation = new EventUcStatusOperation();
        eventUcStatusOperation.setId(id);
        eventUcStatusOperation.setNewRecord(false);
        eventUcStatusOperation.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusOperationService.delete(eventUcStatusOperation) > 0);
    }

    /**
     * 批量删除事件状态操作信息
     *
     * @param eventUcStatusOperation
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除事件状态操作", notes = "根据事件状态操作ID批量删除事件状态操作")
    @ApiImplicitParam(name = "eventUcStatusOperation", value = "事件状态操作信息", dataType = "eventUcStatusOperation")
    @Log("批量删除事件状态操作")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventUcStatusOperation(@RequestBody EventUcStatusOperation eventUcStatusOperation) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventUcStatusOperation.getIdString()))
                success = eventUcStatusOperationService.deleteAll(eventUcStatusOperation.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除事件状态操作失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
