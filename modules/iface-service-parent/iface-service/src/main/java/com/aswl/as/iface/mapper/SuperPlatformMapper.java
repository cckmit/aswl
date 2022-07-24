package com.aswl.as.iface.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.iface.model.SuperPlatform;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 上级平台Mapper
 * @author aswl
 * @date 2019-09-11
 */
@Mapper
public interface SuperPlatformMapper extends CrudMapper<SuperPlatform> {

    /**
     * 根据serviceId查询接口用户
     * @param serviceId
     * @return
     */
    SuperPlatform findByServiceId(String serviceId);

    @Select("SELECT t1.client_id AS serviceId,t1.client_secret AS secret FROM as_super_platform t1 WHERE t1.client_id = #{appId} AND t1.status = 1 AND t1.del_flag = 0")
    SuperPlatform findSuperPlatform(String appId);
}
