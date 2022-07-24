package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.CityOnlineStatisticsDto;
import com.aswl.as.ibrs.api.module.CityOnlineStatistics;
import com.aswl.as.ibrs.service.CityOnlineStatisticsService;
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
 * 市级平台在线统计表controller
 *
 * @author hzj
 * @date 2021/01/23 17:26
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/cityOnlineStatistics", tags = "市级平台在线统计表")
@RestController
@RequestMapping("/v1/cityOnlineStatistics")
public class CityOnlineStatisticsController extends BaseController {

    private final CityOnlineStatisticsService cityOnlineStatisticsService;

    /**
     * 根据ID获取市级平台在线统计表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据市级平台在线统计表ID查询市级平台在线统计表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "市级平台在线统计表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<CityOnlineStatistics> findById(@PathVariable("id") String id) {
        CityOnlineStatistics cityOnlineStatistics = new CityOnlineStatistics();
        cityOnlineStatistics.setId(id);
        return new ResponseBean<>(cityOnlineStatisticsService.get(cityOnlineStatistics));
    }

    /**
     * 查询所有市级平台在线统计表
     *
     * @param cityOnlineStatistics
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有市级平台在线统计表列表")
    @ApiImplicitParam(name = "cityOnlineStatistics", value = "市级平台在线统计表对象", paramType = "path", required = true, dataType = "cityOnlineStatistics")
    @GetMapping(value = "cityOnlineStatisticss")
    public ResponseBean
            <List<CityOnlineStatistics>> findAll(CityOnlineStatistics cityOnlineStatistics) {
        cityOnlineStatistics.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(cityOnlineStatisticsService.findList(cityOnlineStatistics));
    }

    /**
     * 分页查询市级平台在线统计表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param cityOnlineStatistics
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询市级平台在线统计表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "cityOnlineStatistics", value = "市级平台在线统计表信息", dataType = "cityOnlineStatistics")
    })

    @GetMapping("cityOnlineStatisticsList")
    public ResponseBean
            <PageInfo<CityOnlineStatistics>> cityOnlineStatisticsList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                      @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                      @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                      @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                      CityOnlineStatistics cityOnlineStatistics) {
        cityOnlineStatistics.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(cityOnlineStatisticsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), cityOnlineStatistics));
    }

    /**
     * 新增市级平台在线统计表
     *
     * @param cityOnlineStatisticsDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增市级平台在线统计表", notes = "新增市级平台在线统计表")
    @PostMapping
    @Log("新增市级平台在线统计表")
    public ResponseBean
            <Boolean> insertCityOnlineStatistics(@RequestBody @Valid CityOnlineStatisticsDto cityOnlineStatisticsDto) {
        CityOnlineStatistics cityOnlineStatistics = new CityOnlineStatistics();
        BeanUtils.copyProperties(cityOnlineStatisticsDto, cityOnlineStatistics);
        cityOnlineStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityOnlineStatisticsService.insert(cityOnlineStatistics) > 0);
    }

    /**
     * 修改市级平台在线统计表
     *
     * @param cityOnlineStatisticsDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新市级平台在线统计表信息", notes = "根据市级平台在线统计表ID更新市级平台在线统计表信息")
    @Log("修改市级平台在线统计表")
    @PutMapping
    public ResponseBean
            <Boolean> updateCityOnlineStatistics(@RequestBody @Valid CityOnlineStatisticsDto cityOnlineStatisticsDto) {
        CityOnlineStatistics cityOnlineStatistics = new CityOnlineStatistics();
        BeanUtils.copyProperties(cityOnlineStatisticsDto, cityOnlineStatistics);
        cityOnlineStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityOnlineStatisticsService.update(cityOnlineStatistics) > 0);
    }

    /**
     * 根据市级平台在线统计表ID删除市级平台在线统计表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除市级平台在线统计表信息", notes = "根据市级平台在线统计表ID删除市级平台在线统计表信息")
    @ApiImplicitParam(name = "id", value = "市级平台在线统计表ID", paramType = "path", required = true, dataType =
            "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteCityOnlineStatisticsById(@PathVariable("id") String id) {
        CityOnlineStatistics cityOnlineStatistics = new CityOnlineStatistics();
        cityOnlineStatistics.setId(id);
        cityOnlineStatistics.setNewRecord(false);
        cityOnlineStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(cityOnlineStatisticsService.delete(cityOnlineStatistics) > 0);
    }

    /**
     * 批量删除市级平台在线统计表信息
     *
     * @param cityOnlineStatistics
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除市级平台在线统计表", notes = "根据市级平台在线统计表ID批量删除市级平台在线统计表")
    @ApiImplicitParam(name = "cityOnlineStatistics", value = "市级平台在线统计表信息", dataType = "cityOnlineStatistics")
    @Log("批量删除市级平台在线统计表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllCityOnlineStatistics(@RequestBody CityOnlineStatistics cityOnlineStatistics) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(cityOnlineStatistics.getIdString()))
                success = cityOnlineStatisticsService.deleteAll(cityOnlineStatistics.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除市级平台在线统计表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
