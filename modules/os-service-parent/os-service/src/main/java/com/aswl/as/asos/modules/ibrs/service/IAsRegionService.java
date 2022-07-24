package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsDeviceModelAlarmThreshold;
import com.aswl.as.asos.modules.ibrs.entity.AsRegion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 区域表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-12-11
 */
public interface IAsRegionService extends IService<AsRegion> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsRegion entity);

    public boolean updateEntityById(AsRegion entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public List<AsRegion> findList(AsRegion entity);

    public String getRegionByRegionCode(String regionCode);

    public AsRegion getEntityById(String id);

}
