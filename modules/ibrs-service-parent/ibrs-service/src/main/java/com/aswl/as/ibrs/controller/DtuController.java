package com.aswl.as.ibrs.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.common.core.constant.DtuPushConstant;
import com.aswl.as.common.core.model.DtuBodyBean;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.JPushUtils;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.FlowRun;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.service.FlowRunService;
import com.aswl.as.user.api.dto.SmsDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.iot.dto.MessageCatSendMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("dtu")
@Slf4j
public class DtuController {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private FlowRunService flowRunService;

    @Value("${sms.templateCode}")
    private String templateCode;

    @Value("${sms.signName}")
    private String signName;


    @PostMapping(value = "message")
    public void message(@RequestBody String message){

        try {
//            Map map = JSON.parseObject(message, Map.class);
//            log.info("Dtu接口:" + map.toString());
//            String telephoneNo = (String) map.get("telephoneNo");
//            String msg = (String) map.get("msg");
//            String flag = (String) map.get("flag"); //sendMsg 发短信  pushApp 推送
//            List<JSONObject> flowRuns = (List<JSONObject>) map.get("data");
//            String ip = (String) map.get("ip");
////        String tenantCode = (String) map.get("tenantCode");
//            String projectCode = (String) map.get("projectCode");

            DtuBodyBean dtuBodyBean = JSON.parseObject(message, DtuBodyBean.class);
            log.info("Dtu接口:" + dtuBodyBean.toString());
            String telephoneNo = dtuBodyBean.getTelephoneNo();
            String msg = dtuBodyBean.getMsg();
            String flag = dtuBodyBean.getFlag(); //sendMsg 发短信  pushApp 推送
            List<JSONObject> flowRuns = (List<JSONObject>) dtuBodyBean.getData();
            String ip = dtuBodyBean.getIp();
            String projectCode = dtuBodyBean.getProjectCode();

            Device device = new Device();
            device.setIp(ip);
//        device.setTenantCode(tenantCode);
            device.setProjectCode(projectCode);
            Device dbDevice = deviceService.findByIpAndProjectCode(device);
            if (dbDevice != null){
                if(DtuPushConstant.DtuFlagEnum.SEND_SMS_ALARM.equals(flag)){  //发短信-告警
                    //todo  发短信
                    SmsDto smsDto = new SmsDto();
                    smsDto.setReceiver(telephoneNo);
                    smsDto.setContent(msg);
                    smsDto.setTemplateCode(templateCode);
                    smsDto.setSignName(signName);
                    userServiceClient.sendSms(smsDto);
                }else if((DtuPushConstant.DtuFlagEnum.SEND_SMS_WORK_ORDER.equals(flag))) { //发短信-工单

                }else if(DtuPushConstant.DtuFlagEnum.PUSH_APP_ALARM.equals(flag)){  //推送APP-告警

                }else if(DtuPushConstant.DtuFlagEnum.PUSH_APP_WORK_ORDER.equals(flag)){ //推送APP-工单
                    ResponseBean<UserVo> userByIdentifier = userServiceClient.findUserByIdentifier(telephoneNo, "");
                    if(userByIdentifier != null && userByIdentifier.getStatus() == ResponseBean.SUCCESS ){
                        UserVo data = userByIdentifier.getData();
                        if (data != null) {
                            String userId = data.getUserId();
                            log.info("userId:" + userId);
                            JPushUtils.sendAlias(msg,"告警推送",300,userId);
                        }
                    }
                }
    //            List<FlowRun> flowRuns = JSON.parseArray(flowRunsStr, FlowRun.class);
                    for (JSONObject jsonObject : flowRuns) {
                        FlowRun flowRun = JSON.toJavaObject(jsonObject, FlowRun.class);
                        flowRun.setBeginDeviceId(dbDevice.getId());
                        flowRunService.insert(flowRun);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
