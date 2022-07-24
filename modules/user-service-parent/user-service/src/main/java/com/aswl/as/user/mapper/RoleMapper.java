package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色mapper
 *
 * @author aswl.com
 * @date 2018/8/26 09:33
 */
@Mapper
public interface RoleMapper extends CrudMapper<Role> {

    /**
     * 查询管理员角色
     * @param tenantCode
     * @return
     */
    Role getRoleIdByTenantCode(@Param("tenantCode") String tenantCode);

    /**
     * 验证角色CODE是否存在
     * @param roleCode
     * @param tenantCode
     * @return int
     */
    int checkRoleCode(@Param("roleCode") String roleCode,@Param("tenantCode") String tenantCode);

    /**
     * 查询系统值班员角色
     * @param roleCode 
     * @param tenantCode
     * @return Role
     */
    Role getRoleByRoleCodeAndTenantCode(@Param("roleCode") String roleCode,@Param("tenantCode") String tenantCode);


    /**
     * 根据用户ID查询用户角色
     * @param userId
     * @return Role
     */
    Role findByUserId(@Param("userId") String userId);


    /**
     * 根据租户编码删除角色
     * @param tenantCode
     * @return int
     */
    int deleteByTenantCode(@Param("tenantCode") String tenantCode);
}
