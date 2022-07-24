package com.aswl.as.ibrs.controller;

import cn.hutool.core.collection.CollUtil;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.utils.TreeUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.CategoryTreeDto;
import com.aswl.as.ibrs.api.dto.RegionDto;
import com.aswl.as.ibrs.api.module.CategoryTree;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.service.CategoryTreeService;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

/**
 * 通用分类树表controller
 *
 * @author hfx
 * @date 2020-03-06 09:16
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/categoryTree", tags = "通用分类树表")
@RestController
@RequestMapping("/v1/categoryTree")
public class CategoryTreeController extends BaseController {

    private final CategoryTreeService categoryTreeService;

    /**
     * 根据ID获取通用分类树表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据通用分类树表ID查询通用分类树表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "通用分类树表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<CategoryTree> findById(@PathVariable("id") String id) {
        CategoryTree categoryTree = new CategoryTree();
        categoryTree.setId(id);
        return new ResponseBean<>(categoryTreeService.get(categoryTree));
    }

    /**
     * 查询所有通用分类树表
     *
     * @param categoryTree
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有通用分类树表列表")
    @ApiImplicitParam(name = "categoryTree", value = "通用分类树表对象", paramType = "path", required = true, dataType = "categoryTree")
    @GetMapping(value = "categoryTrees")
    public ResponseBean
            <List<CategoryTree>> findAll(CategoryTree categoryTree) {
        categoryTree.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(categoryTreeService.findList(categoryTree));
    }

    /**
     * 分页查询通用分类树表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param categoryTree
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询通用分类树表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "categoryTree", value = "通用分类树表信息", dataType = "categoryTree")
    })

    @GetMapping("categoryTreeList")
    public ResponseBean<PageInfo<CategoryTree>> categoryTreeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                 @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                 @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                 @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                 CategoryTree categoryTree) {
        categoryTree.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(categoryTreeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), categoryTree));
    }

    /**
     * 查询树形区域集合
     * @return
     */
    @GetMapping(value = "tree")
    @ApiOperation(value = "获取区域列表")
    public ResponseBean<List<CategoryTreeDto>> categoryTrees(@RequestParam(value = "category") Integer category) {

        if(category!=null)
        {
            String parentId="-1";
            CategoryTree categoryTree=new CategoryTree();
            categoryTree.setCategory(category);
            Stream<CategoryTree> stream = categoryTreeService.findList(categoryTree).stream();
            if (Optional.ofNullable(stream).isPresent()) {
                List<CategoryTreeDto> treeList = stream.map(CategoryTreeDto::new).collect(Collectors.toList());
                // 排序、构建树形结构
                List<CategoryTreeDto> list = TreeUtil.buildTree(CollUtil.sort(treeList, Comparator.comparingInt(CategoryTreeDto::getSort)), parentId);
                return new ResponseBean<>(list);
            }
        }

        return new ResponseBean<>(new ArrayList<>());
    }

    /**
     * 新增通用分类树表
     *
     * @param categoryTreeDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增通用分类树表", notes = "新增通用分类树表")
    @PostMapping
    @Log("新增通用分类树表")
    public ResponseBean
            <Boolean> insertCategoryTree(@RequestBody @Valid CategoryTreeDto categoryTreeDto) {
        CategoryTree categoryTree = new CategoryTree();
        BeanUtils.copyProperties(categoryTreeDto, categoryTree);
        categoryTree.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(categoryTreeService.insert(categoryTree) > 0);
    }

    /**
     * 修改通用分类树表
     *
     * @param categoryTreeDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新通用分类树表信息", notes = "根据通用分类树表ID更新通用分类树表信息")
    @Log("修改通用分类树表")
    @PutMapping
    public ResponseBean<Boolean> updateCategoryTree(@RequestBody @Valid CategoryTreeDto categoryTreeDto) {
        CategoryTree categoryTree = new CategoryTree();
        BeanUtils.copyProperties(categoryTreeDto, categoryTree);
        categoryTree.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(categoryTreeService.update(categoryTree) > 0);
    }

    /**
     * 根据通用分类树表ID删除通用分类树表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除通用分类树表信息", notes = "根据通用分类树表ID删除通用分类树表信息")
    @ApiImplicitParam(name = "id", value = "通用分类树表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteCategoryTreeById(@PathVariable("id") String id) {
        CategoryTree categoryTree = new CategoryTree();
        categoryTree.setId(id);
        categoryTree.setNewRecord(false);
        categoryTree.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(categoryTreeService.delete(categoryTree) > 0);
    }

    /**
     * 添加一个专门给APP用的接口，用来获取大分类下面的小分类
     *
     * @param category
     * @return ResponseBean
     */
    @ApiOperation(value = "查询所有通用分类树表列表")
    @ApiImplicitParam(name = "categoryTree", value = "通用分类树表对象", paramType = "path", required = true, dataType = "categoryTree")
    @GetMapping(value = "findCategoryForApp/{category}")
    public ResponseBean
            <List<CategoryTree>> findCategoryForApp(@PathVariable("category") Integer category) {
        CategoryTree categoryTree=new CategoryTree();
        categoryTree.setCategory(category);
        categoryTree.setDelFlag(0);
        List<CategoryTree> list=categoryTreeService.findList(categoryTree);

        for(int i=0;i<list.size();i++)
        {
            CategoryTree temp=list.get(i);
            if("-1".equals(temp.getParentId()))
            {
                list.remove(i);
                break;
            }
        }
        return new ResponseBean<>(list);
    }

    /**
     * 批量删除通用分类树表信息
     *
     * @param categoryTree
     * @return ResponseBean
     */
    @ApiOperation(value = "批量删除通用分类树表", notes = "根据通用分类树表ID批量删除通用分类树表")
    @ApiImplicitParam(name = "categoryTree", value = "通用分类树表信息", dataType = "categoryTree")
    @Log("批量删除通用分类树表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllCategoryTree(@RequestBody CategoryTree categoryTree) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(categoryTree.getIdString()))
                success = categoryTreeService.deleteAll(categoryTree.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除通用分类树表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
