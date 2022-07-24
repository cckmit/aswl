package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.common.utils.LRUMap;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.aswl.as.ibrs.api.module.Region;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * SAAS租户信息表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-18
 */
public interface ISysTenantService extends IService<SysTenant> {


    PageUtils queryPage(Map<String, Object> params);

    SysTenant getEntityById(String id);

    boolean saveEntity(SysTenant entity);

    boolean updateEntityById(SysTenant entity);

    boolean update(SysTenant entity);

    boolean deleteEntityById(String id);

    boolean deleteEntityByIds(String[] ids);

    SysTenant getSysTenantByTenantCode(String tenantCode);

    boolean updateStatus(String[] tenantCodes, String status);

    boolean insertTenantData(SysTenant entity) throws Exception;

    Region getRegionListByUsers();

    List<SysTenant> findList(SysTenant entity);

    boolean enableTenant(SysTenant entity);

    boolean disableTenant(SysTenant entity);

    boolean renewTenant(SysTenant entity);

    int getMobileExist(String mobile,String id);

    SysTenant findTenantByTenantCode(String tenantCode);


}
