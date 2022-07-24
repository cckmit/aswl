package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceKind;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备种类 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-02
 */
public interface IAsDeviceKindService extends IService<AsDeviceKind> {

    public PageUtils queryPage(Map<String, Object> params);

    public List<AsDeviceKind> findList(AsDeviceKind entity);

    public boolean saveEntity(AsDeviceKind entity);

    public boolean updateEntityById(AsDeviceKind entity);

    public AsDeviceKind getEntityById(String id);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

}
