package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.RegionLeaderDto;
import com.aswl.as.ibrs.api.module.RegionLeader;
import com.aswl.as.ibrs.api.vo.RegionLeaderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 区域负责人Mapper
* @author dingfei
* @date 2019-11-08 10:20
*/

@Mapper
public interface RegionLeaderMapper extends CrudMapper<RegionLeader> {

    /**
     * 查询所有区域负责人
     * @param regionLeaderDto
     * @return
     */
    List<RegionLeaderVo> findRegionLeaderInfo(RegionLeaderDto regionLeaderDto);

    /**
     * 查询该区域下是否存在数据
     * @param regionCode
     * @return
     */
    RegionLeader findByRegionCode(@Param("regionCode") String regionCode);

    /**
     * 区域Id获取区域负责人
     * @param regionId
     * @return
     */
    RegionLeader findByRegionId(@Param("regionId") String regionId);
}
