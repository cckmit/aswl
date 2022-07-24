package com.aswl.as.user.service;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.user.api.module.AppMenu;
import com.aswl.as.user.api.module.Role;
import com.aswl.as.user.mapper.RoleMapper;
import com.aswl.as.user.mapper.RoleMenuMapper;
import com.aswl.as.user.mapper.UserRoleMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色service
 *
 * @author aswl.com
 * @date 2018/8/26 14:16
 */
@AllArgsConstructor
@Service
public class RoleService extends CrudService<RoleMapper, Role> {

    private final RoleMenuMapper roleMenuMapper;

    private final UserRoleMapper userRoleMapper;

    private final AppMenuService appMenuService;

    private final AppRoleMenuService appRoleMenuService;
    
    private final RoleMapper roleMapper;

    /**
     * 查询所有角色
     *
     * @param role role
     * @return List
     * @author aswl.com
     * @date 2019/05/15 23:32
     */
    @Override
    @Cacheable(value = "role#" + CommonConstant.CACHE_EXPIRE, key = "#role.applicationCode")
    public List<Role> findAllList(Role role) {
        return super.findAllList(role);
    }

    /**
     * 新增
     *
     * @param role
     * @return int
     */
    @Override
    @Transactional
    public int insert(Role role) {

        // 产品说新创建角色就分配所有APP菜单
        AppMenu appMenu=new AppMenu();
        appMenu.setTenantCode(SysUtil.getTenantCode());
        List<AppMenu> list=appMenuService.findList(appMenu);
        List<String> ids=new ArrayList<>();
        for(AppMenu m:list)
        {
            ids.add(m.getId());
        }
        appRoleMenuService.saveAppRoleMenus(role.getId(),ids);
        return super.insert(role);
    }

    /**
     * 更新
     *
     * @param role role
     * @return int
     */
    @Override
    @Transactional
    public int update(Role role) {
        return super.update(role);
    }

    /**
     * 删除
     *
     * @param role role
     * @return int
     */
    @Override
    @Transactional
    public int delete(Role role) {
        // 删除角色菜单关系
        roleMenuMapper.deleteByRoleId(role.getId());
        // 删除用户角色关系
        userRoleMapper.deleteByRoleId(role.getId());
        return super.delete(role);
    }


    /**
     * 验证角色CODE是否存在
     *
     * @param roleCode  roleCode
     * @return int
     */
    public int checkRoleCode(String roleCode) {
        String tenantCode = SysUtil.getTenantCode();
        return roleMapper.checkRoleCode(roleCode,tenantCode);
    }
}
