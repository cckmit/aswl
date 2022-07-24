package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.EventPatrolDto;
import com.aswl.as.ibrs.api.module.EventPatrol;
import com.aswl.as.ibrs.api.module.RegionLeader;
import com.aswl.as.ibrs.api.vo.EventPatrolVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventPatrolMapper extends CrudMapper<EventPatrol> {

    /**
     * 分页查询
     * @param eventPatrolDto
     * @return
     */
    List<EventPatrolVo> findByPage(EventPatrolDto eventPatrolDto);

    /**
     * 按照区域负责人的巡更周期查询当前巡更记录
     * @param patrolKeyId 巡更钥匙Id
     * @param periodBeginTime 巡更开始时间
     * @param periodEndTime 巡更结束时间
     * @return 前已巡更设备的id集合
     */
    List<String> findByPeriod(@Param("patrolKeyId") String patrolKeyId,
                              @Param("periodBeginTime") String periodBeginTime,
                              @Param("periodEndTime") String periodEndTime);

    @Delete("delete  from as_event_bt_patrol where device_id in (${deviceIds})")
    void deleteByDeviceIds(@Param("deviceIds") String deviceIds);
}
