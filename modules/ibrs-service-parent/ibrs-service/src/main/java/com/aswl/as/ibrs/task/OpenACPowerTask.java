package com.aswl.as.ibrs.task;

import cn.hutool.json.JSONUtil;
import com.aswl.as.common.core.utils.SpringContextHolder;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.Timetask;
import com.aswl.as.ibrs.enums.MQExchange;
import com.aswl.as.ibrs.mapper.TimetaskMapper;
import com.aswl.as.ibrs.mq.MQSender;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.service.TimetaskService;
import com.aswl.as.ibrs.utils.AbstractQuartzJob;
import com.aswl.as.ibrs.utils.BeanUtils;
import com.aswl.iot.dto.ContentBody;
import com.aswl.iot.dto.DeviceCommand;
import com.aswl.iot.dto.constant.MQConstants;
import com.aswl.iot.dto.constant.OperationMoudle;
import lombok.AllArgsConstructor;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时打开220V电源口
 */
@Component
public class OpenACPowerTask implements Job {

//    @Autowired
//    private TimetaskService timetaskService;
//
//    @Autowired
//    private DeviceService deviceService;
//
//    @Autowired
//    private MQSender mqSender;

//    @Autowired
//    private ApplicationContext applicationContext;

    private static final Logger log = LoggerFactory.getLogger(OpenACPowerTask.class);

    @Override
    public void execute(JobExecutionContext context) {

        try {
            ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
            TimetaskService timetaskService = (TimetaskService) applicationContext.getBean("timetaskService");
            DeviceService deviceService = (DeviceService) applicationContext.getBean("deviceService");
            AmqpTemplate amqpTemplate = (AmqpTemplate) applicationContext.getBean(AmqpTemplate.class);
            JobKey key = context.getJobDetail().getKey();
            String keyId = key.getName();
            Timetask timetask = new Timetask();
            timetask.setId(keyId);
            Timetask task = timetaskService.get(timetask);
            //具体的业务逻辑
            String remark = task.getRemark();
            String[] deviceArr = remark.split(";");
            if (deviceArr.length > 0) {
                for (String deviceCode : deviceArr) {
                    DeviceCommand command = new DeviceCommand();
                    command.setOperationMoudle(OperationMoudle.POWER);
                    command.setOperationCodeA(1);
                    com.aswl.iot.dto.Device device = deviceService.getByDeviceCode(deviceCode);
                    command.setDevice(device);
                    command.setOperationCodeB(255);
                    String jsonStr = JSONUtil.toJsonStr(command);
                    amqpTemplate.convertAndSend(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.COMMAND_DEVICE_QUEUE, jsonStr);
                    log.info(jsonStr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
