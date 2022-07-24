package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper extends CrudMapper<Post> {

    /**
     * 根据租户编码删除岗位
     * @param tenantCode
     * @return int 
     */
    int deleteByTenantCode(@Param("tenantCode") String tenantCode);
}
