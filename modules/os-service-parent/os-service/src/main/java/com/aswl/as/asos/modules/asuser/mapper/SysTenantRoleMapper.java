package com.aswl.as.asos.modules.asuser.mapper;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.aswl.as.asos.modules.asuser.entity.SysTenantRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author df
 * @date 2020/10/19 11:18
 */
@Mapper
public interface SysTenantRoleMapper extends BaseMapper<SysTenantRole> {
    /**
     * 根据角色查找菜单
     *
     * @param roleId
     * @return List
     */
    List<AsUserSysMenu> findMenuByRoleId(@Param("roleId") String roleId);

}
