package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.PageUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.ibrs.api.dto.*;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.service.*;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * App端接口
 *
 * @author df
 * @date 2021/07/20 14:03
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/app", tags = "App端接口")
@RestController
@RequestMapping("/v1/app")
public class AppController extends BaseController {
    private final DeviceService deviceService;
    private final FlowRunService flowRunService;
    private FlowRunLogService flowRunLogService;
    private AlarmStatisticsService alarmStatisticsService;
    private final PatrolHisNoRecordService patrolHisNoRecordService;
    private final EventPatrolService eventPatrolService;

    /**
     * 设备故障类型统计
     *
     * @return map
     */
    @ApiOperation(value = "APP首页设备统计")
    @GetMapping(value = "/deviceCount")
    public ResponseBean<Map> getDeviceAlarmCount(DeviceAlarmConditionDto deviceAlarmConditionDto) {
        return new ResponseBean<>(deviceService.getMapDeviceCount(deviceAlarmConditionDto));
    }


    /**
     * 运行情况
     */

    @ApiOperation(value = "运行情况")
    @GetMapping("deviceList")
    public ResponseBean<PageInfo<DeviceAlarmVo>> monitorDeviceList(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                   @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                   DeviceAlarmConditionDto deviceAlarmConditionDto) {
        return new ResponseBean<>(deviceService.monitorDeviceList(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmConditionDto));
    }


    /**
     * 获取工单管理--(待处理/处理完成列表)
     *
     * @return ResponseBean
     */
    @GetMapping(value = "workOrderList")
    @ApiOperation(value = "获取工单管理--(待处理/处理完成列表)")
    public ResponseBean<PageInfo<DeviceFaultVo>> findWorkOrderList(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                   @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                   DeviceFaultDto deviceFaultDto) {

        return new ResponseBean<>(flowRunService.findWorkOrderList(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceFaultDto));
    }

    /**
     * APP端获取各状态工单数量
     *
     * @return Map
     */
    @GetMapping(value = "workOrderCount")
    @ApiOperation(value = "APP端获取各状态工单数量")
    public ResponseBean<Map> findWorkOrderCount(DeviceFaultDto deviceFaultDto) {
        return new ResponseBean<>(flowRunService.findWorkOrderCount(deviceFaultDto));
    }

    /**
     * 根据工单ID获取工单详情
     *
     * @return FlowRunVo
     */
    @GetMapping(value = "orderDetails")
    @ApiOperation(value = "根据工单ID获取工单详情")
    public ResponseBean<FlowRunVo> findWorkOrderDetails(@RequestParam("id") String id) {
        return new ResponseBean<>(flowRunService.findWorkOrderDetails(id));
    }


    /**
     * 已完成工单，同时被处理的工单接口
     *
     * @return List
     */
    @GetMapping(value = "handledOrder")
    @ApiOperation(value = "已完成工单，同时被处理的工单接口")
    public ResponseBean<List<FlowRunVo>> getHandledOrder(@RequestParam("beginUserId") String beginUserId, @RequestParam("endTime") Integer endTime,@RequestParam("id") String id) {
        return new ResponseBean<>(flowRunService.getHandledOrder(beginUserId, endTime,id));
    }

    /**
     * 通过工单ID查看工单日志
     *
     * @return PageInfo
     */
    @GetMapping(value = "orderLog")
    @ApiOperation(value = "通过工单ID查看工单日志")
    public ResponseBean<PageInfo<DeviceFaultVo>> findWorkOrderHistory(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                      @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                      @RequestParam("id") String id) {
        return new ResponseBean<>(flowRunLogService.findWorkOrderHistory(PageUtil.pageInfo(pageNum, pageSize, "", ""), id));
    }


