package com.aswl.as.asos.modules.sys.controller;


import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.common.utils.DateUtils;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysAttachment;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.Servlets;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.vo.AttachmentVo;
import com.aswl.as.user.api.module.Attachment;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.common.validator.ValidatorUtils;
import com.aswl.as.asos.common.utils.R;
import com.aswl.as.asos.common.annotation.SysLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import com.aswl.as.asos.modules.sys.entity.SysAttachmentEntity;
import com.aswl.as.asos.modules.sys.service.SysAttachmentService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 附件表 前端控制器
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@RestController

@RequestMapping("/sys/attachment")
@Api("运营端使用的附件表")
public class SysAttachmentController extends AbstractController {

    @Autowired
    SysAttachmentService sysAttachmentService;

    /**
    * 附件表分页列表
    */
    @GetMapping("/list")
    @RequiresPermissions("os:attachment:list")
    @ApiOperation("附件表分页列表")
    public R list(@RequestParam Map<String, Object> params){

        PageUtils page= sysAttachmentService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
    * 附件表列表
    */
    @PostMapping("/findList")
    @RequiresPermissions("os:attachment:list")
    @ApiOperation("附件表列表")
    public R findList(@RequestBody SysAttachmentEntity entity){
        List<SysAttachmentEntity> list= sysAttachmentService.findList(entity);
        return R.ok().put("list",list);
    }

    /**
    * 附件表信息
    */
    @GetMapping("/info/{entityId}")
    @RequiresPermissions("os:attachment:info")
    @ApiOperation("附件表信息")
    public R info(@PathVariable("entityId") Long entityId){
        SysAttachmentEntity role = sysAttachmentService.getById(entityId);
        return R.ok().put("role", role);
    }

    /**
     * 上传文件
     *
     * @param file       file
     * @param attachment attachment
     * @author aswl.com
     * @date 2018/10/30 21:54
     */
    @PostMapping("upload")
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "busiType", value = "业务分类", dataType = "String"),
            @ApiImplicitParam(name = "busiId", value = "业务Id", dataType = "String"),
            @ApiImplicitParam(name = "busiModule", value = "业务模块", dataType = "String"),
    })
    @SysLog("上传文件")
    public ResponseBean<SysAttachmentEntity> upload(@ApiParam(value = "要上传的文件", required = true) @RequestParam("file") MultipartFile file,
                                                    SysAttachmentEntity attachment) {
        if (file.isEmpty())
            return new ResponseBean<>(new SysAttachmentEntity());

        attachment.setCreator(getUserName());
        attachment.setCreateDate(DateUtils.format(new Date(),DateUtils.DATE_TIME_MILLISECOND_PATTERN));
//        attachment.setApplicationCode(SysUtil.getSysCode()); //这个参数最好也传过来
//        if(StringUtils.isEmpty(attachment.getTenantCode()))
//        {
//            attachment.setTenantCode(Constant.TEMP_IMAGE_TENANT_CODE); //暂时设置是aswlTemp，然后等上传之后再更新
//        }

        return new ResponseBean<>(sysAttachmentService.upload(file, attachment)); // 上传文件
    }

    /**
     * 下载文件
     *
     * @param id id
     * @author aswl.com
     * @date 2018/10/30 22:26
     */
    @GetMapping("download")
    @ApiOperation(value = "下载附件", notes = "根据ID下载附件")
    @ApiImplicitParam(name = "id", value = "附件ID", required = true, dataType = "String")
    public void download(@NotBlank String id, HttpServletRequest request, HttpServletResponse response) {
        SysAttachmentEntity attachment = new SysAttachmentEntity();
        attachment.setId(id);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            attachment = sysAttachmentService.getEntityById(id);
            if (attachment == null)
                throw new CommonException("附件不存在！");
            inputStream = sysAttachmentService.download(attachment); // 下载附件 TODO
            inputStream=null;
            // 输出流
            outputStream = response.getOutputStream();
            response.setContentType("application/zip");
            response.setHeader(HttpHeaders.CACHE_CONTROL, "max-age=10");
            // IE之外的浏览器使用编码输出名称
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, attachment.getAttachName()));
            response.setContentLength(inputStream.available());
            // 下载文件
            FileCopyUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            //log.error(e.getMessage(), e);
            System.err.println(e.getMessage());
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
    * 删除附件表
    */
    @SysLog("删除附件表")
    @PostMapping("/delete")
    @RequiresPermissions("os:attachment:delete")
    @ApiOperation("删除附件表")
    public R delete(@RequestBody String[] ids){
        sysAttachmentService.deleteEntityByIds(ids);
        return R.ok();
    }


    /**
     * 根据附件ID批量查询
     * @param ids
     * @return
     */
    @PostMapping(value = "findById")
    @ApiOperation(value = "批量查询附件信息", notes = "根据附件ID批量查询附件信息")
    @ApiImplicitParam(name = "attachmentVo", value = "附件信息", dataType = "AttachmentVo")
    public ResponseBean<List<AttachmentVo>> findById(@RequestBody String[] ids) {
        ResponseBean<List<AttachmentVo>> returnT = null;
        Attachment attachment = new Attachment();
//        attachment.setIds(attachmentVo.getIds());

        List<SysAttachmentEntity> attachmentList = sysAttachmentService.findListByIds(ids); // 查找附件列表

        if (CollectionUtils.isNotEmpty(attachmentList)) {
            // 流处理转换成AttachmentVo
            List<AttachmentVo> attachmentVoList = attachmentList.stream().map(tempAttachment -> {
                AttachmentVo tempAttachmentVo = new AttachmentVo();
                BeanUtils.copyProperties(tempAttachment, tempAttachmentVo);
                return tempAttachmentVo;
            }).collect(Collectors.toList());
            returnT = new ResponseBean<>(attachmentVoList);
        }
        return returnT;
    }

    /**
     * 获取预览地址
     *
     * @param id id
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/06/19 15:47
     */
    @GetMapping("/{id}/preview")
    @ApiOperation(value = "获取预览地址", notes = "根据附件ID获取预览地址")
    @ApiImplicitParam(name = "id", value = "附件id", required = true, dataType = "String", paramType = "path")
    public ResponseBean<String> getPreviewUrl(@PathVariable String id) {
        SysAttachmentEntity attachment = new SysAttachmentEntity();
        attachment.setId(id);
        return new ResponseBean<>(sysAttachmentService.getPreviewUrl(attachment)); // 获取预览地址
    }
}
