package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceEventAlarm;
import com.aswl.as.ibrs.api.vo.DeviceEventAlarmVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
*
* 设备事件报警Mapper
* @author dingfei
* @date 2019-10-28 17:03
*/

@Mapper
public interface DeviceEventAlarmMapper extends CrudMapper<DeviceEventAlarm> {


    /**
     * 根据区域查询告警级别统计
     * @param regionCode 区域编码
     * @param alarmLevel 告警级别
     * @return List<Map>
     */
    Map getAlarmLevelCounts(@Param("regionCode") String regionCode, @Param("alarmLevel") Integer alarmLevel);

    /**
     *  根据区域编码和告警类型获取数量
     * @param regionCode 区域编码
     * @param alarmType 告警类型
     * @return Map
     */
     Map getAlarmTypeCounts(@Param("regionCode") String regionCode, @Param("alarmType") String alarmType);


    /**
     * 根据区域编码获取总体健康数据量
     * @return Map
     */
    Map getHealthyDataCounts();

    /**
     * 根据区域编码获取告警数据量
     * @param regionCode 区域编码
     * @return Integer
     */
    Integer getAlarmDataCounts(@Param("regionCode") String regionCode);

    /**
     * 根据设备ID获取历史告警列表
      * @param deviceId 设备ID
     * @return List
     */
    List<DeviceEventAlarmVo> getHistoryAlarm(@Param("deviceId") String deviceId);

    /**
     * 根据设备ID获取当前报警数据
     * @param deviceId
     * @return
     */
    @Select("select * from as_device_event_alarm where device_id = #{deviceId}")
    DeviceEventAlarm findByDeviceId(@Param("deviceId") String deviceId);
}
