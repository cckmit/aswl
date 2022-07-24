package com.aswl.as.metadata.mapper;
import com.aswl.as.ibrs.api.module.EventSecCtlPower;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备分控板-电源Mapper
* @author zgl
* @date 2021-07-25 16:48
*/

@Mapper
public interface EventSecCtlPowerMapper extends CrudMapper<EventSecCtlPower> {

    /**
     * 根据设备ID获取分控板-电源
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_sec_ctl_power where device_id = #{deviceId}")
    EventSecCtlPower findByDeviceId(@Param("deviceId") String deviceId);
}
