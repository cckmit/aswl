package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventUcMetadataDto;
import com.aswl.as.ibrs.api.dto.EventUcMetadataStatusOperationDto;
import com.aswl.as.ibrs.api.dto.EventUcStatusOperationDto;
import com.aswl.as.ibrs.api.dto.MetadataStatusOperationDto;
import com.aswl.as.ibrs.api.module.EventUcMetadata;
import com.aswl.as.ibrs.api.vo.EventUcMetadataVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
import com.aswl.as.ibrs.service.EventUcMetadataService;
import com.aswl.as.ibrs.service.EventUcMetadataStatusOperationService;
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
 * 事件元数据controller
 *
 * @author dingfei
 * @date 2019-10-22 11:42
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventUcMetadata", tags = "事件元数据")
@RestController
@RequestMapping("/v1/eventUcMetadata")
public class EventUcMetadataController extends BaseController {

    private final EventUcMetadataService eventUcMetadataService;

    private final EventUcMetadataStatusOperationService statusOperationService;

    /**
     * 根据ID获取事件元数据
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据事件元数据ID查询事件元数据")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "事件元数据ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventUcMetadata> findById(@PathVariable("id") String id) {
        EventUcMetadata eventUcMetadata = new EventUcMetadata();
        eventUcMetadata.setId(id);
        return new ResponseBean<>(eventUcMetadataService.get(eventUcMetadata));
    }

    /**
     * 查询所有事件元数据
     *
     * @param eventUcMetadata
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有事件元数据列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcMetadata", value = "事件元数据对象", paramType = "path", required = true, dataType = "eventUcMetadata"))
    @GetMapping(value = "eventUcMetadatas")
    public ResponseBean
            <List<EventUcMetadata>> findAll(EventUcMetadata eventUcMetadata) {
        return new ResponseBean<>(eventUcMetadataService.findList(eventUcMetadata));
    }

    /**
     * 分页查询事件元数据列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventUcMetadata
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询事件元数据列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventUcMetadata", value = "事件元数据信息", dataType = "eventUcMetadata")
    })

    @GetMapping("eventUcMetadataList")
    public ResponseBean<PageInfo<EventUcMetadataVo>> eventUcMetadataList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                         @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                         @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                         @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                         EventUcMetadata eventUcMetadata) {
        return new ResponseBean<>(eventUcMetadataService.findPageInfo(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventUcMetadata));
    }

    /**
     * 新增事件元数据
     *
     * @param eventUcMetadataDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增事件元数据", notes = "新增事件元数据")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcMetadataDto", value = "设备dto", required = true, paramType = "body", dataType = "eventUcMetadataDto"))
    @PostMapping
    @Log("新增事件元数据")
    public ResponseBean
            <Boolean> insertEventUcMetadata(@RequestBody @Valid EventUcMetadataDto eventUcMetadataDto) {
        EventUcMetadata eventUcMetadata = new EventUcMetadata();
        BeanUtils.copyProperties(eventUcMetadataDto, eventUcMetadata);
        eventUcMetadata.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataService.insert(eventUcMetadata) > 0);
    }

    /**
     * 修改事件元数据
     *
     * @param eventUcMetadataDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新事件元数据信息", notes = "根据事件元数据ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcMetadataDto", value = "设备dto", required = true, paramType = "body", dataType = "eventUcMetadataDto"))
    @Log("修改事件元数据")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventUcMetadata(@RequestBody @Valid EventUcMetadataDto eventUcMetadataDto) {
        EventUcMetadata eventUcMetadata = new EventUcMetadata();
        BeanUtils.copyProperties(eventUcMetadataDto, eventUcMetadata);
        eventUcMetadata.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataService.update(eventUcMetadata) > 0);
    }

    /**
     * 根据事件元数据ID删除事件元数据信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除事件元数据信息", notes = "根据事件元数据ID删除事件元数据信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "事件元数据ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventUcMetadataById(@PathVariable("id") String id) {
        EventUcMetadata eventUcMetadata = new EventUcMetadata();
        eventUcMetadata.setId(id);
        eventUcMetadata.setNewRecord(false);
        eventUcMetadata.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataService.delete(eventUcMetadata) > 0);
    }

    /**
     * 批量删除事件元数据信息
     *
     * @param eventUcMetadata
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除事件元数据", notes = "根据事件元数据ID批量删除事件元数据")
    @ApiImplicitParam(name = "eventUcMetadata", value = "事件元数据信息", dataType = "eventUcMetadata")
    @Log("批量删除事件元数据")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventUcMetadata(@RequestBody EventUcMetadata eventUcMetadata) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventUcMetadata.getIdString()))
                success = eventUcMetadataService.deleteAll(eventUcMetadata.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除事件元数据失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 根据元数据ID查询元数据操作已选择列表
     * @param id 元数据ID
     * @return ResponseBean
     */

    @ApiOperation(value = "根据元数据ID查询元数据操作已选择列表", notes = "根据元数据ID查询元数据操作已选择列表")
    @ApiImplicitParam(name = "id", value = "事件元数据ID", dataType = "String")
    @GetMapping("seletedOperation")
    public ResponseBean<List<EventUcStatusOperationVo>> findSeletedOperationByMetadataId(@RequestParam("id") String id) {
        return  new ResponseBean<>(eventUcMetadataService.findSeletedOperationByMetadataId(id));
    }



    /**
     * 修改状态操作列表
     * @param dto 集合
     * @return ResponseBean
     */

    @ApiOperation(value = "修改状态操作列表", notes = "修改状态操作列表")
    @ApiImplicitParam(name = "list", value = "状态操作列表集合", dataType = "list")
    @PostMapping("updateOperation")
    public ResponseBean<Boolean> updateSeletedOperation(@RequestBody MetadataStatusOperationDto dto) {
        // 先删除原数据
        statusOperationService.deleteByEventMetadataId(dto.getMetadataId());
        if (dto.getOperationDtoList()==null || dto.getOperationDtoList().size()==0){
            return new ResponseBean<>(Boolean.TRUE);
        }else {
            // 新增数据
            return new ResponseBean<>(statusOperationService.insertBatch(dto.getOperationDtoList()) > 0);
        }
    }

    /**
     * 根据设备型号ID获取元数据
     * @param id 型号ID
     * @return ResponseBean
     */

    @ApiOperation(value = "根据设备型号ID获取元数据", notes = "根据设备型号ID获取元数据")
    @ApiImplicitParam(name = "id", value = "型号ID", dataType = "String")
    @GetMapping("findUcMetadataByDeviceModelId")
    public ResponseBean<List<EventUcMetadataVo>> findEventUcMetadataByDeviceModelId(@RequestParam("id") String id) {
        return  new ResponseBean<>(eventUcMetadataService.findEventUcMetadataByDeviceModelId(id));
    }

    //-----------------------------  下面是提供给运营端使用的接口  -------------------------------------------


}
