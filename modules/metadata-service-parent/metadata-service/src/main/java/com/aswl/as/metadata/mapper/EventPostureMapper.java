package com.aswl.as.metadata.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventPosture;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* 设备事件-姿态信息Mapper
* @author zgl
* @date 2022-07-16 11:14
*/

@Mapper
public interface EventPostureMapper extends CrudMapper<EventPosture> {

    /**
     * 根据设备ID获取姿态信息
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_posture where device_id = #{deviceId}")
    EventPosture findByDeviceId(@Param("deviceId") String deviceId);
}
