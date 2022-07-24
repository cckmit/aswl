package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ElectricStatisticsDto;
import com.aswl.as.ibrs.api.module.ElectricStatistics;
import com.aswl.as.ibrs.api.vo.UnitElectricStatisticsVo;
import com.aswl.as.ibrs.service.ElectricStatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import com.github.pagehelper.PageInfo;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 电量统计表controller
 *
 * @author hzj
 * @date 2021/06/01 21:38
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/electricStatistics", tags = "电量统计表")
@RestController
@RequestMapping("/v1/electricStatistics")
public class ElectricStatisticsController extends BaseController {

    private final ElectricStatisticsService electricStatisticsService;

    /**
     * 根据ID获取电量统计表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据电量统计表ID查询电量统计表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "电量统计表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ElectricStatistics> findById(@PathVariable("id") String id) {
        ElectricStatistics electricStatistics = new ElectricStatistics();
        electricStatistics.setId(id);
        return new ResponseBean<>(electricStatisticsService.get(electricStatistics));
    }

    /**
     * 查询所有电量统计表
     *
     * @param electricStatistics
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有电量统计表列表")
    @ApiImplicitParam(name = "electricStatistics", value = "电量统计表对象", paramType = "path", required = true, dataType = "electricStatistics")
    @GetMapping(value = "electricStatisticss")
    public ResponseBean
            <List<ElectricStatistics>> findAll(ElectricStatistics electricStatistics) {
        electricStatistics.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(electricStatisticsService.findList(electricStatistics));
    }

    /**
     * 分页查询电量统计表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param electricStatistics
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询电量统计表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "electricStatistics", value = "电量统计表信息", dataType = "electricStatistics")
    })

    @GetMapping("electricStatisticsList")
    public ResponseBean
            <PageInfo<ElectricStatistics>> electricStatisticsList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                  @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                  @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                  @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                  ElectricStatistics electricStatistics) {
        electricStatistics.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(electricStatisticsService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), electricStatistics));
    }

    /**
     * 新增电量统计表
     *
     * @param electricStatisticsDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增电量统计表", notes = "新增电量统计表")
    @PostMapping
    @Log("新增电量统计表")
    public ResponseBean
            <Boolean> insertElectricStatistics(@RequestBody @Valid ElectricStatisticsDto electricStatisticsDto) {
        ElectricStatistics electricStatistics = new ElectricStatistics();
        BeanUtils.copyProperties(electricStatisticsDto, electricStatistics);
        electricStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricStatisticsService.insert(electricStatistics) > 0);
    }

    /**
     * 修改电量统计表
     *
     * @param electricStatisticsDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新电量统计表信息", notes = "根据电量统计表ID更新电量统计表信息")
    @Log("修改电量统计表")
    @PutMapping
    public ResponseBean
            <Boolean> updateElectricStatistics(@RequestBody @Valid ElectricStatisticsDto electricStatisticsDto) {
        ElectricStatistics electricStatistics = new ElectricStatistics();
        BeanUtils.copyProperties(electricStatisticsDto, electricStatistics);
        electricStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricStatisticsService.update(electricStatistics) > 0);
    }

    /**
     * 根据电量统计表ID删除电量统计表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除电量统计表信息", notes = "根据电量统计表ID删除电量统计表信息")
    @ApiImplicitParam(name = "id", value = "电量统计表ID", paramType = "path", required = true, dataType =
            "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteElectricStatisticsById(@PathVariable("id") String id) {
        ElectricStatistics electricStatistics = new ElectricStatistics();
        electricStatistics.setId(id);
        electricStatistics.setNewRecord(false);
        electricStatistics.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(electricStatisticsService.delete(electricStatistics) > 0);
    }

    /**
     * 批量删除电量统计表信息
     *
     * @param electricStatistics
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除电量统计表", notes = "根据电量统计表ID批量删除电量统计表")
    @ApiImplicitParam(name = "electricStatistics", value = "电量统计表信息", dataType = "electricStatistics")
    @Log("批量删除电量统计表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllElectricStatistics(@RequestBody ElectricStatistics electricStatistics) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(electricStatistics.getIdString()))
                success = electricStatisticsService.deleteAll(electricStatistics.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除电量统计表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 首页--用电量统计
     *
     * @return ResponseBean
     */
    @GetMapping("electricNum")
    @ApiOperation(value = "用电量统计", notes = "用电量统计")
    public ResponseBean
            <Map> getElectricNum(@RequestParam(value = "unitId",required = false) String unitId) {
            return new ResponseBean<>(electricStatisticsService.getElectricNum(unitId));
    }

    /**
     * 用电量统计--用电对比
     *
     * @return ResponseBean
     */
    @GetMapping("electricContrast")
    @ApiOperation(value = "用电量统计--用电对比", notes = "用电量统计--用电对比")
    public ResponseBean
            <Object> getElectricContrast(ElectricStatisticsDto dto) {
        return new ResponseBean<>(electricStatisticsService.getElectricContrast(dto));
    }

    /**
     * 用电量统计--各单位用电对比
     *
     * @return ResponseBean
     */
    @GetMapping("unitElectricContrast")
    @ApiOperation(value = "用电量统计--各单位用电对比", notes = "用电量统计--各单位用电对比")
    public ResponseBean
            <List<UnitElectricStatisticsVo>> getUnitElectricContrast(ElectricStatisticsDto dto) {
        return new ResponseBean<>(electricStatisticsService.getUnitElectricContrast(dto));
    }
    /**
     * 用电量报表统计导出
     *
     * @param dto
     */
    @ApiOperation(value = "用电量报表统计导出")
    @GetMapping("export")
    public ResponseEntity<byte[]> export(ElectricStatisticsDto dto) throws Exception {
        return electricStatisticsService.export(dto);
    }
}
