package com.aswl.as.asos.modules.asuser.mapper;
import com.aswl.as.asos.modules.asuser.entity.SysTenantRoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * <p>
 * 租户角色菜单表 Mapper 接口
 * </p>
 *
 * @author df
 * @since 2020-11-19
 */
@Mapper
public interface SysTenantRoleMenuMapper extends BaseMapper<SysTenantRoleMenu> {
    int insertBatch(List<SysTenantRoleMenu> tenantRoleMenus);

    int deleteByRoleId(String roleId);
}

