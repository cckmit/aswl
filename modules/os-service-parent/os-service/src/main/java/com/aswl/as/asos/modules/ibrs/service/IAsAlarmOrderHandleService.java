package com.aswl.as.asos.modules.ibrs.service;

import com.aswl.as.asos.modules.ibrs.entity.AsAlarmOrderHandle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 派单处理工单设置 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-23
 */
public interface IAsAlarmOrderHandleService extends IService<AsAlarmOrderHandle> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsAlarmOrderHandle entity);

    public boolean updateEntityById(AsAlarmOrderHandle entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public AsAlarmOrderHandle getEntityById(String id);

    public List<AsAlarmOrderHandle> findList(AsAlarmOrderHandle entity);

    public boolean saveEntityByTenantCode(String tenantCode,String creator);

}
