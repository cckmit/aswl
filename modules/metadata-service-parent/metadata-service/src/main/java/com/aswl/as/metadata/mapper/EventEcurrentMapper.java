package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventEcurrent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备事件-电源电流Mapper
* @author zgl
* @date 2019-11-01 13:48
*/

@Mapper
public interface EventEcurrentMapper extends CrudMapper<EventEcurrent> {


    /**
     * 根据deviceId查询设备电流事件
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_ecurrent where device_id = #{deviceId}")
    EventEcurrent findByDeviceId(@Param("deviceId") String deviceId);
}
