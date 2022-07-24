package com.aswl.as.ibrs.task;

import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.utils.ScheduleUtils;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ScheduledExecutorService;
@Slf4j
public class DebugDeviceTask implements Runnable{

    private DeviceService deviceService;

    private Device device;

    public DebugDeviceTask(DeviceService deviceService, Device device) {
        this.deviceService = deviceService;
        this.device = device;
    }

    @Override
    public void run() {
        //清除调试模式
        device.setDebug(0);
        device.setDebugDeadline(null);
        device.setDebugDuration(null);
        deviceService.update(device);
        log.info("调度器清除到期的调式设备.....");
    }
}
