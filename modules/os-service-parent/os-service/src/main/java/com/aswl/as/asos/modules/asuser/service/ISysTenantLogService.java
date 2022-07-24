package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.SysTenantLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SAAS租户信息记录表 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-02
 */
public interface ISysTenantLogService extends IService<SysTenantLog> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(SysTenantLog entity);

    public boolean updateEntityById(SysTenantLog entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public SysTenantLog getEntityById(String id);

    public List<SysTenantLog> findList(SysTenantLog entity);

}
