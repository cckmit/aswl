package com.aswl.as.ibrs;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aswl.as.common.core.config.CityPlatformSender;
import com.aswl.as.common.core.constant.CityPlatformConstant;
import com.aswl.as.common.core.constant.MqConstant;
import com.aswl.as.common.core.dto.CityPlatformDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.task.DebugDeviceTask;
import com.aswl.as.ibrs.utils.CityPlatformUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author dingfei
 * @date 2019-09-26 11:23
 */
@SpringBootApplication
@EnableDiscoveryClient
// 扫描api包里的FeignClient
@EnableFeignClients(basePackages = {"com.aswl.as"})
@ComponentScan(basePackages = {"com.aswl.as"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCircuitBreaker
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
public class IbrsServiceApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(IbrsServiceApplication.class,args);
    }


    @Autowired
    private CityPlatformUtil cityPlatformUtil;

    @Autowired
    private CityPlatformSender cityPlatformSender;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ThreadPoolTaskScheduler scheduler;

    @Override
    public void run(String... args) {
        if(cityPlatformUtil.isCityPlatform()){
            CityPlatformDto cityPlatformDto = new CityPlatformDto();
            cityPlatformDto.setCityCode(cityPlatformUtil.getCityCode());
            cityPlatformDto.setCityName(cityPlatformUtil.getCityName());
            cityPlatformDto.setProjectCode(cityPlatformUtil.getProjectCode());
            cityPlatformDto.setProjectName(cityPlatformUtil.getProjectName());
            cityPlatformDto.setFlag(CityPlatformConstant.SUB_START);
            byte[] bytes = JSON.toJSONString(cityPlatformDto, SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
            cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_QUEUE, bytes);

            List<Device> allList = deviceService.findAllList(new Device());
            if(allList != null && allList.size()>0){
                cityPlatformDto.setFlag(CityPlatformConstant.ADD_DEVCICE);
                int pageSize = 100; //分页大小
                int listPage = ((allList.size() % pageSize) == 0) ? allList.size()/pageSize : allList.size()/pageSize + 1;  //总页数
                List<Device> cityList = null;
                for(int pageIndex = 1; pageIndex <= listPage; pageIndex++){
                    int startIndex = (pageIndex - 1) * pageSize;
                    int endIndex = (listPage == pageIndex) ? allList.size() : pageIndex * pageSize;
                    cityList = allList.subList(startIndex, endIndex);
                    cityPlatformDto.setData(cityList);
                    byte[] bytes1 = JSON.toJSONString(cityPlatformDto,SerializerFeature.WriteDateUseDateFormat).getBytes(StandardCharsets.UTF_8);
                    cityPlatformSender.sender(MqConstant.CITY_PLATFORM_EXCHANGE,MqConstant.CITY_PLATFORM_QUEUE, bytes1);
                    cityList = null;
                }
            }
        }

        //清除到期调式设备在服务重启后重新计时
        Device device = new Device();
        device.setDebug(1);
        List<Device> deviceList = deviceService.findList(device);
        if(deviceList.size()>0){
            Date date = new Date();
            for (Device dbDevice : deviceList) {
                Date debugDeadline = dbDevice.getDebugDeadline();
                if(debugDeadline != null && date.before(debugDeadline)){  //还未超过调试截至日期
                    scheduler.getScheduledThreadPoolExecutor().schedule(new DebugDeviceTask(deviceService,dbDevice),debugDeadline.getTime()-date.getTime(), TimeUnit.MILLISECONDS);
                }else {  //已超过调试截至日期
                    dbDevice.setDebug(0);
                    dbDevice.setDebugDuration(null);
                    dbDevice.setDebugDeadline(null);
                    deviceService.update(dbDevice);
                }
            }
        }
    }
}
