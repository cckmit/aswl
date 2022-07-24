package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysAttachment;
import com.aswl.as.user.api.module.Attachment;
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
 * @since 2019-12-02
 */
public interface IAsUserSysAttachmentService extends IService<AsUserSysAttachment> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysAttachment entity);

    public boolean updateEntityById(AsUserSysAttachment entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsUserSysAttachment getEntityById(String id);

    public AsUserSysAttachment upload(MultipartFile file, AsUserSysAttachment attachment);

    public InputStream download(AsUserSysAttachment attachment);

    public String getPreviewUrl(AsUserSysAttachment attachment);

    public List<AsUserSysAttachment> findListByIds(String[] ids);

    public List<AsUserSysAttachment> findListForUpdateTenantCode(List<String> imageNames);

}
