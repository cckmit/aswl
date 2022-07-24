package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.module.Config;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 系统配置信息表Mapper
* @author dingfei
* @date 2019-12-18 10:57
*/

@Mapper
public interface ConfigMapper extends CrudMapper<Config> {

    /**
     * 查询是否云平台
     * @return int 数量
     */
    int findIsCloud(@Param("tenantCode") String tenantCode);
}
