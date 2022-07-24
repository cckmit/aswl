package com.aswl.as.metadata.config;

import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.iot.dto.constant.MQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author aswl.com
 * @version 1.0
 * createTime 2019-11-05 16:29
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 创建一个设备状态响应的交换机 : DeviceResponse
     */
    @Bean
    public DirectExchange deviceStatusExchange() {
        return new DirectExchange(MQConstants.DEVICE_STATUS_EXCHANGE, true, false);
    }
    
    /**
     * 创建一个设备状态响应的交换机 : DeviceResponse
     */
    @Bean
    public DirectExchange ifaceDeviceStatusExchange() {
        return new DirectExchange("ifaceDeviceStatusExchange", true, false);
    }

    /**
     * 设备告警消息队列 DeviceAlarmQueue
     */
    @Bean
    public Queue deviceAlarmQueue(){
        return new Queue(MQConstants.DEVICE_ALARM_QUEUE);
    }

    /**
     * 设备监控状态电压队列
     */
    @Bean
    public Queue deviceEventEvoltageQueue(){
        return new Queue(MQConstants.DEVICE_VOLTAGE_QUEUE);
    }

    /**
     * 电源状态队列
     */
    @Bean
    public Queue deviceEventEswitchQueue(){
        return new Queue(MQConstants.DEVICE_SWITCH_QUEUE);
    }

    /**
     * 电流状态队列
     */
    @Bean
    public Queue deviceEventEcurrentQueue(){
        return new Queue(MQConstants.DEVICE_CURRENT_QUEUE);
    }

    /**
     * 光口队列
     */
    @Bean
    public Queue deviceEventSfpQueue(){
        return new Queue(MQConstants.DEVICE_SFP_QUEUE);
    }

    /**
     * 电口队列
     */
    @Bean
    public Queue deviceEventEoutletQueue(){
        return new Queue(MQConstants.DEVICE_OUTLET_QUEUE);
    }

    /**
     * GPS信息队列
     */
    @Bean
    public Queue deviceEventGPSQueue(){
        return new Queue(MQConstants.DEVICE_GPS_QUEUE);
    }

    /**
     * 物联网链接状态队列
     */
    @Bean
    public Queue deviceEventIotQueue(){
        return new Queue(MQConstants.DEVICE_IOT_QUEUE);
    }

    /**
     * 设备网络队列
     */
    @Bean
    public Queue deviceEventNetworkQueue(){
        return new Queue(MQConstants.DEVICE_NETWORK_QUEUE);
    }
    /**
     * 第三方告警队里
     */
    @Bean
    public Queue ifaceCallbackQueue(){
        return new Queue("ifaceCallbackQueue");
    }

    /**
     * 绑定告警状态队列
     */
    @Bean
    public Binding bindingDeviceAlarmQueue(Queue deviceAlarmQueue, DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceAlarmQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_ALARM_QUEUE);
    }

    /**
     * 绑定监控状态电压队列
     */
    @Bean
    public Binding bindingDeviceEventEvoltageQueue(Queue deviceEventEvoltageQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceEventEvoltageQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_VOLTAGE_QUEUE);
    }

    /**
     * 绑定电源状态队列
     */
    @Bean
    public Binding bindingDeviceEventEswitchQueue(Queue deviceEventEswitchQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceEventEswitchQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_SWITCH_QUEUE);
    }

    /**
     * 绑定电流状态队列
     */
    @Bean
    public Binding bindingDeviceEventEcurrentQueue(Queue deviceEventEcurrentQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceEventEcurrentQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_CURRENT_QUEUE);
    }

    /**
     * 绑定光口队列
     */
    @Bean
    public Binding bindingDeviceEventSfpQueue(Queue deviceEventSfpQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceEventSfpQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_SFP_QUEUE);
    }

    /**
     * 绑定电口队列
     */
    @Bean
    public Binding bindingDeviceEventEoutletQueue(Queue deviceEventEoutletQueue, DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceEventEoutletQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_OUTLET_QUEUE);
    }

    /**
     * 绑定物联网连接状态队列
     */
    @Bean
    public Binding bindingDeviceEventIotQueue(Queue deviceEventIotQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceEventIotQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_IOT_QUEUE);
    }

    /**
     * 绑定GPS信息
     */
    @Bean
    public Binding bindingDeviceEventGPSQueue(Queue deviceEventGPSQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceEventGPSQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_GPS_QUEUE);
    }

    /**
     * 绑定网络队列
     */
    @Bean
    Binding bindingDeviceEventNetworkQueue(Queue deviceEventNetworkQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(deviceEventNetworkQueue).to(deviceStatusExchange).with(MQConstants.DEVICE_NETWORK_QUEUE);
    }
    /**
     * 第三方队列
     */
    @Bean
    Binding bindingiIfaceCallbackQueue(Queue ifaceCallbackQueue,DirectExchange ifaceDeviceStatusExchange){
        return BindingBuilder.bind(ifaceCallbackQueue).to(ifaceDeviceStatusExchange).with("ifaceCallbackQueue");
    }

    /**
     * 交换机
     * @return
     */
    @Bean
    public DirectExchange messageExchange(){
        return new DirectExchange("messageExchange");
    }

    /**
     * 队列
     */
    @Bean
    public Queue sendMessageQueue(){
        return new Queue("sendMessageQueue");
    }

    @Bean
    public Queue receiveMessageQueue(){
        return new Queue("receiveMessageQueue");
    }

    /**
     * 绑定
     */
    @Bean
    public Binding bindingSendMessageQueue(DirectExchange messageExchange,Queue sendMessageQueue){
        return BindingBuilder.bind(sendMessageQueue).to(messageExchange).with("sendMessageQueue");
    }

    @Bean
    public Binding bindingReceiveMessageQueue(DirectExchange messageExchange,Queue receiveMessageQueue){
        return BindingBuilder.bind(receiveMessageQueue).to(messageExchange).with("receiveMessageQueue");
    }

    @Bean
    public DirectExchange deviceCtrlExchange(){
        return new DirectExchange(MQConstants.DEVICE_CTRL_EXCHANGE,true,false);
    }

    @Bean
    public Queue addDeviceQueue(){
        return new Queue(MQConstants.ADD_DEVICE_QUEUE);
    }

    @Bean
    public Queue updateDeviceQueue(){
        return new Queue(MQConstants.UPDATE_DEVICE_QUEUE);
    }

    @Bean
    public Queue deleteDeviceQueue(){
        return new Queue(MQConstants.DELETE_DEVICE_QUEUE);
    }

    @Bean
    public Queue commandDeviceQueue(){
        return new Queue(MQConstants.COMMAND_DEVICE_QUEUE);
    }

    @Bean
    Binding bindingAddDeviceQueue(Queue addDeviceQueue,DirectExchange deviceCtrlExchange){
        return BindingBuilder.bind(addDeviceQueue).to(deviceCtrlExchange).with(MQConstants.ADD_DEVICE_QUEUE);
    }
    @Bean
    Binding bindingUpdateDeviceQueue(Queue updateDeviceQueue,DirectExchange deviceCtrlExchange){
        return BindingBuilder.bind(updateDeviceQueue).to(deviceCtrlExchange).with(MQConstants.UPDATE_DEVICE_QUEUE);
    }
    @Bean
    Binding bindingDeleteDeviceQueue(Queue deleteDeviceQueue,DirectExchange deviceCtrlExchange){
        return BindingBuilder.bind(deleteDeviceQueue).to(deviceCtrlExchange).with(MQConstants.DELETE_DEVICE_QUEUE);
    }

    @Bean
    Binding bindingCommandDeviceQueue(Queue commandDeviceQueue,DirectExchange deviceCtrlExchange){
        return BindingBuilder.bind(commandDeviceQueue).to(deviceCtrlExchange).with(MQConstants.COMMAND_DEVICE_QUEUE);
    }

    @Bean
    public Queue commandDeviceQueue_whwc(){
        return new Queue(MQConstants.COMMAND_DEVICE_QUEUE_WHWC);
    }

    @Bean
    Binding bindingdeviceContrllerQueueWhwc(Queue commandDeviceQueue_whwc,DirectExchange deviceCtrlExchange){
        return BindingBuilder.bind(commandDeviceQueue_whwc).to(deviceCtrlExchange).with(MQConstants.COMMAND_DEVICE_QUEUE_WHWC);
    }

    /**
     * 授权状态队列
     */
    @Bean
    public Queue authorizeStatusQueue(){
        return new Queue(MQConstants.AUTHORIZE_STATUS_QUEUE);
    }

    /**
     * 修改钥匙ID队列
     */
    @Bean
    public Queue updateSecretIdQueue(){
        return new Queue(MQConstants.UPDATE_SECRETID_QUEUE);
    }

    /**
     * 黑名单/白名单管理队列
     */
    @Bean
    public Queue blackWhiteListQueue(){
        return new Queue(MQConstants.BLACK_WHITE_LIST_QUEUE);
    }

    /**
     * 巡更状态队列
     */
    @Bean
    public Queue patrolStatusQueue(){
        return new Queue(MQConstants.PATROL_STATUS_QUEUE);
    }

    /**
     * 绑定巡更状态队列
     */
    @Bean
    public Binding bindingPatrolStatusQueue(Queue patrolStatusQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(patrolStatusQueue).to(deviceStatusExchange).with(MQConstants.PATROL_STATUS_QUEUE);
    }


    /**
     * 绑定授权状态队列
     */
    @Bean
    Binding bindingAuthorizeStatusQueue(Queue authorizeStatusQueue,DirectExchange deviceStatusExchange){
        return BindingBuilder.bind(authorizeStatusQueue).to(deviceStatusExchange).with(MQConstants.AUTHORIZE_STATUS_QUEUE);
    }

    /**
     * 绑定修改钥匙id队列
     */
    @Bean
    Binding bindingUpdateSecretIdQueue(Queue updateSecretIdQueue,DirectExchange deviceCtrlExchange){
        return BindingBuilder.bind(updateSecretIdQueue).to(deviceCtrlExchange).with(MQConstants.UPDATE_SECRETID_QUEUE);
    }

    /**
     * 绑定黑名单/白名单管理队列
     */
    @Bean
    Binding bindingBlackWhiteListQueue(Queue blackWhiteListQueue,DirectExchange deviceCtrlExchange){
        return BindingBuilder.bind(blackWhiteListQueue).to(deviceCtrlExchange).with(MQConstants.BLACK_WHITE_LIST_QUEUE);
    }


    /************************************commonMessageFanoutExchange begin********************************************/
    @Bean
    public DirectExchange commonMessageFanoutExchange(){
        return new DirectExchange(MqConstant.SystemMqMessage.COMMON_MESSAGE_FANOUT_EXCHANGE);
    }

    /** 系统广播信息队列 */
    @Bean
    public Queue systemBroadcastMessageQueue(){
        return new Queue(MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE);
    }

    /** 绑定系统广播信息队列 */
    @Bean
    public Binding bindingSystemBroadcastMessageQueue(DirectExchange commonMessageFanoutExchange,Queue systemBroadcastMessageQueue){
        return BindingBuilder.bind(systemBroadcastMessageQueue).to(commonMessageFanoutExchange).with(MqConstant.SystemMqMessage.SYSTEM_BROADCAST_MESSAGE_QUEUE);
    }
    /************************************commonMessageFanoutExchange end********************************************/
}
