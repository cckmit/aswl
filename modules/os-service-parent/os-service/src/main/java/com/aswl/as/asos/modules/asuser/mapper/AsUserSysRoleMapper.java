package com.aswl.as.asos.modules.asuser.mapper;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
@Mapper
public interface AsUserSysRoleMapper extends BaseMapper<AsUserSysRole> {

    List<AsUserSysRole> findRoleByTenantCode(@Param("tenantCode") String tenantCode);
}
