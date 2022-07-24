package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceFaultDto;
import com.aswl.as.ibrs.api.module.CityAlarmStatistics;
import com.aswl.as.ibrs.api.module.CityRunTimeStatistics;
import com.aswl.as.ibrs.api.vo.DeviceAlarmVo;
import com.aswl.as.ibrs.api.vo.DeviceFaultVo;
import com.aswl.as.ibrs.api.vo.FlowRunVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.FlowRun;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
*
* 流程实例Mapper
* @author dingfei
* @date 2019-10-25 18:16
*/

@Mapper
public interface FlowRunMapper extends CrudMapper<FlowRun> {

    /**
     * 获取故障维护待处理列表
     * @return
     */
    List<DeviceFaultVo> findUndoneList(DeviceFaultDto deviceFaultDto);

    Map findFlowStatus(@Param("id") String id);

    DeviceFaultVo findAlarmType(DeviceFaultVo vo);

    List<DeviceFaultVo> findWorkorderByEventId(@Param("eventId") String eventId);

    /**
     *  根据设备ID删除工单信息
     * @param deviceId
     * @return
     */
    int deleteFlowRunByDeviceId(@Param("deviceId") String deviceId);

    @Delete("delete from as_flow_run where begin_device_id in (${deviceIds})")
    int deleteFlowRunByDeviceIds(@Param("deviceIds") String deviceIds);

    /**
     * 维护历史统计
     * @param type （0：自动；1：手动）
     *  @param deviceId
     *  @param startTime
     *  @param endTime
     * @return  int
     */
    int getHistoryMaintainCounts(@Param("type") String type,@Param("deviceId") String deviceId,@Param("startTime") String startTime,@Param("endTime") String endTime);

    int batchCreateWorkOrder(@Param("flowRuns") List<FlowRun> flowRuns);

    /**
     *  故障维护统计列表
     * @param deviceAlarmDto 查询对象
     * @return  DeviceFaultVo
     */
    List<DeviceFaultVo> getFlowRunData(DeviceAlarmDto deviceAlarmDto);

