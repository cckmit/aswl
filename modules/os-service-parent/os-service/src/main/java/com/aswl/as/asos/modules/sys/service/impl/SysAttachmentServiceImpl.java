package com.aswl.as.asos.modules.sys.service.impl;

import com.aswl.as.asos.modules.asuser.service.IOsFastDfsService;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.properties.SysProperties;
import com.aswl.as.common.core.utils.FileUtil;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.sys.entity.SysAttachmentEntity;
import com.aswl.as.asos.modules.sys.dao.SysAttachmentDao;
import com.aswl.as.asos.modules.sys.service.SysAttachmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
@Service
@Slf4j
@AllArgsConstructor
public class SysAttachmentServiceImpl extends ServiceImpl<SysAttachmentDao, SysAttachmentEntity> implements SysAttachmentService {

    // 需要添加IOsfastDfsService
    private final IOsFastDfsService iOsfastDfsService;

    private final SysProperties sysProperties;

    public PageUtils queryPage(Map<String, Object> params)
    {
        IPage<SysAttachmentEntity> page = this.page(
            new Query<SysAttachmentEntity>().getPage(params),
                new QueryWrapper<SysAttachmentEntity>()

            );

        return new PageUtils(page);
    }

    public List<SysAttachmentEntity> findList(SysAttachmentEntity entity)
    {
        return list(new QueryWrapper<SysAttachmentEntity>());
    }

    //根据id返回对应信息
    public SysAttachmentEntity getEntityById(String id)
    {
        return this.getById(id);
    }

    public boolean saveEntity(SysAttachmentEntity entity)
    {
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    public boolean updateEntityById(SysAttachmentEntity entity)
    {
        return this.updateById(entity);
    }

    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    public boolean deleteEntityByIds(String[] ids)
    {
        for(String id:ids)
        {
            this.removeById(id);
        }
        return true;
    }


    public SysAttachmentEntity upload(MultipartFile file, SysAttachmentEntity attachment) {
        InputStream inputStream = null;
        try {
            long start = System.currentTimeMillis();
            inputStream = file.getInputStream();
            long attachSize = file.getSize();
            String fastFileId = iOsfastDfsService.uploadFile(inputStream, attachSize, FileUtil.getFileNameEx(file.getOriginalFilename()));
            if (StringUtils.isBlank(fastFileId))
                throw new CommonException("上传失败！");

            String preview = sysProperties.getFdfsHttpHost() + "/" + fastFileId;
            SysAttachmentEntity newAttachment = new SysAttachmentEntity();

//            newAttachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode()); // 需要添加创建人等默认值

            // 设置ID
            newAttachment.setId(IdGen.snowflakeId());

            newAttachment.setCreator(attachment.getCreator());
            newAttachment.setCreateDate(attachment.getCreateDate());

            newAttachment.setGroupName(fastFileId.substring(0, fastFileId.indexOf("/")));
            newAttachment.setFastFileId(fastFileId);
            newAttachment.setAttachName(new String(file.getOriginalFilename().getBytes(), StandardCharsets.UTF_8));
            newAttachment.setAttachSize(Long.toString(attachSize));
            newAttachment.setBusiId(attachment.getBusiId());
            newAttachment.setBusiModule(attachment.getBusiModule());
            newAttachment.setBusiType(attachment.getBusiType());
            newAttachment.setPreviewUrl(preview);
            //super.insert(newAttachment);
            this.save(newAttachment);
            log.info("上传文件{}成功，耗时：{}ms", file.getName(), System.currentTimeMillis() - start); // 修改日志
            return newAttachment;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return null;
    }

    public InputStream download(SysAttachmentEntity attachment) {
        // 下载附件
        return iOsfastDfsService.downloadStream(attachment.getGroupName(), attachment.getFastFileId());
    }

    //提供接口给批量查找数据
    public List<SysAttachmentEntity> findListByIds(String[] ids)
    {
        return this.list(new QueryWrapper<SysAttachmentEntity>().in("id",ids));
    }

    /**
     * 获取附件的预览地址
     *
     * @param attachment attachment
     * @return String
     * @author aswl.com
     * @date 2019/06/21 17:45
     */
    public String getPreviewUrl(SysAttachmentEntity attachment) {
        //attachment = this.get(attachment);
        attachment=this.getById(attachment.getId());
        if (attachment == null)
            throw new CommonException("附件不存在.");
        String preview = attachment.getPreviewUrl();
        if (StringUtils.isBlank(preview))
            preview = sysProperties.getFdfsHttpHost() + "/" + attachment.getFastFileId();
        //log.debug("id为：{}的附件的预览地址：{}", attachment.getId(), preview);
        return preview;
    }


}
