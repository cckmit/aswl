package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ContentIndustryDto;
import com.aswl.as.ibrs.api.module.ContentIndustry;
import com.aswl.as.ibrs.service.ContentIndustryService;
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
 * 行业资讯表controller
 *
 * @author hfx
 * @date 2020-03-06 09:57
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/contentIndustry", tags = "行业资讯表")
@RestController
@RequestMapping("/v1/contentIndustry")
public class ContentIndustryController extends BaseController {

    private final ContentIndustryService contentIndustryService;

    /**
     * 根据ID获取行业资讯表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据行业资讯表ID查询行业资讯表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "行业资讯表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ContentIndustry> findById(@PathVariable("id") String id) {
        ContentIndustry contentIndustry = new ContentIndustry();
        contentIndustry.setId(id);
        return new ResponseBean<>(contentIndustryService.get(contentIndustry));
    }

    /**
     * 根据ID获取行业资讯表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据行业资讯表ID查询行业资讯表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "行业资讯表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/html/{id}",produces = {"text/plain;charset=utf-8","text/html;charset=utf-8"})
    @ResponseBody
    public String findHtmlById(@PathVariable("id") String id) {
        ContentIndustry contentIndustry = new ContentIndustry();
        contentIndustry.setId(id);
        contentIndustry=contentIndustryService.get(contentIndustry);
        return contentIndustry==null?null:contentIndustry.getContent();
    }

    //TODO 获取行业数据和行业所关联的表的数据


    /**
     * 查询所有行业资讯表
     *
     * @param contentIndustry
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有行业资讯表列表")
    @ApiImplicitParam(name = "contentIndustry", value = "行业资讯表对象", paramType = "path", required = true, dataType = "contentIndustry")
    @GetMapping(value = "contentIndustrys")
    public ResponseBean
            <List<ContentIndustry>> findAll(ContentIndustry contentIndustry) {
        contentIndustry.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentIndustryService.findList(contentIndustry));
    }

    /**
     * 分页查询行业资讯表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param contentIndustry
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询行业资讯表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contentIndustry", value = "行业资讯表信息", dataType = "contentIndustry")
    })

    @GetMapping("contentIndustryList")
    public ResponseBean<PageInfo<ContentIndustry>> contentIndustryList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                       @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                       @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                       @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                       ContentIndustry contentIndustry) {
        contentIndustry.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentIndustryService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), contentIndustry));
    }

    /**
     * 新增行业资讯表
     *
     * @param contentIndustryDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增行业资讯表", notes = "新增行业资讯表")
    @PostMapping
    @Log("新增行业资讯表")
    public ResponseBean
            <Boolean> insertContentIndustry(@RequestBody @Valid ContentIndustryDto contentIndustryDto) {
        ContentIndustry contentIndustry = new ContentIndustry();
        BeanUtils.copyProperties(contentIndustryDto, contentIndustry);
        contentIndustry.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentIndustryService.insert(contentIndustry) > 0);
    }

    /**
     * 修改行业资讯表
     *
     * @param contentIndustryDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新行业资讯表信息", notes = "根据行业资讯表ID更新行业资讯表信息")
    @Log("修改行业资讯表")
    @PutMapping
    public ResponseBean
            <Boolean> updateContentIndustry(@RequestBody @Valid ContentIndustryDto contentIndustryDto) {
        ContentIndustry contentIndustry = new ContentIndustry();
        BeanUtils.copyProperties(contentIndustryDto, contentIndustry);
        contentIndustry.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentIndustryService.update(contentIndustry) > 0);
    }

    /**
     * 根据行业资讯表ID删除行业资讯表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除行业资讯表信息", notes = "根据行业资讯表ID删除行业资讯表信息")
    @ApiImplicitParam(name = "id", value = "行业资讯表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteContentIndustryById(@PathVariable("id") String id) {
        ContentIndustry contentIndustry = new ContentIndustry();
        contentIndustry.setId(id);
        contentIndustry.setNewRecord(false);
        contentIndustry.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentIndustryService.delete(contentIndustry) > 0);
    }

    /**
     * 批量删除行业资讯表信息
     *
     * @param contentIndustry
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除行业资讯表", notes = "根据行业资讯表ID批量删除行业资讯表")
    @ApiImplicitParam(name = "contentIndustry", value = "行业资讯表信息", dataType = "contentIndustry")
    @Log("批量删除行业资讯表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllContentIndustry(@RequestBody ContentIndustry contentIndustry) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(contentIndustry.getIdString()))
                success = contentIndustryService.deleteAll(contentIndustry.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除行业资讯表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