    /**
     *  获取设备修复时长
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param kind  设备种类（1：光纤收发器，2：传输箱，3：摄像机）
     * @return Map
     */
    Map<String,Number> getRepairTime(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("kind") String kind,
                                     @Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     * 故障派单数
     * @return
     */
    Map<String,Number>findFaultDispatchCount(DeviceAlarmDto deviceAlarmDto);

    /**
     * 派单趋势
     * @param deviceAlarmDto
     * @return
     */
    List<Map<String,Long>> dispatchTrend(DeviceAlarmDto deviceAlarmDto);

    /**
     * 派单完成趋势
     * @param deviceAlarmDto
     * @return list
     */
    List<Map<String,Long>> doneTrent(DeviceAlarmDto deviceAlarmDto);


    /**
     * 派单修复中趋势
     * @param deviceAlarmDto
     * @return list
     */
    List<Map<String,Long>> repairTrent(DeviceAlarmDto deviceAlarmDto);


    /**
     * 未派单趋势
     * @param deviceAlarmDto
     * @return list
     */
    List<Map<String,BigDecimal>> noDispatchTrent(DeviceAlarmDto deviceAlarmDto);

    /**
     * 每个告警类型的派单数和完成数
     * @param deviceAlarmDto
     * @return
     */
    List<Map<String,Long>> dispatchAndDoneByType(DeviceAlarmDto deviceAlarmDto);

    /**
     * 故障派单数
     * @param deviceAlarmDto
     * @return
     */
    Long faultDispatchCount(DeviceAlarmDto deviceAlarmDto);

    /**
     * 维修完成数
     * @param deviceAlarmDto
     * @return
     */
    Long finishedCount(DeviceAlarmDto deviceAlarmDto);
    

    /**
    月度派单明细
     */
    List<Map> MonthlyDispatchRate(DeviceAlarmDto deviceAlarmDto);

    /**
     * 月度维修完成
     */
    List<Map> monthlyRepairRate(DeviceAlarmDto deviceAlarmDto);

    /**
     * 区域派单数
     * @param deviceAlarmDto
     * @return
     */
    List<Map> RegionDispatchRate(DeviceAlarmDto deviceAlarmDto);

    /**
     * 区域修复率
     * @param deviceAlarmDto
     * @return
     */
    List<Map> regionRepairRate(DeviceAlarmDto deviceAlarmDto);

    /**
     * 月度派单率明细
     * @param deviceAlarmDto
     * @return
     */
    List<Map> monthlyDispatchDetail(DeviceAlarmDto deviceAlarmDto);

    /**
     * 月度修复明细
     * @param deviceAlarmDto
     * @return
     */
    List<Map> monthlyRepairDetail(DeviceAlarmDto deviceAlarmDto);

    /**
     * 区域派单率排名
     * @param deviceAlarmDto
     * @return
     */
    List<Map> regionDispatchRateTop(DeviceAlarmDto deviceAlarmDto);

    Number boxRepairDuration(@Param("regionCode") String regionCode,@Param("deviceKind") String deviceKind,@Param("startTime") String startTime,
                             @Param("endTime") String endTime,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    Double getRepairRate(@Param("startTime") String startTime,@Param("endTime") String endTime,@Param("regionCode") String regionCode,
                         @Param("deviceKind") String deviceKind,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     * 根据考核时段和响应时长查询工单数
     * @param beginTime 考核开始时段
     * @param endTime 考核结束时段
     * @param respondTime 响应时长
     * @param previousMonthStart 上个月的开始时间
     * @param previousMonthEnd 上个月的结束时间
     * @return
     */
    List<FlowRunVo> findCountByExamineTime(@Param("beginTimeStr") String beginTimeStr, @Param("endTimeStr") String endTimeStr,
                                           @Param("respondTime") Integer respondTime, @Param("previousMonthStart") String previousMonthStart,
                                           @Param("previousMonthEnd") String previousMonthEnd);

    /**
     * 年度每个月的平均修复时长
     * @param year
     * @param projectId
     * @param kind
     * @return
     */
    List<Map> repairDuration(@Param("year") String year, @Param("projectId") String projectId, @Param("kind") String kind,
                             @Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode);


    /**
     * 未完成工单
     * @param deviceFaultDto
     * @return
     */
    List<DeviceFaultVo> findNoFinish(DeviceFaultDto deviceFaultDto);

    int findTodayCount(@Param("today") String today,@Param("type") String type,@Param("alarmType") String alarmType,@Param("deviceModelId") String deviceModelId);

    List<Map> findTodayFinish(@Param("today") String today,@Param("type") String type);

    CityRunTimeStatistics findPeriodRun(@Param("today") String today,@Param("type") String type);

    CityAlarmStatistics getByDateAlarmTypeKind(@Param("beginTime") String beginTime, @Param("type") String type,
                                               @Param("alarmType") String alarmType, @Param("deviceModelId") String deviceModelId);

    /**
     * 根据元数据ID查询工单信息
     * @param uEventIds
     * @return list
     */
    List<FlowRun> getFlowRunByUeventIds(@Param("uEventIds") List<String> uEventIds);

    /**
     * 根据事件源数据ID删除工单
     * @param uEventIds
     * @return int
     */
    int deleteByUEventIds(@Param("uEventIds") List<String> uEventIds);


    /**
     * 获取工单管理--(待处理/处理完成列表)
     * @return
     */
    List<DeviceFaultVo> findWorkOrderList(DeviceFaultDto deviceFaultDto);




    /**
     * 根据状态查询工单数量
     * @param projectId
     * @param regionCode
     * @param status
     * @param tenantCode
     * @return map
     */
    Map findWorkOrderCount(@Param("maintainUserId")String maintainUserId,@Param("projectId") String projectId, 
                           @Param("regionCode") String regionCode,@Param("status") String status ,
                           @Param("tenantCode") String tenantCode);


    /**
     * 获取我的工单数量
     * @param maintainUserId
     * @param projectId
     * @param regionCode
     * @param status
     * @param tenantCode
     * @return map
     */
    Map myWorkOrderCount(@Param("maintainUserId")String maintainUserId, @Param("projectId") String projectId, 
                         @Param("regionCode") String regionCode,@Param("status") String status ,
                         @Param("tenantCode") String tenantCode);


    /**
     * 待我审核工单数量
     * @param projectId
     * @param regionCode
     * @param status
     * @param tenantCode
     * @return map
     */
    Map getExamineOrderCount(@Param("projectId") String projectId,
                         @Param("regionCode") String regionCode,@Param("status") String status ,
                         @Param("tenantCode") String tenantCode);


    /**
     * 根据工单ID获取工单详情
     * @param id
     * @return  FlowRunVo
     */
    FlowRunVo findFlowRunById(@Param("id") String id);

    /**
     * 已完成工单，同时被处理的工单接口
     * @param beginUserId
     * @param endTime
     * @param id
     * @return list
     */
    List<FlowRunVo> getHandledOrder( @Param("beginUserId") String beginUserId, @Param("endTime") Integer endTime,@Param("id") String id);


    /**
     * 统计报表->故障维护统计->历史平均修复时长
     * @param deviceAlarmDto
     * @return map
     */
    Map getOrderAvgRepairDuration (DeviceAlarmDto deviceAlarmDto);
}
