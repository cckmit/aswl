package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventEvoltage;
import org.apache.ibatis.annotations.Param;

/**
*
* 设备事件-电压Mapper
* @author zgl
* @date 2019-11-01 14:03
*/

@Mapper
public interface EventEvoltageMapper extends CrudMapper<EventEvoltage> {


    @Delete("delete  from  as_event_evoltage where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from  as_event_evoltage where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);

}
