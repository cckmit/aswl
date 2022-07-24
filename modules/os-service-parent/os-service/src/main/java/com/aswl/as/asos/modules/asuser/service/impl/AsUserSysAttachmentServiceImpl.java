package com.aswl.as.asos.modules.asuser.service.impl;

import com.aswl.as.asos.common.utils.Constant;
import com.aswl.as.asos.modules.asuser.service.IOsFastDfsService;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.properties.SysProperties;
import com.aswl.as.common.core.utils.FileUtil;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.asos.common.utils.Query;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysAttachment;
import com.aswl.as.asos.modules.asuser.mapper.SysAttachmentMapper;
import com.aswl.as.asos.modules.asuser.service.IAsUserSysAttachmentService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.user.api.module.Attachment;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.transaction.annotation.Transactional;
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
 * @since 2019-12-02
 */
@Service
@Slf4j
@AllArgsConstructor
public class AsUserSysAttachmentServiceImpl extends ServiceImpl<SysAttachmentMapper, AsUserSysAttachment> implements IAsUserSysAttachmentService {

    // 需要添加IOsfastDfsService
    private final IOsFastDfsService iOsfastDfsService;

    private final SysProperties sysProperties;


    @DataSource("slave2")
    public PageUtils queryPage(Map<String, Object> params)
    {
        /*
        String projectName = (String)params.get("projectName");
        String projectDes = (String)params.get("projectDes");
        String projectOwner = (String)params.get("projectOwner");
        */

        IPage<AsUserSysAttachment> page = this.page(
            new Query<AsUserSysAttachment>().getPage(params),
                new QueryWrapper<AsUserSysAttachment>()
                /*
                .like(StringUtils.isNotBlank(projectName),"project_name",projectName)
                .like(StringUtils.isNotBlank(projectDes),"project_des",projectDes)
                .like(StringUtils.isNotBlank(projectOwner),"project_owner",projectOwner)
                */
            );

        return new PageUtils(page);
    }

    //根据id返回对应信息
    @DataSource("slave2")
    public AsUserSysAttachment getEntityById(String id)
    {
        return this.getById(id);
    }

    @DataSource("slave2")
    public boolean saveEntity(AsUserSysAttachment entity)
    {
//        entity.setProjectId(IdGen.snowflakeId());
        entity.setId(IdGen.snowflakeId());
        return this.save(entity);
    }

    @DataSource("slave2")
    public boolean updateEntityById(AsUserSysAttachment entity)
    {
        return this.updateById(entity);
    }

    @DataSource("slave2")
    public boolean deleteEntityById(String id)
    {
        return this.removeById(id);
    }

    @DataSource("slave2")
    public boolean deleteEntityByIds(String[] ids)
    {
        for(String id:ids)
        {
            this.removeById(id);
        }
        return true;
    }

    /**
     * 上传
     *
     * @param file       file
     * @param attachment attachment
     * @return int
     */
    @Transactional
    @DataSource("slave2")
    public AsUserSysAttachment upload(MultipartFile file, AsUserSysAttachment attachment) {
        InputStream inputStream = null;
        try {
            long start = System.currentTimeMillis();
            inputStream = file.getInputStream();
            long attachSize = file.getSize();
            String fastFileId = iOsfastDfsService.uploadFile(inputStream, attachSize, FileUtil.getFileNameEx(file.getOriginalFilename()));
            if (StringUtils.isBlank(fastFileId))
                throw new CommonException("上传失败！");

            String preview = sysProperties.getFdfsHttpHost() + "/" + fastFileId;
            AsUserSysAttachment newAttachment = new AsUserSysAttachment();

//            newAttachment.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode()); // 需要添加创建人等默认值

            // 设置ID
            newAttachment.setId(IdGen.snowflakeId());

            newAttachment.setCreator(attachment.getCreator());
            newAttachment.setCreateDate(attachment.getCreateDate());
            newAttachment.setApplicationCode(attachment.getApplicationCode());
            newAttachment.setTenantCode(attachment.getTenantCode());

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

    /**
     * 下载
     *
     * @param attachment attachment
     * @return InputStream
     */
    @DataSource("slave2")
    public InputStream download(AsUserSysAttachment attachment) {
        // 下载附件
        return iOsfastDfsService.downloadStream(attachment.getGroupName(), attachment.getFastFileId());
    }

    /**
     * 获取附件的预览地址
     *
     * @param attachment attachment
     * @return String
     * @author aswl.com
     * @date 2019/06/21 17:45
     */
    public String getPreviewUrl(AsUserSysAttachment attachment) {
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

    //提供接口给批量查找数据
    @DataSource("slave2")
    public List<AsUserSysAttachment> findListByIds(String[] ids)
    {
        return this.list(new QueryWrapper<AsUserSysAttachment>().in("id",ids));
    }

    @DataSource("slave2")
    public List<AsUserSysAttachment> findListForUpdateTenantCode(List<String> imageNames)
    {
        //已经保存过的不会被查出来

        //查出作为临时图片的标记的图片，并且图片名不为空的图
        return this.list(new QueryWrapper<AsUserSysAttachment>()
                .eq("tenant_code", Constant.TEMP_IMAGE_TENANT_CODE).in("preview_url",imageNames));
    }

}
