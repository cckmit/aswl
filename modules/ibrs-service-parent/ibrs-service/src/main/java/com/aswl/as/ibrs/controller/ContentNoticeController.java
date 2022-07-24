package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.ContentNoticeDto;
import com.aswl.as.ibrs.api.module.ContentNotice;
import com.aswl.as.ibrs.service.ContentNoticeService;
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
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 系统消息表controller
 *
 * @author hfx
 * @date 2020-03-11 13:28
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/contentNotice", tags = "系统消息表")
@RestController
@RequestMapping("/v1/contentNotice")
public class ContentNoticeController extends BaseController {

    private final ContentNoticeService contentNoticeService;

    /**
     * 根据ID获取系统消息表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据系统消息表ID查询系统消息表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "系统消息表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<ContentNotice> findById(@PathVariable("id") String id) {
        ContentNotice contentNotice = new ContentNotice();
        contentNotice.setId(id);

        Integer notRelease=0;
        Date now=new Date();
        ContentNotice n =contentNoticeService.get(contentNotice);
        if(notRelease.equals(n.getIsRelease()) && now.after(n.getReleaseTime()) )
        {
            n.setIsRelease(1);
            contentNoticeService.update(n);
        }

        return new ResponseBean<>(n);
    }

    /**
     * 查询所有系统消息表
     *
     * @param contentNotice
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有系统消息表列表")
    @ApiImplicitParam(name = "contentNotice", value = "系统消息表对象", paramType = "path", required = true, dataType = "contentNotice")
    @GetMapping(value = "contentNotices")
    public ResponseBean
            <List<ContentNotice>> findAll(ContentNotice contentNotice) {
        contentNotice.setTenantCode(SysUtil.getTenantCode());


        List<ContentNotice> list=contentNoticeService.findList(contentNotice);

        Integer notRelease=0;
        Date now=new Date();
        for(ContentNotice n:list)
        {
            if(notRelease.equals(n.getIsRelease()) && now.after(n.getReleaseTime()) )
            {
                n.setIsRelease(1);
                contentNoticeService.update(n);
            }
        }

        return new ResponseBean<>(list);
    }

    /**
     * 分页查询系统消息表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param contentNotice
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询系统消息表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contentNotice", value = "系统消息表信息", dataType = "contentNotice")
    })

    @GetMapping("contentNoticeList")
    public ResponseBean<PageInfo<ContentNotice>> contentNoticeList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                   @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                   @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                                                   @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                                                   ContentNotice contentNotice) {
        contentNotice.setTenantCode(SysUtil.getTenantCode());
        PageInfo<ContentNotice> result=contentNoticeService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), contentNotice);
        List<ContentNotice> list=result.getList();
        Integer notRelease=0;
        Date now=new Date();
        for(ContentNotice n:list)
        {
            if(notRelease.equals(n.getIsRelease()) && now.after(n.getReleaseTime()) )
            {
                n.setIsRelease(1);
                contentNoticeService.update(n);
            }
        }

        return new ResponseBean<>(result);
    }

    /**
     * 新增系统消息表
     *
     * @param contentNoticeDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增系统消息表", notes = "新增系统消息表")
    @PostMapping
    @Log("新增系统消息表")
    public ResponseBean
            <Boolean> insertContentNotice(@RequestBody @Valid ContentNoticeDto contentNoticeDto) {
        ContentNotice contentNotice = new ContentNotice();
        BeanUtils.copyProperties(contentNoticeDto, contentNotice);
        contentNotice.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentNoticeService.insert(contentNotice) > 0);
    }

    /**
     * 修改系统消息表
     *
     * @param contentNoticeDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新系统消息表信息", notes = "根据系统消息表ID更新系统消息表信息")
    @Log("修改系统消息表")
    @PutMapping
    public ResponseBean
            <Boolean> updateContentNotice(@RequestBody @Valid ContentNoticeDto contentNoticeDto) {
        ContentNotice contentNotice = new ContentNotice();
        BeanUtils.copyProperties(contentNoticeDto, contentNotice);
        contentNotice.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentNoticeService.update(contentNotice) > 0);
    }

    /**
     * 根据系统消息表ID删除系统消息表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除系统消息表信息", notes = "根据系统消息表ID删除系统消息表信息")
    @ApiImplicitParam(name = "id", value = "系统消息表ID", paramType = "path", required = true, dataType = "String")
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteContentNoticeById(@PathVariable("id") String id) {
        ContentNotice contentNotice = new ContentNotice();
        contentNotice.setId(id);
        contentNotice.setNewRecord(false);
        contentNotice.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(contentNoticeService.delete(contentNotice) > 0);
    }

    /**
     * 批量删除系统消息表信息
     *
     * @param contentNotice
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除系统消息表", notes = "根据系统消息表ID批量删除系统消息表")
    @ApiImplicitParam(name = "contentNotice", value = "系统消息表信息", dataType = "contentNotice")
    @Log("批量删除系统消息表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllContentNotice(@RequestBody ContentNotice contentNotice) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(contentNotice.getIdString()))
                success = contentNoticeService.deleteAll(contentNotice.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除系统消息表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
