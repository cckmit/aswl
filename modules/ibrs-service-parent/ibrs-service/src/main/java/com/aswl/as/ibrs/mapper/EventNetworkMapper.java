package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.module.CityAlarmStatistics;
import com.aswl.as.ibrs.api.module.CityOnlineStatistics;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventNetwork;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
*
* 设备事件-网络Mapper
* @author zgl
* @date 2019-11-01 14:06
*/

@Mapper
public interface EventNetworkMapper extends CrudMapper<EventNetwork> {


    @Delete("delete  from  as_event_network where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from  as_event_network where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);

    List<CityOnlineStatistics > onlineCount(@Param("type") String type, @Param("day") String day,@Param("hisTable") String hisTable);

    EventNetwork findByDeviceId(@Param("deviceId") String deviceId);
}
