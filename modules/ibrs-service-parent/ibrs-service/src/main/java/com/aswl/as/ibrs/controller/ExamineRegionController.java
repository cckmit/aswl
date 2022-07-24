package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ExamineRegionDto;
import com.aswl.as.ibrs.api.module.ExamineRegion;
import com.aswl.as.ibrs.service.ExamineRegionService;
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
 * 考核区域关联表controller
 *
 * @author hfx
 * @date 2020-03-19 10:19
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/examineRegion", tags = "考核区域关联表")
@RestController
@RequestMapping("/v1/examineRegion")
public class ExamineRegionController extends BaseController {

    private final ExamineRegionService examineRegionService;

    /**
     * 根据ID获取考核区域关联表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据考核区域关联表ID查询考核区域关联表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "考核区域关联表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ExamineRegion> findById(@PathVariable("id") String id) {
        ExamineRegion examineRegion = new ExamineRegion();
        examineRegion.setId(id);
        return new ResponseBean<>(examineRegionService.get(examineRegion));
    }

    /**
     * 查询所有考核区域关联表
     *
     * @param examineRegion
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有考核区域关联表列表")
    @ApiImplicitParam(name = "examineRegion", value = "考核区域关联表对象", paramType = "path", required = true, dataType = "examineRegion")
    @GetMapping(value = "examineRegions")
    public ResponseBean
            <List<ExamineRegion>> findAll(ExamineRegion examineRegion) {
        examineRegion.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineRegionService.findList(examineRegion));
    }

    /**
     * 分页查询考核区域关联表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param examineRegion
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询考核区域关联表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "examineRegion", value = "考核区域关联表信息", dataType = "examineRegion")
    })

    @GetMapping("examineRegionList")
    public ResponseBean<PageInfo<ExamineRegion>> examineRegionList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                   ExamineRegion examineRegion) {
        examineRegion.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineRegionService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examineRegion));
    }

    /**
     * 新增考核区域关联表
     *
     * @param examineRegionDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增考核区域关联表", notes = "新增考核区域关联表")
    @PostMapping
    @Log("新增考核区域关联表")
    public ResponseBean
            <Boolean> insertExamineRegion(@RequestBody @Valid ExamineRegionDto examineRegionDto) {
        ExamineRegion examineRegion = new ExamineRegion();
        BeanUtils.copyProperties(examineRegionDto, examineRegion);
        examineRegion.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineRegionService.insert(examineRegion) > 0);
    }

    /**
     * 修改考核区域关联表
     *
     * @param examineRegionDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新考核区域关联表信息", notes = "根据考核区域关联表ID更新考核区域关联表信息")
    @Log("修改考核区域关联表")
    @PutMapping
    public ResponseBean
            <Boolean> updateExamineRegion(@RequestBody @Valid ExamineRegionDto examineRegionDto) {
        ExamineRegion examineRegion = new ExamineRegion();
        BeanUtils.copyProperties(examineRegionDto, examineRegion);
        examineRegion.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineRegionService.update(examineRegion) > 0);
    }

    /**
     * 根据考核区域关联表ID删除考核区域关联表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除考核区域关联表信息", notes = "根据考核区域关联表ID删除考核区域关联表信息")
    @ApiImplicitParam(name = "id", value = "考核区域关联表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteExamineRegionById(@PathVariable("id") String id) {
        ExamineRegion examineRegion = new ExamineRegion();
        examineRegion.setId(id);
        examineRegion.setNewRecord(false);
        examineRegion.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineRegionService.delete(examineRegion) > 0);
    }

    /**
     * 批量删除考核区域关联表信息
     *
     * @param examineRegion
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除考核区域关联表", notes = "根据考核区域关联表ID批量删除考核区域关联表")
    @ApiImplicitParam(name = "examineRegion", value = "考核区域关联表信息", dataType = "examineRegion")
    @Log("批量删除考核区域关联表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllExamineRegion(@RequestBody ExamineRegion examineRegion) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(examineRegion.getIdString()))
                success = examineRegionService.deleteAll(examineRegion.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除考核区域关联表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
