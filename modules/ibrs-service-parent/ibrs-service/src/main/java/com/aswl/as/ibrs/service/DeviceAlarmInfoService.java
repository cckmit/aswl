package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.DeviceEventAlarm;
import com.aswl.as.ibrs.api.module.FlowRun;
import com.aswl.as.ibrs.mapper.*;
import com.aswl.as.ibrs.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 告警记录清理
 * @author df
 * @date 2021/07/21 10:43
 */
@AllArgsConstructor
@Slf4j
@Service
public class DeviceAlarmInfoService extends CrudService<DeviceEventAlarmMapper, DeviceEventAlarm> {
    private final AsEventHisAlarmMapper asEventHisAlarmMapper;
    private final FlowRunMapper flowRunMapper;
    private final FlowRunLogMapper flowRunLogMapper;
    private final AlarmStatisticsMapper alarmStatisticsMapper;

    /**
     * 删除历史告警记录信息
     *
     * @param deviceAlarmDto
     * @return
     */
    @Transactional
    public int deleteAlarmInfo(DeviceAlarmDto deviceAlarmDto) {
            this.clearHisAlarmInfo(deviceAlarmDto);
            return 1;
    }


    /**
     * 删除历史告警状态信息
     * @param deviceAlarmDto
     * @return int
     */
    @Transactional
    public  int deleteAlarmStatus(DeviceAlarmDto deviceAlarmDto){
        String uEventId = deviceAlarmDto.getUEventId();
        String tableName = "as_device_event_his_alarm_";
        String alarmTime = deviceAlarmDto.getAlarmTime().substring(0,7).replaceAll("-","");
        // 删除历史表数据
        asEventHisAlarmMapper.deleteHisAlarm(tableName +alarmTime, Arrays.asList(uEventId),null,null);
        //删除设备历史事件-报警
        asEventHisAlarmMapper.deleteHisAlarm("as_event_his_alarm_"+alarmTime,Arrays.asList(uEventId),null,null);
        //设备历史事件-基础
        asEventHisAlarmMapper.deleteHisAlarm("as_event_his_base_"+alarmTime,Arrays.asList(uEventId),null,null);
        //设备历史事件-电流
        asEventHisAlarmMapper.deleteHisAlarm("as_event_his_ecurrent_" +alarmTime,Arrays.asList(uEventId),null,null);
        //设备历史事件-电口
        asEventHisAlarmMapper.deleteHisAlarm("as_event_his_eoutlet_" +alarmTime,Arrays.asList(uEventId),null,null);
        //设备历史事件-电源开关
        asEventHisAlarmMapper.deleteHisAlarm("as_event_his_eswitch_" +alarmTime,Arrays.asList(uEventId),null,null);
        //设备历史事件-网络
        asEventHisAlarmMapper.deleteHisAlarm("as_event_his_network_" +alarmTime,Arrays.asList(uEventId),null,null);
        //设备历史事件-光口
        asEventHisAlarmMapper.deleteHisAlarm("as_event_his_sfp_" +alarmTime,Arrays.asList(uEventId),null,null);
        // 清除工单信息表数据
        List<FlowRun> flowRunList = flowRunMapper.getFlowRunByUeventIds(Arrays.asList(uEventId));
        // 删除工单流程实例日志
        if (flowRunList != null && flowRunList.size() > 0) {
            flowRunLogMapper.deleteByRunId(flowRunList.stream().map(FlowRun::getId).collect(Collectors.toList()));
            return flowRunMapper.deleteByUEventIds(Arrays.asList(uEventId));
        }
        return 1;
    }


    /**
     * 清除设备事件历史报警信息
     */
    @Transactional
    public void clearHisAlarmInfo(DeviceAlarmDto deviceAlarmDto) {
        String projectId = deviceAlarmDto.getProjectId();
        String alarmLevel = deviceAlarmDto.getAlarmLevel();
        List<String> uEventIds = new ArrayList<>();
        // 获取所有历史告警表信息
        List<String> hisAlarmTab = asEventHisAlarmMapper.findListTab(1);
        // 获取所有事件历史元表
        List<String> listTab = asEventHisAlarmMapper.findEventHisTab(1);
        for (String tabName : hisAlarmTab) {
            // 根据项目ID和告警级别查询统一事件ID集合
             uEventIds = asEventHisAlarmMapper.findByUeventIds(tabName, projectId, alarmLevel);
            // 查询工单信息
            if (uEventIds != null && uEventIds.size() > 0) {
            List<FlowRun> flowRunList = flowRunMapper.getFlowRunByUeventIds(uEventIds);
            if (flowRunList != null && flowRunList.size() > 0) {
                // 删除工单流程实例日志
                flowRunLogMapper.deleteByRunId(flowRunList.stream().map(FlowRun::getId).collect(Collectors.toList()));
                //清除工单信息
                flowRunMapper.deleteByUEventIds(uEventIds);
            }
                // 删除历史告警表数据
                asEventHisAlarmMapper.deleteHisAlarm(tabName, null, projectId,alarmLevel);
        }
    }
        for (String tab : listTab) {
            // 清除事件历史元表信息
            asEventHisAlarmMapper.deleteHisAlarm(tab, uEventIds,projectId,null);
        }
        //删除设备故障统计数据
        alarmStatisticsMapper.deleteAlarmStatistics(projectId,alarmLevel);
    }
}
