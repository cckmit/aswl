package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventEvoltage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备事件-电压Mapper
* @author zgl
* @date 2019-11-01 14:03
*/

@Mapper
public interface EventEvoltageMapper extends CrudMapper<EventEvoltage> {

    /**
     * 根据deviceId查询
     * @param deviceId
     * @return
     */
    @Select("SELECT * FROM as_event_evoltage WHERE device_id = #{deviceId}")
    EventEvoltage findByDeviceId(@Param("deviceId") String deviceId);
}
