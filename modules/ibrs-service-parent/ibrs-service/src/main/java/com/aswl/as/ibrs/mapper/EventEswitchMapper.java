package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventEswitch;
import org.apache.ibatis.annotations.Param;

/**
*
* 设备事件-电源开关Mapper
* @author zgl
* @date 2019-11-01 14:02
*/

@Mapper
public interface EventEswitchMapper extends CrudMapper<EventEswitch> {


    @Delete("delete  from as_event_eswitch where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from as_event_eswitch where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);

}
