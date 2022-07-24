package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventEcurrent;
import org.apache.ibatis.annotations.Param;

/**
*
* 设备事件-电源电流Mapper
* @author zgl
* @date 2019-11-01 13:48
*/

@Mapper
public interface EventEcurrentMapper extends CrudMapper<EventEcurrent> {


    @Delete("delete  from as_event_eoutlet where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from as_event_eoutlet where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);
}
