package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventUcStatusGroupModelDto;
import com.aswl.as.ibrs.api.module.EventUcStatusGroupModel;
import com.aswl.as.ibrs.service.EventUcStatusGroupModelService;
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
 * 事件状态组-设备型号controller
 *
 * @author dingfei
 * @date 2019-12-02 10:22
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventUcStatusGroupModel", tags = "事件状态组-设备型号")
@RestController
@RequestMapping("/v1/eventUcStatusGroupModel")
public class EventUcStatusGroupModelController extends BaseController {

    private final EventUcStatusGroupModelService eventUcStatusGroupModelService;

    /**
     * 根据ID获取事件状态组-设备型号
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据事件状态组-设备型号ID查询事件状态组-设备型号")
    @ApiImplicitParam(name = "id", value = "事件状态组-设备型号ID", paramType = "path", required = true, dataType = "String")
    @GetMapping(value = "/{id}")
    public ResponseBean<EventUcStatusGroupModel> findById(@PathVariable("id") String id) {
        EventUcStatusGroupModel eventUcStatusGroupModel = new EventUcStatusGroupModel();
        eventUcStatusGroupModel.setId(id);
        return new ResponseBean<>(eventUcStatusGroupModelService.get(eventUcStatusGroupModel));
    }

    /**
     * 查询所有事件状态组-设备型号
     *
     * @param eventUcStatusGroupModel
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有事件状态组-设备型号列表")
    @ApiImplicitParam(name = "eventUcStatusGroupModel", value = "事件状态组-设备型号对象", paramType = "path", required = true, dataType = "eventUcStatusGroupModel")
    @GetMapping(value = "eventUcStatusGroupModels")
    public ResponseBean
            <List<EventUcStatusGroupModel>> findAll(EventUcStatusGroupModel eventUcStatusGroupModel) {
        return new ResponseBean<>(eventUcStatusGroupModelService.findList(eventUcStatusGroupModel));
    }

    /**
     * 分页查询事件状态组-设备型号列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventUcStatusGroupModel
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询事件状态组-设备型号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventUcStatusGroupModel", value = "事件状态组-设备型号信息", dataType = "eventUcStatusGroupModel")
    })

    @GetMapping("eventUcStatusGroupModelList")
    public ResponseBean<PageInfo<EventUcStatusGroupModel>> eventUcStatusGroupModelList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                                       EventUcStatusGroupModel eventUcStatusGroupModel) {
        return new ResponseBean<>(eventUcStatusGroupModelService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventUcStatusGroupModel));
    }

    /**
     * 新增事件状态组-设备型号
     *
     * @param eventUcStatusGroupModelDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增事件状态组-设备型号", notes = "新增事件状态组-设备型号")
    @PostMapping
    @Log("新增事件状态组-设备型号")
    public ResponseBean
            <Boolean> insertEventUcStatusGroupModel(@RequestBody @Valid EventUcStatusGroupModelDto eventUcStatusGroupModelDto) {
        EventUcStatusGroupModel eventUcStatusGroupModel = new EventUcStatusGroupModel();
        BeanUtils.copyProperties(eventUcStatusGroupModelDto, eventUcStatusGroupModel);
        eventUcStatusGroupModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusGroupModelService.insert(eventUcStatusGroupModel) > 0);
    }

    /**
     * 修改事件状态组-设备型号
     *
     * @param eventUcStatusGroupModelDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新事件状态组-设备型号信息", notes = "根据事件状态组-设备型号ID更新职位信息")
    @Log("修改事件状态组-设备型号")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventUcStatusGroupModel(@RequestBody @Valid EventUcStatusGroupModelDto eventUcStatusGroupModelDto) {
        EventUcStatusGroupModel eventUcStatusGroupModel = new EventUcStatusGroupModel();
        BeanUtils.copyProperties(eventUcStatusGroupModelDto, eventUcStatusGroupModel);
        eventUcStatusGroupModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusGroupModelService.update(eventUcStatusGroupModel) > 0);
    }

    /**
     * 根据事件状态组-设备型号ID删除事件状态组-设备型号信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除事件状态组-设备型号信息", notes = "根据事件状态组-设备型号ID删除事件状态组-设备型号信息")
    @ApiImplicitParam(name = "id", value = "事件状态组-设备型号ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventUcStatusGroupModelById(@PathVariable("id") String id) {
        EventUcStatusGroupModel eventUcStatusGroupModel = new EventUcStatusGroupModel();
        eventUcStatusGroupModel.setId(id);
        eventUcStatusGroupModel.setNewRecord(false);
        return new ResponseBean<>(eventUcStatusGroupModelService.delete(eventUcStatusGroupModel) > 0);
    }

    /**
     * 批量删除事件状态组-设备型号信息
     *
     * @param eventUcStatusGroupModel
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除事件状态组-设备型号", notes = "根据事件状态组-设备型号ID批量删除事件状态组-设备型号")
    @ApiImplicitParam(name = "eventUcStatusGroupModel", value = "事件状态组-设备型号信息", dataType = "eventUcStatusGroupModel")
    @Log("批量删除事件状态组-设备型号")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventUcStatusGroupModel(@RequestBody EventUcStatusGroupModel eventUcStatusGroupModel) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventUcStatusGroupModel.getIdString()))
                success = eventUcStatusGroupModelService.deleteAll(eventUcStatusGroupModel.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除事件状态组-设备型号失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
