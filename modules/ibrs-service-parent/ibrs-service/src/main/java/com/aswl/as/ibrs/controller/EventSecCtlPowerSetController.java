package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.EventSecCtlPowerSetDto;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerSet;
import com.aswl.as.ibrs.service.EventSecCtlPowerSetService;
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
 * 设备分控板-电源设置controller
 *
 * @author df
 * @date 2021/07/26 19:34
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventSecCtlPowerSet", tags = "设备分控板-电源设置")
@RestController
@RequestMapping("/v1/eventSecCtlPowerSet")
public class EventSecCtlPowerSetController extends BaseController {

    private final EventSecCtlPowerSetService eventSecCtlPowerSetService;

    /**
     * 根据ID获取设备分控板-电源设置
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备分控板-电源设置ID查询设备分控板-电源设置")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备分控板-电源设置ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventSecCtlPowerSet> findById(@PathVariable("id") String id) {
        EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
        eventSecCtlPowerSet.setId(id);
        return new ResponseBean<>(eventSecCtlPowerSetService.get(eventSecCtlPowerSet));
    }

    /**
     * 查询所有设备分控板-电源设置
     *
     * @param eventSecCtlPowerSet
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备分控板-电源设置列表")
    @ApiImplicitParam(name = "eventSecCtlPowerSet", value = "设备分控板-电源设置对象", paramType = "path", required = true, dataType = "eventSecCtlPowerSet")
    @GetMapping(value = "eventSecCtlPowerSets")
    public ResponseBean
            <List<EventSecCtlPowerSet>> findAll(EventSecCtlPowerSet eventSecCtlPowerSet) {
        eventSecCtlPowerSet.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerSetService.findList(eventSecCtlPowerSet));
    }

    /**
     * 分页查询设备分控板-电源设置列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventSecCtlPowerSet
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备分控板-电源设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventSecCtlPowerSet", value = "设备分控板-电源设置信息", dataType = "eventSecCtlPowerSet")
    })

    @GetMapping("eventSecCtlPowerSetList")
    public ResponseBean
            <PageInfo<EventSecCtlPowerSet>> eventSecCtlPowerSetList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                    EventSecCtlPowerSet eventSecCtlPowerSet) {
        eventSecCtlPowerSet.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerSetService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventSecCtlPowerSet));
    }

    /**
     * 新增设备分控板-电源设置
     *
     * @param eventSecCtlPowerSetDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备分控板-电源设置", notes = "新增设备分控板-电源设置")
    @Log(value = "新增设备分控板-电源设置", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertEventSecCtlPowerSet(@RequestBody @Valid EventSecCtlPowerSetDto eventSecCtlPowerSetDto) {
        EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
        BeanUtils.copyProperties(eventSecCtlPowerSetDto, eventSecCtlPowerSet);
        eventSecCtlPowerSet.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerSetService.insert(eventSecCtlPowerSet) > 0);
    }

    /**
     * 修改设备分控板-电源设置
     *
     * @param eventSecCtlPowerSetDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备分控板-电源设置信息", notes = "根据设备分控板-电源设置ID更新设备分控板-电源设置信息")
    @Log(value = "更新设备分控板-电源设置", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateEventSecCtlPowerSet(@RequestBody @Valid EventSecCtlPowerSetDto eventSecCtlPowerSetDto) {
        EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
        BeanUtils.copyProperties(eventSecCtlPowerSetDto, eventSecCtlPowerSet);
        eventSecCtlPowerSet.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerSetService.update(eventSecCtlPowerSet) > 0);
    }

    /**
     * 根据设备分控板-电源设置ID删除设备分控板-电源设置信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备分控板-电源设置信息", notes = "根据设备分控板-电源设置ID删除设备分控板-电源设置信息")
    @ApiImplicitParam(name = "id", value = "设备分控板-电源设置ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除设备分控板-电源设置", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteEventSecCtlPowerSetById(@PathVariable("id") String id) {
        EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
        eventSecCtlPowerSet.setId(id);
        eventSecCtlPowerSet.setNewRecord(false);
        eventSecCtlPowerSet.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerSetService.delete(eventSecCtlPowerSet) > 0);
    }

    /**
     * 批量删除设备分控板-电源设置信息
     *
     * @param eventSecCtlPowerSet
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备分控板-电源设置", notes = "根据设备分控板-电源设置ID批量删除设备分控板-电源设置")
    @ApiImplicitParam(name = "eventSecCtlPowerSet", value = "设备分控板-电源设置信息", dataType = "eventSecCtlPowerSet")
    @Log(value = "删除设备分控板-电源设置", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllEventSecCtlPowerSet(@RequestBody EventSecCtlPowerSet eventSecCtlPowerSet) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventSecCtlPowerSet.getIdString()))
                success = eventSecCtlPowerSetService.deleteAll(eventSecCtlPowerSet.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备分控板-电源设置失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
