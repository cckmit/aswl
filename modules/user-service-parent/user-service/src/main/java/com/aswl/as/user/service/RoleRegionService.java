package com.aswl.as.user.service;

import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.user.api.module.RoleRegion;
import com.aswl.as.user.api.vo.RegionRoleVo;
import com.aswl.as.user.mapper.RoleRegionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class RoleRegionService extends CrudService<RoleRegionMapper, RoleRegion> {
    private final RoleRegionMapper roleRegionMapper;

    /**
     * 新增区域角色
     *
     * @param roleRegion
     * @return int
     */
    @Transactional
    @Override
    public int insert(RoleRegion roleRegion) {
        return super.insert(roleRegion);
    }

    /**
     * 删除区域角色
     *
     * @param roleRegion
     * @return int
     */
    @Transactional
    @Override
    public int delete(RoleRegion roleRegion) {
        return super.delete(roleRegion);
    }


    /**
     * 根据角色查找区域菜单
     *
     * @param role       角色标识
     * @param tenantCode 租户标识
     * @return List
     * @author dingfei
     * @date 2019/10/15 16:00
     */
    @Cacheable(value = "region#" + CommonConstant.CACHE_EXPIRE, key = "#role")
    public List<RegionRoleVo> findRegionByRole(String role, String tenantCode) {
        return roleRegionMapper.findByRole(role, tenantCode);
    }
    /**
     * @param role  role
     * @param regions 区域ID集合
     * @return int
     * @author dingfei
     * @date 2019/10/15 10:00
     */
    @Transactional
    @CacheEvict(value = "region", allEntries = true)
    public int saveRoleRegions(String role, List<String> regions) {
        int update = -1;
        if (CollectionUtils.isNotEmpty(regions)) {
            // 删除旧的管理数据
            roleRegionMapper.deleteByRoleId(role);
            List<RoleRegion> roleRegions = new ArrayList<>();
            for (String regionId : regions) {
                RoleRegion roleRegion = new RoleRegion();
                roleRegion.setId(IdGen.snowflakeId());
                roleRegion.setRegionId(regionId);
                roleRegion.setRoleId(role);
                roleRegions.add(roleRegion);
            }
            update = roleRegionMapper.insertBatch(roleRegions);
        }
        return update;
    }

}