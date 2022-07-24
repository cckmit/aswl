package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventAlarm;
import org.apache.ibatis.annotations.Param;

/**
*
* 设备事件-报警Mapper
* @author zgl
* @date 2019-11-01 11:29
*/

@Mapper
public interface EventAlarmMapper extends CrudMapper<EventAlarm> {
    @Delete("delete  from as_event_alarm where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from as_event_alarm where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);
}
