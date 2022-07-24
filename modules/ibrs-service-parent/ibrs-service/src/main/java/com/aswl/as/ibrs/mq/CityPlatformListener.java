package com.aswl.as.ibrs.mq;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.aswl.as.common.core.constant.CityPlatformConstant;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.dto.CityPlatformDto;
import com.aswl.as.common.core.enums.AlarmLevelType;
import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.mapper.DeviceMapper;
import com.aswl.as.ibrs.service.*;
import com.aswl.iot.dto.DeviceEventNetwork;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.util.JsonUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class CityPlatformListener {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AlarmStatisticsService alarmStatisticsService;

    @Autowired
    private OnlineStatisticsService onlineStatisticsService;

    @Autowired
    @Qualifier(value = "rabbitTemplate")
    private AmqpTemplate amqpTemplate;

    @Autowired
    private CityService cityService;

    @Autowired
    private CityAlarmStatisticsService cityAlarmStatisticsService;

    @Autowired
    private CityRunTimeStatisticsService cityRunTimeStatisticsService;

    @Autowired
    private CityOnlineStatisticsService cityOnlineStatisticsService;

    @Autowired
    private DeviceEventAlarmService deviceEventAlarmService;

    @Autowired
    private EventNetworkService eventNetworkService;

    @Autowired
    private EventAlarmService eventAlarmService;

    @Autowired
    private EventBaseService eventBaseService;

    @Autowired
    private EventPatrolService eventPatrolService;

    @Autowired
    private EventEcurrentService eventEcurrentService;

    @Autowired
    private EventEoutletService eventEoutletService;

    @Autowired
    private EventEswitchService eventEswitchService;

    @Autowired
    private EventEvoltageService eventEvoltageService;

    @Autowired
    private EventSfpService eventSfpService;

    @RabbitListener(queues = MqConstant.CITY_PLATFORM_QUEUE)
    @RabbitHandler
    public void cityPlatformQueue(byte[] bytes) {

        try {
            String message = new String(bytes, StandardCharsets.UTF_8);
            log.info("cityPlatformQueue:" + message);
            CityPlatformDto cityPlatformDto = JSONUtil.toBean(message, CityPlatformDto.class);
            String flag = cityPlatformDto.getFlag();
            if (CityPlatformConstant.SUB_START.equals(flag)) {  //下级启动
                subStartHandler(cityPlatformDto);
            }
            if (CityPlatformConstant.ADD_DEVCICE.equals(flag)) { //新增设备
                addDevice(cityPlatformDto);
            }
            if (CityPlatformConstant.UPDATE_DEVICE.equals(flag)) { //修改设备
                updateDevice(cityPlatformDto);
            }
            if (CityPlatformConstant.DELETE_DEVICE.equals(flag)) { //删除设备
                deleteDevice(cityPlatformDto);
            }
            if (CityPlatformConstant.UPDATE_DEVICE_EVENT.equals(flag)) { //更新汇总
                updateDeviceEvent(cityPlatformDto);
            }
            if (CityPlatformConstant.UPDATE_NETWORK.equals(flag)) { //更新网络状态
                updateNetWork(cityPlatformDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = MqConstant.CITY_PLATFORM_ONLINESTATICS_QUEUE)
    @RabbitHandler
    public void cityPlatformOnlineStatucs(byte[] bytes){
        try {
            String message = new String(bytes,StandardCharsets.UTF_8);
            log.info("cityPlatformOnlineStatucs:" + message);
            CityPlatformDto cityPlatformDto = JSONUtil.toBean(message, CityPlatformDto.class);
            String projectCode = cityPlatformDto.getProjectCode();
            String cityCode = cityPlatformDto.getCityCode();
            Project project = projectService.findByCode(projectCode);
            if (project != null) {
                String projectId = project.getProjectId();
                cn.hutool.json.JSONArray jsonArray = (cn.hutool.json.JSONArray) cityPlatformDto.getData();
                if (jsonArray != null) {
                    List<CityOnlineStatistics> cityOnlineStatistics = JSONUtil.toList(jsonArray, CityOnlineStatistics.class);
                    for (CityOnlineStatistics cityOnlineStatistic : cityOnlineStatistics) {
                        Date statisticsDate = cityOnlineStatistic.getStatisticsDate();
                        String deviceKind = cityOnlineStatistic.getDeviceKind();
                        String deviceModelId = cityOnlineStatistic.getDeviceModelId();
                        CityOnlineStatistics condition = cityOnlineStatisticsService.findByCondition(statisticsDate, deviceKind, cityCode, project.getProjectId(), deviceModelId);
                        if (condition == null) {
                            cityOnlineStatistic.setCityCode(cityCode);
                            cityOnlineStatistic.setProjectId(projectId);
                            cityOnlineStatistic.setId(IdGen.snowflakeId());
                            cityOnlineStatisticsService.insert(cityOnlineStatistic);
                        } else {
                            condition.setDeviceNum(cityOnlineStatistic.getDeviceNum());
                            condition.setOnlineNum(cityOnlineStatistic.getOnlineNum());
                            condition.setIntactNum(cityOnlineStatistic.getIntactNum());
                            cityOnlineStatisticsService.update(condition);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    /**
     * 下级局域网启动,查询市级平台是否有该下级的项目信息
     *
     * @param cityPlatformDto
     */
    private void subStartHandler(CityPlatformDto cityPlatformDto) {
        String projectCode = cityPlatformDto.getProjectCode();
        String projectName = cityPlatformDto.getProjectName();
        Project project = projectService.findByCode(projectCode);
        if (project == null) {
            project = new Project();
            project.setProjectId(IdGen.snowflakeId());
            project.setProjectCode(projectCode);
            project.setProjectName(projectName);
            City city = cityService.findByCode(cityPlatformDto.getCityCode());
            if(city != null){
                project.setCityId(city.getId());
            }
            project.setCreateAt(new Date());
            project.setApplicationCode(SysUtil.getSysCode());
            project.setTenantCode(SysUtil.getTenantCode());
            projectService.cityPlatformInsert(project);
//            AmqpAdmin amqpAdmin = new RabbitAdmin((RabbitTemplate) amqpTemplate);
//            FanoutExchange fanoutExchange = new FanoutExchange(MqConstant.CITY_PLATFORM_FANOUT_EXCHANGE, true, false);
//            amqpAdmin.declareExchange(fanoutExchange);
//            Queue queue = new Queue(MqConstant.CITY_PLATFORM_FANOUT+"_"+projectCode);
//            amqpAdmin.declareQueue(queue);
//            Binding binding = BindingBuilder.bind(queue).to(fanoutExchange);
//            amqpAdmin.declareBinding(binding);
        }
    }

    /**
     * 下级局域网新增设备,市级平台同步新增
     *
     * @param cityPlatformDto
     */
    private void addDevice(CityPlatformDto cityPlatformDto) {
        String projectCode = cityPlatformDto.getProjectCode();
        Project project = projectService.findByCode(projectCode);
        if(project != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cn.hutool.json.JSONArray jsonArray = (cn.hutool.json.JSONArray) cityPlatformDto.getData();
            List<Device> devices = JSONUtil.toList(jsonArray, Device.class);
            for (Device device : devices) {
                String applicationCode = device.getApplicationCode();
                String tenantCode = device.getTenantCode();
                device.setProjectId(project.getProjectId());
                String deviceId = device.getId();
                DeviceVo dbDevice = deviceService.findById(deviceId);
                if(dbDevice == null){
                    deviceService.insert(device);
                }else {
                 deviceService.update(device);
                }
                DeviceEventAlarm eventAlarm = deviceEventAlarmService.findByDeviceId(deviceId);
                if(eventAlarm == null){
                    eventAlarm = new DeviceEventAlarm();
                    eventAlarm.setId(IdGen.snowflakeId());
                    eventAlarm.setDeviceId(deviceId);
                    eventAlarm.setRegionNo(device.getRegionCode());
                    Long currentSecond = System.currentTimeMillis()/1000;
                    eventAlarm.setRecTime(currentSecond.intValue());
                    eventAlarm.setIsAlarm(1);
                    eventAlarm.setAlarmLevel(AlarmLevelType.FAULT.getType());
                    eventAlarm.setStoreTime(new Date());
                    eventAlarm.setApplicationCode(applicationCode);
                    eventAlarm.setTenantCode(tenantCode);
                    eventAlarm.setUEventId("0");
                    eventAlarm.setAlarmTypes("IsOnlineAlarm0");
                    eventAlarm.setAlarmTypesDes("离线");
                    eventAlarm.setAlarmLevels(AlarmLevelType.FAULT.getDescription());
                    deviceEventAlarmService.insert(eventAlarm);
                }
                EventNetwork network = eventNetworkService.findByDeviceId(deviceId);
                if(network == null){
                    network = new EventNetwork();
                    network.setId(IdGen.snowflakeId());
                    network.setDeviceId(deviceId);
                    network.setRegionNo(device.getRegionCode());
                    network.setNetworkState(0);
                    network.setApplicationCode(applicationCode);
                    network.setTenantCode(tenantCode);
                    eventNetworkService.insert(network);
                }
            }
        }
    }

    /**
     * 下级局域网修改设备,市级平台同步修改
     *
     * @param cityPlatformDto
     */
    public void updateDevice(CityPlatformDto cityPlatformDto) {
        String projectCode = cityPlatformDto.getProjectCode();
        Project project = projectService.findByCode(projectCode);
        if(project != null){
            String projectId = project.getProjectId();
            cn.hutool.json.JSONArray jsonArray = (cn.hutool.json.JSONArray) cityPlatformDto.getData();
            List<Device> devices = JSONUtil.toList(jsonArray, Device.class);
            if(devices.size()>0){
                for (Device device : devices) {
                    device.setProjectId(projectId);
                    deviceService.update(device);
                }
            }
        }

//        List<DeviceDto> deviceDtos = JSONUtil.toList(jsonArray, DeviceDto.class);
//        if (deviceDtos.size() > 0) {
//            for (DeviceDto deviceDto : deviceDtos) {
//                Device device = new Device();
//                BeanUtil.copyProperties(deviceDto, device);
////                device.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
//                device.setProjectId(projectId);
//                deviceMapper.update(device);
//                if (deviceDto.getChilDevice().size() > 0) {
//                    List<Map> chilDevice = deviceDto.getChilDevice();
//                    for (Map chilMap : chilDevice) {
//                        String childrenId = (String) chilMap.get("childrenId");
//                        Device query = new Device();
//                        query.setId(childrenId);
//                        query = deviceMapper.get(query);
//                        query.setParentDeviceId(deviceDto.getId());
//                        Integer parentDcpowerNo = (Integer) chilMap.get("parentDcpowerNo");
//                        query.setParentDcpowerNo(parentDcpowerNo);
//                        Integer parentFibreOpticalNo = (Integer) chilMap.get("parentFibreOpticalNo");
//                        query.setParentFibreOpticalNo(parentFibreOpticalNo);
//                        Integer parentRj45No = (Integer) chilMap.get("parentRj45No");
//                        query.setParentRj45No(parentRj45No);
//                        deviceMapper.update(query);
//                    }
//                }
//            }
//        }
    }

    /**
     * 下级局域网删除设备,市级平台同步删除
     *
     * @param cityPlatformDto
     */
    public void deleteDevice(CityPlatformDto cityPlatformDto) {
        String ids = (String) cityPlatformDto.getData();
        deviceService.delCityDevice(ids);
    }


//    @RabbitHandler
//    @RabbitListener(queues = MqConstant.CITY_PLATFORM_FANOUT+"_"+"${platform.projectCode}",containerFactory = "customizeContainerFactory")
//    public void cityPlarformFanout(String message){
//
//    }


    @RabbitHandler
    @RabbitListener(queues = MqConstant.CITY_PLATFORM_ALARM_STATISTICS_QUEUE)
    public void cityPlatformRunRepair(byte[] bytes){
        try {
            String message = new String(bytes,StandardCharsets.UTF_8);
            log.info("cityPlatAlarmStatisticsQueue:" + message);
            CityPlatformDto cityPlatformDto = JSONUtil.toBean(message, CityPlatformDto.class);
            String cityCode = cityPlatformDto.getCityCode();
            String cityName = cityPlatformDto.getCityName();
            String projectCode = cityPlatformDto.getProjectCode();
            Project project = projectService.findByCode(projectCode);
            if(project == null) return;
            String projectName = cityPlatformDto.getProjectName();
            Map alarmStatisticsMap = (Map) cityPlatformDto.getData();
            Map boxMap = (Map) alarmStatisticsMap.get(DeviceKindType.BOX.getType());
            String projectId = project.getProjectId();
            if(boxMap != null){
                cn.hutool.json.JSONArray todayJsonArray = (cn.hutool.json.JSONArray) boxMap.get("today");
                if(todayJsonArray != null){
                    List<CityAlarmStatistics> todayBoxCityAlarmStatistics = JSONUtil.toList(todayJsonArray, CityAlarmStatistics.class);
                    for (CityAlarmStatistics cityAlarmStatistic : todayBoxCityAlarmStatistics) {
                        List<CityAlarmStatistics> list = cityAlarmStatisticsService.findByCondition(cityAlarmStatistic.getStatisticsDate(), cityAlarmStatistic.getDeviceKind(), cityCode, projectId, cityAlarmStatistic.getAlarmType(),cityAlarmStatistic.getDeviceModelId());
                        if(list.size()>0){
                            for (CityAlarmStatistics cityAlarmStatistics : list) {
                                cityAlarmStatistic.setId(cityAlarmStatistics.getId());
//                                Double avgTime = Math.floor((cityAlarmStatistics.getAvgRepairTime() + cityAlarmStatistic.getAvgRepairTime()) / 2);
//                                cityAlarmStatistic.setAvgRepairTime(avgTime.intValue());
                                cityAlarmStatisticsService.update(cityAlarmStatistic);
                            }
                        }else {
                            cityAlarmStatistic.setId(IdGen.snowflakeId());
                            cityAlarmStatistic.setCityCode(cityCode);
                            cityAlarmStatistic.setProjectId(projectId);
                            cityAlarmStatisticsService.insert(cityAlarmStatistic);
                        }
                    }
                }
                cn.hutool.json.JSONArray beforeJsonArray = (cn.hutool.json.JSONArray) boxMap.get("before");
                if(beforeJsonArray != null){
                    List<CityAlarmStatistics> beforeCityAlarmStatistics = JSONUtil.toList(beforeJsonArray, CityAlarmStatistics.class);
                    for (CityAlarmStatistics beforeCityAlarmStatistic : beforeCityAlarmStatistics) {
                        beforeCityAlarmStatistic.setCityCode(cityCode);
                        beforeCityAlarmStatistic.setProjectId(projectId);
                        List<CityAlarmStatistics> conditions = cityAlarmStatisticsService.findByCondition(beforeCityAlarmStatistic.getStatisticsDate(), beforeCityAlarmStatistic.getDeviceKind(), projectId, cityCode, beforeCityAlarmStatistic.getAlarmType(), beforeCityAlarmStatistic.getDeviceModelId());
                        if(conditions.size()>0){
                            for (CityAlarmStatistics condition : conditions) {
//                                condition.setRepairNum(condition.getRepairNum()+beforeCityAlarmStatistic.getRepairNum());
//                                Double avg = Math.floor(((condition.getRepairNum() * condition.getAvgRepairTime()) + (beforeCityAlarmStatistic.getRepairNum() * beforeCityAlarmStatistic.getAvgRepairTime())) / (condition.getRepairNum() + beforeCityAlarmStatistic.getRepairNum()));
//                                condition.setAvgRepairTime(avg.intValue());
                                //cityAlarmStatisticsService.update(condition);
                                beforeCityAlarmStatistic.setId(condition.getId());
                                cityAlarmStatisticsService.update(beforeCityAlarmStatistic);
                            }
                        }
                        //cityAlarmStatisticsService.updateRepairNum(beforeCityAlarmStatistic);
                    }
                }
            }
            Map camMap = (Map) alarmStatisticsMap.get(DeviceKindType.CAMERA.getType());
            if(camMap != null){
                cn.hutool.json.JSONArray todayJsonArray = (cn.hutool.json.JSONArray) camMap.get("today");
                if(todayJsonArray != null){
                    List<CityAlarmStatistics> todayCamCityAlarmStatistics = JSONUtil.toList(todayJsonArray, CityAlarmStatistics.class);
                    for (CityAlarmStatistics cityAlarmStatistic : todayCamCityAlarmStatistics) {
                        List<CityAlarmStatistics> list = cityAlarmStatisticsService.findByCondition(cityAlarmStatistic.getStatisticsDate(), cityAlarmStatistic.getDeviceKind(), cityCode, projectId, cityAlarmStatistic.getAlarmType(),cityAlarmStatistic.getDeviceModelId());
                        if(list.size()>0){
                            for (CityAlarmStatistics cityAlarmStatistics : list) {
                                cityAlarmStatistic.setId(cityAlarmStatistics.getId());
//                                Double avgTime = Math.floor((cityAlarmStatistics.getAvgRepairTime() + cityAlarmStatistic.getAvgRepairTime()) / 2);
//                                cityAlarmStatistic.setAvgRepairTime(avgTime.intValue());
                                cityAlarmStatisticsService.update(cityAlarmStatistic);
                            }
                        }else {
                            cityAlarmStatistic.setId(IdGen.snowflakeId());
                            cityAlarmStatistic.setCityCode(cityCode);
                            cityAlarmStatistic.setProjectId(projectId);
                            cityAlarmStatisticsService.insert(cityAlarmStatistic);
                        }
                    }
                }
                cn.hutool.json.JSONArray beforeJsonArray = (cn.hutool.json.JSONArray) camMap.get("before");
                if(beforeJsonArray != null){
                    List<CityAlarmStatistics> beforeCityAlarmStatistics = JSONUtil.toList(beforeJsonArray, CityAlarmStatistics.class);
                    for (CityAlarmStatistics beforeCityAlarmStatistic : beforeCityAlarmStatistics) {
                        List<CityAlarmStatistics> conditions = cityAlarmStatisticsService.findByCondition(beforeCityAlarmStatistic.getStatisticsDate(), beforeCityAlarmStatistic.getDeviceKind(), projectId, cityCode, beforeCityAlarmStatistic.getAlarmType(), beforeCityAlarmStatistic.getDeviceModelId());
                        if(conditions.size()>0){
                            for (CityAlarmStatistics condition : conditions) {
//                                condition.setRepairNum(condition.getRepairNum()+beforeCityAlarmStatistic.getRepairNum());
//                                Double avg = Math.floor(((condition.getRepairNum() * condition.getAvgRepairTime()) + (beforeCityAlarmStatistic.getRepairNum() * beforeCityAlarmStatistic.getAvgRepairTime())) / (condition.getRepairNum() + beforeCityAlarmStatistic.getRepairNum()));
//                                condition.setAvgRepairTime(avg.intValue());
                                cityAlarmStatisticsService.update(condition);
                                beforeCityAlarmStatistic.setId(condition.getId());
                                cityAlarmStatisticsService.update(beforeCityAlarmStatistic);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    @RabbitListener(queues = MqConstant.CITY_PLATFORM_ONLINE_QUEUE)
    public void online(byte[] bytes){
        try{
            String message = new String(bytes,StandardCharsets.UTF_8);
            log.info("cityPlatOnlineQueue:" + message);
            CityPlatformDto cityPlatformDto = JSONUtil.toBean(message, CityPlatformDto.class);
            String cityCode = cityPlatformDto.getCityCode();
            String cityName = cityPlatformDto.getCityName();
            String projectCode = cityPlatformDto.getProjectCode();
            Project project = projectService.findByCode(projectCode);
            if(project == null) return;
            String projectId = project.getProjectId();
            String projectName = cityPlatformDto.getProjectName();
            cn.hutool.json.JSONArray jsonArray = (cn.hutool.json.JSONArray) cityPlatformDto.getData();
            List<OnlineStatistics> onlineStatisticsList = JSONUtil.toList(jsonArray, OnlineStatistics.class);
            if(onlineStatisticsList.size()>0){
                for (OnlineStatistics onlineStatistics : onlineStatisticsList) {
                    Date statisticsDate = onlineStatistics.getCreateDate();
                    String deviceKind = onlineStatistics.getDeviceKind();
                    OnlineStatistics dbOnlineStatistics = onlineStatisticsService.getByCondition(statisticsDate,deviceKind,projectId);
                    if(dbOnlineStatistics != null){
                        onlineStatistics.setId(dbOnlineStatistics.getId());
                        onlineStatisticsService.update(onlineStatistics);
                    }else {
                        onlineStatistics.setId(IdGen.snowflakeId());
                        onlineStatistics.setProjectId(projectId);
                        onlineStatisticsService.insert(onlineStatistics);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @RabbitHandler
    @RabbitListener(queues = MqConstant.CITY_PLATFORM_RUN_PERIOD_QUEUE)
    public void periodRun(byte[] bytes){
        try{
            String message = new String(bytes,StandardCharsets.UTF_8);
            log.info("periodRun:" + message);
            CityPlatformDto cityPlatformDto = JSONUtil.toBean(message, CityPlatformDto.class);
            String cityCode = cityPlatformDto.getCityCode();
            String cityName = cityPlatformDto.getCityName();
            String projectCode = cityPlatformDto.getProjectCode();
            String projectName = cityPlatformDto.getProjectName();
            Project project = projectService.findByCode(projectCode);
            if(project == null) return;
            cn.hutool.json.JSONArray data = (cn.hutool.json.JSONArray) cityPlatformDto.getData();
            if(data != null){
                String sysCode = SysUtil.getSysCode();
                String tenantCode = SysUtil.getTenantCode();
                List<CityRunTimeStatistics> cityRunTimeStatistics = JSONUtil.toList(data, CityRunTimeStatistics.class);
                for (CityRunTimeStatistics cityRunTimeStatistic : cityRunTimeStatistics) {
                    String deviceKind = cityRunTimeStatistic.getDeviceKind();
                    Date statisticsDate = cityRunTimeStatistic.getStatisticsDate();
                    CityRunTimeStatistics dbCityRunTimeStatistics = cityRunTimeStatisticsService.findByCondition(cityCode,deviceKind,statisticsDate,project.getProjectId());
                    if(dbCityRunTimeStatistics == null){
                        cityRunTimeStatistic.setId(IdGen.snowflakeId());
                        cityRunTimeStatistic.setCityCode(cityCode);
                        cityRunTimeStatistic.setApplicationCode(sysCode);
                        cityRunTimeStatistic.setTenantCode(tenantCode);
                        cityRunTimeStatistic.setProjectId(project.getProjectId());
                        cityRunTimeStatisticsService.insert(cityRunTimeStatistic);
                    }else {
                        cityRunTimeStatistic.setId(dbCityRunTimeStatistics.getId());
                        cityRunTimeStatisticsService.update(cityRunTimeStatistic);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 更新当前汇总
     * @param cityPlatformDto
     */
    private void updateDeviceEvent(CityPlatformDto cityPlatformDto) {
        cn.hutool.json.JSONArray data = (cn.hutool.json.JSONArray) cityPlatformDto.getData();
        if(data != null){
            List<DeviceEventAlarm> deviceEventAlarms = JSONUtil.toList(data, DeviceEventAlarm.class);
            if (deviceEventAlarms.size()>0) {
                Date date = new Date();
                for (DeviceEventAlarm deviceEventAlarm : deviceEventAlarms) {
                    deviceEventAlarm.setStoreTime(date);
                    DeviceEventAlarm alarm = deviceEventAlarmService.findByDeviceId(deviceEventAlarm.getDeviceId());
                    if(alarm == null){
                        deviceEventAlarm.setId(IdGen.snowflakeId());
                        deviceEventAlarmService.insert(deviceEventAlarm);
                    }else {
                        deviceEventAlarm.setId(alarm.getId());
                        deviceEventAlarmService.update(deviceEventAlarm);
                    }
                }
            }
        }
    }


    /**
     * 更新当前汇总
     * @param cityPlatformDto
     */
    private void updateNetWork(CityPlatformDto cityPlatformDto) {
        cn.hutool.json.JSONArray data = (cn.hutool.json.JSONArray) cityPlatformDto.getData();
        if(data != null){
            List<EventNetwork> eventNetworks = JSONUtil.toList(data, EventNetwork.class);
            if (eventNetworks.size()>0) {
                Date date = new Date();
                for (EventNetwork eventNetwork : eventNetworks) {
                    eventNetwork.setStoreTime(date);
                    EventNetwork alarm = eventNetworkService.findByDeviceId(eventNetwork.getDeviceId());
                    if(alarm == null){
                        eventNetwork.setId(IdGen.snowflakeId());
                        eventNetworkService.insert(eventNetwork);
                    }else {
                        eventNetwork.setId(alarm.getId());
                        eventNetworkService.update(eventNetwork);
                    }
                }
            }
        }
    }
}
