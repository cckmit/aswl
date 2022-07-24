package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventPatrol;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 设备巡更事件mapper
 */
@Mapper
public interface EventPatrolMapper extends CrudMapper<EventPatrol> {
    /**
     * 根据设备Id查询
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_bt_patrol where device_id = #{deviceId}")
    EventPatrol findByDeviceId(String deviceId);

    void save(EventPatrol eventPatrol);
}