    /**
     * 故障类型统计
     * @param deviceAlarmDto
     * @return list
     */
    @ApiOperation("故障类型统计")
    @GetMapping("faultType")
    public ResponseBean<List<AlarmTypeStatisticsVo>> getFaultTypeStatistics(DeviceAlarmDto deviceAlarmDto) {
        return new ResponseBean<>(alarmStatisticsService.alarmTypeAnalys(deviceAlarmDto));
    }

    /**
     * 故障点位排名
     *
     * @param deviceAlarmDto
     * @return PageInfo
     */
    @ApiOperation(value = "故障点位排名")
    @GetMapping(value = "faultList")
    public ResponseBean<PageInfo<DeviceAlarmVo>> getFaultDeviceList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                    @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                    DeviceAlarmDto deviceAlarmDto) {

        return new ResponseBean<>(alarmStatisticsService.getHealthyListPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmDto));
    }

    /**
     * 维护概况派单率和修复率
     * @param deviceAlarmDto
     * @return map
     */
    @ApiOperation(value = "维护概况派单率和修复率")
    @GetMapping(value = "dispatchRepairRate")
    public ResponseBean<Map> dispatchRepairRate(DeviceAlarmDto deviceAlarmDto) {
        return new ResponseBean<>(alarmStatisticsService.dispatchRepairRate(deviceAlarmDto));
    }

    /**
     * 区域负责人排名
     *
     * @param deviceAlarmDto
     * @return list
     */
    @ApiOperation(value = "区域负责人排名")
    @GetMapping(value = "regionLeaderTop")
    public ResponseBean<List<FlowRunStatisticsVo>> getRegionLeaderTop(DeviceAlarmDto deviceAlarmDto) {
        return new ResponseBean<>(alarmStatisticsService.getAlarmFaultData(deviceAlarmDto));
    }

    /**
     * 健康监测---> 在线率完好率
     *
     * @param deviceAlarmDto
     * @return ResponseBean
     */

    @GetMapping(value = "regionOnlineIntactStatistics")
    @ApiOperation(value = "各区域在线率完好率", notes = "各区域在线率完好率")
    public ResponseBean<List<RegionDeviceListVo>> getRegionOnlineIntactStatistics(DeviceAlarmDto deviceAlarmDto)
    {
        return new ResponseBean<>(alarmStatisticsService.getRegionOnlineIntactStatistics(deviceAlarmDto));


    }

    /**
     * 分页查询已巡更列表
     * @return
     */
    @GetMapping("patrolList")
    @ApiOperation(value = "分页查询巡更列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EventPatrolDto", value = "设备巡更信息", dataType = "EventPatrolDto")
    })
    public ResponseBean<PageInfo<EventPatrolVo>> patrolList(@RequestParam(value = CommonConstant.PAGE_NUM,required = false,defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                            @RequestParam(value = CommonConstant.PAGE_SIZE,required = false,defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                            EventPatrolDto eventPatrolDto){

        return new ResponseBean<>(eventPatrolService.findByPage(pageNum,pageSize,eventPatrolDto));
    }

    /**
     * 分页查询未巡更列表
     * @param pageNum
     * @param pageSize
     * @param eventPatrolDto
     * @return
     */
    @GetMapping("patrolNoRecordList")
    @ApiOperation(value = "分页查询未巡更列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = CommonConstant.PAGE_NUM, value = "分页页码", defaultValue = CommonConstant.PAGE_NUM_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = CommonConstant.PAGE_SIZE, value = "分页大小", defaultValue = CommonConstant.PAGE_SIZE_DEFAULT, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "EventPatrolDto", value = "设备未巡更信息", dataType = "EventPatrolDto")
    })
    public ResponseBean<PageInfo<PatrolHisNoRecordVo>> patrolNoRecordList(@RequestParam(value = CommonConstant.PAGE_NUM,required = false,defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                          @RequestParam(value = CommonConstant.PAGE_SIZE,required = false,defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                          EventPatrolDto eventPatrolDto){
        return patrolHisNoRecordService.findByPage(pageNum,pageSize,eventPatrolDto);
    }
    
    
}
