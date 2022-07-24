package com.aswl.as.asos.modules.asuser.mapper;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-12-13
 */
@Mapper
public interface AsUserSysMenuMapper extends BaseMapper<AsUserSysMenu> {

    int deleteByTenantCode(@Param("tenantCode") String tenantCode);

}
