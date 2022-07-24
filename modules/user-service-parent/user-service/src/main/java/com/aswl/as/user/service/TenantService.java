package com.aswl.as.user.service;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.feign.RegionServiceClient;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.user.api.module.AppMenu;
import com.aswl.as.user.api.module.Role;
import com.aswl.as.user.api.module.Tenant;
import com.aswl.as.user.api.module.User;
import com.aswl.as.user.mapper.*;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 租户Service
 *
 * @author aswl.com
 * @date 2019/5/22 22:51
 */
@AllArgsConstructor
@Service
public class TenantService extends CrudService<TenantMapper, Tenant> {
    private final UserMapper userMapper;
    private final UserAuthsMapper userAuthsMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final MenuMapper menuMapper;
    private final AppMenuMapper appMenuMapper;
    private final AppRoleMenuMapper appRoleMenuMapper;
    private final ConfigMapper configMapper;
    private final DeptMapper deptMapper;
    private final PositionMapper positionMapper;
    private final PostMapper postMapper;
    private final  RoleMenuMapper roleMenuMapper;
    private final  RegionServiceClient regionServiceClient;

    /**
     * 根据租户标识获取
     *
     * @param tenantCode tenantCode
     * @return Tenant
     * @author aswl.com
     * @date 2019/05/26 10:28
     */
   // @Cacheable(value = "tenant#" + CommonConstant.CACHE_EXPIRE, key = "#tenantCode")
    public Tenant getByTenantCode(String tenantCode) {

        Tenant t=this.dao.getByTenantCode(tenantCode);
        checkEffective(t);

        return t;
    }

    /**
     * 更新
     *
     * @param tenant tenant
     * @return Tenant
     * @author aswl.com
     * @date 2019/05/26 10:28
     */
    @Transactional
    @CacheEvict(value = "tenant", key = "#tenant.tenantCode")
    @Override
    public int update(Tenant tenant) {
        return super.update(tenant);
    }

    /**
     * 删除
     *
     * @param tenant tenant
     * @return Tenant
     * @author aswl.com
     * @date 2019/05/26 10:28
     */
    @Transactional
    @CacheEvict(value = "tenant", key = "#tenant.tenantCode")
    @Override
    public int delete(Tenant tenant) {
        return super.delete(tenant);
    }

    /**
     * 删除
     *
     * @param tenant
     * @return Tenant
     * @author aswl.com
     * @date 2019/05/26 10:37
     */
    @Transactional
    @CacheEvict(value = "tenant", allEntries = true)
    public int deleteAll(Tenant tenant) {
        //根据租户ID查询租户对象
        Tenant t= null;
        try {
             t = this.get(tenant.getIdString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.deleteAll(t.getTenantCode());
        try {
            if (StringUtils.isNotEmpty(tenant.getIdString())) {
                return super.deleteAll(tenant.getIdString().split(","));
            }
        } catch (Exception e) {
            throw new CommonException("删除租户失败.");
        }
        return 0;
    }

    /**
     * 如果判断到租户已过期 并且是 激活状态，就把状态更新为已过期
     */
    private void checkEffective(Tenant t)
    {
        if("1".equals(t.getStatus()) && t.getEffectiveEndTime()!=null && t.getEffectiveEndTime().before(new Date()) )
        {
            t.setStatus("2");
            this.update(t);
        }
    }


    /**
     *  删除数据
     * @param tenantCode
     */
    public void deleteAll(String  tenantCode){
       // 删除as-user数据库数据
        //删除APP角色权限表
        AppMenu appMenu = new AppMenu();
        appMenu.setTenantCode(tenantCode);
        List<AppMenu> list = appMenuMapper.findList(appMenu);
        if (list != null && list.size() > 0 ){
            for (AppMenu menu : list) {
                appRoleMenuMapper.deleteByMenuId(menu.getId());
            }
        }
       //删除APP菜单表
        appMenuMapper.deleteByTenantCode(tenantCode);
      
        //删除系统配置表
        configMapper.deleteByTenantCode(tenantCode);
        //删除部门
        deptMapper.deleteByTenantCode(tenantCode);
        //删除菜单表
        menuMapper.deleteByTenantCode(tenantCode);
        //删除职位
        positionMapper.deleteByTenantCode(tenantCode);
        //删除岗位
        postMapper.deleteByTenantCode(tenantCode);
        //删除角色菜单
        Role role = new Role();
        role.setTenantCode(tenantCode);
        List<Role> roles = roleMapper.findList(role);
        if (roles != null && roles.size() > 0){
            for (Role r: roles) {
                roleMenuMapper.deleteByRoleId(r.getId());
            }
        }
        //删除角色
        roleMapper.deleteByTenantCode(tenantCode);
        //删除用户角色表
        List<User> users = userMapper.selectByTenantCode(tenantCode);
        if (users!= null && users.size() > 0){
            for (User u: users) {
                userRoleMapper.deleteByUserId(u.getId());
            }
        }
        //删除用户
        userMapper.deleteUserByTenantCode(tenantCode);
        //删除用户授权表
        userAuthsMapper.deleteByTenantCode(tenantCode);
        //删除as-libs数据库数据
         // 删除项目
        ResponseBean<List<Project>> project = regionServiceClient.findProjectByTenantCode(tenantCode);
        if (ResponseBean.SUCCESS == project.getCode()){
            if (project.getData() != null && project.getData().size() > 0){
                for (Project p:project.getData()) {
                    regionServiceClient.deleteProjectById(p.getProjectId());
                }
            }
        }
        //删除报警级别
        regionServiceClient.deleteAlarmLevelByTenantCode(tenantCode);
        
        //删除工单设置数据
        
        regionServiceClient.deleteAlarmOrderHandleByTenantCode(tenantCode);
    }
}
