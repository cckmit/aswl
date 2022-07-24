package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ContentMalfunctionDto;
import com.aswl.as.ibrs.api.module.ContentMalfunction;
import com.aswl.as.ibrs.service.ContentMalfunctionService;
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
 * 常见故障表controller
 *
 * @author hfx
 * @date 2020-03-06 10:20
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/contentMalfunction", tags = "常见故障表")
@RestController
@RequestMapping("/v1/contentMalfunction")
public class ContentMalfunctionController extends BaseController {

    private final ContentMalfunctionService contentMalfunctionService;

    /**
     * 根据ID获取常见故障表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据常见故障表ID查询常见故障表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "常见故障表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ContentMalfunction> findById(@PathVariable("id") String id) {
        ContentMalfunction contentMalfunction = new ContentMalfunction();
        contentMalfunction.setId(id);
        return new ResponseBean<>(contentMalfunctionService.get(contentMalfunction));
    }

    /**
     * 根据ID获取常见故障表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据常见故障表ID查询常见故障表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "常见故障表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/html/{id}",produces = {"text/plain;charset=utf-8","text/html;charset=utf-8"})
    @ResponseBody
    public String findHtmlById(@PathVariable("id") String id) {
        ContentMalfunction contentMalfunction = new ContentMalfunction();
        contentMalfunction.setId(id);
        contentMalfunction=contentMalfunctionService.get(contentMalfunction);
        return contentMalfunction==null?null:contentMalfunction.getContent();
    }

    /**
     * 查询所有常见故障表
     *
     * @param contentMalfunction
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有常见故障表列表")
    @ApiImplicitParam(name = "contentMalfunction", value = "常见故障表对象", paramType = "path", required = true, dataType = "contentMalfunction")
    @GetMapping(value = "contentMalfunctions")
    public ResponseBean
            <List<ContentMalfunction>> findAll(ContentMalfunction contentMalfunction) {
        contentMalfunction.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(contentMalfunctionService.findList(contentMalfunction));
    }

    /**
     * 分页查询常见故障表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param contentMalfunction
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询常见故障表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contentMalfunction", value = "常见故障表信息", dataType = "contentMalfunction")
    })

    @GetMapping("contentMalfunctionList")
    public ResponseBean<PageInfo<ContentMalfunction>> contentMalfunctionList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                             @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                             @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                             @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                             ContentMalfunction contentMalfunction) {
        contentMalfunction.setTenantCode(SysUtil.getTenantCode());

        PageInfo info=contentMalfunctionService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), contentMalfunction);

        List<ContentMalfunction>list=info.getList();
        contentMalfunctionService.addHeatCount(list);

        return new ResponseBean<>(info);
    }

    /**
     * 新增常见故障表
     *
     * @param contentMalfunctionDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增常见故障表", notes = "新增常见故障表")
    @PostMapping
    @Log("新增常见故障表")
    public ResponseBean
            <Boolean> insertContentMalfunction(@RequestBody @Valid ContentMalfunctionDto contentMalfunctionDto) {
        ContentMalfunction contentMalfunction = new ContentMalfunction();
        BeanUtils.copyProperties(contentMalfunctionDto, contentMalfunction);
        contentMalfunction.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentMalfunctionService.insert(contentMalfunction) > 0);
    }

    /**
     * 修改常见故障表
     *
     * @param contentMalfunctionDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新常见故障表信息", notes = "根据常见故障表ID更新常见故障表信息")
    @Log("修改常见故障表")
    @PutMapping
    public ResponseBean
            <Boolean> updateContentMalfunction(@RequestBody @Valid ContentMalfunctionDto contentMalfunctionDto) {
        ContentMalfunction contentMalfunction = new ContentMalfunction();
        BeanUtils.copyProperties(contentMalfunctionDto, contentMalfunction);
        contentMalfunction.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentMalfunctionService.update(contentMalfunction) > 0);
    }

    /**
     * 根据常见故障表ID删除常见故障表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除常见故障表信息", notes = "根据常见故障表ID删除常见故障表信息")
    @ApiImplicitParam(name = "id", value = "常见故障表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteContentMalfunctionById(@PathVariable("id") String id) {
        ContentMalfunction contentMalfunction = new ContentMalfunction();
        contentMalfunction.setId(id);
        contentMalfunction.setNewRecord(false);
        contentMalfunction.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentMalfunctionService.delete(contentMalfunction) > 0);
    }

    /**
     * 批量删除常见故障表信息
     *
     * @param contentMalfunction
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除常见故障表", notes = "根据常见故障表ID批量删除常见故障表")
    @ApiImplicitParam(name = "contentMalfunction", value = "常见故障表信息", dataType = "contentMalfunction")
    @Log("批量删除常见故障表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllContentMalfunction(@RequestBody ContentMalfunction contentMalfunction) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(contentMalfunction.getIdString()))
                success = contentMalfunctionService.deleteAll(contentMalfunction.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除常见故障表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
