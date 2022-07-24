package com.aswl.as.asos.modules.asuser.mapper;

import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * SAAS租户信息表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-11-18
 */
@Mapper
public interface SysTenantMapper extends BaseMapper<SysTenant> {

     int updateDelFlag(SysTenant entity);

     SysTenant selectTenantForTenantCode(SysTenant sysTenant);
     
     int getMobileExist(@Param("mobile") String mobile, @Param("id") String id);
     
     SysTenant findTenantByTenantCode(@Param("tenantCode") String tenantCode);
     
}
