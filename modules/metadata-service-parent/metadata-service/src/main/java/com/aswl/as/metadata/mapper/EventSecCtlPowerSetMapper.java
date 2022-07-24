package com.aswl.as.metadata.mapper;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerSet;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备分控板-电源设置Mapper
* @author zgl
* @date 2021-07-25 17:09
*/

@Mapper
public interface EventSecCtlPowerSetMapper extends CrudMapper<EventSecCtlPowerSet> {

    /**
     * 根据设备ID获取分控板-电源设置
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_sec_ctl_power_set where device_id = #{deviceId}")
    EventSecCtlPowerSet findByDeviceId(@Param("deviceId") String deviceId);
}
