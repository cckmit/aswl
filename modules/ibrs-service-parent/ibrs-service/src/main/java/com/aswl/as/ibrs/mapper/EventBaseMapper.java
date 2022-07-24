package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventBase;
import org.apache.ibatis.annotations.Param;

/**
*
* 设备事件-基础Mapper
* @author zgl
* @date 2019-11-01 11:59
*/

@Mapper
public interface EventBaseMapper extends CrudMapper<EventBase> {

    @Delete("delete  from as_event_base where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from as_event_base where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);
}
