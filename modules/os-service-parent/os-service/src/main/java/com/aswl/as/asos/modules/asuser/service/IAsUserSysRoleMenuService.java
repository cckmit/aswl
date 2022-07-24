package com.aswl.as.asos.modules.asuser.service;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aswl.as.asos.common.utils.PageUtils;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 角色权限表 服务类
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
public interface IAsUserSysRoleMenuService extends IService<AsUserSysRoleMenu> {

    public PageUtils queryPage(Map<String, Object> params);

    public boolean saveEntity(AsUserSysRoleMenu entity);

    public boolean updateEntityById(AsUserSysRoleMenu entity);

    public boolean deleteEntityById(String id);

    public boolean deleteEntityByIds(String[] ids);

    public int deleteByRoleId(String roleId);

}
