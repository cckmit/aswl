package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
public interface IAsDeviceModelService extends IService<AsDeviceModel> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsDeviceModel entity);

    public boolean updateEntityById(AsDeviceModel entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public List<AsDeviceModel> findList(AsDeviceModel entity);

    public boolean checkDeviceModel(AsDeviceModel entity);

    public AsDeviceModel getEntityById(String id);

}
