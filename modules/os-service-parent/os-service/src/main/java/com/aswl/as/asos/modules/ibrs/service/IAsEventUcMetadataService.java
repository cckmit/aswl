package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadata;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件元数据 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
public interface IAsEventUcMetadataService extends IService<AsEventUcMetadata> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsEventUcMetadata entity);

    public boolean updateEntityById(AsEventUcMetadata entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsEventUcMetadata getEntityById(String id);

    public List<AsEventUcMetadata> findList(AsEventUcMetadata entity);

    public List<AsEventUcMetadata> osFindEventUcMetadataByDeviceModelId(String id);

}
