package com.aswl.as.metadata.handler;

import com.aswl.as.ibrs.api.module.AlarmLevel;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.EventNetwork;
import com.aswl.as.metadata.api.dto.CollectDataDto;
import com.aswl.as.metadata.api.module.EventUcId;
import com.aswl.as.metadata.common.Globals;
import com.aswl.as.metadata.service.DeviceEventService;
import com.aswl.as.metadata.service.EventNetworkService;
import com.aswl.as.metadata.websocket.push.PushEventData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ResPowerCallBackHandler implements PicVedioHandler.ResPowerCallBack {

    @Autowired
    private EventNetworkService networkService;

    //@Autowired
    private DeviceEventHandler deviceEventHandler;

    @Autowired
    private DeviceEventService deviceEventService;

    @Autowired
    @Lazy
    public void setResPowerCallBackHandler(DeviceEventHandler deviceEventHandler) {
        this.deviceEventHandler = deviceEventHandler;
    }

    @Override
    public void callBack(Device device, CollectDataDto collectDataDto, EventNetwork eventNetwork_new, EventUcId eventUcId, AlarmLevel alarmLevelHighest, PushEventData pushEventData, List<AlarmType> alarmTypeList) {

            try {
                EventNetwork network = networkService.findByDeviceId(device.getId());
                if(network.getNetworkState() != 1) {
                    deviceEventHandler.pushMsg(device, pushEventData);  //推送
                    deviceEventHandler.updateDeviceDynamic(device, collectDataDto.getRegionName(), alarmTypeList);  //更新设备动态
                    //处理汇总数据
                    String statusNames = collectDataDto.getStatusNames();
                    String alarmTypes = collectDataDto.getAlarmTypes();
                    String alarmTypesDes = collectDataDto.getAlarmTypesDes();
                    String alarmLevels = collectDataDto.getAlarmLevels();
                    String alarmDates = collectDataDto.getAlarmDates();
                    String uEventIds = collectDataDto.getuEventIds();
                    String promptTypes = collectDataDto.getPromptTypes();
                    String changeTypes = collectDataDto.getChangeTypes();
                    String changeTypesDes = collectDataDto.getChangeTypesDes();
                    Date storeTime = eventNetwork_new.getStoreTime();
                    long recTime = storeTime.getTime() / 1000;
                    String removeAlarmTypes = deviceEventHandler.updateDeviceEventAlarm(device, statusNames, alarmTypes, alarmTypesDes, alarmLevels, alarmDates, uEventIds, promptTypes, storeTime, recTime);
                    //添加历史数据
                    deviceEventHandler.addDeviceEventHisAlarm(device,eventUcId,alarmLevelHighest,alarmTypes,changeTypes,changeTypesDes,recTime);
                    //生成详细历史数据
                    deviceEventService.generateEventHisRecord(device,eventUcId);
                    if (alarmTypes.length() > 0 && alarmLevelHighest.getAlarmLevel() <= Globals.AlarmLevelConsts.level2) { // 若有报警数据
                        deviceEventHandler.updateAlarmStatistics(device, alarmTypes, storeTime);
                    }
                    //生成工单
                    deviceEventHandler.generateWorkFlow(device,alarmLevelHighest,pushEventData.getPushMessage(),alarmTypes,uEventIds,recTime,removeAlarmTypes);
                    deviceEventHandler.notifyThirdParties(device,changeTypes.split(Globals.EventAlarmSeparateConsts.COMMA),null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
