package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.SysTenantRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户角色菜单表 服务类
 * </p>
 *
 * @author df
 * @since 2020-11-19
 */
public interface ISysTenantRoleMenuService extends IService<SysTenantRoleMenu> {

     PageUtils queryPage(Map<String, Object> params);

     boolean saveEntity(SysTenantRoleMenu entity);

     boolean updateEntityById(SysTenantRoleMenu entity);

     boolean deleteEntityById(String id);

     boolean deleteEntityByIds(String[] ids);

     SysTenantRoleMenu getEntityById(String id);

     List<SysTenantRoleMenu> findList(SysTenantRoleMenu entity);

     int saveRoleMenus(String role, List<String> menus);

}
