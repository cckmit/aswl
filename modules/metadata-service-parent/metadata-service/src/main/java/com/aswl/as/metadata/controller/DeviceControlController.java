package com.aswl.as.metadata.controller;


import com.aswl.as.metadata.websocket.request.SocketUser;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.metadata.enums.DeviceOperationEnum;
import com.aswl.as.metadata.enums.MQExchange;
import com.aswl.as.metadata.enums.PowerSwitchEnum;
import com.aswl.as.metadata.mq.MQSender;
import com.aswl.as.metadata.service.DeviceService;
import com.aswl.as.metadata.websocket.request.ControlDevice;
import com.aswl.as.metadata.websocket.request.RequestBean;
import com.aswl.as.metadata.websocket.response.ResponseBean;
import com.aswl.iot.dto.DeviceCommand;
import com.aswl.iot.dto.constant.MQConstants;
import com.aswl.iot.dto.constant.OperationMoudle;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
public class DeviceControlController {

    private final static String RESPONSE_DESTINATION = "/queue/rspMsg";
    private final static String COMMAND_DEVICE_ROUTING = MQConstants.COMMAND_DEVICE_QUEUE;
    private final static String COMMAND_DEVICE_ROUTING_WHWC = MQConstants.COMMAND_DEVICE_QUEUE_WHWC;

    @Autowired
    public SimpMessagingTemplate template;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private MQSender mqSender;

