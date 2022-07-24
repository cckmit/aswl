package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventEoutlet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;
import java.util.Set;

/**
*
* 设备事件-电源电口Mapper
* @author zgl
* @date 2019-11-01 14:01
*/

@Mapper
public interface EventEoutletMapper extends CrudMapper<EventEoutlet> {

    /**
     * 根据DeviceId查询
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_eoutlet where device_id = #{deviceId}")
    EventEoutlet findByDeviceId(@Param("deviceId") String deviceId);


    /**
     * 有改变的电口状态
     * @param keySet
     * @return
     */
    Map<String,String> findOutletState(@Param("keySet") Set<String> keySet,@Param("deviceId")String deviceId);


    /**
     * 修改电口启用/禁用状态
     * @param fld
     * @param value
     * @param deviceId
     * @return
     */
    @Update("update  as_event_eoutlet set ${fld} = #{value} where device_id =#{deviceId}")
    int updateByDeviceIdAndFld(@Param("fld") String fld ,@Param("value") String value,@Param("deviceId") String deviceId);
}
