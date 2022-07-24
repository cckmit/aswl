package com.aswl.as.asos.modules.asuser.service;
import com.aswl.as.asos.common.utils.PageUtils;
import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.aswl.as.asos.modules.asuser.entity.SysTenantRole;
import com.aswl.as.user.api.module.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author df
 * @date 2020/10/19 11:20
 */
public interface ISysTenantRoleService extends IService<SysTenantRole> {

     PageUtils queryPage(Map<String, Object> params);

     SysTenantRole getEntityById(String id);

      boolean saveEntity(SysTenantRole entity);

      boolean updateEntityById(SysTenantRole entity);

      boolean deleteEntityByIds(String[] ids);

      List<AsUserSysMenu> findMenuByRoleId(String roleId);
}
