package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventUcMetadataModelDto;
import com.aswl.as.ibrs.api.module.EventUcMetadataModel;
import com.aswl.as.ibrs.service.EventUcMetadataModelService;
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
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.utils.PageUtil;

/**
 * 事件元数据-设备型号controller
 *
 * @author dingfei
 * @date 2019-11-29 09:41
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventUcMetadataModel", tags = "事件元数据-设备型号")
@RestController
@RequestMapping("/v1/eventUcMetadataModel")
public class EventUcMetadataModelController extends BaseController {

    private final EventUcMetadataModelService eventUcMetadataModelService;

    /**
     * 根据ID获取事件元数据-设备型号
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据事件元数据-设备型号ID查询事件元数据-设备型号")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "事件元数据-设备型号ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventUcMetadataModel> findById(@PathVariable("id") String id) {
        EventUcMetadataModel eventUcMetadataModel = new EventUcMetadataModel();
        eventUcMetadataModel.setId(id);
        return new ResponseBean<>(eventUcMetadataModelService.get(eventUcMetadataModel));
    }

    /**
     * 查询所有事件元数据-设备型号
     *
     * @param eventUcMetadataModel
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有事件元数据-设备型号列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcMetadataModel", value = "事件元数据-设备型号对象", paramType = "path", required = true, dataType = "eventUcMetadataModel"))
    @GetMapping(value = "eventUcMetadataModels")
    public ResponseBean
            <List<EventUcMetadataModel>> findAll(EventUcMetadataModel eventUcMetadataModel) {
          return new ResponseBean<>(eventUcMetadataModelService.findList(eventUcMetadataModel));
    }

    /**
     * 分页查询事件元数据-设备型号列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventUcMetadataModel
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询事件元数据-设备型号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventUcMetadataModel", value = "事件元数据-设备型号信息", dataType = "eventUcMetadataModel")
    })

    @GetMapping("eventUcMetadataModelList")
    public ResponseBean<PageInfo<EventUcMetadataModel>> eventUcMetadataModelList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                                 @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                                 @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                                 @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                                 EventUcMetadataModel eventUcMetadataModel) {
        eventUcMetadataModel.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataModelService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventUcMetadataModel));
    }

    /**
     * 新增事件元数据-设备型号
     *
     * @param eventUcMetadataModelDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增事件元数据-设备型号", notes = "新增事件元数据-设备型号")
   // @ApiImplicitParam(name = "eventUcMetadataModelDto", value = "eventUcMetadataModelDto", required = true, dataType = "eventUcMetadataModelDto")
    @PostMapping
    @Log("新增事件元数据-设备型号")
    public ResponseBean
            <Boolean> insertEventUcMetadataModel(@RequestBody @Valid EventUcMetadataModelDto eventUcMetadataModelDto) {
        EventUcMetadataModel eventUcMetadataModel = new EventUcMetadataModel();
        BeanUtils.copyProperties(eventUcMetadataModelDto, eventUcMetadataModel);
        eventUcMetadataModel.setId(IdGen.snowflakeId());
        eventUcMetadataModel.setCreateBy(SysUtil.getUser());
        eventUcMetadataModel.setCreateDate(new Date());
        return new ResponseBean<>(eventUcMetadataModelService.insert(eventUcMetadataModel) > 0);
    }

    /**
     * 修改事件元数据-设备型号
     *
     * @param eventUcMetadataModelDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新事件元数据-设备型号信息", notes = "根据事件元数据-设备型号ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcMetadataModelDto", value = "设备dto", required = true, paramType = "body", dataType = "eventUcMetadataModelDto"))
    @Log("修改事件元数据-设备型号")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventUcMetadataModel(@RequestBody @Valid EventUcMetadataModelDto eventUcMetadataModelDto) {
        EventUcMetadataModel eventUcMetadataModel = new EventUcMetadataModel();
        BeanUtils.copyProperties(eventUcMetadataModelDto, eventUcMetadataModel);
        eventUcMetadataModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataModelService.update(eventUcMetadataModel) > 0);
    }

    /**
     * 根据事件元数据-设备型号ID删除事件元数据-设备型号信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除事件元数据-设备型号信息", notes = "根据事件元数据-设备型号ID删除事件元数据-设备型号信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "事件元数据-设备型号ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventUcMetadataModelById(@PathVariable("id") String id) {
        EventUcMetadataModel eventUcMetadataModel = new EventUcMetadataModel();
        eventUcMetadataModel.setId(id);
        eventUcMetadataModel.setNewRecord(false);
        eventUcMetadataModel.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcMetadataModelService.delete(eventUcMetadataModel) > 0);
    }

    /**
     * 批量删除事件元数据-设备型号信息
     *
     * @param eventUcMetadataModel
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除事件元数据-设备型号", notes = "根据事件元数据-设备型号ID批量删除事件元数据-设备型号")
    @ApiImplicitParam(name = "eventUcMetadataModel", value = "事件元数据-设备型号信息", dataType = "eventUcMetadataModel")
    @Log("批量删除事件元数据-设备型号")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventUcMetadataModel(@RequestBody EventUcMetadataModel eventUcMetadataModel) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventUcMetadataModel.getIdString()))
                success = eventUcMetadataModelService.deleteAll(eventUcMetadataModel.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除事件元数据-设备型号失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
