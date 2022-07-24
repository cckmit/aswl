package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.log.annotation.Log;
import com.aswl.as.ibrs.api.dto.AttachmentDto;
import com.aswl.as.ibrs.api.module.Attachment;
import com.aswl.as.ibrs.service.AttachmentService;
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
 * 附件表controller
 *
 * @author com.aswl
 * @date 2020-04-17 13:56
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/attachment", tags = "附件表")
@RestController
@RequestMapping("/v1/attachment")
public class AttachmentController extends BaseController {

    private final AttachmentService attachmentService;

    /**
     * 根据ID获取附件表
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "根据附件表ID查询附件表")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "附件表ID", paramType = "path", required = true, dataType = "String"))
    @GetMapping(value = "/{id}")
    public ResponseBean<Attachment> findById(@PathVariable("id") String id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        return new ResponseBean<>(attachmentService.get(attachment));
    }

    /**
     * 查询所有附件表
     *
     * @param attachment
     * @return ResponseBean
     */

    @ApiOperation(value = "查询所有附件表列表")
    @ApiImplicitParams(@ApiImplicitParam(name = "attachment", value = "附件表对象", paramType = "path", required = true, dataType = "attachment"))
    @GetMapping(value = "attachments")
    public ResponseBean
            <List<Attachment>> findAll(Attachment attachment) {
        attachment.setTenantCode(SysUtil.getTenantCode());
        return new ResponseBean<>(attachmentService.findAllList(attachment));
    }

    /**
     * 分页查询附件表列表
     *
     * @param pageNum
     * @param pageSize
     * @param sort
     * @param order
     * @param attachment
     * @return PageInfo
     */
    @ApiOperation(value = "分页查询附件表列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.SORT, value = "排序字段", defaultValue = CommonConstant.PAGE_SORT_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.ORDER, value = "排序方向", defaultValue = CommonConstant.PAGE_ORDER_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "attachment", value = "附件表信息", dataType = "attachment")
    })

    @GetMapping("attachmentList")
    public PageInfo<Attachment> attachmentList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                               @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                               @RequestParam(value = CommonConstant.SORT, required = false, defaultValue = CommonConstant.PAGE_SORT_DEFAULT) String sort,
                                               @RequestParam(value = CommonConstant.ORDER, required = false, defaultValue = CommonConstant.PAGE_ORDER_DEFAULT) String order,
                                               Attachment attachment) {
        attachment.setTenantCode(SysUtil.getTenantCode());
        return attachmentService.findPage(PageUtil.pageInfo(pageNum, pageSize, sort, order), attachment);
    }

    /**
     * 新增附件表
     *
     * @param attachmentDto
     * @return ResponseBean
     */
    @ApiOperation(value = "新增附件表", notes = "新增附件表")
    @ApiImplicitParams(@ApiImplicitParam(name = "attachmentDto", value = "设备dto", required = true, paramType = "body", dataType = "attachmentDto"))
    @PostMapping
    @Log("新增附件表")
    public ResponseBean
            <Boolean> insertAttachment(@RequestBody @Valid AttachmentDto attachmentDto) {
        Attachment attachment = new Attachment();
        BeanUtils.copyProperties(attachmentDto, attachment);
        attachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(attachmentService.insert(attachment) > 0);
    }

    /**
     * 修改附件表
     *
     * @param attachmentDto
     * @return ResponseBean
     */

    @ApiOperation(value = "更新附件表信息", notes = "根据附件表ID更新职位信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "attachmentDto", value = "设备dto", required = true, paramType = "body", dataType = "attachmentDto"))
    @Log("修改附件表")
    @PutMapping
    public ResponseBean
            <Boolean> updateAttachment(@RequestBody @Valid AttachmentDto attachmentDto) {
        Attachment attachment = new Attachment();
        BeanUtils.copyProperties(attachmentDto, attachment);
        attachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(attachmentService.update(attachment) > 0);
    }

    /**
     * 根据附件表ID删除附件表信息
     *
     * @param id
     * @return ResponseBean
     */
    @ApiOperation(value = "删除附件表信息", notes = "根据附件表ID删除附件表信息")
    @ApiImplicitParams(@ApiImplicitParam(name = "id", value = "附件表ID", paramType = "path", required = true, dataType = "String"))
    @DeleteMapping(value = "/{id}")
    public ResponseBean
            <Boolean> deleteAttachmentById(@PathVariable("id") String id) {
        Attachment attachment = new Attachment();
        attachment.setId(id);
        attachment.setNewRecord(false);
        attachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        return new ResponseBean<>(attachmentService.delete(attachment) > 0);
    }

    /**
     * 批量删除附件表信息
     *
     * @param attachment
     * @return ResponseBean
     */

    @ApiOperation(value = "批量删除附件表", notes = "根据附件表ID批量删除附件表")
    @ApiImplicitParam(name = "attachment", value = "附件表信息", dataType = "attachment")
    @Log("批量删除附件表")
    @PostMapping("deleteAll")
    public ResponseBean
            <Boolean> deleteAllAttachment(@RequestBody Attachment attachment) {
        boolean success = false;
        try {
            if (StringUtils.isNotEmpty(attachment.getIdString()))
                success = attachmentService.deleteAll(attachment.getIdString().split(",")) > 0;
        } catch (Exception e) {
            log.error("删除附件表失败！", e);
        }
        return new ResponseBean<>(success);
    }
}
