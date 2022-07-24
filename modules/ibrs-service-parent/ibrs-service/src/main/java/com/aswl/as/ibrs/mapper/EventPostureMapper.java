package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventPosture;
import org.apache.ibatis.annotations.Param;

/**
*
* 设备事件-姿态信息Mapper
* @author zgl
* @date 2022-07-15 16:36
*/

@Mapper
public interface EventPostureMapper extends CrudMapper<EventPosture> {

    /**
     * 通过设备ID删除数据
     * @param deviceIds
     * @return
     */
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);

    /**
     * 通过设备ID获取设备姿态息
     * @param deviceId
     * @return EventPosture
     */

    EventPosture findByDeviceId(@Param("deviceId") String deviceId);
}
