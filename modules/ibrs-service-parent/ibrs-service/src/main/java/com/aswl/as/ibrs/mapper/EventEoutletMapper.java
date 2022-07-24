package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventEoutlet;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
*
* 设备事件-电源电口Mapper
* @author zgl
* @date 2019-11-01 14:01
*/

@Mapper
public interface EventEoutletMapper extends CrudMapper<EventEoutlet> {


    @Delete("delete  from as_event_ecurrent where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from as_event_ecurrent where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);

    @Update("update  as_event_eoutlet set ${fld} = #{value} where device_id =#{deviceId}")
    int updateBydeviceIdAndFld(@Param("fld") String fld ,@Param("value") String value,@Param("deviceId") String deviceId);
}
