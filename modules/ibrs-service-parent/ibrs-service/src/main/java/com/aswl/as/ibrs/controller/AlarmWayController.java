package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AlarmWayDto;
import com.aswl.as.ibrs.api.module.AlarmWay;
import com.aswl.as.ibrs.service.AlarmWayService;
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
 * 报警方式controller
 *
 * @author dingfei
 * @date 2019-11-06 09:51
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmWay", tags = "报警方式")
@RestController
@RequestMapping("/v1/alarmWay")
public class AlarmWayController extends BaseController {

    private final AlarmWayService alarmWayService;

    /**
     * 根据ID获取报警方式
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据报警方式ID查询报警方式")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警方式ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<AlarmWay> findById(@PathVariable("id") String id) {
        AlarmWay alarmWay = new AlarmWay();
        alarmWay.setId(id);
        return new ResponseBean<>(alarmWayService.get(alarmWay));
    }

    /**
     * 查询所有报警方式
     *
     * @param alarmWay
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有报警方式列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmWay", value = "报警方式对象", paramType = "path", required = true, dataType = "alarmWay"))
    @GetMapping(value = "alarmWays")
    public ResponseBean
            <List<AlarmWay>> findAll(AlarmWay alarmWay) {
        alarmWay.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWayService.findAllList(alarmWay));
    }

    /**
     * 分页查询报警方式列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param alarmWay
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询报警方式列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "alarmWay", value = "报警方式信息", dataType = "alarmWay")
    })

    @GetMapping("alarmWayList")
    public ResponseBean<PageInfo<AlarmWay>> alarmWayList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                           AlarmWay alarmWay) {
        return new ResponseBean<>(alarmWayService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), alarmWay));
    }

    /**
     * 新增报警方式
     *
     * @param alarmWayDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增报警方式", notes = "新增报警方式")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmWayDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmWayDto"))
    @PostMapping
    @Log("新增报警方式")
    public ResponseBean
            <Boolean> insertAlarmWay(@RequestBody @Valid AlarmWayDto alarmWayDto) {
        AlarmWay alarmWay = new AlarmWay();
        BeanUtils.copyProperties(alarmWayDto, alarmWay);
        alarmWay.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWayService.insert(alarmWay) > 0);
    }

    /**
     * 修改报警方式
     *
     * @param alarmWayDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新报警方式信息", notes = "根据报警方式ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "alarmWayDto", value = "设备dto", required = true, paramType = "body", dataType = "alarmWayDto"))
    @Log("修改报警方式")
    @PutMapping
    public ResponseBean
            <Boolean> updateAlarmWay(@RequestBody @Valid AlarmWayDto alarmWayDto) {
        AlarmWay alarmWay = new AlarmWay();
        BeanUtils.copyProperties(alarmWayDto, alarmWay);
        alarmWay.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWayService.update(alarmWay) > 0);
    }

    /**
     * 根据报警方式ID删除报警方式信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除报警方式信息", notes = "根据报警方式ID删除报警方式信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报警方式ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAlarmWayById(@PathVariable("id") String id) {
        AlarmWay alarmWay = new AlarmWay();
        alarmWay.setId(id);
        alarmWay.setNewRecord(false);
        alarmWay.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(alarmWayService.delete(alarmWay) > 0);
    }

    /**
     * 批量删除报警方式信息
     *
     * @param alarmWay
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除报警方式", notes = "根据报警方式ID批量删除报警方式")
    @ApiImplicitParam(name = "alarmWay", value = "报警方式信息", dataType = "alarmWay")
    @Log("批量删除报警方式")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAlarmWay(@RequestBody AlarmWay alarmWay) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(alarmWay.getIdString()))
                success = alarmWayService.deleteAll(alarmWay.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除报警方式失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
