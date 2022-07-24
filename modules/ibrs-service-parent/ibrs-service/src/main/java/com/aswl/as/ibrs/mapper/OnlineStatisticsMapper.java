package com.aswl.as.ibrs.mapper;

import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.OnlineStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备在线统计Mapper
 *
 * @author dingfei
 * @date 2019-12-16 16:01
 */

@Mapper
public interface OnlineStatisticsMapper extends CrudMapper<OnlineStatistics> {

    /**
     *  查询本月本年设备数量
     * @param type 查询参数 month 本月 year 本年
     * @param regionCode 区域编码
     * @param kind 设备种类（1：光纤收发器，2：传输箱，3：摄像机）
     * @return map
     */
     Map getOnlineStatisticsCounts(@Param("type") String type,@Param("regionCode") String regionCode,@Param("kind") String kind,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     * 在线率统计查询
     * @param deviceAlarmDto
     * @return map
     */
    Map getOnlineStatistics(DeviceAlarmDto deviceAlarmDto);

    /**
     * 批量新增在线记录
     * @param list
     */
    void bathInsert(List<OnlineStatistics> list);

    /**
     * 根据区域和设备种类查询设备总数和在线数量
     */
    List<OnlineStatistics> getByRegionCodeAndDeviceKind(@Param("regionCode") String regionCode, @Param("deviceKind") String deviceKind, @Param("day") String day, @Param("hisTable") String hisTable);

    /**
     * 统计报障箱摄像机数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param kind  设备种类（1：光纤收发器，2：传输箱，3：摄像机）
     * @return Map
     */
    Map getDevivceCount(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("kind") String kind,
                        @Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     * 区域在线占比
     * @param deviceAlarmDto
     * @return
     */
    List<Map> regionOnlineRatio(DeviceAlarmDto deviceAlarmDto);

    /**
     * 设备在线趋势分析
     * @param deviceAlarmDto
     * @return
     */
    List<Map> queryOnlineTrend(DeviceAlarmDto deviceAlarmDto);

    Map getCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("regionCode") String regionCode,
                 @Param("type") String type,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    List<Map> lowestRate(@Param("year") String year,@Param("projectId") String projectId,@Param("kind") String kind,
                         @Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode);

    OnlineStatistics getByDeviceKind(@Param("kind") String kind);

    OnlineStatistics getByCondition(@Param("statisticsDate") Date statisticsDate, @Param("deviceKind") String deviceKind,@Param("projectId") String projectId);

    /**
     * 获取历史健康指数
     * @param deviceAlarmDto
     * @return list
     */
    
    List<Map> getHisHealthyCount(DeviceAlarmDto deviceAlarmDto);

    /**
     * 删除指定日期的统计数据
     * @param createDate
     * @return
     */
    int deleteByCreateDate(String createDate);
}
