package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.AlarmStatistics;
import com.aswl.as.ibrs.api.vo.AlarmDeviceVo;
import com.aswl.as.ibrs.api.vo.AlarmTypeStatisticsVo;
import com.aswl.as.ibrs.api.vo.DeviceEventHisAlarmVo;
import com.aswl.as.ibrs.api.vo.FlowRunStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备故障统计Mapper
 *
 * @author dingfei
 * @date 2019-10-22 19:01
 */

@Mapper
public interface AlarmStatisticsMapper extends CrudMapper<AlarmStatistics> {

    /**
     * 首页报障箱故障趋势统计
     * @param type 类型
     * @param deviceId 设备ID
     * @param regionNo 区域编码
     * @param format 格式化日期
     * @return
     */

    List<AlarmDeviceVo> getAlarmCounts(@Param("type") int type, @Param("deviceId") String deviceId, @Param("regionNo") String regionNo, @Param("format") String format);


    /**
     *  统计报表-->告警级别统计
     * @param deviceAlarmDto  查询对象
     * @return List
     */
    List<DeviceEventHisAlarmVo> findAlarmlevelData(DeviceAlarmDto deviceAlarmDto);


    /**
     *  统计报表-->告警分类统计
     * @param deviceAlarmDto 查询对象
     * @return List<Map>
     */
    List<AlarmTypeStatisticsVo> findAlarmTypesData(DeviceAlarmDto deviceAlarmDto);


    /**
     * 统计报表-->故障维护统计
     * @param deviceAlarmDto 查询对象
     * @return list
     */
    List<FlowRunStatisticsVo> getAlarmFaultData(DeviceAlarmDto deviceAlarmDto);

    /**
     * 根据区域编码统计未处理数量
     * @param regionCode 区域编码
     * @return int
     */
    Integer getUntreatedRunCount(@Param("regionCode") String regionCode);


    /**
     *  查询报障箱摄像头在线数据
     * @param regionCode  区域编码
     * @param type  2报障箱 3摄像机
     * @return
     */
    Map getOnLineDeviceCount(@Param("regionCode") String regionCode, @Param("type") String type);

    /**
     * 根据设备ID和日期查询数据
     * @param deviceId
     * @param date
     * @return
     */
    @Select("select * from as_alarm_statistics where device_id = #{deviceId} and alarm_type = #{alarmType} and create_date = DATE_FORMAT(#{date}, '%Y-%m-%d')")
    AlarmStatistics findByDeviceIdWithDate(@Param("deviceId") String deviceId, @Param("alarmType") String alarmType, @Param("date") Date date);

}
