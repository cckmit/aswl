package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.enums.StatisticsColorTypeEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.ReportStatisticsColorSettingDto;
import com.aswl.as.ibrs.api.module.ReportStatisticsColorSetting;
import com.aswl.as.ibrs.service.ReportStatisticsColorSettingService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.enums.BusinessType;

/**
 * 图形色标设置表controller
 *
 * @author df
 * @date 2021/07/12 16:54
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/reportStatisticsColorSetting", tags = "图形色标设置表")
@RestController
@RequestMapping("/v1/reportStatisticsColorSetting")
public class ReportStatisticsColorSettingController extends BaseController {

    private final ReportStatisticsColorSettingService reportStatisticsColorSettingService;

    /**
     * 根据ID获取图形色标设置表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据图形色标设置表ID查询图形色标设置表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "图形色标设置表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ReportStatisticsColorSetting> findById(@PathVariable("id") String id) {
        ReportStatisticsColorSetting reportStatisticsColorSetting = new ReportStatisticsColorSetting();
        reportStatisticsColorSetting.setId(id);
        return new ResponseBean<>(reportStatisticsColorSettingService.get(reportStatisticsColorSetting));
    }

    /**
     * 查询所有图形色标设置表
     *
     * @param reportStatisticsColorSetting
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有图形色标设置表列表")
    @ApiImplicitParam(name = "reportStatisticsColorSetting", value = "图形色标设置表对象", paramType = "path", required = true, dataType = "reportStatisticsColorSetting")
    @GetMapping(value = "reportStatisticsColorSettings")
    public ResponseBean
            <List<ReportStatisticsColorSetting>> findAll(ReportStatisticsColorSetting reportStatisticsColorSetting) {
        reportStatisticsColorSetting.setTenantCode(SysUtil.getTenantCode());
        List<ReportStatisticsColorSetting> settingList = new ArrayList<>();
        List<ReportStatisticsColorSetting> list = reportStatisticsColorSettingService.findList(reportStatisticsColorSetting);
        for(StatisticsColorTypeEnum statisticsColorTypeEnum : StatisticsColorTypeEnum.values()){
            ReportStatisticsColorSetting statisticsColorSetting  = list.stream().filter(s -> String.valueOf(statisticsColorTypeEnum.getType()).equals(s.getStatisticsType())).findFirst().orElse(null);
            if(statisticsColorSetting == null){ //若未设置过，则返回默认设置数据
                statisticsColorSetting = new ReportStatisticsColorSetting();
                statisticsColorSetting.setId(null);
                statisticsColorSetting.setColors("#5B9BD5,#ED7D31,#A5A5A5,#FFC000,#4472C4,#ED34A5");
                statisticsColorSetting.setStatisticsType(String.valueOf(statisticsColorTypeEnum.getType()));
            }
            settingList.add(statisticsColorSetting);
        }
        return new ResponseBean<>(settingList);
    }

    /**
     * 分页查询图形色标设置表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param reportStatisticsColorSetting
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询图形色标设置表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reportStatisticsColorSetting", value = "图形色标设置表信息", dataType = "reportStatisticsColorSetting")
    })

    @GetMapping("reportStatisticsColorSettingList")
    public ResponseBean
            <PageInfo<ReportStatisticsColorSetting>> reportStatisticsColorSettingList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                                      @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                                      @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                                      @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                                      ReportStatisticsColorSetting reportStatisticsColorSetting) {
        reportStatisticsColorSetting.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(reportStatisticsColorSettingService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), reportStatisticsColorSetting));
    }

    /**
     * 新增图形色标设置表
     *
     * @param reportStatisticsColorSettingDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增图形色标设置表", notes = "新增图形色标设置表")
    @Log(value = "新增图形色标设置表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertReportStatisticsColorSetting(@RequestBody @Valid ReportStatisticsColorSettingDto reportStatisticsColorSettingDto) {
        ReportStatisticsColorSetting reportStatisticsColorSetting = new ReportStatisticsColorSetting();
        BeanUtils.copyProperties(reportStatisticsColorSettingDto, reportStatisticsColorSetting);
        reportStatisticsColorSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportStatisticsColorSettingService.insert(reportStatisticsColorSetting) > 0);
    }

    /**
     * 修改图形色标设置表
     *
     * @param reportStatisticsColorSettingDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新图形色标设置表信息", notes = "根据图形色标设置表ID更新图形色标设置表信息")
    @Log(value = "更新图形色标设置表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateReportStatisticsColorSetting(@RequestBody @Valid ReportStatisticsColorSettingDto reportStatisticsColorSettingDto) {
        ReportStatisticsColorSetting reportStatisticsColorSetting = new ReportStatisticsColorSetting();
        BeanUtils.copyProperties(reportStatisticsColorSettingDto, reportStatisticsColorSetting);
        reportStatisticsColorSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportStatisticsColorSettingService.update(reportStatisticsColorSetting) > 0);
    }

    /**
     * 根据图形色标设置表ID删除图形色标设置表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除图形色标设置表信息", notes = "根据图形色标设置表ID删除图形色标设置表信息")
    @ApiImplicitParam(name = "id", value = "图形色标设置表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除图形色标设置表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteReportStatisticsColorSettingById(@PathVariable("id") String id) {
        ReportStatisticsColorSetting reportStatisticsColorSetting = new ReportStatisticsColorSetting();
        reportStatisticsColorSetting.setId(id);
        reportStatisticsColorSetting.setNewRecord(false);
        reportStatisticsColorSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportStatisticsColorSettingService.delete(reportStatisticsColorSetting) > 0);
    }

    /**
     * 批量删除图形色标设置表信息
     *
     * @param reportStatisticsColorSetting
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除图形色标设置表", notes = "根据图形色标设置表ID批量删除图形色标设置表")
    @ApiImplicitParam(name = "reportStatisticsColorSetting", value = "图形色标设置表信息", dataType = "reportStatisticsColorSetting")
    @Log(value = "删除图形色标设置表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllReportStatisticsColorSetting(@RequestBody ReportStatisticsColorSetting reportStatisticsColorSetting) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(reportStatisticsColorSetting.getIdString()))
                success = reportStatisticsColorSettingService.deleteAll(reportStatisticsColorSetting.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除图形色标设置表失败！", e);
        }
        return new ResponseBean<>(success);
    }

    /**
     * 批量保存图形色标设置
     *
     * @param reportStatisticsColorSettingDtoList
     * @return ResponseBean
     */
    @PostMapping("batchSaveReportStatisticsColorSetting")
    @ApiOperation(value = "批量保存图形色标设置", notes = "批量保存图形色标设置")
    @Log(value = "批量保存图形色标设置", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> batchSaveReportStatisticsColorSetting(@RequestBody @Valid List<ReportStatisticsColorSettingDto> reportStatisticsColorSettingDtoList) {
        List<ReportStatisticsColorSetting> reportStatisticsColorSettingList = new ArrayList<>();
        ReportStatisticsColorSetting reportStatisticsColorSetting = null;
        for(ReportStatisticsColorSettingDto dto : reportStatisticsColorSettingDtoList){
            reportStatisticsColorSetting = new ReportStatisticsColorSetting();
            BeanUtils.copyProperties(dto, reportStatisticsColorSetting);
            reportStatisticsColorSettingList.add(reportStatisticsColorSetting);
        }
        return new ResponseBean<>(reportStatisticsColorSettingService.batchSave(reportStatisticsColorSettingList) > 0);
    }
}
