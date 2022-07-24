package com.aswl.as.asos.modules.sys.service;

import com.aswl.as.asos.modules.sys.entity.SysAttachmentEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
public interface SysAttachmentService extends IService<SysAttachmentEntity> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(SysAttachmentEntity entity);

    public boolean updateEntityById(SysAttachmentEntity entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public SysAttachmentEntity getEntityById(String id);

    public List<SysAttachmentEntity> findList(SysAttachmentEntity entity);

    public List<SysAttachmentEntity> findListByIds(String[] ids);

    public String getPreviewUrl(SysAttachmentEntity attachment);

    public SysAttachmentEntity upload(MultipartFile file, SysAttachmentEntity attachment);

    public InputStream download(SysAttachmentEntity attachment);

}
