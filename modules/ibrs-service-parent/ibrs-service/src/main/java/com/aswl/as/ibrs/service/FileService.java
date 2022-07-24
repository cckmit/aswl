package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.Attachment;
import com.aswl.as.ibrs.api.module.File;
import com.aswl.as.ibrs.mapper.AttachmentMapper;
import com.aswl.as.ibrs.mapper.FileMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@AllArgsConstructor
@Slf4j
@Service
public class FileService extends CrudService<FileMapper, File> {
    private final FileMapper fileMapper;
    private final AttachmentMapper attachmentMapper;

    /**
     * 新增上传文件表
     *
     * @param file
     * @return int
     */
    @Transactional
    public int insert(File file,String businessKey) {
        file.setId(IdGen.snowflakeId());
        file.setApplicationCode(SysUtil.getSysCode());
        file.setTenantCode(SysUtil.getTenantCode());
        file.setUploadTime(new Date());
        int row = super.insert(file);
        if (row > 0) {
            Attachment attachment = new Attachment();
            attachment.setNewRecord(true);
            attachment.setId(IdGen.snowflakeId());
            attachment.setDownloadUrl(file.getUrl());
            attachment.setFileName(file.getFileName());
            attachment.setFileId(file.getId());
            attachment.setBusinessKey(businessKey);
            attachment.setProjectId(file.getProjectId());
            attachment.setExtend(file.getFileSize());
            attachment.setCreator(file.getUserName());
            attachment.setCreateDate(file.getUploadTime());
            attachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
            int flag = attachmentMapper.insert(attachment);
            return flag;
        }
        return row;
    }

    /**
     * 删除上传文件表
     *
     * @param file
     * @return int
     */

    @Override
    public int delete(File file) {
        return super.delete(file);
    }

    /**
     * 根据fileMd5查询
     * @param fileMd5
     * @return
     */
    @Transactional
   public  File selectByFileMd5( String fileMd5){
        return fileMapper.selectByFileMd5(fileMd5);
    }
}