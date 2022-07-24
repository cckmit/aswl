package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.EventSfpInfoDto;
import com.aswl.as.ibrs.api.module.EventSfpInfo;
import com.aswl.as.ibrs.service.EventSfpInfoService;
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
import com.aswl.as.common.core.enums.BusinessType;

/**
 * sfp信息controller
 *
 * @author df
 * @date 2021/07/26 22:09
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventSfpInfo", tags = "sfp信息")
@RestController
@RequestMapping("/v1/eventSfpInfo")
public class EventSfpInfoController extends BaseController {

    private final EventSfpInfoService eventSfpInfoService;

    /**
     * 根据ID获取sfp信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据sfp信息ID查询sfp信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "sfp信息ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventSfpInfo> findById(@PathVariable("id") String id) {
        EventSfpInfo eventSfpInfo = new EventSfpInfo();
        eventSfpInfo.setId(id);
        return new ResponseBean<>(eventSfpInfoService.get(eventSfpInfo));
    }

    /**
     * 查询所有sfp信息
     *
     * @param eventSfpInfo
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有sfp信息列表")
    @ApiImplicitParam(name = "eventSfpInfo", value = "sfp信息对象", paramType = "path", required = true, dataType = "eventSfpInfo")
    @GetMapping(value = "eventSfpInfos")
    public ResponseBean
            <List<EventSfpInfo>> findAll(EventSfpInfo eventSfpInfo) {
        eventSfpInfo.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpInfoService.findList(eventSfpInfo));
    }

    /**
     * 分页查询sfp信息列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventSfpInfo
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询sfp信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventSfpInfo", value = "sfp信息信息", dataType = "eventSfpInfo")
    })

    @GetMapping("eventSfpInfoList")
    public ResponseBean
            <PageInfo<EventSfpInfo>> eventSfpInfoList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                      @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                      @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                      @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                      EventSfpInfo eventSfpInfo) {
        eventSfpInfo.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpInfoService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventSfpInfo));
    }

    /**
     * 新增sfp信息
     *
     * @param eventSfpInfoDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增sfp信息", notes = "新增sfp信息")
    @Log(value = "新增sfp信息", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertEventSfpInfo(@RequestBody @Valid EventSfpInfoDto eventSfpInfoDto) {
        EventSfpInfo eventSfpInfo = new EventSfpInfo();
        BeanUtils.copyProperties(eventSfpInfoDto, eventSfpInfo);
        eventSfpInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpInfoService.insert(eventSfpInfo) > 0);
    }

    /**
     * 修改sfp信息
     *
     * @param eventSfpInfoDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新sfp信息信息", notes = "根据sfp信息ID更新sfp信息信息")
    @Log(value = "更新sfp信息", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateEventSfpInfo(@RequestBody @Valid EventSfpInfoDto eventSfpInfoDto) {
        EventSfpInfo eventSfpInfo = new EventSfpInfo();
        BeanUtils.copyProperties(eventSfpInfoDto, eventSfpInfo);
        eventSfpInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpInfoService.update(eventSfpInfo) > 0);
    }

    /**
     * 根据sfp信息ID删除sfp信息信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除sfp信息信息", notes = "根据sfp信息ID删除sfp信息信息")
    @ApiImplicitParam(name = "id", value = "sfp信息ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除sfp信息", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteEventSfpInfoById(@PathVariable("id") String id) {
        EventSfpInfo eventSfpInfo = new EventSfpInfo();
        eventSfpInfo.setId(id);
        eventSfpInfo.setNewRecord(false);
        eventSfpInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSfpInfoService.delete(eventSfpInfo) > 0);
    }

    /**
     * 批量删除sfp信息信息
     *
     * @param eventSfpInfo
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除sfp信息", notes = "根据sfp信息ID批量删除sfp信息")
    @ApiImplicitParam(name = "eventSfpInfo", value = "sfp信息信息", dataType = "eventSfpInfo")
    @Log(value = "删除sfp信息", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllEventSfpInfo(@RequestBody EventSfpInfo eventSfpInfo) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventSfpInfo.getIdString()))
                success = eventSfpInfoService.deleteAll(eventSfpInfo.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除sfp信息失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
