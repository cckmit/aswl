package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.vo.CityVo;
import com.aswl.as.ibrs.api.vo.DeviceOnlineIntactVo;
import com.aswl.as.ibrs.api.vo.DeviceRepairRateVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.CityAlarmStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 故障统计表Mapper
 *
 * @author df
 * @date 2021/01/15 11:21
 */

@Mapper
public interface CityAlarmStatisticsMapper extends CrudMapper<CityAlarmStatistics> {

    /**
     * 近30天在线率完好率总体变化趋势查询
     * @param kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市CODE
     * @param projectId  项目ID
     * @param startTime  开始日期
     * @param endTime  结束日期
     * @param timeUnit  显示单位
     * @return list
     */
    List<Map> getDeviceOnlineIntactTrend(@Param("kind") String kind, @Param("cityCode") String cityCode, @Param("projectId") String projectId, @Param("startTime") String startTime,@Param("endTime") String endTime,@Param("timeUnit") String timeUnit);


    /**
     * 近30天设备修复率查询
     * @param kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市CODE
     * @param projectId  项目ID
     * @param startTime  开始日期
     * @param endTime  结束日期
     * @return list
     */
    List<DeviceRepairRateVo> findRepairRate(@Param("kind") String kind, @Param("cityCode") String cityCode, @Param("projectId") String projectId, @Param("startTime") String startTime,@Param("endTime") String endTime);

    /**
     * 多条件查询
     * @param statisticsDate
     * @param deviceKind
     * @param cityCode
     * @param projectId
     * @return
     */
    List<CityAlarmStatistics> findByCondition(@Param("statisticsDate") Date statisticsDate, @Param("deviceKind") String deviceKind, @Param("cityCode") String cityCode, @Param("projectId") String projectId, @Param("alarmType") String alarmType,@Param("deviceModelId") String deviceModelId);


    /**
     *  获取总体设备数量
     * @param kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市CODE
     * @param projectId  项目ID
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return Map
     */
    Map findDeviceCount(@Param("kind") String kind, @Param("cityCode") String cityCode, @Param("projectId") String projectId, @Param("startTime") String startTime,@Param("endTime") String endTime);



    /**
     *  获取区域设备数量
     * @param kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市CODE
     * @param parentId 父级ID
     * @param projectId  项目ID
     * @param level 级别
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return Map
     */
    List<CityVo> findCityDeviceCount(@Param("kind") String kind, @Param("cityCode") String cityCode,@Param("parentId") String parentId, @Param("projectId") String projectId, @Param("level") String level, @Param("startTime") String startTime, @Param("endTime") String endTime);



    /**
     *  各设备厂商在线/完好数量
     * @param kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市CODE
     * @param projectId  项目ID
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @param groupName 分组字段
     * @return Map
     */
    List<CityVo> findManufacturerDeviceCount(@Param("kind") String kind, @Param("cityCode") String cityCode, @Param("projectId") String projectId, @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("groupName") String groupName);



    /**
     *  各设备厂商在线/完好数量
     * @param kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市CODE
     * @param projectId  项目ID
     * @param startTime 开始日期
     * @param endTime 结束日期
     * @return Map
     */
    List<Map> findFaultTypeDeviceCount(@Param("kind") String kind, @Param("cityCode") String cityCode, @Param("projectId") String projectId,
                                          @Param("startTime") String startTime, @Param("endTime") String endTime);


    /**
     * 市级平台派单率
     * @param cityAlarmStatisticsDto
     * @return
     */
    CityAlarmStatistics cityPlatDistrRate(CityAlarmStatisticsDto cityAlarmStatisticsDto);

    /**
     * 告警类型派单占比
     * @param cityAlarmStatisticsDto
     * @return
     */
    List<CityAlarmStatistics> cityPlatAlarmTypeDistr(CityAlarmStatisticsDto cityAlarmStatisticsDto);


    /**
     * 告区域派单率
     * @param cityAlarmStatisticsDto
     * @return
     */
    List<CityAlarmStatisticsDto> cityPlatCityDistr(CityAlarmStatisticsDto cityAlarmStatisticsDto);

    /**
     * 各月份派单修复率
     */
    List<CityAlarmStatisticsDto> cityPlatMonthlyRepairReta(CityAlarmStatisticsDto cityAlarmStatisticsDto);

    /**
     * 各区域 修复率
     * @param cityAlarmStatisticsDto
     * @return
     */
    List<CityAlarmStatisticsDto> cityPlatCityRepair(CityAlarmStatisticsDto cityAlarmStatisticsDto);

    /**
     * 故障率
     * @param cityAlarmStatisticsDto
     * @return
     */
    Map wrongRate(CityAlarmStatisticsDto cityAlarmStatisticsDto);

}


