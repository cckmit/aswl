package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventEswitch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备事件-电源开关Mapper
* @author zgl
* @date 2019-11-01 14:02
*/

@Mapper
public interface EventEswitchMapper extends CrudMapper<EventEswitch> {

    /**
     * 根据设备ID查询设备网络
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_eswitch where device_id = #{deviceId}")
    EventEswitch findByDeviceId(@Param("deviceId") String deviceId);

    @Select("select ${fld} from as_event_eswitch where device_id = #{deviceId}")
    Integer findFldValue(@Param("deviceId") String deviceId, @Param("fld") String fld);
}
