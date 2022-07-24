package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsAlarmLevel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 报警级别 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-23
 */
public interface IAsAlarmLevelService extends IService<AsAlarmLevel> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsAlarmLevel entity);

    public boolean updateEntityById(AsAlarmLevel entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsAlarmLevel getEntityById(String id);

    public List<AsAlarmLevel> findList(AsAlarmLevel entity);

    public boolean newAlarmLevelByTenant(String tenantCode);

}
