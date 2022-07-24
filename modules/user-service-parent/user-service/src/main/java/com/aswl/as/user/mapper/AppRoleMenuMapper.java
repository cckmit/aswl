package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.AppRoleMenu;
import com.aswl.as.user.api.module.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * App角色菜单mapper
 *
 * @author aswl.com
 * @date 2018/8/26 22:34
 */
@Mapper
public interface AppRoleMenuMapper extends CrudMapper<AppRoleMenu> {

    /**
     * 根据角色ID删除
     *
     * @param roleId 角色ID
     * @return int
     */
    int deleteByRoleId(String roleId);

    /**
     * 根据菜单ID删除
     *
     * @param appMenuId App菜单ID
     * @return int
     */
    int deleteByMenuId(String appMenuId);

    /**
     * 批量保存
     *
     * @param roleMenus roleMenus
     * @return int
     */
    int insertBatch(List<AppRoleMenu> roleMenus);
}
