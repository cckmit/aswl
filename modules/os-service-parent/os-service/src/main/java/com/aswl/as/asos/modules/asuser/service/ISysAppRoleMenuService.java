package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.SysAppRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * APP角色权限表 服务类
 * </p>
 *
 * @author hfx
 * @since 2020-03-25
 */
public interface ISysAppRoleMenuService extends IService<SysAppRoleMenu> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(SysAppRoleMenu entity);

    public boolean updateEntityById(SysAppRoleMenu entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public SysAppRoleMenu getEntityById(String id);

    public List<SysAppRoleMenu> findList(SysAppRoleMenu entity);

}
