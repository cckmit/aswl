package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.dto.CityRunTimeStatisticsDto;
import com.aswl.as.ibrs.api.module.CityRunTimeStatistics;
import com.aswl.as.ibrs.service.CityRunTimeStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;

import javax.validation.Valid;
import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * 市级平台各时段工单趋势统计controller
 *
 * @author hzj
 * @date 2021/01/22 15:57
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/cityRunTimeStatistics", tags = "市级平台各时段工单趋势统计")
@RestController
@RequestMapping("/v1/cityRunTimeStatistics")
public class CityRunTimeStatisticsController extends BaseController {

    private final CityRunTimeStatisticsService cityRunTimeStatisticsService;

    /**
     * 根据ID获取市级平台各时段工单趋势统计
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据市级平台各时段工单趋势统计ID查询市级平台各时段工单趋势统计")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "市级平台各时段工单趋势统计ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<CityRunTimeStatistics> findById(@PathVariable("id") String id) {
        CityRunTimeStatistics cityRunTimeStatistics = new CityRunTimeStatistics();
        cityRunTimeStatistics.setId(id);
        return new ResponseBean<>(cityRunTimeStatisticsService.get(cityRunTimeStatistics));
    }

    /**
     * 查询所有市级平台各时段工单趋势统计
     *
     * @param cityRunTimeStatistics
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有市级平台各时段工单趋势统计列表")
    @ApiImplicitParam(name = "cityRunTimeStatistics", value = "市级平台各时段工单趋势统计对象", paramType = "path", required = true, dataType = "cityRunTimeStatistics")
    @GetMapping(value = "cityRunTimeStatisticss")
    public ResponseBean
            <List<CityRunTimeStatistics>> findAll(CityRunTimeStatistics cityRunTimeStatistics) {
        cityRunTimeStatistics.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(cityRunTimeStatisticsService.findList(cityRunTimeStatistics));
    }

    /**
     * 分页查询市级平台各时段工单趋势统计列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param cityRunTimeStatistics
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询市级平台各时段工单趋势统计列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityRunTimeStatistics", value = "市级平台各时段工单趋势统计信息", dataType = "cityRunTimeStatistics")
    })

    @GetMapping("cityRunTimeStatisticsList")
    public ResponseBean
            <PageInfo<CityRunTimeStatistics>> cityRunTimeStatisticsList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                        @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                        @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                        @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                        CityRunTimeStatistics cityRunTimeStatistics) {
        cityRunTimeStatistics.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(cityRunTimeStatisticsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), cityRunTimeStatistics));
    }

    /**
     * 新增市级平台各时段工单趋势统计
     *
     * @param cityRunTimeStatisticsDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增市级平台各时段工单趋势统计", notes = "新增市级平台各时段工单趋势统计")
    @PostMapping
    @Log("新增市级平台各时段工单趋势统计")
    public ResponseBean
            <Boolean> insertCityRunTimeStatistics(@RequestBody @Valid CityRunTimeStatisticsDto cityRunTimeStatisticsDto) {
        CityRunTimeStatistics cityRunTimeStatistics = new CityRunTimeStatistics();
        BeanUtils.copyProperties(cityRunTimeStatisticsDto, cityRunTimeStatistics);
        cityRunTimeStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityRunTimeStatisticsService.insert(cityRunTimeStatistics) > 0);
    }

    /**
     * 修改市级平台各时段工单趋势统计
     *
     * @param cityRunTimeStatisticsDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新市级平台各时段工单趋势统计信息", notes = "根据市级平台各时段工单趋势统计ID更新市级平台各时段工单趋势统计信息")
    @Log("修改市级平台各时段工单趋势统计")
    @PutMapping
    public ResponseBean
            <Boolean> updateCityRunTimeStatistics(@RequestBody @Valid CityRunTimeStatisticsDto cityRunTimeStatisticsDto) {
        CityRunTimeStatistics cityRunTimeStatistics = new CityRunTimeStatistics();
        BeanUtils.copyProperties(cityRunTimeStatisticsDto, cityRunTimeStatistics);
        cityRunTimeStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityRunTimeStatisticsService.update(cityRunTimeStatistics) > 0);
    }

    /**
     * 根据市级平台各时段工单趋势统计ID删除市级平台各时段工单趋势统计信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除市级平台各时段工单趋势统计信息", notes = "根据市级平台各时段工单趋势统计ID删除市级平台各时段工单趋势统计信息")
    @ApiImplicitParam(name = "id", value = "市级平台各时段工单趋势统计ID", paramType = "path", required = true, dataType =
            "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteCityRunTimeStatisticsById(@PathVariable("id") String id) {
        CityRunTimeStatistics cityRunTimeStatistics = new CityRunTimeStatistics();
        cityRunTimeStatistics.setId(id);
        cityRunTimeStatistics.setNewRecord(false);
        cityRunTimeStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityRunTimeStatisticsService.delete(cityRunTimeStatistics) > 0);
    }

    /**
     * 批量删除市级平台各时段工单趋势统计信息
     *
     * @param cityRunTimeStatistics
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除市级平台各时段工单趋势统计", notes = "根据市级平台各时段工单趋势统计ID批量删除市级平台各时段工单趋势统计")
    @ApiImplicitParam(name = "cityRunTimeStatistics", value = "市级平台各时段工单趋势统计信息", dataType = "cityRunTimeStatistics")
    @Log("批量删除市级平台各时段工单趋势统计")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllCityRunTimeStatistics(@RequestBody CityRunTimeStatistics cityRunTimeStatistics) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(cityRunTimeStatistics.getIdString()))
                success = cityRunTimeStatisticsService.deleteAll(cityRunTimeStatistics.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除市级平台各时段工单趋势统计失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 各时段的派单率和修复率
     */
    @ApiOperation("各时段的派单率和修复率")
    @GetMapping("periodRun")
    public ResponseBean<CityRunTimeStatistics> periodRun(CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        return new ResponseBean<>(cityRunTimeStatisticsService.periodRun(cityAlarmStatisticsDto));
    }

}
