package com.aswl.as.user.mapper;
import com.aswl.as.user.api.vo.RegionRoleVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.RoleRegion;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 区域角色Mapper
* @author dingfei
* @date 2019-10-15 13:56
*/

@Mapper
public interface RoleRegionMapper extends CrudMapper<RoleRegion> {

    /**
     * 根据角色查找区域菜单
     *
     * @param role       角色标识
     * @param tenantCode 租户标识
     * @return List
     */
    List<RegionRoleVo> findByRole(@Param("role") String role, @Param("tenantCode") String tenantCode);

    /**
     * 根据角色ID删除
     *
     * @param roleId 角色ID
     * @return int
     */
    int deleteByRoleId(String roleId);


    /**
     * 批量保存
     *
     * @param roleRegions roleMenus
     * @return int
     */
    int insertBatch(List<RoleRegion> roleRegions);
}
