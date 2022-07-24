package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.ReportHisInfoDto;
import com.aswl.as.ibrs.api.module.ReportHisInfo;
import com.aswl.as.ibrs.api.vo.ReportHisInfoVo;
import com.aswl.as.ibrs.service.ReportHisInfoService;
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
 * 后台统计报表记录信息表controller
 *
 * @author df
 * @date 2021/07/20 17:28
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/reportHisInfo", tags = "后台统计报表记录信息表")
@RestController
@RequestMapping("/v1/reportHisInfo")
public class ReportHisInfoController extends BaseController {

    private final ReportHisInfoService reportHisInfoService;

    /**
     * 根据ID获取统计报表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据统计报表ID查询统计报表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "统计报表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ReportHisInfo> findById(@PathVariable("id") String id) {
        ReportHisInfo reportHisInfo = new ReportHisInfo();
        reportHisInfo.setId(id);
        return new ResponseBean<>(reportHisInfoService.get(reportHisInfo));
    }

    /**
     * 查询所有统计报表
     *
     * @param reportHisInfo
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有统计报表列表")
    @ApiImplicitParam(name = "reportHisInfo", value = "统计报表对象", paramType = "path", required = true, dataType = "reportHisInfo")
    @GetMapping(value = "reportHisInfos")
    public ResponseBean
            <List<ReportHisInfo>> findAll(ReportHisInfo reportHisInfo) {
        reportHisInfo.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(reportHisInfoService.findList(reportHisInfo));
    }

    /**
     * 分页查询统计报表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param reportHisInfoDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询统计报表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reportHisInfo", value = "统计报表信息", dataType = "reportHisInfo")
    })

    @GetMapping("reportHisInfoList")
    public ResponseBean
            <PageInfo<ReportHisInfoVo>> reportHisInfoList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                          @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                          @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                          @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                          ReportHisInfoDto reportHisInfoDto) {
        return new ResponseBean<>(reportHisInfoService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), reportHisInfoDto));
    }

    /**
     * 新增统计报表
     *
     * @param reportHisInfoDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增统计报表", notes = "新增统计报表")
    @Log(value = "新增统计报表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertReportHisInfo(@RequestBody @Valid ReportHisInfoDto reportHisInfoDto) {
        ReportHisInfo reportHisInfo = new ReportHisInfo();
        BeanUtils.copyProperties(reportHisInfoDto, reportHisInfo);
        reportHisInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportHisInfoService.insert(reportHisInfo) > 0);
    }

    /**
     * 修改统计报表
     *
     * @param reportHisInfoDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新统计报表信息", notes = "根据统计报表ID更新统计报表信息")
    @Log(value = "更新统计报表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateReportHisInfo(@RequestBody @Valid ReportHisInfoDto reportHisInfoDto) {
        ReportHisInfo reportHisInfo = new ReportHisInfo();
        BeanUtils.copyProperties(reportHisInfoDto, reportHisInfo);
        reportHisInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportHisInfoService.update(reportHisInfo) > 0);
    }

    /**
     * 根据统计报表ID删除统计报表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除统计报表信息", notes = "根据统计报表ID删除统计报表信息")
    @ApiImplicitParam(name = "id", value = "统计报表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除统计报表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteReportHisInfoById(@PathVariable("id") String id) {
        ReportHisInfo reportHisInfo = new ReportHisInfo();
        reportHisInfo.setId(id);
        reportHisInfo.setNewRecord(false);
        reportHisInfo.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportHisInfoService.delete(reportHisInfo) > 0);
    }

    /**
     * 批量删除统计报表信息
     *
     * @param reportHisInfo
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除统计报表", notes = "根据统计报表ID批量删除统计报表")
    @ApiImplicitParam(name = "reportHisInfo", value = "统计报表信息", dataType = "reportHisInfo")
    @Log(value = "删除统计报表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllReportHisInfo(@RequestBody ReportHisInfo reportHisInfo) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(reportHisInfo.getIdString()))
                success = reportHisInfoService.deleteAll(reportHisInfo.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除统计报表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
