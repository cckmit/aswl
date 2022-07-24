package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.RegionLeaderDto;
import com.aswl.as.ibrs.api.vo.RegionLeaderVo;
import com.aswl.as.user.api.module.UserAuths;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.RegionLeader;
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


    /**
     * 根据用户id批量查询区域负责人信息
     *
     * @param userIds userIds
     * @return List
     */
    List<RegionLeader> getRegionLeaderByUserIds(@Param("userIds") String[] userIds);


    /**
     * 根据用户id更新区域负责人信息
     *
     * @param regionLeaderDto regionLeaderDto
     * @return int
     */
    int  updateByUserId(RegionLeaderDto regionLeaderDto);

    /**
     * 根据用户id删除区域负责人信息
     *
     * @param userIds userIds
     * @return int
     */
    int  deleteByUserId(@Param("userIds") String[] userIds);

    /**
     * 根据用户ID查询区域负责人
     * @param userId
     * @return
     */
    RegionLeader findRegionLeaderByUserId(@Param("userId") String userId);


    /**
     * 根据区域ID集合删除区域负责人信息
     * @param regionIds
     * @return int
     */
    int deleteByRegionIds(@Param("regionIds") List<String> regionIds);
}
