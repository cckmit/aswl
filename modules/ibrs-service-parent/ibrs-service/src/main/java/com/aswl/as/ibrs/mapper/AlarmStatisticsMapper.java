package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.vo.*;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

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

    List<AlarmDeviceVo> getAlarmCounts(@Param("type") int type, @Param("deviceId") String deviceId, @Param("regionNo") String regionNo, @Param("format") String format,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);


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
    AlarmTypeStatisticsVo findAlarmTypesData(DeviceAlarmDto deviceAlarmDto);


    /**
     * 统计报表-->故障维护统计 -- >区域负责人统计
     * @param deviceAlarmDto 查询对象
     * @return list
     */
    List<FlowRunStatisticsVo> getAlarmFaultData(DeviceAlarmDto deviceAlarmDto);

    /**
     * 根据区域编码统计未处理数量
     * @param regionCode 区域编码
     * @param flag 标识 web app
     * @return int
     */
    Integer getUntreatedRunCount(@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId ,@Param("flag") String flag);


    /**
     *  查询报障箱摄像头在线数据
     * @param regionCode  区域编码
     * @param type  2报障箱 3摄像机
     * @return map
     */
    Map getOnLineDeviceCount(@Param("regionCode") String regionCode,@Param("type") String type,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);


    /**
     *  首页近30天告警数据TOP5
     * @param deviceAlarmDto 查询对象
     * @return list
     */
    List<AlarmTypeStatisticsVo> getHistoryTop5(DeviceAlarmDto deviceAlarmDto);

    /**
     * 在线率统计
     * @param deviceAlarmDto
     * @return map
     */
    Map getOnlineRateData(DeviceAlarmDto deviceAlarmDto);


    /**
     * 在线率统计列表
     * @param deviceAlarmDto
     * @return list
     */
    List<DeviceAlarmVo> getOnlineList(DeviceAlarmDto deviceAlarmDto);


    /**
     * 总体健康率统计
     * @param deviceAlarmDto
     * @return map
     */
    Map getHealthyRateData(DeviceAlarmDto deviceAlarmDto);


    /**
     * 统计报表--->健康指数统计 --->项目健康指数排名
     * @param deviceAlarmDto
     * @return map
     */
    List<Map> getHealthyTop(DeviceAlarmDto deviceAlarmDto);


    /**
     * 总体健康率统计列表
     * @param deviceAlarmDto
     * @return list
     */
    List<DeviceAlarmVo> getHealthyList(DeviceAlarmDto deviceAlarmDto);

    /**
     * 统计报障箱摄像机告警数量
     * @param deviceAlarmDto
     * @return Map
     */
    Map findAlarmlCount(DeviceAlarmDto deviceAlarmDto);

    /**
     * 查询指定时间内的所有区域的报障数量
     * @param deviceAlarmDto
     * @return
     */
    Integer findAlarmCountByTime(DeviceAlarmDto deviceAlarmDto);

    /**
     * 每个区域指定时间的历史报障数
     * @param deviceAlarmDto
     * @return
     */
    List<RegionAlarmCountVo> findAlarmCountByTimeAndRegion(DeviceAlarmDto deviceAlarmDto);

    /**
     * 每个型号指定时间的历史报障数
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceModelAlarmCountVo> findAlarmCountByTimeAndModel(DeviceAlarmDto deviceAlarmDto);

    /**
     * 故障设备列表
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceAlarmVo> getWrongList(DeviceAlarmDto deviceAlarmDto);

    /**
     * 故障设备趋势分析
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceEventHisWrongVo> queryWrongTrend(DeviceAlarmDto deviceAlarmDto);

    /**
     * 指定时间段内和指定区域的告警总数
     * @param deviceAlarmDto
     * @return
     */
    Long findAlarmTotal(DeviceAlarmDto deviceAlarmDto);

    /**
     * 告警趋势
     * @param deviceAlarmDto
     * @return
     */
    List<Map> findAlarmTrend(DeviceAlarmDto deviceAlarmDto);

    /**
     * 统计报警类型的报警数
     * @param deviceAlarmDto
     * @return
     */
    List<Map<String,Long>> findAlarmCountByType(DeviceAlarmDto deviceAlarmDto);

    /**
     * 每个区域的告警数
     * @param deviceAlarmDto
     * @return
     */
    List<Map<String,Long>> alarmCountByRegion(DeviceAlarmDto deviceAlarmDto);

    /**
     * 每个区域指定时间的历史报警数数
     * @param deviceAlarmDto
     * @return
     */
    List<Map> findAlarmCountByTimeAndRegionMap(DeviceAlarmDto deviceAlarmDto);

    /**
     * 月度告警明细
     * @param deviceAlarmDto
     * @return
     */
    List<Map> monthlyAlarmDetail(DeviceAlarmDto deviceAlarmDto);

    long getWrongCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("regionCode") String regionCode, @Param("type") String type,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    List<String> alarmCount(DeviceAlarmDto deviceAlarmDto);

    List<StatisticsVo> queryPerMonthProjectDeviceOnline(DeviceAlarmDto deviceAlarmDto);

    List<StatisticsVo> queryPerMonthProjectDeviceRepairTime(DeviceAlarmDto deviceAlarmDto);

    List<AlarmTypeCountVo> findAlarmCount(@Param("tableNames") List<String> tableNames,
                                          @Param("startTime") String startTime, @Param("endTime") String endTime,
                                          @Param("projectId") String projectId,
                                          @Param("deviceType") String deviceType,
                                          @Param("regionCode") String regionCode,
                                          @Param("tenantCode") String tenantCode);

    List<Map> annualTrend(@Param("tables") List<String> tables,@Param("kind") String kind,@Param("projectId") String projectId,
                          @Param("regionCode") String regionCode,
                          @Param("tenantCode") String tenantCode);

    /**
     * 数据分析---> 当前告警类型分析列表
     * @param deviceAlarmDto
     * @return list
     */
    List<AlarmTypeStatisticsVo> getCurrentAlarmTypeList(DeviceAlarmDto deviceAlarmDto);


    /**
     * 告警类型分析
     * @param deviceAlarmDto
     * @return
     */
    List<AlarmTypeStatisticsVo> alarmTypeAnalys(DeviceAlarmDto deviceAlarmDto);

    /**
     * 故障类型排名
     * @param deviceAlarmDto
     * @return list
     */
    List<AlarmTypeStatisticsVo> getFaultTypeTop (DeviceAlarmDto deviceAlarmDto);

    /**
     * 故障类型设备数统计
     * @param deviceAlarmDto
     * @return list
     */
    List<AlarmTypeStatisticsVo> getFaultTypeDeviceCount (DeviceAlarmDto deviceAlarmDto);


    /**
     * 统计报表--->健康指数统计 --->健康指数趋势分析
     * @param deviceAlarmDto
     * @return map
     */
    List<Map> getHealthyTrend(DeviceAlarmDto deviceAlarmDto);

    /**
     * 删除告警统计表数据
     * @param projectId
     * @param alarmLevel
     * @return
     */
    int deleteAlarmStatistics(@Param("projectId") String projectId, @Param("alarmLevel") String alarmLevel );

}
