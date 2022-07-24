package com.aswl.as.ibrs.task;

import cn.hutool.json.JSONUtil;
import com.aswl.as.common.core.utils.SpringContextHolder;
import com.aswl.as.ibrs.api.module.Timetask;
import com.aswl.as.ibrs.enums.MQExchange;
import com.aswl.as.ibrs.mapper.TimetaskMapper;
import com.aswl.as.ibrs.mq.MQSender;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.service.TimetaskService;
import com.aswl.as.ibrs.utils.AbstractQuartzJob;
import com.aswl.iot.dto.DeviceCommand;
import com.aswl.iot.dto.constant.MQConstants;
import com.aswl.iot.dto.constant.OperationMoudle;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CloseACPowerTask implements Job {

//    @Autowired
//    private TimetaskService timetaskService;
//
//    @Autowired
//    private DeviceService deviceService;
//
//    @Autowired
//    private MQSender mqSender;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            TimetaskService timetaskService = (TimetaskService) applicationContext.getBean("timetaskService");
            DeviceService deviceService = (DeviceService) applicationContext.getBean("deviceService");
            AmqpTemplate amqpTemplate = applicationContext.getBean(AmqpTemplate.class);
            JobKey key = context.getJobDetail().getKey();
            String keyId = key.getName();
            Timetask timetask = new Timetask();
            timetask.setId(keyId);
            Timetask task = timetaskService.get(timetask);
            //doExecute(context, task);
            String remark = task.getRemark();
            String[] deviceArr = remark.split(";");
            if (deviceArr.length > 0) {
                for (String deviceCode : deviceArr) {
                    DeviceCommand command = new DeviceCommand();
                    command.setOperationMoudle(OperationMoudle.POWER);
                    command.setOperationCodeA(2);
                    com.aswl.iot.dto.Device device = deviceService.getByDeviceCode(deviceCode);
                    command.setDevice(device);
                    command.setOperationCodeB(255);
                    amqpTemplate.convertAndSend(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.COMMAND_DEVICE_QUEUE, JSONUtil.toJsonStr(command));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
