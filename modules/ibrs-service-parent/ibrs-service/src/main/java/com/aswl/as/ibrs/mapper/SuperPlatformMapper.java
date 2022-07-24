package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.SuperPlatform;
import org.apache.ibatis.annotations.Param;

/**
 * 上级平台信息Mapper
 *
 * @author dingfei
 * @date 2019-11-11 16:03
 */

@Mapper
public interface SuperPlatformMapper extends CrudMapper<SuperPlatform> {

    SuperPlatform findByClientId(@Param("clientId") String clientId);
}
