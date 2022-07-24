package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ExamineStandardDto;
import com.aswl.as.ibrs.api.module.ExamineStandard;
import com.aswl.as.ibrs.service.ExamineStandardService;
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
 * 考核标准controller
 *
 * @author hfx
 * @date 2020-03-19 10:19
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/examineStandard", tags = "考核标准")
@RestController
@RequestMapping("/v1/examineStandard")
public class ExamineStandardController extends BaseController {

    private final ExamineStandardService examineStandardService;

    /**
     * 根据ID获取考核标准
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据考核标准ID查询考核标准")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "考核标准ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ExamineStandard> findById(@PathVariable("id") String id) {
        ExamineStandard examineStandard = new ExamineStandard();
        examineStandard.setId(id);
        return new ResponseBean<>(examineStandardService.get(examineStandard));
    }

    /**
     * 查询所有考核标准
     *
     * @param examineStandard
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有考核标准列表")
    @ApiImplicitParam(name = "examineStandard", value = "考核标准对象", paramType = "path", required = true, dataType = "examineStandard")
    @GetMapping(value = "examineStandards")
    public ResponseBean
            <List<ExamineStandard>> findAll(ExamineStandard examineStandard) {
        examineStandard.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineStandardService.findList(examineStandard));
    }

    /**
     * 分页查询考核标准列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param examineStandard
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询考核标准列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "examineStandard", value = "考核标准信息", dataType = "examineStandard")
    })

    @GetMapping("examineStandardList")
    public ResponseBean<PageInfo<ExamineStandard>> examineStandardList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                       ExamineStandard examineStandard) {
        examineStandard.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineStandardService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examineStandard));
    }

    /**
     * 新增考核标准
     *
     * @param examineStandardDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增考核标准", notes = "新增考核标准")
    @PostMapping
    @Log("新增考核标准")
    public ResponseBean
            <Boolean> insertExamineStandard(@RequestBody @Valid ExamineStandardDto examineStandardDto) {
        ExamineStandard examineStandard = new ExamineStandard();
        BeanUtils.copyProperties(examineStandardDto, examineStandard);
//        examineStandard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineStandardService.insert(examineStandard) > 0);
    }


    //public ExamineStandard getStandardDetails(ExamineStandard standard)
    /**
     * 根据考核标准ID删除考核标准信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除考核标准信息", notes = "根据考核标准ID删除考核标准信息")
    @ApiImplicitParam(name = "id", value = "考核标准ID", paramType = "path", required = true, dataType = "String")
    @GetMapping(value = "/getStandardDetails/{id}")
    public ResponseBean
            <ExamineStandard> getStandardDetails(@PathVariable("id") String id) {
        ExamineStandard examineStandard = new ExamineStandard();
        examineStandard.setId(id);
        return new ResponseBean<>(examineStandardService.getStandardDetails(examineStandard));
    }

    /**
     * 修改考核标准
     *
     * @param examineStandardDto
     * @return ResponseBean
     */
    @ApiOperation(value = "更新考核标准信息", notes = "根据考核标准ID更新考核标准信息")
    @Log("修改考核标准")
    @PutMapping
    public ResponseBean
            <Boolean> updateExamineStandard(@RequestBody @Valid ExamineStandardDto examineStandardDto) {
        ExamineStandard examineStandard = new ExamineStandard();
        BeanUtils.copyProperties(examineStandardDto, examineStandard);
//        examineStandard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineStandardService.update(examineStandard) > 0);
    }


    //TODO 获取左边显示的树
    /**
     * 根据考核标准ID删除考核标准信息
     *
     * @return ResponseBean
     */
    @ApiOperation(value = "删除考核标准信息", notes = "根据考核标准ID删除考核标准信息")
    @ApiImplicitParam(name = "id", value = "考核标准ID", paramType = "path", required = true, dataType = "String")
    @GetMapping(value = "/getExamineTree")
    public ResponseBean<List> getStandardDetails() {
        return new ResponseBean<>(examineStandardService.getExamineTree());
    }




    /**
     * 根据考核标准ID删除考核标准信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除考核标准信息", notes = "根据考核标准ID删除考核标准信息")
    @ApiImplicitParam(name = "id", value = "考核标准ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteExamineStandardById(@PathVariable("id") String id) {
        ExamineStandard examineStandard = new ExamineStandard();
        examineStandard.setId(id);
        examineStandard.setNewRecord(false);
        examineStandard.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineStandardService.delete(examineStandard) > 0);
    }

    /**
     * 批量删除考核标准信息
     *
     * @param examineStandard
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除考核标准", notes = "根据考核标准ID批量删除考核标准")
    @ApiImplicitParam(name = "examineStandard", value = "考核标准信息", dataType = "examineStandard")
    @Log("批量删除考核标准")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllExamineStandard(@RequestBody ExamineStandard examineStandard) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(examineStandard.getIdString()))
//                success = examineStandardService.deleteAll(examineStandard.getIdString().split(",")) > 0;
                success = examineStandardService.deleteExamineData(examineStandard.getIdString())>0;
        } catch (Exception e) {
            log.error("删除考核标准失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
