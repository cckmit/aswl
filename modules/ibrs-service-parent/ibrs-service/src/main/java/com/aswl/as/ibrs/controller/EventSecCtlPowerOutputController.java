package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.EventSecCtlPowerOutputDto;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerOutput;
import com.aswl.as.ibrs.service.EventSecCtlPowerOutputService;
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
 * 设备分控板-电源输出controller
 *
 * @author df
 * @date 2021/07/26 19:31
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventSecCtlPowerOutput", tags = "设备分控板-电源输出")
@RestController
@RequestMapping("/v1/eventSecCtlPowerOutput")
public class EventSecCtlPowerOutputController extends BaseController {

    private final EventSecCtlPowerOutputService eventSecCtlPowerOutputService;

    /**
     * 根据ID获取设备分控板-电源输出
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备分控板-电源输出ID查询设备分控板-电源输出")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备分控板-电源输出ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventSecCtlPowerOutput> findById(@PathVariable("id") String id) {
        EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
        eventSecCtlPowerOutput.setId(id);
        return new ResponseBean<>(eventSecCtlPowerOutputService.get(eventSecCtlPowerOutput));
    }

    /**
     * 查询所有设备分控板-电源输出
     *
     * @param eventSecCtlPowerOutput
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备分控板-电源输出列表")
    @ApiImplicitParam(name = "eventSecCtlPowerOutput", value = "设备分控板-电源输出对象", paramType = "path", required = true, dataType = "eventSecCtlPowerOutput")
    @GetMapping(value = "eventSecCtlPowerOutputs")
    public ResponseBean
            <List<EventSecCtlPowerOutput>> findAll(EventSecCtlPowerOutput eventSecCtlPowerOutput) {
        eventSecCtlPowerOutput.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerOutputService.findList(eventSecCtlPowerOutput));
    }

    /**
     * 分页查询设备分控板-电源输出列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventSecCtlPowerOutput
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备分控板-电源输出列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventSecCtlPowerOutput", value = "设备分控板-电源输出信息", dataType = "eventSecCtlPowerOutput")
    })

    @GetMapping("eventSecCtlPowerOutputList")
    public ResponseBean
            <PageInfo<EventSecCtlPowerOutput>> eventSecCtlPowerOutputList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                          @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                          @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                          @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                          EventSecCtlPowerOutput eventSecCtlPowerOutput) {
        eventSecCtlPowerOutput.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerOutputService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventSecCtlPowerOutput));
    }

    /**
     * 新增设备分控板-电源输出
     *
     * @param eventSecCtlPowerOutputDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备分控板-电源输出", notes = "新增设备分控板-电源输出")
    @Log(value = "新增设备分控板-电源输出", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertEventSecCtlPowerOutput(@RequestBody @Valid EventSecCtlPowerOutputDto eventSecCtlPowerOutputDto) {
        EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
        BeanUtils.copyProperties(eventSecCtlPowerOutputDto, eventSecCtlPowerOutput);
        eventSecCtlPowerOutput.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerOutputService.insert(eventSecCtlPowerOutput) > 0);
    }

    /**
     * 修改设备分控板-电源输出
     *
     * @param eventSecCtlPowerOutputDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备分控板-电源输出信息", notes = "根据设备分控板-电源输出ID更新设备分控板-电源输出信息")
    @Log(value = "更新设备分控板-电源输出", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateEventSecCtlPowerOutput(@RequestBody @Valid EventSecCtlPowerOutputDto eventSecCtlPowerOutputDto) {
        EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
        BeanUtils.copyProperties(eventSecCtlPowerOutputDto, eventSecCtlPowerOutput);
        eventSecCtlPowerOutput.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerOutputService.update(eventSecCtlPowerOutput) > 0);
    }

    /**
     * 根据设备分控板-电源输出ID删除设备分控板-电源输出信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备分控板-电源输出信息", notes = "根据设备分控板-电源输出ID删除设备分控板-电源输出信息")
    @ApiImplicitParam(name = "id", value = "设备分控板-电源输出ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除设备分控板-电源输出", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteEventSecCtlPowerOutputById(@PathVariable("id") String id) {
        EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
        eventSecCtlPowerOutput.setId(id);
        eventSecCtlPowerOutput.setNewRecord(false);
        eventSecCtlPowerOutput.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerOutputService.delete(eventSecCtlPowerOutput) > 0);
    }

    /**
     * 批量删除设备分控板-电源输出信息
     *
     * @param eventSecCtlPowerOutput
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备分控板-电源输出", notes = "根据设备分控板-电源输出ID批量删除设备分控板-电源输出")
    @ApiImplicitParam(name = "eventSecCtlPowerOutput", value = "设备分控板-电源输出信息", dataType = "eventSecCtlPowerOutput")
    @Log(value = "删除设备分控板-电源输出", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllEventSecCtlPowerOutput(@RequestBody EventSecCtlPowerOutput eventSecCtlPowerOutput) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventSecCtlPowerOutput.getIdString()))
                success = eventSecCtlPowerOutputService.deleteAll(eventSecCtlPowerOutput.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备分控板-电源输出失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
