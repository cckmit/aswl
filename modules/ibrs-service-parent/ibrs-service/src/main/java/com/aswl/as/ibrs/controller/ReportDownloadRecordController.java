package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.ibrs.api.dto.ReportDownloadRecordDto;
import com.aswl.as.ibrs.api.module.ReportDownloadRecord;
import com.aswl.as.ibrs.service.ReportDownloadRecordService;
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
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import com.aswl.as.common.core.enums.BusinessType;

/**
 * 报表下载记录表controller
 *
 * @author df
 * @date 2021/07/20 17:31
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/reportDownloadRecord", tags = "报表下载记录表")
@RestController
@RequestMapping("/v1/reportDownloadRecord")
public class ReportDownloadRecordController extends BaseController {

    private final ReportDownloadRecordService reportDownloadRecordService;

    /**
     * 根据ID获取报表下载记录表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据报表下载记录表ID查询报表下载记录表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报表下载记录表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ReportDownloadRecord> findById(@PathVariable("id") String id) {
        ReportDownloadRecord reportDownloadRecord = new ReportDownloadRecord();
        reportDownloadRecord.setId(id);
        return new ResponseBean<>(reportDownloadRecordService.get(reportDownloadRecord));
    }

    /**
     * 查询所有报表下载记录表
     *
     * @param reportDownloadRecord
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有报表下载记录表列表")
    @ApiImplicitParam(name = "reportDownloadRecord", value = "报表下载记录表对象", paramType = "path", required = true, dataType = "reportDownloadRecord")
    @GetMapping(value = "reportDownloadRecords")
    public ResponseBean
            <List<ReportDownloadRecord>> findAll(ReportDownloadRecord reportDownloadRecord) {
        reportDownloadRecord.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(reportDownloadRecordService.findList(reportDownloadRecord));
    }

    /**
     * 分页查询报表下载记录表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param reportDownloadRecord
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询报表下载记录表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reportDownloadRecord", value = "报表下载记录表信息", dataType = "reportDownloadRecord")
    })

    @GetMapping("reportDownloadRecordList")
    public ResponseBean
            <PageInfo<ReportDownloadRecord>> reportDownloadRecordList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                      @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                      @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                      @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                      ReportDownloadRecord reportDownloadRecord) {
        reportDownloadRecord.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(reportDownloadRecordService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), reportDownloadRecord));
    }

    /**
     * 新增报表下载记录表
     *
     * @param reportDownloadRecordDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增报表下载记录表", notes = "新增报表下载记录表")
    @Log(value = "新增报表下载记录表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertReportDownloadRecord(@RequestBody @Valid ReportDownloadRecordDto reportDownloadRecordDto) {
        ReportDownloadRecord reportDownloadRecord = new ReportDownloadRecord();
        BeanUtils.copyProperties(reportDownloadRecordDto, reportDownloadRecord);
        reportDownloadRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        reportDownloadRecord.setDownloadDate(new Date());
        return new ResponseBean<>(reportDownloadRecordService.insert(reportDownloadRecord) > 0);
    }

    /**
     * 修改报表下载记录表
     *
     * @param reportDownloadRecordDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新报表下载记录表信息", notes = "根据报表下载记录表ID更新报表下载记录表信息")
    @Log(value = "更新报表下载记录表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateReportDownloadRecord(@RequestBody @Valid ReportDownloadRecordDto reportDownloadRecordDto) {
        ReportDownloadRecord reportDownloadRecord = new ReportDownloadRecord();
        BeanUtils.copyProperties(reportDownloadRecordDto, reportDownloadRecord);
        reportDownloadRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportDownloadRecordService.update(reportDownloadRecord) > 0);
    }

    /**
     * 根据报表下载记录表ID删除报表下载记录表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除报表下载记录表信息", notes = "根据报表下载记录表ID删除报表下载记录表信息")
    @ApiImplicitParam(name = "id", value = "报表下载记录表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除报表下载记录表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteReportDownloadRecordById(@PathVariable("id") String id) {
        ReportDownloadRecord reportDownloadRecord = new ReportDownloadRecord();
        reportDownloadRecord.setId(id);
        reportDownloadRecord.setNewRecord(false);
        reportDownloadRecord.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportDownloadRecordService.delete(reportDownloadRecord) > 0);
    }

    /**
     * 批量删除报表下载记录表信息
     *
     * @param reportDownloadRecord
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除报表下载记录表", notes = "根据报表下载记录表ID批量删除报表下载记录表")
    @ApiImplicitParam(name = "reportDownloadRecord", value = "报表下载记录表信息", dataType = "reportDownloadRecord")
    @Log(value = "删除报表下载记录表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllReportDownloadRecord(@RequestBody ReportDownloadRecord reportDownloadRecord) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(reportDownloadRecord.getIdString()))
                success = reportDownloadRecordService.deleteAll(reportDownloadRecord.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除报表下载记录表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
