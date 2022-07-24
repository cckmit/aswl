package com.aswl.as.metadata.mapper;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerOutput;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备分控板-电源输出Mapper
* @author zgl
* @date 2021-07-25 17:10
*/

@Mapper
public interface EventSecCtlPowerOutputMapper extends CrudMapper<EventSecCtlPowerOutput> {

    /**
     * 根据设备ID获取分控板-电源输出
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_sec_ctl_power_output where device_id = #{deviceId}")
    EventSecCtlPowerOutput findByDeviceId(@Param("deviceId") String deviceId);
}
