package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.Tenant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 租户Mapper
 *
 * @author aswl.com
 * @date 2019/5/22 22:50
 */
@Mapper
public interface TenantMapper extends CrudMapper<Tenant> {

    /**
     * 根据租户标识获取
     *
     * @param tenantCode tenantCode
     * @return Tenant
     * @author aswl.com
     * @date 2019/05/26 10:29
     */
    Tenant getByTenantCode(String tenantCode);


    int getExistMobile(@Param("mobile") String mobile);

    Tenant getTenantByMobile(@Param("mobile") String mobile);
}
