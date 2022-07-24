package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.EventSecCtlPowerDto;
import com.aswl.as.ibrs.api.module.EventSecCtlPower;
import com.aswl.as.ibrs.service.EventSecCtlPowerService;
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
 * 设备分控板-电源controller
 *
 * @author df
 * @date 2021/07/26 19:28
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/eventSecCtlPower", tags = "设备分控板-电源")
@RestController
@RequestMapping("/v1/eventSecCtlPower")
public class EventSecCtlPowerController extends BaseController {

    private final EventSecCtlPowerService eventSecCtlPowerService;

    /**
     * 根据ID获取设备分控板-电源
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据设备分控板-电源ID查询设备分控板-电源")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "设备分控板-电源ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<EventSecCtlPower> findById(@PathVariable("id") String id) {
        EventSecCtlPower eventSecCtlPower = new EventSecCtlPower();
        eventSecCtlPower.setId(id);
        return new ResponseBean<>(eventSecCtlPowerService.get(eventSecCtlPower));
    }

    /**
     * 查询所有设备分控板-电源
     *
     * @param eventSecCtlPower
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有设备分控板-电源列表")
    @ApiImplicitParam(name = "eventSecCtlPower", value = "设备分控板-电源对象", paramType = "path", required = true, dataType = "eventSecCtlPower")
    @GetMapping(value = "eventSecCtlPowers")
    public ResponseBean
            <List<EventSecCtlPower>> findAll(EventSecCtlPower eventSecCtlPower) {
        eventSecCtlPower.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerService.findList(eventSecCtlPower));
    }

    /**
     * 分页查询设备分控板-电源列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param eventSecCtlPower
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询设备分控板-电源列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "eventSecCtlPower", value = "设备分控板-电源信息", dataType = "eventSecCtlPower")
    })

    @GetMapping("eventSecCtlPowerList")
    public ResponseBean
            <PageInfo<EventSecCtlPower>> eventSecCtlPowerList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                              @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                              @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                              @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                              EventSecCtlPower eventSecCtlPower) {
        eventSecCtlPower.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), eventSecCtlPower));
    }

    /**
     * 新增设备分控板-电源
     *
     * @param eventSecCtlPowerDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增设备分控板-电源", notes = "新增设备分控板-电源")
    @Log(value = "新增设备分控板-电源", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertEventSecCtlPower(@RequestBody @Valid EventSecCtlPowerDto eventSecCtlPowerDto) {
        EventSecCtlPower eventSecCtlPower = new EventSecCtlPower();
        BeanUtils.copyProperties(eventSecCtlPowerDto, eventSecCtlPower);
        eventSecCtlPower.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerService.insert(eventSecCtlPower) > 0);
    }

    /**
     * 修改设备分控板-电源
     *
     * @param eventSecCtlPowerDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新设备分控板-电源信息", notes = "根据设备分控板-电源ID更新设备分控板-电源信息")
    @Log(value = "更新设备分控板-电源", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateEventSecCtlPower(@RequestBody @Valid EventSecCtlPowerDto eventSecCtlPowerDto) {
        EventSecCtlPower eventSecCtlPower = new EventSecCtlPower();
        BeanUtils.copyProperties(eventSecCtlPowerDto, eventSecCtlPower);
        eventSecCtlPower.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerService.update(eventSecCtlPower) > 0);
    }

    /**
     * 根据设备分控板-电源ID删除设备分控板-电源信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除设备分控板-电源信息", notes = "根据设备分控板-电源ID删除设备分控板-电源信息")
    @ApiImplicitParam(name = "id", value = "设备分控板-电源ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除设备分控板-电源", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteEventSecCtlPowerById(@PathVariable("id") String id) {
        EventSecCtlPower eventSecCtlPower = new EventSecCtlPower();
        eventSecCtlPower.setId(id);
        eventSecCtlPower.setNewRecord(false);
        eventSecCtlPower.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(eventSecCtlPowerService.delete(eventSecCtlPower) > 0);
    }

    /**
     * 批量删除设备分控板-电源信息
     *
     * @param eventSecCtlPower
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除设备分控板-电源", notes = "根据设备分控板-电源ID批量删除设备分控板-电源")
    @ApiImplicitParam(name = "eventSecCtlPower", value = "设备分控板-电源信息", dataType = "eventSecCtlPower")
    @Log(value = "删除设备分控板-电源", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllEventSecCtlPower(@RequestBody EventSecCtlPower eventSecCtlPower) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(eventSecCtlPower.getIdString()))
                success = eventSecCtlPowerService.deleteAll(eventSecCtlPower.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除设备分控板-电源失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
