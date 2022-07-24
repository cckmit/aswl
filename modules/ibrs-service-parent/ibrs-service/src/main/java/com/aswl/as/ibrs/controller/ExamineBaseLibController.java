package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ExamineBaseLibDto;
import com.aswl.as.ibrs.api.module.ExamineBaseLib;
import com.aswl.as.ibrs.service.ExamineBaseLibService;
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
 * 考核指标库controller
 *
 * @author hfx
 * @date 2020-03-19 10:17
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/examineBaseLib", tags = "考核指标库")
@RestController
@RequestMapping("/v1/examineBaseLib")
public class ExamineBaseLibController extends BaseController {

    private final ExamineBaseLibService examineBaseLibService;

    /**
     * 根据ID获取考核指标库
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据考核指标库ID查询考核指标库")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "考核指标库ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ExamineBaseLib> findById(@PathVariable("id") String id) {
        ExamineBaseLib examineBaseLib = new ExamineBaseLib();
        examineBaseLib.setId(id);
        return new ResponseBean<>(examineBaseLibService.get(examineBaseLib));
    }

    /**
     * 查询所有考核指标库
     *
     * @param examineBaseLib
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有考核指标库列表")
    @ApiImplicitParam(name = "examineBaseLib", value = "考核指标库对象", paramType = "path", required = true, dataType = "examineBaseLib")
    @GetMapping(value = "examineBaseLibs")
    public ResponseBean
            <List<ExamineBaseLib>> findAll(ExamineBaseLib examineBaseLib) {
        examineBaseLib.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineBaseLibService.findList(examineBaseLib));
    }

    /**
     * 分页查询考核指标库列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param examineBaseLib
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询考核指标库列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "examineBaseLib", value = "考核指标库信息", dataType = "examineBaseLib")
    })

    @GetMapping("examineBaseLibList")
    public ResponseBean<PageInfo<ExamineBaseLib>> examineBaseLibList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                     @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                     @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                     @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                     ExamineBaseLib examineBaseLib) {
        examineBaseLib.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineBaseLibService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examineBaseLib));
    }

    /**
     * 新增考核指标库
     *
     * @param examineBaseLibDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增考核指标库", notes = "新增考核指标库")
    @PostMapping
    @Log("新增考核指标库")
    public ResponseBean
            <Boolean> insertExamineBaseLib(@RequestBody @Valid ExamineBaseLibDto examineBaseLibDto) {
        ExamineBaseLib examineBaseLib = new ExamineBaseLib();
        BeanUtils.copyProperties(examineBaseLibDto, examineBaseLib);
        examineBaseLib.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineBaseLibService.insert(examineBaseLib) > 0);
    }

    /**
     * 修改考核指标库
     *
     * @param examineBaseLibDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新考核指标库信息", notes = "根据考核指标库ID更新考核指标库信息")
    @Log("修改考核指标库")
    @PutMapping
    public ResponseBean
            <Boolean> updateExamineBaseLib(@RequestBody @Valid ExamineBaseLibDto examineBaseLibDto) {
        ExamineBaseLib examineBaseLib = new ExamineBaseLib();
        BeanUtils.copyProperties(examineBaseLibDto, examineBaseLib);
        examineBaseLib.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineBaseLibService.update(examineBaseLib) > 0);
    }

    /**
     * 根据考核指标库ID删除考核指标库信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除考核指标库信息", notes = "根据考核指标库ID删除考核指标库信息")
    @ApiImplicitParam(name = "id", value = "考核指标库ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteExamineBaseLibById(@PathVariable("id") String id) {
        ExamineBaseLib examineBaseLib = new ExamineBaseLib();
        examineBaseLib.setId(id);
        examineBaseLib.setNewRecord(false);
        examineBaseLib.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineBaseLibService.delete(examineBaseLib) > 0);
    }

    /**
     * 批量删除考核指标库信息
     *
     * @param examineBaseLib
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除考核指标库", notes = "根据考核指标库ID批量删除考核指标库")
    @ApiImplicitParam(name = "examineBaseLib", value = "考核指标库信息", dataType = "examineBaseLib")
    @Log("批量删除考核指标库")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllExamineBaseLib(@RequestBody ExamineBaseLib examineBaseLib) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(examineBaseLib.getIdString()))
                success = examineBaseLibService.deleteAll(examineBaseLib.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除考核指标库失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
