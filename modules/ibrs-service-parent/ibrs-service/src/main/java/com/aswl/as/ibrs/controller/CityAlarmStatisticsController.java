package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.CityAlarmStatistics;
import com.aswl.as.ibrs.api.vo.MaintenanceStatisticsVo;
import com.aswl.as.ibrs.mq.CityPlatformListener;
import com.aswl.as.ibrs.service.CityAlarmStatisticsService;
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
import javax.xml.ws.Response;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.enums.BusinessType;

/**
 * 故障统计表controller
 *
 * @author df
 * @date 2021/01/15 11:21
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/cityAlarmStatistics", tags = "故障统计表")
@RestController
@RequestMapping("/v1/cityAlarmStatistics")
public class CityAlarmStatisticsController extends BaseController {

    private final CityAlarmStatisticsService cityAlarmStatisticsService;

    /**
     * 根据ID获取故障统计表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据故障统计表ID查询故障统计表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "故障统计表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<CityAlarmStatistics> findById(@PathVariable("id") String id) {
        CityAlarmStatistics cityAlarmStatistics = new CityAlarmStatistics();
        cityAlarmStatistics.setId(id);
        return new ResponseBean<>(cityAlarmStatisticsService.get(cityAlarmStatistics));
    }

    /**
     * 查询所有故障统计表
     *
     * @param cityAlarmStatistics
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有故障统计表列表")
    @ApiImplicitParam(name = "cityAlarmStatistics", value = "故障统计表对象", paramType = "path", required = true, dataType = "cityAlarmStatistics")
    @GetMapping(value = "cityAlarmStatisticss")
    public ResponseBean
            <List<CityAlarmStatistics>> findAll(CityAlarmStatistics cityAlarmStatistics) {
        cityAlarmStatistics.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(cityAlarmStatisticsService.findList(cityAlarmStatistics));
    }

    /**
     * 分页查询故障统计表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param cityAlarmStatistics
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询故障统计表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityAlarmStatistics", value = "故障统计表信息", dataType = "cityAlarmStatistics")
    })

    @GetMapping("cityAlarmStatisticsList")
    public ResponseBean
            <PageInfo<CityAlarmStatistics>> cityAlarmStatisticsList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                    @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                    @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                    CityAlarmStatistics cityAlarmStatistics) {
        cityAlarmStatistics.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(cityAlarmStatisticsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), cityAlarmStatistics));
    }

    /**
     * 新增故障统计表
     *
     * @param cityAlarmStatisticsDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增故障统计表", notes = "新增故障统计表")
    @Log(value = "新增故障统计表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertCityAlarmStatistics(@RequestBody @Valid CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        CityAlarmStatistics cityAlarmStatistics = new CityAlarmStatistics();
        BeanUtils.copyProperties(cityAlarmStatisticsDto, cityAlarmStatistics);
        cityAlarmStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityAlarmStatisticsService.insert(cityAlarmStatistics) > 0);
    }

    /**
     * 修改故障统计表
     *
     * @param cityAlarmStatisticsDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新故障统计表信息", notes = "根据故障统计表ID更新故障统计表信息")
    @Log(value = "更新故障统计表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateCityAlarmStatistics(@RequestBody @Valid CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        CityAlarmStatistics cityAlarmStatistics = new CityAlarmStatistics();
        BeanUtils.copyProperties(cityAlarmStatisticsDto, cityAlarmStatistics);
        cityAlarmStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityAlarmStatisticsService.update(cityAlarmStatistics) > 0);
    }

    /**
     * 根据故障统计表ID删除故障统计表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除故障统计表信息", notes = "根据故障统计表ID删除故障统计表信息")
    @ApiImplicitParam(name = "id", value = "故障统计表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除故障统计表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteCityAlarmStatisticsById(@PathVariable("id") String id) {
        CityAlarmStatistics cityAlarmStatistics = new CityAlarmStatistics();
        cityAlarmStatistics.setId(id);
        cityAlarmStatistics.setNewRecord(false);
        cityAlarmStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityAlarmStatisticsService.delete(cityAlarmStatistics) > 0);
    }

    /**
     * 批量删除故障统计表信息
     *
     * @param cityAlarmStatistics
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除故障统计表", notes = "根据故障统计表ID批量删除故障统计表")
    @ApiImplicitParam(name = "cityAlarmStatistics", value = "故障统计表信息", dataType = "cityAlarmStatistics")
    @Log(value = "删除故障统计表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllCityAlarmStatistics(@RequestBody CityAlarmStatistics cityAlarmStatistics) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(cityAlarmStatistics.getIdString()))
                success = cityAlarmStatisticsService.deleteAll(cityAlarmStatistics.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除故障统计表失败！", e);
        }
        return new ResponseBean<>(success);
    }



    /**
     * 市级平台派单率和修复率
     */
    @ApiOperation("市级平台派单率和修复率")
    @GetMapping("cityPlatDistrRate")
    public ResponseBean<CityAlarmStatistics> cityPlatDistrRate(CityAlarmStatisticsDto cityAlarmStatisticsDto){
        return new ResponseBean<>(cityAlarmStatisticsService.cityPlatDistrRate(cityAlarmStatisticsDto));
    }

    /**
     * 告警类型派单占比
     */
    @ApiOperation("市级平台各告警类型派单占比")
    @GetMapping("cityPlatAlarmTypeDistr")
    public ResponseBean<List<CityAlarmStatistics>> cityPlatAlarmTypeDistr(CityAlarmStatisticsDto cityAlarmStatisticsDto){
        return new ResponseBean<>(cityAlarmStatisticsService.cityPlatAlarmTypeDistr(cityAlarmStatisticsDto));
    }

    /**
     * 各区域派单率
     */
    @ApiOperation("市级平台各区域派单占比")
    @GetMapping("cityPlatCityDistr")
    public ResponseBean<List<CityAlarmStatisticsDto>> cityPlatCityDistr(CityAlarmStatisticsDto cityAlarmStatisticsDto){
        return new ResponseBean<>(cityAlarmStatisticsService.cityPlatCityDistr(cityAlarmStatisticsDto));
    }

    /**
     * 各月份派单修复率
     */
    @ApiOperation("市级平台各月份派单修复率")
    @GetMapping("cityPlatMonthlyRepairReta")
    public ResponseBean<Map> cityPlatMonthlyRepairReta(CityAlarmStatisticsDto cityAlarmStatisticsDto){
        return new ResponseBean<>(cityAlarmStatisticsService.cityPlatMonthlyRepairReta(cityAlarmStatisticsDto));
    }

    /**
     * 各区域修复率
     */
    @ApiOperation("市级平台各区域派单占比")
    @GetMapping("cityPlatCityRepair")
    public ResponseBean<List<CityAlarmStatisticsDto>> cityPlatCityRepair(CityAlarmStatisticsDto cityAlarmStatisticsDto){
        return new ResponseBean<>(cityAlarmStatisticsService.cityPlatCityRepair(cityAlarmStatisticsDto));
    }

    /**
     * 运维汇总
     */
    @ApiOperation("运维汇总")
    @GetMapping("maintenanStatist")
    public ResponseBean<MaintenanceStatisticsVo> maintenanStatist(CityAlarmStatisticsDto cityAlarmStatisticsDto){
        return new ResponseBean<>(cityAlarmStatisticsService.maintenanStatist(cityAlarmStatisticsDto));
    }

}
