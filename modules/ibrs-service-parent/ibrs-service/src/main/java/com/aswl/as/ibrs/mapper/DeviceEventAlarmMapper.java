package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceEventAlarmDto;
import com.aswl.as.ibrs.api.module.CityAlarmStatistics;
import com.aswl.as.ibrs.api.module.CityRunTimeStatistics;
import com.aswl.as.ibrs.api.vo.DeviceEventAlarmVo;
import com.aswl.as.ibrs.api.vo.DeviceStatusVo;
import org.apache.ibatis.annotations.*;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceEventAlarm;

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
    Map getAlarmLevelCounts(@Param("regionCode") String regionCode,@Param("alarmLevel") Integer alarmLevel,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     *  根据区域编码和告警类型获取数量
     * @param regionCode 区域编码
     * @param alarmType 告警类型
     * @return Map
     */
     Map getAlarmTypeCounts(@Param("regionCode") String regionCode,@Param("alarmType") String alarmType,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);


    /**
     * 根据区域编码获取总体健康数据量
     * @return Map
     */
    Map getHealthyDataCounts();


    /**
     * 根据区域编码获取告警数据量
     * @param regionCode 区域编码
     * flag 1,健康率设备数 ,2:地图故障设备数
     * @return Integer
     */
    Integer getAlarmCounts(@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId,@Param("flag") String flag);

    /**
     * 根据设备ID获取历史告警列表
      * @param deviceId 设备ID
     * @return List
     */
    List<DeviceEventAlarmVo> getHistoryAlarm(@Param("deviceId") String deviceId );

    /**
     * 离线告警数据
     * @param regionCode 区域编码
     * @return Integer
     */

    Integer getOffAlarmDataCounts(@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    @Delete("delete  from as_device_event_alarm where device_id =#{deviceId}")
    int deleteByDeviceId( String deviceId);

    @Delete("delete  from as_device_event_alarm where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);


    /**
     * 设备详细信息(告警记录列表)
     * @param deviceAlarmDto 设备报警Dto
     * @return List
     */
    List<DeviceEventAlarmVo> getHistoryAlarmData(DeviceAlarmDto deviceAlarmDto);


    /**
     * 设备详细信息(告警记录列表统计)
     * @param deviceAlarmDto 设备报警Dto
     * @return List
     */
    List<DeviceEventAlarmVo> getHistoryAlarmCounts(DeviceAlarmDto deviceAlarmDto);

    @Select("select u_event_id AS eventIds,alarm_types AS alarmTypes,alarm_types_des AS alarmTypesDes," +
            "alarm_levels AS alarmLevels,alarm_dates AS alarmDates from as_device_event_alarm where device_id =#{deviceId}")
    DeviceEventAlarmVo findAlarmByDeviceId(String deviceId);


    int updateByDeviceId(DeviceEventAlarmDto eventAlarmDto);

    /**
     * 根据统一事件ID和月年获取历史表
     * @param uEventId
     * @param ym
     * @return DeviceEventAlarmVo
     */
    DeviceEventAlarmVo findByEventId(@Param("uEventId") String uEventId,@Param("ym") Integer ym);


    /**
     * 根据统一事件ID和月年更新数据
     * @param deviceEventAlarm
     * @return DeviceEventAlarmVo
     */
    int updateDeviceEventHisAlarm(DeviceEventAlarm deviceEventAlarm);

    /**
     * 当前故障的设备箱或摄像机
     * @return
     */
    int currentWrongBoxCount(@Param("regionCode") String regionCode, @Param("deviceKind") String deviceKind,@Param("tenantCode")String tenantCode, @Param("projectId") String projectId);

    /**
     * 告警的设备数和故障设备数(单独的不合计)
     * @param regionCode
     * @param tenantCode
     * @param projectId
     * @param flag
     * @return
     */
    Integer getAlarmOrWrongCount(@Param("kind") String kind,@Param("regionCode") String regionCode,
                                 @Param("tenantCode") String tenantCode, @Param("projectId") String projectId,
                                 @Param("flag") String flag);

    String findAlarmInfo(@Param("deviceId") String deviceId, @Param("eventIds") String eventIds, @Param("alarmType") String alarmType);


    int getNoWrongDeviceCount(@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode, @Param("projectId") String projectId);

    List<CityAlarmStatistics> findTodayAlarmCount(@Param("month") String month, @Param("today") String today, @Param("type") String type);

    CityRunTimeStatistics findPeriodAlarmNum(@Param("yearMonth") String yearMonth, @Param("today") String today, @Param("type") String type);

    /**
     * 根据设备ID获取当前报警数据
     * @param deviceId
     * @return
     */
    @Select("select * from as_device_event_alarm where device_id = #{deviceId}")
    DeviceEventAlarm findByDeviceId(@Param("deviceId") String deviceId);

    /**
     * 市级平台设备详情-->设备状态
     *
     * @param id 设备ID
     * @return ResponseBean
     */
    List<DeviceStatusVo> getDeviceStatusData(@Param("id") String id);

    /**
     * 故障数和预警数
     * @param regionCode
     * @param tenantCode
     * @param projectId
     * @return
     */
    Map getFaultAlarmCount(@Param("regionCode") String regionCode, @Param("tenantCode") String tenantCode, @Param("projectId") String projectId,@Param("model") String model);

    /**
     * 历史的故障设备数和预警数
     * @param deviceAlarmDto
     * @return
     */
    Map getHisFaultAlarmCount(DeviceAlarmDto deviceAlarmDto);


    /**
     * 删除历史表设备相关
     * @param hisTable
     * @param idsLis
     */
    void deleteHisByDeviceIds(@Param("hisTable") String hisTable,@Param("idsList") List idsLis);


    /**
     * 项目区域告警排名
     * @param deviceAlarmDto
     * @return
     */
    List<Map> alarmTop(DeviceAlarmDto deviceAlarmDto);

}