    @MessageMapping("/controlDevice")
    public void controlDevice(StompHeaderAccessor accessor, RequestBean<ControlDevice> rb) {
        SocketUser user = (SocketUser) accessor.getUser();
        if (user == null){
            return;
        }
        String userName = user.getUserName();
        log.info("userName : {}", userName);
        log.info("controlDevice : {}", JSON.toJSONString(rb));
        ResponseBean<Object> responseBean = new ResponseBean<Object>();
        responseBean.setEventNo(rb.getEventNo());
        ControlDevice controlDevice = rb.getData();
        //操作实体
        DeviceCommand deviceCommand = new DeviceCommand();
        deviceCommand.setOperationMoudle(OperationMoudle.POWER);
//        deviceCommand.setOperationCodeB(controlDevice.getPortSerial() != null ? controlDevice.getPortSerial() : -1);
//        Device device = deviceService.findById(controlDevice.getDeviceId());
        List<Device> deviceList = deviceService.findByIds(controlDevice.getDeviceId());
        if(deviceList == null || deviceList.size() <= 0){
            responseBean.setCode(0);
            responseBean.setMsg("无该设备");
            template.convertAndSendToUser(userName, RESPONSE_DESTINATION, responseBean);
            return;
        }
        Device device = deviceList.get(0);
        //操作标记
        boolean commandFlag = false;
        String operCode = controlDevice.getOperCode();
		if (!device.getDeviceType().equals(DeviceOperationEnum.DeviceMfrs.WC_DEVICE)) {
            switch (DeviceOperationEnum.getByOperCode(operCode)){
            case DC_POWER_OPEN:
                //电源口打开
                deviceCommand.setOperationCodeA(PowerSwitchEnum.POWER_ON.getSwitchType());
                commandFlag = true;
                break;
            case DC_POWER_CLOSE:
                //电源口关闭
                deviceCommand.setOperationCodeA(PowerSwitchEnum.POWER_OFF.getSwitchType());
                commandFlag = true;
                break;
            case DC_POWER_RESTART:
                //电源口重启
                deviceCommand.setOperationCodeA(PowerSwitchEnum.POWER_RESTART.getSwitchType());
                commandFlag = true;
                break;
            case AC_POWER_OPEN:
                //220V电源口打开
                deviceCommand.setOperationCodeA(PowerSwitchEnum.POWER_ON.getSwitchType());
                deviceCommand.setOperationCodeB(255);
                commandFlag = true;
                break;
            case AC_POWER_CLOSE:
                //电源口关闭
                deviceCommand.setOperationCodeA(PowerSwitchEnum.POWER_OFF.getSwitchType());
                deviceCommand.setOperationCodeB(255);
                commandFlag = true;
                break;
            case AC_POWER_RESTART:
                //电源口重启
                deviceCommand.setOperationCodeA(PowerSwitchEnum.POWER_RESTART.getSwitchType());
                deviceCommand.setOperationCodeB(255);
                commandFlag = true;
                break;
            case DELAYED_DC_POWER_RESTART:
                //电源口重启
                deviceCommand.setOperationCodeA(PowerSwitchEnum.POWER_RESTART.getSwitchType());
                deviceCommand.setOperationCodeB(254);
                commandFlag = true;
                break;
            case POWER_RESTART:
                //系统重启
                deviceCommand.setOperationMoudle(OperationMoudle.POWER_RESTART);
                deviceCommand.setOperationCodeA(0);
                deviceCommand.setOperationCodeB(0);
                commandFlag = true;
                break;
            case FAN_TEMP_VAL_SET:
                //风扇温度阈值设置
                deviceCommand.setOperationMoudle(OperationMoudle.FAN_CTRL);
                deviceCommand.setOperationCodeA((controlDevice.getFanTempVal1() == null || "".equals(controlDevice.getFanTempVal1())) ? 65535 : controlDevice.getFanTempVal1());
                deviceCommand.setOperationCodeB((controlDevice.getFanTempVal2() == null || "".equals(controlDevice.getFanTempVal2())) ?
                        (deviceCommand.getOperationCodeA() == 0 ? 60 : deviceCommand.getOperationCodeA()) : controlDevice.getFanTempVal2());
                commandFlag = true;
                break;
            case MAIN_POWER_RESTART:
                //重合闸重启
                deviceCommand.setOperationMoudle(OperationMoudle.MAIN_POWER_RESTART);
                deviceCommand.setOperationCodeA(0);
                deviceCommand.setOperationCodeB(0);
                commandFlag = true;
                break;
            default:
                break;
            }
        }else if(device.getDeviceType().equals(DeviceOperationEnum.DeviceMfrs.WC_DEVICE)) {
            deviceCommand.setOperationCodeB(controlDevice.getPortSerial() != null ? Integer.parseInt(controlDevice.getPortSerial()) : -1);
            switch (DeviceOperationEnum.getByOperCode(operCode)){
            case DC_POWER_OPEN:
                //电源口打开
                deviceCommand.setOperationCodeA(1);
                commandFlag = true;
                break;
            case DC_POWER_CLOSE:
                //电源口关闭
            	deviceCommand.setOperationCodeA(0);
                commandFlag = true;
                break;
            case TEMP_AUTO_MODE:
            	//风扇自动模式
            	deviceCommand.setOperationMoudle("FanMode");
            	deviceCommand.setOperationCodeA(1);
            	commandFlag = true;
            	break;
            case TEMP_MANUAL_MODE:
            	//风扇手动模式
            	deviceCommand.setOperationMoudle("FanMode");
            	deviceCommand.setOperationCodeA(0);
            	commandFlag = true;
            	break;
            default:
                break;
            }
        }
        if(commandFlag){
        	if(!device.getDeviceType().equals(DeviceOperationEnum.DeviceMfrs.WC_DEVICE)) {
                if(DeviceOperationEnum.getByOperCode(operCode) == DeviceOperationEnum.DC_POWER_RESTART || DeviceOperationEnum.getByOperCode(operCode) == DeviceOperationEnum.DC_POWER_OPEN || DeviceOperationEnum.getByOperCode(operCode) == DeviceOperationEnum.DC_POWER_CLOSE){
                    for(Device device1 : deviceList){
                        com.aswl.iot.dto.Device deviceC = JSONObject.parseObject(JSON.toJSONString(device1), com.aswl.iot.dto.Device.class);
                        deviceCommand.setDevice(deviceC);
                        String[] portArr = controlDevice.getPortSerial().split(",");
                        for (String port : portArr) {
                            deviceCommand.setOperationCodeB(Integer.parseInt(port));
                            mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), COMMAND_DEVICE_ROUTING, JSON.toJSONString(deviceCommand), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                        }
                    }
                }else {
                    for(Device device1 : deviceList){
                        com.aswl.iot.dto.Device deviceC = JSONObject.parseObject(JSON.toJSONString(device1), com.aswl.iot.dto.Device.class);
                        deviceCommand.setDevice(deviceC);
                        mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), COMMAND_DEVICE_ROUTING, JSON.toJSONString(deviceCommand), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                    }
                }
        	}else if(device.getDeviceType().equals(DeviceOperationEnum.DeviceMfrs.WC_DEVICE)) {
                for(Device device1 : deviceList){
                    com.aswl.iot.dto.Device deviceC = JSONObject.parseObject(JSON.toJSONString(device1), com.aswl.iot.dto.Device.class);
                    deviceCommand.setDevice(deviceC);
                    mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), COMMAND_DEVICE_ROUTING_WHWC, JSON.toJSONString(deviceCommand), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                }
        	}
            responseBean.setCode(1);
            responseBean.setMsg("success");
            template.convertAndSendToUser(userName, RESPONSE_DESTINATION, responseBean);
        }else{
            responseBean.setCode(0);
            responseBean.setMsg("fail");
            template.convertAndSendToUser(userName, RESPONSE_DESTINATION, responseBean);
        }

    }
}
