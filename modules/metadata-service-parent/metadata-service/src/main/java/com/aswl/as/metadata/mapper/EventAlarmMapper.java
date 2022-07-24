package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventAlarm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备事件-报警Mapper
* @author zgl
* @date 2019-11-01 11:29
*/

@Mapper
public interface EventAlarmMapper extends CrudMapper<EventAlarm> {

    /**
     * 根据deviceId查询报警事件
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_alarm where device_id = #{deviceId}")
    EventAlarm findByDeviceId(@Param("deviceId") String deviceId);
}
