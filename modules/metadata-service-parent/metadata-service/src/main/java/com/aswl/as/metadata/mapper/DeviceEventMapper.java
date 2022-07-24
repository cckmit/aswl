package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventAlarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DeviceEventMapper extends CrudMapper<EventAlarm> {

    /**
     * 根据设备ID获取报警事件
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_alarm where device_id = #{deviceId}")
    EventAlarm findEventAlarmByDeviceId(@Param("deviceId") String deviceId);
}
