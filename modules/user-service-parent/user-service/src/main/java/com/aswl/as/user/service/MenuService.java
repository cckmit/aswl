package com.aswl.as.user.service;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.user.api.module.Menu;
import com.aswl.as.user.api.module.Role;
import com.aswl.as.user.mapper.ConfigMapper;
import com.aswl.as.user.mapper.MenuMapper;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜单service
 *
 * @author aswl.com
 * @date 2018/8/26 22:45
 */
@AllArgsConstructor
@Service
public class MenuService extends CrudService<MenuMapper, Menu> {

    private final MenuMapper menuMapper;
    private final ConfigMapper configMapper;

    /**
     * 根据角色查找菜单
     *
     * @param role       角色标识
     * @param tenantCode 租户标识
     * @return List
     * @author aswl.com
     * @date 2018/8/27 16:00
     */
    @Cacheable(value = "menu#" + CommonConstant.CACHE_EXPIRE, key = "#role")
    public List<Menu> findMenuByRole(String role, String tenantCode) {
        return menuMapper.findByRole(role, tenantCode);
    }

    /**
     * 批量查询菜单
     *
     * @param roleList   roleList
     * @param tenantCode tenantCode
     * @return List
     * @author aswl.com
     * @date 2019/07/03 23:50:35
     */
    public List<Menu> finMenuByRoleList(List<Role> roleList, String tenantCode) {
        List<Menu> menus = Lists.newArrayList();
        for (Role role : roleList) {
            List<Menu> roleMenus = this.findMenuByRole(role.getRoleCode(), tenantCode);
            if (CollectionUtils.isNotEmpty(roleMenus))
                menus.addAll(roleMenus);
        }
        return menus;
    }

    /**
     * 查询全部菜单
     *
     * @param menu menu
     * @return List
     * @author aswl.com
     * @date 2019/04/10 17:58
     */
    @Override
    public List<Menu> findAllList(Menu menu) {
        // 查询是否云平台
        int isCloud = configMapper.findIsCloud(SysUtil.getTenantCode());
        if (isCloud > 0) {
            menu.setIsHidden(0);
            return menuMapper.findAllList(menu);
        }else {
            return menuMapper.findAllList(menu);
        }
    }

    /**
     * 新增菜单
     *
     * @param menu menu
     * @return int
     * @author aswl.com
     * @date 2018/10/28 15:56
     */
    @Transactional
    @Override
    @CacheEvict(value = {"menu", "user"}, allEntries = true)
    public int insert(Menu menu) {
        return super.insert(menu);
    }

    /**
     * 更新菜单
     *
     * @param menu menu
     * @return int
     * @author aswl.com
     * @date 2018/10/30 20:19
     */
    @Transactional
    @Override
    @CacheEvict(value = {"menu", "user"}, allEntries = true)
    public int update(Menu menu) {
        return super.update(menu);
    }

    /**
     * 删除菜单
     *
     * @param menu menu
     * @return int
     * @author aswl.com
     * @date 2018/8/27 16:22
     */
    @Override
    @Transactional
    @CacheEvict(value = {"menu", "user"}, allEntries = true)
    public int delete(Menu menu) {
        // 删除当前菜单
        super.delete(menu);
        // 删除父节点为当前节点的菜单
        Menu parentMenu = new Menu();
        parentMenu.setParentId(menu.getId());
        parentMenu.setNewRecord(false);
        parentMenu.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
        parentMenu.setDelFlag(CommonConstant.DEL_FLAG_DEL);
        return super.update(parentMenu);
    }
}
