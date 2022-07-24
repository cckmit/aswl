package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.ReportSettingDto;
import com.aswl.as.ibrs.api.module.ReportSetting;
import com.aswl.as.ibrs.api.vo.ReportSettingVo;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.service.ReportSettingService;
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
 * 报送人、报送人配置表controller
 *
 * @author df
 * @date 2021/07/08 18:12
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/reportSetting", tags = "报送人、报送人配置表")
@RestController
@RequestMapping("/v1/reportSetting")
public class ReportSettingController extends BaseController {

    private final ReportSettingService reportSettingService;

    /**
     * 根据ID获取报送人、报送人配置表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据报送人、报送人配置表ID查询报送人、报送人配置表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "报送人、报送人配置表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ReportSetting> findById(@PathVariable("id") String id) {
        ReportSetting reportSetting = new ReportSetting();
        reportSetting.setId(id);
        return new ResponseBean<>(reportSettingService.get(reportSetting));
    }

    /**
     * 查询所有报送人、报送人配置表
     *
     * @param reportSetting
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有报送人、报送人配置表列表")
    @ApiImplicitParam(name = "reportSetting", value = "报送人、报送人配置表对象", paramType = "path", required = true, dataType = "reportSetting")
    @GetMapping(value = "reportSettings")
    public ResponseBean
            <List<ReportSetting>> findAll(ReportSetting reportSetting) {
        reportSetting.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(reportSettingService.findList(reportSetting));
    }

    /**
     * 分页查询报送人、报送人配置表列表
     *
     * @param pageNum
     * @param pageSize
     * @param reportSettingDto
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询报送人、报送人配置表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "reportSettingDto", value = "报送人、报送人配置表信息", dataType = "ReportSettingDto")
    })

    @GetMapping("reportSettingList")
    public ResponseBean
            <PageInfo<ReportSettingVo>> reportSettingList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                          @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                          ReportSettingDto reportSettingDto) {
        String roles = RoleContextHolder.getRole();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {

        }
        reportSettingDto.setTenantCode(tenantCode);
        reportSettingDto.setProjectId(projectId);
        return new ResponseBean<>(reportSettingService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), reportSettingDto));
    }

    /**
     * 新增报送人、报送人配置表
     *
     * @param reportSettingDto
     * @return ResponseBean
     */
    @PostMapping
    @ApiOperation(value = "新增报送人、报送人配置表", notes = "新增报送人、报送人配置表")
    @Log(value = "新增报送人、报送人配置表", businessType = BusinessType.INSERT)
    public ResponseBean
            <Boolean> insertReportSetting(@RequestBody @Valid ReportSettingDto reportSettingDto) {
        ReportSetting reportSetting = new ReportSetting();
        BeanUtils.copyProperties(reportSettingDto, reportSetting);
        reportSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportSettingService.insert(reportSetting) > 0);
    }

    /**
     * 修改报送人、报送人配置表
     *
     * @param reportSettingDto
     * @return ResponseBean
     */
    @PutMapping
    @ApiOperation(value = "更新报送人、报送人配置表信息", notes = "根据报送人、报送人配置表ID更新报送人、报送人配置表信息")
    @Log(value = "更新报送人、报送人配置表", businessType = BusinessType.UPDATE)
    public ResponseBean
            <Boolean> updateReportSetting(@RequestBody @Valid ReportSettingDto reportSettingDto) {
        ReportSetting reportSetting = new ReportSetting();
        BeanUtils.copyProperties(reportSettingDto, reportSetting);
        reportSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportSettingService.update(reportSetting) > 0);
    }

    /**
     * 根据报送人、报送人配置表ID删除报送人、报送人配置表信息
     *
     * @param id
     * @return ResponseBean
     */

    @DeleteMapping(value = "/{id}")
    @ApiOperation(value = "删除报送人、报送人配置表信息", notes = "根据报送人、报送人配置表ID删除报送人、报送人配置表信息")
    @ApiImplicitParam(name = "id", value = "报送人、报送人配置表ID", paramType = "path", required = true, dataType =
            "String")
    @Log(value = "删除报送人、报送人配置表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteReportSettingById(@PathVariable("id") String id) {
        ReportSetting reportSetting = new ReportSetting();
        reportSetting.setId(id);
        reportSetting.setNewRecord(false);
        reportSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(reportSettingService.delete(reportSetting) > 0);
    }

    /**
     * 批量删除报送人、报送人配置表信息
     *
     * @param reportSetting
     * @return ResponseBean
     */
    @PostMapping("deleteAll")
    @ApiOperation(value = "批量删除报送人、报送人配置表", notes = "根据报送人、报送人配置表ID批量删除报送人、报送人配置表")
    @ApiImplicitParam(name = "reportSetting", value = "报送人、报送人配置表信息", dataType = "reportSetting")
    @Log(value = "删除报送人、报送人配置表", businessType = BusinessType.DELETE)
    public ResponseBean
            <Boolean> deleteAllReportSetting(@RequestBody ReportSetting reportSetting) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(reportSetting.getIdString()))
                success = reportSettingService.deleteAll(reportSetting.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除报送人、报送人配置表失败！", e);
        }
        return new ResponseBean<>(success);
    }


    /**
     * 批量新增报送人、报送人配置表信息
     *
     * @param list list
     * @return ResponseBean
     * @author aswl.com
     */
    @PostMapping(value = "insertBath")
    @ApiOperation(value = "批量新增报送人、报送人配置表信息", notes = "批量新增报送人、报送人配置表信息")
    @ApiImplicitParam(name = "list", value = "批量新增报送人、报送人配置表信息", required = true, dataType = "ReportSetting")
    @Log(value = "批量新增报送人、报送人配置表信息", businessType = BusinessType.INSERT)
    public ResponseBean<Boolean> insertBath(@RequestBody List<ReportSetting> list) {
        return new ResponseBean<>(reportSettingService.insertBath(list) > 0);
    }
}
