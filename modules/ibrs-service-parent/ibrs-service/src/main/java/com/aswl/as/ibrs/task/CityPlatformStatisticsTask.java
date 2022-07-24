package com.aswl.as.ibrs.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aswl.as.common.core.config.CityPlatformSender;
import com.aswl.as.common.core.constant.CityPlatformConstant;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.dto.CityPlatformDto;
import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.service.DeviceEventAlarmService;
import com.aswl.as.ibrs.service.EventNetworkService;
import com.aswl.as.ibrs.service.FlowRunService;
import com.aswl.as.ibrs.utils.CityPlatformUtil;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CityPlatformStatisticsTask {

    @Autowired
    private CityPlatformUtil cityPlatformUtil;

    @Autowired
    private FlowRunService flowRunService;

    @Autowired
    private CityPlatformSender cityPlatformSender;


    @Autowired
    private DeviceEventAlarmService deviceEventAlarmService;

    @Autowired
    private EventNetworkService eventNetworkService;

    private static final SimpleDateFormat formatDay = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat formatMonth = new SimpleDateFormat("yyyyMM");

    /**
     * 修复率
     */
    @Async
    @Scheduled(cron = "59 57 * * * ?")
//    @Scheduled(initialDelay = 10*1000,fixedDelay = 2*60*1000)
    public void cityPlatformRepair() {

        try{
            if(cityPlatformUtil.isCityPlatform()){
                Map cityMap = new HashMap();
                Date date = new Date();
                String today = formatDay.format(date);
                String month = formatMonth.format(date);
                CityPlatformDto cityPlatformDto = new CityPlatformDto();
                cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
                cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
                cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
                cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
                List<CityAlarmStatistics> boxCityRunStatisticss = deviceEventAlarmService.findTodayAlarmCount(month,today, DeviceKindType.BOX.getType());
                if(boxCityRunStatisticss.size() > 0){
                    Map boxMap = new HashMap();
                    List<Map> boxTodayFinish = flowRunService.findTodayFinish(today, DeviceKindType.BOX.getType());
                    for (CityAlarmStatistics boxCityRunStatistics : boxCityRunStatisticss) {
                        String alarmType = boxCityRunStatistics.getAlarmType();
                        String deviceModelId = boxCityRunStatistics.getDeviceModelId();
                        int todayCount = flowRunService.findTodayCount(today, DeviceKindType.BOX.getType(), alarmType,deviceModelId);
                        boxCityRunStatistics.setStatisticsDate(formatDay.parse(today));
                        boxCityRunStatistics.setRunNum(todayCount);
                        boxCityRunStatistics.setRepairNum(0);  //默认设为0
                        boxCityRunStatistics.setAvgRepairTime(0);  //默认设为0
                        if(boxTodayFinish.size() > 0){
                            for (int i = 0; i < boxTodayFinish.size(); i++) {
                                Map map = boxTodayFinish.get(i);
                                if(today.equals(map.get("date")) && alarmType.equals(map.get("alarmType")) && deviceModelId.equals(map.get("deviceModelId"))){
                                    Long finishNum = (Long) map.get("finishNum");
                                    boxCityRunStatistics.setRepairNum(finishNum.intValue());
                                    Number avgRepairTime = (Number) map.get("avgRepairTime");
                                    boxCityRunStatistics.setAvgRepairTime(avgRepairTime.intValue());
                                    boxTodayFinish.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                    boxMap.put("today",boxCityRunStatisticss);
                    if(boxTodayFinish.size() > 0){
                        List<CityAlarmStatistics>  beforeBox = new ArrayList<>();
                        for (Map todayFinish : boxTodayFinish) {
                            String beginTime = (String) todayFinish.get("date");
                            String alarmType = (String) todayFinish.get("alarmType");
                            String deviceModelId = (String) todayFinish.get("deviceModelId");
                            CityAlarmStatistics cityAlarmStatistics = flowRunService.getByDateAlarmTypeKind(beginTime,DeviceKindType.BOX.getType(),alarmType,deviceModelId);
                            if(cityAlarmStatistics != null && cityAlarmStatistics.getStatisticsDate() != null && cityAlarmStatistics.getAlarmType() != null){
                                beforeBox.add(cityAlarmStatistics);
                            }
                        }
                        boxMap.put("before",beforeBox);
                    }
                    cityMap.put(DeviceKindType.BOX.getType(),boxMap);
                }
                List<CityAlarmStatistics> camCityRunStatisticss = deviceEventAlarmService.findTodayAlarmCount(month,today, DeviceKindType.CAMERA.getType());
                if(camCityRunStatisticss.size() > 0){
                    Map camMap = new HashMap();
                    List<Map> camTodayFinish = flowRunService.findTodayFinish(today, DeviceKindType.CAMERA.getType());
                    for (CityAlarmStatistics camCityRunStatistics : camCityRunStatisticss) {
                        String alarmType = camCityRunStatistics.getAlarmType();
                        String deviceModelId = camCityRunStatistics.getDeviceModelId();
                        int todayCount = flowRunService.findTodayCount(today, DeviceKindType.CAMERA.getType(), alarmType,deviceModelId);
                        camCityRunStatistics.setStatisticsDate(formatDay.parse(today));
                        camCityRunStatistics.setRunNum(todayCount);
                        camCityRunStatistics.setRepairNum(0);  //默认设为0
                        camCityRunStatistics.setAvgRepairTime(0);  //默认设为0
                        if(camTodayFinish.size() > 0){
                            for (int i = 0; i < camTodayFinish.size(); i++) {
                                Map map = camTodayFinish.get(i);
                                if(today.equals(map.get("date")) && alarmType.equals(map.get("alarmType")) && deviceModelId.equals(map.get("deviceModelId"))){
                                    Long finishNum = (Long) map.get("finishNum");
                                    camCityRunStatistics.setRepairNum(finishNum.intValue());
                                    Number avgRepairTime = (Number) map.get("avgRepairTime");
                                    camCityRunStatistics.setAvgRepairTime(avgRepairTime.intValue());
                                    camTodayFinish.remove(i);
                                    break;
                                }
                            }
                        }
                    }
                    camMap.put("today",camCityRunStatisticss);
                    if(camTodayFinish.size() > 0){
                        List<CityAlarmStatistics>  beforecam = new ArrayList<>();
                        for (Map todayFinish : camTodayFinish) {
                            String beginTime = (String) todayFinish.get("date");
                            String alarmType = (String) todayFinish.get("alarmType");
                            String deviceModelId = (String) todayFinish.get("deviceModelId");
                            CityAlarmStatistics cityAlarmStatistics = flowRunService.getByDateAlarmTypeKind(beginTime,DeviceKindType.CAMERA.getType(),alarmType,deviceModelId);
                            if(cityAlarmStatistics != null && cityAlarmStatistics.getStatisticsDate() != null && cityAlarmStatistics.getAlarmType() != null){
                                beforecam.add(cityAlarmStatistics);
                            }
                        }
                        camMap.put("before",beforecam);
                    }
                    cityMap.put(DeviceKindType.CAMERA.getType(),camMap);
                }
                if(cityMap.size()>0){
                    cityPlatformDto.setData(cityMap);
                    byte[] bytes = JSON.toJSONString(cityPlatformDto, SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
                    cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_ALARM_STATISTICS_QUEUE, bytes);
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Async
    @Scheduled(cron = "59 58 * * * ?")
//    @Scheduled(initialDelay = 30*1000,fixedDelay = 60*60*1000)
    public void cityOnlineAlarm() {
        try{
            if(cityPlatformUtil.isCityPlatform()) {
                Date statisticsDate = new Date();
                String day = formatDay.format(statisticsDate);
                Date date = formatDay.parse(day);
                String hisTable = "as_event_his_network_"+formatMonth.format(date);
                //新增集合
                List<CityOnlineStatistics> onlineStatisticList = new ArrayList<>();
                //报障箱deviceKind : 2
                List<CityOnlineStatistics > boxOnline = eventNetworkService.onlineCount(DeviceKindType.BOX.getType(),day,hisTable);
                for (CityOnlineStatistics  cityOnlineStatistics : boxOnline) {
                    cityOnlineStatistics.setStatisticsDate(date);
                }
                //摄像头deviceKind : 3
                List<CityOnlineStatistics > camOnline = eventNetworkService.onlineCount(DeviceKindType.CAMERA.getType(),day,hisTable);
                for (CityOnlineStatistics cityOnlineStatistics : camOnline) {
                    cityOnlineStatistics.setStatisticsDate(date);
                }
                onlineStatisticList.addAll(boxOnline);
                onlineStatisticList.addAll(camOnline);
                CityPlatformDto cityPlatformDto = new CityPlatformDto();
                cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
                cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
                cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
                cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
                cityPlatformDto.setData(onlineStatisticList);
                byte[] bytes = JSON.toJSONString(cityPlatformDto,SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
                cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_ONLINESTATICS_QUEUE, bytes);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 工单时间段统计
     */
    @Async
//    @Scheduled(cron = "59 59 * * * ?")
        @Scheduled(initialDelay = 60*1000,fixedDelay = 4*60*1000)
    public void runPeriodStatist(){
        try{
            if(cityPlatformUtil.isCityPlatform()){
                CityPlatformDto cityPlatformDto = new CityPlatformDto();
                cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
                cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
                cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
                cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMM");
                Date date = new Date();
                String today = format.format(date);
                String yearMonth = formatDate.format(date);
                CityRunTimeStatistics boxStatistics = flowRunService.findPeriodRun(today, DeviceKindType.BOX.getType());
                boxStatistics.setDeviceKind(DeviceKindType.BOX.getType());
                CityRunTimeStatistics boxPeriodAlarmNum = deviceEventAlarmService.findPeriodAlarmNum(yearMonth,today,DeviceKindType.BOX.getType());
                //BeanUtil.copyProperties(boxPeriodAlarmNum,boxStatistics);
                boxStatistics.setStatisticsDate(format.parse(today));
                boxStatistics.setAlarmNumTime1(boxPeriodAlarmNum.getAlarmNumTime1());
                boxStatistics.setAlarmNumTime2(boxPeriodAlarmNum.getAlarmNumTime2());
                boxStatistics.setAlarmNumTime3(boxPeriodAlarmNum.getAlarmNumTime3());
                boxStatistics.setAlarmNumTime4(boxPeriodAlarmNum.getAlarmNumTime4());
                boxStatistics.setAlarmNumTime5(boxPeriodAlarmNum.getAlarmNumTime5());
                boxStatistics.setAlarmNumTime6(boxPeriodAlarmNum.getAlarmNumTime6());
                boxStatistics.setAlarmNumTime7(boxPeriodAlarmNum.getAlarmNumTime7());
                boxStatistics.setAlarmNumTime8(boxPeriodAlarmNum.getAlarmNumTime8());
                boxStatistics.setAlarmNumTime9(boxPeriodAlarmNum.getAlarmNumTime9());
                boxStatistics.setAlarmNumTime10(boxPeriodAlarmNum.getAlarmNumTime10());
                boxStatistics.setAlarmNumTime11(boxPeriodAlarmNum.getAlarmNumTime11());
                boxStatistics.setAlarmNumTime12(boxPeriodAlarmNum.getAlarmNumTime12());
                CityRunTimeStatistics camStatistics = flowRunService.findPeriodRun(today, DeviceKindType.CAMERA.getType());
                camStatistics.setDeviceKind(DeviceKindType.CAMERA.getType());
                CityRunTimeStatistics camPeriodAlarmNum = deviceEventAlarmService.findPeriodAlarmNum(yearMonth,today,DeviceKindType.CAMERA.getType());
                //BeanUtil.copyProperties(camPeriodAlarmNum,camStatistics);
                camStatistics.setStatisticsDate(format.parse(today));
                camStatistics.setAlarmNumTime1(camPeriodAlarmNum.getAlarmNumTime1());
                camStatistics.setAlarmNumTime2(camPeriodAlarmNum.getAlarmNumTime2());
                camStatistics.setAlarmNumTime3(camPeriodAlarmNum.getAlarmNumTime3());
                camStatistics.setAlarmNumTime4(camPeriodAlarmNum.getAlarmNumTime4());
                camStatistics.setAlarmNumTime5(camPeriodAlarmNum.getAlarmNumTime5());
                camStatistics.setAlarmNumTime6(camPeriodAlarmNum.getAlarmNumTime6());
                camStatistics.setAlarmNumTime7(camPeriodAlarmNum.getAlarmNumTime7());
                camStatistics.setAlarmNumTime8(camPeriodAlarmNum.getAlarmNumTime8());
                camStatistics.setAlarmNumTime9(camPeriodAlarmNum.getAlarmNumTime9());
                camStatistics.setAlarmNumTime10(camPeriodAlarmNum.getAlarmNumTime10());
                camStatistics.setAlarmNumTime11(camPeriodAlarmNum.getAlarmNumTime11());
                camStatistics.setAlarmNumTime12(camPeriodAlarmNum.getAlarmNumTime12());
                List<CityRunTimeStatistics> periodRunList = new ArrayList<>();
                periodRunList.add(boxStatistics);
                periodRunList.add(camStatistics);
                cityPlatformDto.setData(periodRunList);
                byte[] bytes = JSON.toJSONString(cityPlatformDto,SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
                cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_RUN_PERIOD_QUEUE, bytes);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备汇总和网络状态
     */
    @Async
//    @Scheduled(cron = "59 59 * * * ?")
    @Scheduled(initialDelay = 60 * 1000, fixedDelay = 60 * 60 * 1000)
    public void updateEvent() {
        if (cityPlatformUtil.isCityPlatform()) {
            CityPlatformDto cityPlatformDto = new CityPlatformDto();
            cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
            cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
            cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
            cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
            List<DeviceEventAlarm> deviceEventAlarms = deviceEventAlarmService.findAllList(new DeviceEventAlarm());
            if (deviceEventAlarms != null && deviceEventAlarms.size() > 0) {
                cityPlatformDto.setFlag(CityPlatformConstant.UPDATE_DEVICE_EVENT);
                int pageSize = 100; //分页大小
                int listPage = ((deviceEventAlarms.size() % pageSize) == 0) ? deviceEventAlarms.size() / pageSize : deviceEventAlarms.size() / pageSize + 1;  //总页数
                List<DeviceEventAlarm> cityList = null;
                for (int pageIndex = 1; pageIndex <= listPage; pageIndex++) {
                    int startIndex = (pageIndex - 1) * pageSize;
                    int endIndex = (listPage == pageIndex) ? deviceEventAlarms.size() : pageIndex * pageSize;
                    cityList = deviceEventAlarms.subList(startIndex, endIndex);
                    cityPlatformDto.setData(cityList);
                    byte[] bytes = JSON.toJSONString(cityPlatformDto,SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
                    cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE, MqConstant.CITY_PLATFORM_QUEUE, bytes);
                    cityList = null;
                }
            }
            List<EventNetwork> eventNetworks = eventNetworkService.findAllList(new EventNetwork());
            if (eventNetworks != null && eventNetworks.size() > 0) {
                cityPlatformDto.setFlag(CityPlatformConstant.UPDATE_NETWORK);
                int pageSize = 100; //分页大小
                int listPage = ((eventNetworks.size() % pageSize) == 0) ? eventNetworks.size() / pageSize : eventNetworks.size() / pageSize + 1;  //总页数
                List<EventNetwork> cityList = null;
                for (int pageIndex = 1; pageIndex <= listPage; pageIndex++) {
                    int startIndex = (pageIndex - 1) * pageSize;
                    int endIndex = (listPage == pageIndex) ? eventNetworks.size() : pageIndex * pageSize;
                    cityList = eventNetworks.subList(startIndex, endIndex);
                    cityPlatformDto.setData(cityList);
                    byte[] bytes = JSON.toJSONString(cityPlatformDto,SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
                    cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE, MqConstant.CITY_PLATFORM_QUEUE, bytes);
                    cityList = null;
                }
            }
        }

    }

}
