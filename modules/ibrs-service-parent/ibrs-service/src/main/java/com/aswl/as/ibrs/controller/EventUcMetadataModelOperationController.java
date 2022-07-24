package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventUcMetadataModelOperationDto;
import com.aswl.as.ibrs.api.dto.MetadataStatusOperationDto;
import com.aswl.as.ibrs.api.dto.metadataModelOperationDto;
import com.aswl.as.ibrs.api.module.EventUcMetadataModelOperation;
import com.aswl.as.ibrs.service.EventUcMetadataModelOperationService;
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
 * 设备型号事件元数据-状态操作controller
 *
 * @author dingfei
 * @date 2019-12-02 10:44
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventUcMetadataModelOperation", tags = "设备型号事件元数据-状态操作")
@RestController
@RequestMapping("/v1/eventUcMetadataModelOperation")
public class EventUcMetadataModelOperationController extends BaseController {

    private final EventUcMetadataModelOperationService eventUcMetadataModelOperationService;

    /**
     * 根据ID获取设备型号事件元数据-状态操作
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备型号事件元数据-状态操作ID查询设备型号事件元数据-状态操作")
   @ApiImplicitParam(name = "id", value = "设备型号事件元数据-状态操作ID", paramType = "path", required = true, dataType = "String")
    @GetMapping(value = "/{id}")
    public ResponseBean<EventUcMetadataModelOperation> findById(@PathVariable("id") String id) {
        EventUcMetadataModelOperation eventUcMetadataModelOperation = new EventUcMetadataModelOperation();
        eventUcMetadataModelOperation.setId(id);
        return new ResponseBean<>(eventUcMetadataModelOperationService.get(eventUcMetadataModelOperation));
    }

    /**
     * 查询所有设备型号事件元数据-状态操作
     *
     * @param eventUcMetadataModelOperation
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备型号事件元数据-状态操作列表")
   @ApiImplicitParam(name = "eventUcMetadataModelOperation", value = "设备型号事件元数据-状态操作对象", paramType = "path", required = true, dataType = "eventUcMetadataModelOperation")
    @GetMapping(value = "eventUcMetadataModelOperations")
    public ResponseBean
            <List<EventUcMetadataModelOperation>> findAll(EventUcMetadataModelOperation eventUcMetadataModelOperation) {
        return new ResponseBean<>(eventUcMetadataModelOperationService.findList(eventUcMetadataModelOperation));
    }

    /**
     * 分页查询设备型号事件元数据-状态操作列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventUcMetadataModelOperation
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备型号事件元数据-状态操作列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventUcMetadataModelOperation", value = "设备型号事件元数据-状态操作信息", dataType = "eventUcMetadataModelOperation")
    })

    @GetMapping("eventUcMetadataModelOperationList")
    public ResponseBean<PageInfo<EventUcMetadataModelOperation>> eventUcMetadataModelOperationList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                                                   EventUcMetadataModelOperation eventUcMetadataModelOperation) {
        return new ResponseBean<>(eventUcMetadataModelOperationService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventUcMetadataModelOperation));
    }

    /**
     * 新增设备型号事件元数据-状态操作
     *
     * @param eventUcMetadataModelOperationDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增设备型号事件元数据-状态操作", notes = "新增设备型号事件元数据-状态操作")
    @PostMapping
    @Log("新增设备型号事件元数据-状态操作")
    public ResponseBean
            <Boolean> insertEventUcMetadataModelOperation(@RequestBody @Valid EventUcMetadataModelOperationDto eventUcMetadataModelOperationDto) {
        EventUcMetadataModelOperation eventUcMetadataModelOperation = new EventUcMetadataModelOperation();
        BeanUtils.copyProperties(eventUcMetadataModelOperationDto, eventUcMetadataModelOperation);
        eventUcMetadataModelOperation.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataModelOperationService.insert(eventUcMetadataModelOperation) > 0);
    }

    /**
     * 修改设备型号事件元数据-状态操作
     *
     * @param eventUcMetadataModelOperationDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新设备型号事件元数据-状态操作信息", notes = "根据设备型号事件元数据-状态操作ID更新职位信息")
    @Log("修改设备型号事件元数据-状态操作")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventUcMetadataModelOperation(@RequestBody @Valid EventUcMetadataModelOperationDto eventUcMetadataModelOperationDto) {
        EventUcMetadataModelOperation eventUcMetadataModelOperation = new EventUcMetadataModelOperation();
        BeanUtils.copyProperties(eventUcMetadataModelOperationDto, eventUcMetadataModelOperation);
        eventUcMetadataModelOperation.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataModelOperationService.update(eventUcMetadataModelOperation) > 0);
    }

    /**
     * 根据设备型号事件元数据-状态操作ID删除设备型号事件元数据-状态操作信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除设备型号事件元数据-状态操作信息", notes = "根据设备型号事件元数据-状态操作ID删除设备型号事件元数据-状态操作信息")
   @ApiImplicitParam(name = "id", value = "设备型号事件元数据-状态操作ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventUcMetadataModelOperationById(@PathVariable("id") String id) {
        EventUcMetadataModelOperation eventUcMetadataModelOperation = new EventUcMetadataModelOperation();
        eventUcMetadataModelOperation.setId(id);
        eventUcMetadataModelOperation.setNewRecord(false);
        eventUcMetadataModelOperation.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataModelOperationService.delete(eventUcMetadataModelOperation) > 0);
    }

    /**
     * 批量删除设备型号事件元数据-状态操作信息
     *
     * @param eventUcMetadataModelOperation
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除设备型号事件元数据-状态操作", notes = "根据设备型号事件元数据-状态操作ID批量删除设备型号事件元数据-状态操作")
    @ApiImplicitParam(name = "eventUcMetadataModelOperation", value = "设备型号事件元数据-状态操作信息", dataType = "eventUcMetadataModelOperation")
    @Log("批量删除设备型号事件元数据-状态操作")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventUcMetadataModelOperation(@RequestBody EventUcMetadataModelOperation eventUcMetadataModelOperation) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventUcMetadataModelOperation.getIdString()))
                success = eventUcMetadataModelOperationService.deleteAll(eventUcMetadataModelOperation.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备型号事件元数据-状态操作失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 修改状态操作列表
     * @param dto 集合
     * @return ResponseBean
     */

    @ApiOperation(value = "修改扩展状态组属性操作列表", notes = "修改扩展状态组属性操作列表")
    @ApiImplicitParam(name = "list", value = "状态操作列表集合", dataType = "list")
    @PostMapping("updateExtendModelAttributeOperation")
    public ResponseBean<Boolean> updateExtendModelAttributeOperation(@RequestBody metadataModelOperationDto dto) {
        // 先删除原数据
        eventUcMetadataModelOperationService.deleteByEventMetadataModelId(dto.getEventMetadataModelId());
        if (dto.getModelOperationDtoList()==null || dto.getModelOperationDtoList().size()==0){
            return new ResponseBean<>(Boolean.TRUE);
        }else {
            // 新增数据
            return new ResponseBean<>(eventUcMetadataModelOperationService.insertBatch(dto.getModelOperationDtoList()) > 0);
        }
    }
}
