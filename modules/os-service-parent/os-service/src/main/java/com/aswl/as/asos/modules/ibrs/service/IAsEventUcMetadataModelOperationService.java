package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.datasource.annotation.DataSource;
import com.aswl.as.asos.modules.ibrs.entity.AsEventUcMetadataModelOperation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号事件元数据-状态操作 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-01-09
 */
public interface IAsEventUcMetadataModelOperationService extends IService<AsEventUcMetadataModelOperation> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsEventUcMetadataModelOperation entity);

    public boolean updateEntityById(AsEventUcMetadataModelOperation entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsEventUcMetadataModelOperation getEntityById(String id);

    public List<AsEventUcMetadataModelOperation> findList(AsEventUcMetadataModelOperation entity);

    public int osDeleteByEventMetadataModelId(String id);

    public int osInsertBatch(List<AsEventUcMetadataModelOperation> list);

}
