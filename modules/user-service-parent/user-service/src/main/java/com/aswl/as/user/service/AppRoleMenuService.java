package com.aswl.as.user.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.user.api.module.AppRoleMenu;
import com.aswl.as.user.api.module.RoleMenu;
import com.aswl.as.user.mapper.AppRoleMenuMapper;
import com.aswl.as.user.mapper.RoleMenuMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aswl.com
 * @date 2018/8/26 22:47
 */
@AllArgsConstructor
@Service
public class AppRoleMenuService extends CrudService<AppRoleMenuMapper, AppRoleMenu> {

    private final AppRoleMenuMapper appRoleMenuMapper;

    /**
     * @param role  role
     * @param menus 菜单ID集合
     * @return int
     * @author aswl.com
     * @date 2018/10/28 14:29
     */
    @Transactional
    @CacheEvict(value = "appMenu", allEntries = true)
    public int saveAppRoleMenus(String role, List<String> menus) {
        int update = -1;
        if (CollectionUtils.isNotEmpty(menus)) {
            // 删除旧的管理数据
            appRoleMenuMapper.deleteByRoleId(role);
            List<AppRoleMenu> roleMenus = new ArrayList<>();
            for (String menuId : menus) {
                AppRoleMenu roleMenu = new AppRoleMenu();
                roleMenu.setId(IdGen.snowflakeId());
                roleMenu.setRoleId(role);
                roleMenu.setAppMenuId(menuId);
                roleMenus.add(roleMenu);
            }
            update = appRoleMenuMapper.insertBatch(roleMenus);
        }
        return update;
    }

    /**
     * 批量保存
     *
     * @param roleMenus roleMenus
     * @return int
     * @author aswl.com
     * @date 2018/10/30 19:59
     */
    @Transactional
    public int insertBatch(List<AppRoleMenu> roleMenus) {
        return appRoleMenuMapper.insertBatch(roleMenus);
    }
}
