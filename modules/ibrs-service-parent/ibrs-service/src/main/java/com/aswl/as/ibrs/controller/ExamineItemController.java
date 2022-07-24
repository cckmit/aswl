package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ExamineItemDto;
import com.aswl.as.ibrs.api.module.ExamineItem;
import com.aswl.as.ibrs.service.ExamineItemService;
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
 * 考核项controller
 *
 * @author hfx
 * @date 2020-03-19 10:18
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/examineItem", tags = "考核项")
@RestController
@RequestMapping("/v1/examineItem")
public class ExamineItemController extends BaseController {

    private final ExamineItemService examineItemService;

    /**
     * 根据ID获取考核项
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据考核项ID查询考核项")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "考核项ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ExamineItem> findById(@PathVariable("id") String id) {
        ExamineItem examineItem = new ExamineItem();
        examineItem.setId(id);
        return new ResponseBean<>(examineItemService.get(examineItem));
    }

    /**
     * 查询所有考核项
     *
     * @param examineItem
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有考核项列表")
    @ApiImplicitParam(name = "examineItem", value = "考核项对象", paramType = "path", required = true, dataType = "examineItem")
    @GetMapping(value = "examineItems")
    public ResponseBean
            <List<ExamineItem>> findAll(ExamineItem examineItem) {
        examineItem.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineItemService.findList(examineItem));
    }

    /**
     * 分页查询考核项列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param examineItem
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询考核项列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "examineItem", value = "考核项信息", dataType = "examineItem")
    })

    @GetMapping("examineItemList")
    public ResponseBean<PageInfo<ExamineItem>> examineItemList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                               ExamineItem examineItem) {
        examineItem.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(examineItemService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), examineItem));
    }

    /**
     * 新增考核项
     *
     * @param examineItemDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增考核项", notes = "新增考核项")
    @PostMapping
    @Log("新增考核项")
    public ResponseBean
            <Boolean> insertExamineItem(@RequestBody @Valid ExamineItemDto examineItemDto) {
        ExamineItem examineItem = new ExamineItem();
        BeanUtils.copyProperties(examineItemDto, examineItem);
        examineItem.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineItemService.insert(examineItem) > 0);
    }

    /**
     * 修改考核项
     *
     * @param examineItemDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新考核项信息", notes = "根据考核项ID更新考核项信息")
    @Log("修改考核项")
    @PutMapping
    public ResponseBean
            <Boolean> updateExamineItem(@RequestBody @Valid ExamineItemDto examineItemDto) {
        ExamineItem examineItem = new ExamineItem();
        BeanUtils.copyProperties(examineItemDto, examineItem);
        examineItem.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineItemService.update(examineItem) > 0);
    }

    /**
     * 根据考核项ID删除考核项信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除考核项信息", notes = "根据考核项ID删除考核项信息")
    @ApiImplicitParam(name = "id", value = "考核项ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteExamineItemById(@PathVariable("id") String id) {
        ExamineItem examineItem = new ExamineItem();
        examineItem.setId(id);
        examineItem.setNewRecord(false);
        examineItem.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(examineItemService.delete(examineItem) > 0);
    }

    /**
     * 批量删除考核项信息
     *
     * @param examineItem
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除考核项", notes = "根据考核项ID批量删除考核项")
    @ApiImplicitParam(name = "examineItem", value = "考核项信息", dataType = "examineItem")
    @Log("批量删除考核项")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllExamineItem(@RequestBody ExamineItem examineItem) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(examineItem.getIdString()))
                success = examineItemService.deleteAll(examineItem.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除考核项失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
