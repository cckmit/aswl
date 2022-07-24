package com.aswl.as.asos.modules.asuser.mapper;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色权限表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
@Mapper
public interface AsUserSysRoleMenuMapper extends BaseMapper<AsUserSysRoleMenu> {
    
    int deleteByRoleId(@Param("roleId") String roleId);
}
