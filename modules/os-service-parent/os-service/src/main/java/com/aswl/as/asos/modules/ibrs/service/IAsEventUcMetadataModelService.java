package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadataModel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 事件元数据-设备型号 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
public interface IAsEventUcMetadataModelService extends IService<AsEventUcMetadataModel> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsEventUcMetadataModel entity);

    public boolean updateEntityById(AsEventUcMetadataModel entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsEventUcMetadataModel getEntityById(String id);

    public List<AsEventUcMetadataModel> findList(AsEventUcMetadataModel entity);

}
