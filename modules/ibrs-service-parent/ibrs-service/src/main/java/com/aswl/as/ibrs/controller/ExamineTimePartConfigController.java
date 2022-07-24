package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ExamineTimePartConfigDto;
import com.aswl.as.ibrs.api.module.ExamineTimePartConfig;
import com.aswl.as.ibrs.service.ExamineTimePartConfigService;
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
 * 考核时段设置controller
 *
 * @author hfx
 * @date 2020-03-19 10:20
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/examineTimePartConfig", tags = "考核时段设置")
@RestController
@RequestMapping("/v1/examineTimePartConfig")
public class ExamineTimePartConfigController extends BaseController {

    private final ExamineTimePartConfigService examineTimePartConfigService;

    /**
     * 根据ID获取考核时段设置
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据考核时段设置ID查询考核时段设置")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "考核时段设置ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ExamineTimePartConfig> findById(@PathVariable("id") String id) {
        ExamineTimePartConfig examineTimePartConfig = new ExamineTimePartConfig();
        examineTimePartConfig.setId(id);
        return new ResponseBean<>(examineTimePartConfigService.get(examineTimePartConfig));
    }

    /**
     * 查询所有考核时段设置
     *
     * @param examineTimePartConfig
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有考核时段设置列表")
    @ApiImplicitParam(name = "examineTimePartConfig", value = "考核时段设置对象", paramType = "path", required = true, dataType = "examineTimePartConfig")
    @GetMapping(value = "examineTimePartConfigs")
    public ResponseBean
            <List<ExamineTimePartConfig>> findAll(ExamineTimePartConfig examineTimePartConfig) {
        examineTimePartConfig.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineTimePartConfigService.findList(examineTimePartConfig));
    }

    /**
     * 分页查询考核时段设置列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param examineTimePartConfig
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询考核时段设置列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "examineTimePartConfig", value = "考核时段设置信息", dataType = "examineTimePartConfig")
    })

    @GetMapping("examineTimePartConfigList")
    public ResponseBean<PageInfo<ExamineTimePartConfig>> examineTimePartConfigList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                                   ExamineTimePartConfig examineTimePartConfig) {
        examineTimePartConfig.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineTimePartConfigService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examineTimePartConfig));
    }

    /**
     * 新增考核时段设置
     *
     * @param examineTimePartConfigDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增考核时段设置", notes = "新增考核时段设置")
    @PostMapping
    @Log("新增考核时段设置")
    public ResponseBean
            <Boolean> insertExamineTimePartConfig(@RequestBody @Valid ExamineTimePartConfigDto examineTimePartConfigDto) {
        ExamineTimePartConfig examineTimePartConfig = new ExamineTimePartConfig();
        BeanUtils.copyProperties(examineTimePartConfigDto, examineTimePartConfig);
        examineTimePartConfig.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineTimePartConfigService.insert(examineTimePartConfig) > 0);
    }

    /**
     * 修改考核时段设置
     *
     * @param examineTimePartConfigDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新考核时段设置信息", notes = "根据考核时段设置ID更新考核时段设置信息")
    @Log("修改考核时段设置")
    @PutMapping
    public ResponseBean
            <Boolean> updateExamineTimePartConfig(@RequestBody @Valid ExamineTimePartConfigDto examineTimePartConfigDto) {
        ExamineTimePartConfig examineTimePartConfig = new ExamineTimePartConfig();
        BeanUtils.copyProperties(examineTimePartConfigDto, examineTimePartConfig);
        examineTimePartConfig.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineTimePartConfigService.update(examineTimePartConfig) > 0);
    }

    /**
     * 根据考核时段设置ID删除考核时段设置信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除考核时段设置信息", notes = "根据考核时段设置ID删除考核时段设置信息")
    @ApiImplicitParam(name = "id", value = "考核时段设置ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteExamineTimePartConfigById(@PathVariable("id") String id) {
        ExamineTimePartConfig examineTimePartConfig = new ExamineTimePartConfig();
        examineTimePartConfig.setId(id);
        examineTimePartConfig.setNewRecord(false);
        examineTimePartConfig.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineTimePartConfigService.delete(examineTimePartConfig) > 0);
    }

    /**
     * 批量删除考核时段设置信息
     *
     * @param examineTimePartConfig
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除考核时段设置", notes = "根据考核时段设置ID批量删除考核时段设置")
    @ApiImplicitParam(name = "examineTimePartConfig", value = "考核时段设置信息", dataType = "examineTimePartConfig")
    @Log("批量删除考核时段设置")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllExamineTimePartConfig(@RequestBody ExamineTimePartConfig examineTimePartConfig) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(examineTimePartConfig.getIdString()))
                success = examineTimePartConfigService.deleteAll(examineTimePartConfig.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除考核时段设置失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
