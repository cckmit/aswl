package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.Region;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
*
* 区域Mapper
* @author dingfei
* @date 2019-09-27 14:01
*/

@Mapper
public interface RegionMapper extends CrudMapper<Region> {

    Region findByRegionCode(@Param("regionCode") String regionCode);
}
