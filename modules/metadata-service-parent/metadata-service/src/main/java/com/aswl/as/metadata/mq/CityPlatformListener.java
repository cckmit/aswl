package com.aswl.as.metadata.mq;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aswl.as.common.core.constant.CityPlatformConstant;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.dto.CityPlatformDto;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.AlarmStatistics;
import com.aswl.as.ibrs.api.module.DeviceEventAlarm;
import com.aswl.as.ibrs.api.module.EventNetwork;
import com.aswl.as.metadata.service.AlarmStatisticsService;
import com.aswl.as.metadata.service.DeviceEventAlarmService;
import com.aswl.as.metadata.service.EventNetworkService;
import com.aswl.iot.dto.constant.MQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
public class CityPlatformListener {

    @Autowired
    private DeviceEventAlarmService deviceEventAlarmService;

    @Autowired
    private AlarmStatisticsService alarmStatisticsService;

    @Autowired
    private EventNetworkService eventNetworkService;

    @RabbitListener(queues = MqConstant.CITY_PLATFORM_DEVICEEVENT_QUEUE)
    @RabbitHandler
    public void cityPlatformDeviceEvent(byte[] bytes){
        try{
            String message = new String(bytes, StandardCharsets.UTF_8);
            log.info("cityPlatformDeviceEvent:" + message);
            CityPlatformDto cityPlatformDto = JSONUtil.toBean(message, CityPlatformDto.class);
            JSONObject jsonObject = (JSONObject) cityPlatformDto.getData();
            DeviceEventAlarm deviceEventAlarm = JSONUtil.toBean(jsonObject, DeviceEventAlarm.class);
            String deviceId = deviceEventAlarm.getDeviceId();
            DeviceEventAlarm eventAlarm = deviceEventAlarmService.findByDeviceId(deviceId);
            if(eventAlarm == null){
                deviceEventAlarmService.insert(deviceEventAlarm);
            }else {
                deviceEventAlarm.setId(eventAlarm.getId());
                deviceEventAlarmService.update(deviceEventAlarm);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = MqConstant.CITY_PLATFORM_ALARM_COUNT_QUEUE)
    @RabbitHandler
    public void cityPlatformAlarmStatics(byte[] bytes){
        try{
            String message = new String(bytes,StandardCharsets.UTF_8);
            log.info("cityPlatformAlarmStatics:" + message);
            CityPlatformDto cityPlatformDto = JSONUtil.toBean(message, CityPlatformDto.class);
            JSONObject jsonObject = (JSONObject) cityPlatformDto.getData();
            AlarmStatistics alarmStatistics = JSONUtil.toBean(jsonObject, AlarmStatistics.class);
            String deviceId = alarmStatistics.getDeviceId();
            Date createDate = alarmStatistics.getCreateDate();
            String alarmType = alarmStatistics.getAlarmType();
            AlarmStatistics statistics = alarmStatisticsService.findByDeviceIdWithDate(deviceId, alarmType, createDate);
            if(statistics == null){
                alarmStatisticsService.insert(alarmStatistics);
            }else {
                statistics.setAlarmNum(alarmStatistics.getAlarmNum());
                statistics.setFaultNum(alarmStatistics.getFaultNum());
                alarmStatisticsService.update(statistics);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @RabbitListener(queues = MqConstant.CITY_PLATFORM_NETWORK_QUEUE)
    @RabbitHandler
    public void cityPlatNetWork(byte[] bytes){
        try{
            String message = new String(bytes,StandardCharsets.UTF_8);
            log.info("cityPlatNetWork:" + message);
            CityPlatformDto cityPlatformDto = JSONUtil.toBean(message, CityPlatformDto.class);
            JSONObject jsonObject = (JSONObject) cityPlatformDto.getData();
            EventNetwork eventNetwork = JSONUtil.toBean(jsonObject, EventNetwork.class);
            String deviceId = eventNetwork.getDeviceId();
            EventNetwork dbEventNetwork = eventNetworkService.findByDeviceId(deviceId);
            if(dbEventNetwork == null){
                eventNetwork.setRegionNo(cityPlatformDto.getCityCode());
//                eventNetwork.setCommonValue(null,SysUtil.getSysCode(),SysUtil.getTenantCode());
                eventNetworkService.insert(eventNetwork);
            }else {
                eventNetwork.setId(dbEventNetwork.getId());
                eventNetworkService.update(eventNetwork);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
