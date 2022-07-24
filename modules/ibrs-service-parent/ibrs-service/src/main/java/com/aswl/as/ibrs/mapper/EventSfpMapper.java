package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventSfp;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
*
* 设备事件-光口Mapper
* @author zgl
* @date 2019-11-01 14:04
*/

@Mapper
public interface EventSfpMapper extends CrudMapper<EventSfp> {


    @Delete("delete  from   as_event_sfp where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from   as_event_sfp where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);
    
    @Update("update  as_event_sfp set ${fld} = #{value} where device_id =#{deviceId}")
    int updateBydeviceIdAndFld(@Param("fld") String fld ,@Param("value") String value,@Param("deviceId") String deviceId);
}
