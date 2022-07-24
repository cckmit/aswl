package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备事件-基础Mapper
* @author zgl
* @date 2019-11-01 11:59
*/

@Mapper
public interface EventBaseMapper extends CrudMapper<EventBase> {

    /**
     * 根据deviceId查询基础设备事件
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_base where device_id = #{deviceId}")
    EventBase findByDeviceId(@Param("deviceId") String deviceId);
}
