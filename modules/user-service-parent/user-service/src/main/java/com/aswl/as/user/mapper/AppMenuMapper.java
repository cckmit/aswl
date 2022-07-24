package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.AppMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单mapper
 *
 * @author aswl.com
 * @date 2018/8/26 22:34
 */
@Mapper
public interface AppMenuMapper extends CrudMapper<AppMenu> {

    /**
     * 根据角色查找菜单
     *
     * @param role       角色标识
     * @param tenantCode 租户标识
     * @return List
     */
    List<AppMenu> findByRole(@Param("role") String role, @Param("tenantCode") String tenantCode);

    /**
     * 根据租户编码删除
     * @param tenantCode
     * @return int
     */
    int deleteByTenantCode(@Param("tenantCode") String tenantCode);
    

}
