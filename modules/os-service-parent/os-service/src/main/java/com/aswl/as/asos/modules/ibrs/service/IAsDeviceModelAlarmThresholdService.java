package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModelAlarmThreshold;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号区间报警 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-01-08
 */
public interface IAsDeviceModelAlarmThresholdService extends IService<AsDeviceModelAlarmThreshold> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsDeviceModelAlarmThreshold entity);

    public boolean updateEntityById(AsDeviceModelAlarmThreshold entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsDeviceModelAlarmThreshold getEntityById(String id);

    public List<AsDeviceModelAlarmThreshold> findList(AsDeviceModelAlarmThreshold entity);

    public boolean insertBatch(List<AsDeviceModelAlarmThreshold> list);

}
