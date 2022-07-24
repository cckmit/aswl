package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ExamineDeductRuleDto;
import com.aswl.as.ibrs.api.module.ExamineDeductRule;
import com.aswl.as.ibrs.service.ExamineDeductRuleService;
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
 * 考核扣分规则controller
 *
 * @author hfx
 * @date 2020-03-19 10:18
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/examineDeductRule", tags = "考核扣分规则")
@RestController
@RequestMapping("/v1/examineDeductRule")
public class ExamineDeductRuleController extends BaseController {

    private final ExamineDeductRuleService examineDeductRuleService;

    /**
     * 根据ID获取考核扣分规则
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据考核扣分规则ID查询考核扣分规则")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "考核扣分规则ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ExamineDeductRule> findById(@PathVariable("id") String id) {
        ExamineDeductRule examineDeductRule = new ExamineDeductRule();
        examineDeductRule.setId(id);
        return new ResponseBean<>(examineDeductRuleService.get(examineDeductRule));
    }

    /**
     * 查询所有考核扣分规则
     *
     * @param examineDeductRule
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有考核扣分规则列表")
    @ApiImplicitParam(name = "examineDeductRule", value = "考核扣分规则对象", paramType = "path", required = true, dataType = "examineDeductRule")
    @GetMapping(value = "examineDeductRules")
    public ResponseBean
            <List<ExamineDeductRule>> findAll(ExamineDeductRule examineDeductRule) {
        examineDeductRule.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineDeductRuleService.findList(examineDeductRule));
    }

    /**
     * 分页查询考核扣分规则列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param examineDeductRule
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询考核扣分规则列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "examineDeductRule", value = "考核扣分规则信息", dataType = "examineDeductRule")
    })

    @GetMapping("examineDeductRuleList")
    public ResponseBean<PageInfo<ExamineDeductRule>> examineDeductRuleList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                           @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                           @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                           @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                           ExamineDeductRule examineDeductRule) {
        examineDeductRule.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineDeductRuleService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examineDeductRule));
    }

    /**
     * 新增考核扣分规则
     *
     * @param examineDeductRuleDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增考核扣分规则", notes = "新增考核扣分规则")
    @PostMapping
    @Log("新增考核扣分规则")
    public ResponseBean
            <Boolean> insertExamineDeductRule(@RequestBody @Valid ExamineDeductRuleDto examineDeductRuleDto) {
        ExamineDeductRule examineDeductRule = new ExamineDeductRule();
        BeanUtils.copyProperties(examineDeductRuleDto, examineDeductRule);
        examineDeductRule.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineDeductRuleService.insert(examineDeductRule) > 0);
    }

    /**
     * 修改考核扣分规则
     *
     * @param examineDeductRuleDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新考核扣分规则信息", notes = "根据考核扣分规则ID更新考核扣分规则信息")
    @Log("修改考核扣分规则")
    @PutMapping
    public ResponseBean
            <Boolean> updateExamineDeductRule(@RequestBody @Valid ExamineDeductRuleDto examineDeductRuleDto) {
        ExamineDeductRule examineDeductRule = new ExamineDeductRule();
        BeanUtils.copyProperties(examineDeductRuleDto, examineDeductRule);
        examineDeductRule.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineDeductRuleService.update(examineDeductRule) > 0);
    }

    /**
     * 根据考核扣分规则ID删除考核扣分规则信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除考核扣分规则信息", notes = "根据考核扣分规则ID删除考核扣分规则信息")
    @ApiImplicitParam(name = "id", value = "考核扣分规则ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteExamineDeductRuleById(@PathVariable("id") String id) {
        ExamineDeductRule examineDeductRule = new ExamineDeductRule();
        examineDeductRule.setId(id);
        examineDeductRule.setNewRecord(false);
        examineDeductRule.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineDeductRuleService.delete(examineDeductRule) > 0);
    }

    /**
     * 批量删除考核扣分规则信息
     *
     * @param examineDeductRule
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除考核扣分规则", notes = "根据考核扣分规则ID批量删除考核扣分规则")
    @ApiImplicitParam(name = "examineDeductRule", value = "考核扣分规则信息", dataType = "examineDeductRule")
    @Log("批量删除考核扣分规则")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllExamineDeductRule(@RequestBody ExamineDeductRule examineDeductRule) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(examineDeductRule.getIdString()))
                success = examineDeductRuleService.deleteAll(examineDeductRule.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除考核扣分规则失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
