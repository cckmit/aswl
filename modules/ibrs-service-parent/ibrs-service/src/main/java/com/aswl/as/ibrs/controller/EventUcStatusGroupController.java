package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.vo.OsVo;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.EventUcStatusGroupDto;
import com.aswl.as.ibrs.api.module.EventUcStatusGroup;
import com.aswl.as.ibrs.api.vo.EventUcMetadataVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
import com.aswl.as.ibrs.api.vo.EventUcStatusGroupModelVo;
import com.aswl.as.ibrs.service.EventUcStatusGroupService;
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
 * 事件状态组controller
 *
 * @author dingfei
 * @date 2019-10-22 11:45
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventUcStatusGroup", tags = "事件状态组")
@RestController
@RequestMapping("/v1/eventUcStatusGroup")
public class EventUcStatusGroupController extends BaseController {

    private final EventUcStatusGroupService eventUcStatusGroupService;

    /**
     * 根据ID获取事件状态组
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据事件状态组ID查询事件状态组")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "事件状态组ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventUcStatusGroup> findById(@PathVariable("id") String id) {
        EventUcStatusGroup eventUcStatusGroup = new EventUcStatusGroup();
        eventUcStatusGroup.setId(id);
        return new ResponseBean<>(eventUcStatusGroupService.get(eventUcStatusGroup));
    }

    /**
     * 查询所有事件状态组
     *
     * @param eventUcStatusGroup
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有事件状态组列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcStatusGroup", value = "事件状态组对象", paramType = "path", required = true, dataType = "eventUcStatusGroup"))
    @GetMapping(value = "eventUcStatusGroups")
    public ResponseBean
            <List<EventUcStatusGroup>> findAll(EventUcStatusGroup eventUcStatusGroup) {
        eventUcStatusGroup.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusGroupService.findAllList(eventUcStatusGroup));
    }

    /**
     * 分页查询事件状态组列表
     *
     * @param pageNum
     * @param pageSize
     * @param eventUcStatusGroup
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询事件状态组列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventUcStatusGroup", value = "事件状态组信息", dataType = "eventUcStatusGroup")
    })

    @GetMapping("eventUcStatusGroupList")
    public ResponseBean<PageInfo<EventUcStatusGroup>> eventUcStatusGroupList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                             EventUcStatusGroup eventUcStatusGroup) {
        eventUcStatusGroup.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusGroupService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), eventUcStatusGroup));
    }

    /**
     * 新增事件状态组
     *
     * @param eventUcStatusGroupDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增事件状态组", notes = "新增事件状态组")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcStatusGroupDto", value = "设备dto", required = true, paramType = "body", dataType = "eventUcStatusGroupDto"))
    @PostMapping
    @Log("新增事件状态组")
    public ResponseBean
            <Boolean> insertEventUcStatusGroup(@RequestBody @Valid EventUcStatusGroupDto eventUcStatusGroupDto) {
        EventUcStatusGroup eventUcStatusGroup = new EventUcStatusGroup();
        BeanUtils.copyProperties(eventUcStatusGroupDto, eventUcStatusGroup);
        eventUcStatusGroup.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusGroupService.insert(eventUcStatusGroup) > 0);
    }

    /**
     * 修改事件状态组
     *
     * @param eventUcStatusGroupDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新事件状态组信息", notes = "根据事件状态组ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "eventUcStatusGroupDto", value = "设备dto", required = true, paramType = "body", dataType = "eventUcStatusGroupDto"))
    @Log("修改事件状态组")
    @PutMapping
    public ResponseBean
            <Boolean> updateEventUcStatusGroup(@RequestBody @Valid EventUcStatusGroupDto eventUcStatusGroupDto) {
        EventUcStatusGroup eventUcStatusGroup = new EventUcStatusGroup();
        BeanUtils.copyProperties(eventUcStatusGroupDto, eventUcStatusGroup);
        eventUcStatusGroup.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusGroupService.update(eventUcStatusGroup) > 0);
    }

    /**
     * 根据事件状态组ID删除事件状态组信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除事件状态组信息", notes = "根据事件状态组ID删除事件状态组信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "事件状态组ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteEventUcStatusGroupById(@PathVariable("id") String id) {
        EventUcStatusGroup eventUcStatusGroup = new EventUcStatusGroup();
        eventUcStatusGroup.setId(id);
        eventUcStatusGroup.setNewRecord(false);
        eventUcStatusGroup.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventUcStatusGroupService.delete(eventUcStatusGroup) > 0);
    }

    /**
     * 批量删除事件状态组信息
     *
     * @param eventUcStatusGroup
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除事件状态组", notes = "根据事件状态组ID批量删除事件状态组")
    @ApiImplicitParam(name = "eventUcStatusGroup", value = "事件状态组信息", dataType = "eventUcStatusGroup")
    @Log("批量删除事件状态组")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllEventUcStatusGroup(@RequestBody EventUcStatusGroup eventUcStatusGroup) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventUcStatusGroup.getIdString()))
                success = eventUcStatusGroupService.deleteAll(eventUcStatusGroup.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除事件状态组失败！", e);
        }
        return new ResponseBean<>(success);
    }

    @ApiOperation(value = "根据设备型号ID查询时间状态组数据", notes = "根据设备型号ID查询时间状态组数据")
    @ApiImplicitParam(name = "deviceModelId", value = "型号ID", dataType = "String")
    @Log("根据设备型号ID查询事件状态组数据")
    @GetMapping("extendStatusGroup")
    public ResponseBean
            <List<EventUcStatusGroupModelVo>> getExtendStatusGroup(@RequestParam("deviceModelId") String deviceModelId) {

             return  new ResponseBean<>(eventUcStatusGroupService.getExtendStatusGroup(deviceModelId));
    }

    @ApiOperation(value = "根据设备型号ID和状态组ID查询事件状态组属性", notes = "根据设备型号ID查询事件状态组属性")
    @Log("根据设备型号ID和状态组ID查询事件状态组属性")
    @GetMapping("extendStatusGroupAttribute")
    public ResponseBean
            <List<EventUcMetadataVo>> getExtendStatusGroupAttribute(@RequestParam("deviceModelId") String deviceModelId, @RequestParam("groupId") String groupId) {

        return  new ResponseBean<>(eventUcStatusGroupService.getExtendStatusGroupAttribute(deviceModelId,groupId));
    }


    @ApiOperation(value = "根据设备型号ID已选择操作列表", notes = "根据设备型号ID已选择操作列表")
    @Log("根据设备型号ID已选择操作列表")
    @GetMapping("extendSelectStatusGroupOperationList")
    public ResponseBean
            <List<EventUcStatusOperationVo>> getSelectExtendStatusGroupOperationList(@RequestParam("eventMetadataModelId") String eventMetadataModelId) {

        return  new ResponseBean<>(eventUcStatusGroupService.getSelectExtendStatusGroupOperationList(eventMetadataModelId));
    }


    @ApiOperation(value = "根据元数据ID查询状态操作列表", notes = "根据元数据ID查询状态操作列表")
    @Log("根据元数据ID查询状态操作列表")
    @GetMapping("extendStatusGroupOperationList")
    public ResponseBean
            <List<EventUcStatusOperationVo>> getExtendStatusGroupOperationList(@RequestParam("eventMetadataId") String eventMetadataId) {

        return  new ResponseBean<>(eventUcStatusGroupService.getExtendStatusGroupOperationList(eventMetadataId));
    }

    //---------------- 下面是提供给运营端使用的接口 TODO 需要提供事件状态组的接口     ---------------------------

    @ApiOperation(value = "运营端根据元数据ID查询状态操作列表", notes = "运营端根据元数据ID查询状态操作列表")
    @Log("运营端 根据元数据ID查询状态操作列表")
    @GetMapping("/os/extendStatusGroupOperationList")
    public ResponseBean<List<EventUcStatusOperationVo>> osEvent1(@RequestParam("eventMetadataId") String eventMetadataId,
                                                                 @RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr)
    {
        if(OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return getExtendStatusGroupOperationList(eventMetadataId);
    }

    @ApiOperation(value = "运营端根据设备型号ID查询时间状态组数据", notes = "运营端根据设备型号ID查询时间状态组数据")
    @ApiImplicitParam(name = "deviceModelId", value = "型号ID", dataType = "String")
    @Log("运营端根据设备型号ID查询事件状态组数据")
    @GetMapping("/os/extendStatusGroup")
    public ResponseBean<List<EventUcStatusGroupModelVo>> osEvent2(@RequestParam("deviceModelId") String deviceModelId,
                                                                  @RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr) {
        if(OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return getExtendStatusGroup(deviceModelId);
    }

    @ApiOperation(value = "根据设备型号ID和状态组ID查询事件状态组属性", notes = "根据设备型号ID查询事件状态组属性")
    @Log("根据设备型号ID和状态组ID查询事件状态组属性")
    @GetMapping("/os/extendStatusGroupAttribute")
    public ResponseBean
            <List<EventUcMetadataVo>> osEvent3(@RequestParam("deviceModelId") String deviceModelId, @RequestParam("groupId") String groupId
            ,@RequestParam(value = OsVo.CHECK_STRING,required = false,defaultValue = "") String randomStr) {

        if(OsVo.isWrongRandomStr(randomStr)) return OsVo.ERR_RESPONSE_BEAN;

        return  getExtendStatusGroupAttribute(deviceModelId,groupId);
    }

    /**
     * eventUcStatusGroup/extendStatusGroup
     * eventUcStatusGroup/extendStatusGroupAttribute
     * eventUcStatusGroup/extendStatusGroupOperationList
     *
     * eventUcMetadata/findUcMetadataByDeviceModelId
     * deviceModelAlarmThreshold/deviceModelAlarmThresholds
     */




}
