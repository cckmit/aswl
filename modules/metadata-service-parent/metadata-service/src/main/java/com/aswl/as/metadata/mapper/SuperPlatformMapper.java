package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.SuperPlatform;
import org.apache.ibatis.annotations.Param;

/**
 * 上级平台信息Mapper
 *
 * @date 2019-11-11 16:03
 */

@Mapper
public interface SuperPlatformMapper extends CrudMapper<SuperPlatform> {

    List<SuperPlatform> findByRegionCode(@Param("regionCode") String regionCode);
}
