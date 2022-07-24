package com.aswl.as.metadata.mq;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.metadata.handler.DeviceEventHandler;
import com.aswl.as.metadata.redis.RedisService;
import com.aswl.as.metadata.service.DeviceService;
import com.aswl.as.metadata.websocket.push.DeviceEventPushExecutor;
import com.aswl.as.metadata.websocket.push.PushParam;
import com.aswl.iot.dto.*;
import com.aswl.iot.dto.constant.MQConstants;
import io.micrometer.core.instrument.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;

/**
 * @author aswl.com
 * @version 1.0
 * createTime 2019-11-09 11:09
 */
@Component
@Slf4j
public class MqListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MqListener.class);

    @Autowired
    private RedisService redisService;
    @Autowired
    private DeviceEventHandler deviceEventHandler;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceEventPushExecutor deviceEventPushExecutor;

    /**
     * 设备告警消息队列
     */
    @RabbitListener(queues = "deviceAlarmQueue")
    @RabbitHandler
    public void deviceAlarm(String message) {
        log.info("deviceAlarmQueue msg -> {}", message);
        DeviceEventAlarm deviceEventAlarm = JSONObject.parseObject(message, DeviceEventAlarm.class);
        //考虑线程处理业务
        try {
            deviceEventHandler.handler(deviceEventAlarm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备告警消息队列
     */
    @RabbitListener(queues = "deviceEventEvoltageQueue")
    @RabbitHandler
    public void deviceEventEvoltage(String message) {
        log.info("deviceEventEvoltageQueue msg -> {}", message);
        DeviceEventEvoltage deviceEventEvoltage = JSONObject.parseObject(message, DeviceEventEvoltage.class);
        try {
            deviceEventHandler.handler(deviceEventEvoltage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备告警消息队列
     */
    @RabbitListener(queues = "deviceEventEswitchQueue")
    @RabbitHandler
    public void deviceEventEswitch(String message) {
        log.info("deviceEventEswitchQueue msg -> {}", message);
        DeviceEventEswitch deviceEventEswitch = JSONObject.parseObject(message, DeviceEventEswitch.class);
        try {
            deviceEventHandler.handler(deviceEventEswitch);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备告警消息队列
     */
    @RabbitListener(queues = "deviceEventEcurrentQueue")
    @RabbitHandler
    public void deviceEventEcurrent(String message) {
        log.info("deviceEventEcurrentQueue msg -> {}", message);
        DeviceEventEcurrent deviceEventEcurrent = JSONObject.parseObject(message, DeviceEventEcurrent.class);
        try {
            deviceEventHandler.handler(deviceEventEcurrent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备告警消息队列
     */
    @RabbitListener(queues = "deviceEventSfpQueue")
    @RabbitHandler
    public void deviceEventSfp(String message) {
        log.info("deviceEventSfpQueue msg -> {}", message);
        DeviceEventSfp deviceEventSfp = JSONObject.parseObject(message, DeviceEventSfp.class);
        try {
            deviceEventHandler.handler(deviceEventSfp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备告警消息队列
     */
    @RabbitListener(queues = "deviceEventEoutletQueue")
    @RabbitHandler
    public void deviceEventEoutlet(String message) {
        log.info("deviceEventEoutletQueue msg -> {}", message);
        DeviceEventEoutlet deviceEventEoutlet = JSONObject.parseObject(message, DeviceEventEoutlet.class);
        try {
            deviceEventHandler.handler(deviceEventEoutlet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备告警消息队列
     */
    @RabbitListener(queues = "deviceEventGPSQueue")
    @RabbitHandler
    public void deviceEventGPS(String message) {
        log.info("deviceEventGPSQueue msg -> {}", message);
        DeviceEventGPS deviceEventGPS = JSONObject.parseObject(message, DeviceEventGPS.class);
        try {
            deviceEventHandler.handler(deviceEventGPS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备告警消息队列
     */
    @RabbitListener(queues = "deviceEventIotQueue")
    @RabbitHandler
    public void deviceEventIot(String message) {
        log.info("deviceEventIotQueue msg -> {}", message);
        DeviceEventIot deviceEventIot = JSONObject.parseObject(message, DeviceEventIot.class);
        try {
            deviceEventHandler.handler(deviceEventIot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设备网络消息队列
     */
    @RabbitListener(queues = "deviceEventNetworkQueue")
    @RabbitHandler
    public void deviceEventNetwork(String message) {
        log.info("deviceEventNetworkQueue msg -> {}", message);
        DeviceEventNetwork deviceEventNetwork = JSONObject.parseObject(message, DeviceEventNetwork.class);
        //考虑线程处理业务
        try {
            deviceEventHandler.handler(deviceEventNetwork);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设备巡更状态队列
     */
    @RabbitHandler
    @RabbitListener(queues = "patrolStatusQueue")
    public void deviceEventPatrol(String message){
        log.info("patrolStatusQueue msg -> {}", message);
        DeviceEventPatrol deviceEventPatrol = JSONObject.parseObject(message, DeviceEventPatrol.class);
        try{
            deviceEventHandler.handler(deviceEventPatrol);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 授权状态队列
     */
    @RabbitHandler
    @RabbitListener(queues = "authorizeStatusQueue")
    public void authorizeStatusQueue(String message){
        log.info("authorizeStatusQueue msg -> {}", message);
        DeviceEventAuthorize deviceEventAuthorize = JSONObject.parseObject(message, DeviceEventAuthorize.class);
        try{
            deviceEventHandler.handler(deviceEventAuthorize);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 收短信
     */
    @RabbitHandler
    @RabbitListener(queues = "receiveMessageQueue")
    public void receiveMsg(String message){

        MessageCatSendMsg messageCatSendMsg = JSONUtil.toBean(message, MessageCatSendMsg.class);
        message = messageCatSendMsg.getMsg();

        if(message.endsWith("as_event_network")){
            DeviceEventNetwork deviceEventNetwork = JSONUtil.toBean(message.substring(0,message.lastIndexOf("@")), DeviceEventNetwork.class);
            Device transferDevice = deviceEventNetwork.getDevice();
            Device dbDevice = deviceService.findByIpAndTenantCode(transferDevice.getIp(), transferDevice.getTenantCode(),transferDevice.getProjectCode());
            deviceEventNetwork.setDevice(dbDevice);
            deviceEventNetwork(JSONUtil.toJsonStr(deviceEventNetwork));
        }
        if(message.endsWith("as_event_alarm")){
            DeviceEventAlarm deviceEventAlarm = JSONUtil.toBean(message.substring(0,message.lastIndexOf("@")), DeviceEventAlarm.class);
            Device transferDevice = deviceEventAlarm.getDevice();
            Device dbDevice = deviceService.findByIpAndTenantCode(transferDevice.getIp(), transferDevice.getTenantCode(),transferDevice.getProjectCode());
            deviceEventAlarm.setDevice(dbDevice);
            deviceAlarm(JSONUtil.toJsonStr(deviceEventAlarm));
        }
        if(message.endsWith("as_event_evoltage")){
            DeviceEventEvoltage deviceEventEvoltage = JSONUtil.toBean(message.substring(0,message.lastIndexOf("@")), DeviceEventEvoltage.class);
            Device transferDevice = deviceEventEvoltage.getDevice();
            Device dbDevice = deviceService.findByIpAndTenantCode(transferDevice.getIp(), transferDevice.getTenantCode(),transferDevice.getProjectCode());
            deviceEventEvoltage.setDevice(dbDevice);
            deviceEventEvoltage(JSONUtil.toJsonStr(deviceEventEvoltage));
        }
        if(message.endsWith("as_event_eswitch")){
            DeviceEventEswitch deviceEventEswitch = JSONUtil.toBean(message.substring(0,message.lastIndexOf("@")), DeviceEventEswitch.class);
            Device transferDevice = deviceEventEswitch.getDevice();
            Device dbDevice = deviceService.findByIpAndTenantCode(transferDevice.getIp(), transferDevice.getTenantCode(),transferDevice.getProjectCode());
            deviceEventEswitch.setDevice(dbDevice);
            deviceEventEswitch(JSONUtil.toJsonStr(deviceEventEswitch));
        }
        if(message.endsWith("as_event_ecurrent")){
            DeviceEventEcurrent deviceEventEcurrent = JSONUtil.toBean(message.substring(0,message.lastIndexOf("@")), DeviceEventEcurrent.class);
            Device transferDevice = deviceEventEcurrent.getDevice();
            Device dbDevice = deviceService.findByIpAndTenantCode(transferDevice.getIp(), transferDevice.getTenantCode(),transferDevice.getProjectCode());
            deviceEventEcurrent.setDevice(dbDevice);
            deviceEventEcurrent(JSONUtil.toJsonStr(deviceEventEcurrent));
        }
        if(message.endsWith("as_event_base")){
            DeviceEventIot deviceEventIot = JSONUtil.toBean(message.substring(0,message.lastIndexOf("@")), DeviceEventIot.class);
            Device transferDevice = deviceEventIot.getDevice();
            Device dbDevice = deviceService.findByIpAndTenantCode(transferDevice.getIp(), transferDevice.getTenantCode(),transferDevice.getProjectCode());
            deviceEventIot.setDevice(dbDevice);
            deviceEventIot(JSONUtil.toJsonStr(deviceEventIot));
        }
        if(message.endsWith("as_event_sfp")){
            DeviceEventSfp deviceEventSfp = JSONUtil.toBean(message.substring(0,message.lastIndexOf("@")), DeviceEventSfp.class);
            Device transferDevice = deviceEventSfp.getDevice();
            Device dbDevice = deviceService.findByIpAndTenantCode(transferDevice.getIp(), transferDevice.getTenantCode(),transferDevice.getProjectCode());
            deviceEventSfp.setDevice(dbDevice);
            deviceEventSfp(JSONUtil.toJsonStr(deviceEventSfp));
        }
    }

    /**
     * 设备用电量计量
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_ELECTRICITY)
    public void deviceElectricity(String message){
        log.info("deviceElectricity msg -> {}", message);
        DeviceElectricity deviceElectricity = JSONUtil.toBean(message, DeviceElectricity.class);
        try{
            deviceEventHandler.handler(deviceElectricity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 系统广播消息队列
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE)
    public void systemBroadcastMessage(String message){
        log.info("systemBroadcastMessage msg -> {}", message);
        PushParam pushParam = JSONUtil.toBean(message, PushParam.class);
        if("sysInfo".equals(pushParam.getAlias())){
            deviceEventPushExecutor.pushTopicMsg(pushParam);
        }else if("projectInfo".equals(pushParam.getAlias())){
            deviceEventPushExecutor.pushTopicMsg(pushParam);
        }else if("userCompany".equals(pushParam.getAlias())){
            deviceEventPushExecutor.pushTopicMsg(pushParam);
        }
    }

    /**
     * 设备分控板电源信息
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SEC_CTL_POWER_QUEUE)
    public void deviceSecCtlPower(String message){
        log.info("deviceSecCtlPower msg -> {}", message);
        DeviceSecCtlPower deviceSecCtlPower = JSONUtil.toBean(message, DeviceSecCtlPower.class);
        try{
            deviceEventHandler.handler(deviceSecCtlPower);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备分控板电源告警
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SEC_CTL_POWER_ALARM_QUEUE)
    public void deviceSecCtlPowerAlarm(String message){
        log.info("deviceSecCtlPowerAlarm msg -> {}", message);
        DeviceSecCtlPowerAlarm deviceSecCtlPowerAlarm = JSONUtil.toBean(message, DeviceSecCtlPowerAlarm.class);
        try{
            deviceEventHandler.handler(deviceSecCtlPowerAlarm);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备分控板电源输出电压
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SEC_CTL_POWER_VOL_QUEUE)
    public void deviceSecCtlPowerVol(String message){
        log.info("deviceSecCtlPowerVol msg -> {}", message);
        DeviceSecCtlPowerVol deviceSecCtlPowerVol = JSONUtil.toBean(message, DeviceSecCtlPowerVol.class);
        try{
            deviceEventHandler.handler(deviceSecCtlPowerVol);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备分控板电源输出轴功率
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SEC_CTL_POWER_RATE_QUEUE)
    public void deviceSecCtlPowerRate(String message){
        log.info("deviceSecCtlPowerRate msg -> {}", message);
        DeviceSecCtlPowerRate deviceSecCtlPowerRate = JSONUtil.toBean(message, DeviceSecCtlPowerRate.class);
        try{
            deviceEventHandler.handler(deviceSecCtlPowerRate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备分控板电源输出电量计量
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SEC_CTL_POWER_ELECTRIC_QUEUE)
    public void deviceSecCtlPowerElectric(String message){
        log.info("deviceSecCtlPowerElectric msg -> {}", message);
        DeviceSecCtlPowerElectric deviceSecCtlPowerElectric = JSONUtil.toBean(message, DeviceSecCtlPowerElectric.class);
        try{
            deviceEventHandler.handler(deviceSecCtlPowerElectric);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备分控板电源过压阈值
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SEC_CTL_POWER_OVER_VOL_QUEUE)
    public void deviceSecCtlPowerOverVol(String message){
        log.info("deviceSecCtlPowerOverVol msg -> {}", message);
        DeviceSecCtlPowerOverVol deviceSecCtlPowerOverVol = JSONUtil.toBean(message, DeviceSecCtlPowerOverVol.class);
        try{
            deviceEventHandler.handler(deviceSecCtlPowerOverVol);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备分控板电源欠压阈值
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SEC_CTL_POWER_UNDER_VOL_QUEUE)
    public void deviceSecCtlPowerUnderVol(String message){
        log.info("deviceSecCtlPowerUnderVol msg -> {}", message);
        DeviceSecCtlPowerUnderVol deviceSecCtlPowerUnderVol = JSONUtil.toBean(message, DeviceSecCtlPowerUnderVol.class);
        try{
            deviceEventHandler.handler(deviceSecCtlPowerUnderVol);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备分控板电源过流阈值
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SEC_CTL_POWER_OVER_ELEC_QUEUE)
    public void deviceSecCtlPowerOverElec(String message){
        log.info("deviceSecCtlPowerOverElec msg -> {}", message);
        DeviceSecCtlPowerOverElec deviceSecCtlPowerOverElec = JSONUtil.toBean(message, DeviceSecCtlPowerOverElec.class);
        try{
            deviceEventHandler.handler(deviceSecCtlPowerOverElec);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备底板槽位信息
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_BASE_BOARD_SLOTS_QUEUE)
    public void deviceBaseBoardSlots(String message){
        log.info("deviceBaseBoardSlots msg -> {}", message);
        DeviceBaseBoardSlots deviceBaseBoardSlots = JSONUtil.toBean(message, DeviceBaseBoardSlots.class);
        try{
            deviceEventHandler.handler(deviceBaseBoardSlots);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * SFP信息
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_SFP_INFO_QUEUE)
    public void deviceSfpInfo(String message){
        log.info("deviceSfpInfo msg -> {}", message);
        DeviceEventSfpInfo deviceEventSfpInfo = JSONUtil.toBean(message, DeviceEventSfpInfo.class);
        try{
            deviceEventHandler.handler(deviceEventSfpInfo);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 姿态信息
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_POSTURE_QUEUE)
    public void deviceEventPosture(String message){
        log.info("devicePostureQueue msg -> {}", message);
        DeviceEventPosture deviceEventPosture = JSONUtil.toBean(message, DeviceEventPosture.class);
        try{
            deviceEventHandler.handler(deviceEventPosture);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 设备事件-输入
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = MQConstants.DEVICE_INPUT_QUEUE)
    public void deviceEventInputInfo(String message){
        log.info("deviceEventInputInfo msg -> {}", message);
        DeviceEventInputStatus deviceEventInputStatus = JSONUtil.toBean(message, DeviceEventInputStatus.class);
        try{
            deviceEventHandler.handler(deviceEventInputStatus);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


