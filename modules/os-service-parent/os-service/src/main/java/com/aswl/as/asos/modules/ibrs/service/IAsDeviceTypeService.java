package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModel;
import com.aswl.as.asos.modules.ibrs.entity.AsDeviceType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
public interface IAsDeviceTypeService extends IService<AsDeviceType> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsDeviceType entity);

    public boolean updateEntityById(AsDeviceType entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public boolean checkDeviceType(AsDeviceType entity);

    public List<AsDeviceType> findList(AsDeviceType entity);

    public AsDeviceType getEntityById(String id);

}
