package com.aswl.as.ibrs.task;

import cn.hutool.core.date.DateUtil;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.ElectricStatistics;
import com.aswl.as.ibrs.api.module.EventElectricity;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.service.DeviceService;
import com.aswl.as.ibrs.service.ElectricStatisticsService;
import com.aswl.as.ibrs.service.EventElectricityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class ElectricityStatistTask {

    @Autowired
    private EventElectricityService eventElectricityService;

    @Autowired
    private ElectricStatisticsService electricStatisticsService;

    @Autowired
    private DeviceService deviceService;

//    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "30 0/5 * * * ?")
    public void ElectricityStatistics() {
        Date date = new Date();
        EventElectricity eventElectricity = new EventElectricity();
        List<EventElectricity> electricityList = eventElectricityService.findAllList(eventElectricity);
        if (electricityList.size() > 0) {
            for (EventElectricity electricity : electricityList) {
                try {
                    String deviceId = electricity.getDeviceId();
                    DeviceVo deviceVo = deviceService.findById(deviceId);
                    if (deviceVo != null) {
                        ElectricStatistics lastRecord = electricStatisticsService.findLastRecord(deviceId);
                        ElectricStatistics todayRecord = electricStatisticsService.findTodayRecord(deviceId, date);
                        if (todayRecord != null) {
                            if (electricity.getElectricity() < todayRecord.getElectricTotalLastDay()) { //如果今天有数据但是当前的读数小于昨天的读数
                                todayRecord.setElectricTotalLastDay(electricity.getElectricity());
                            }
                            todayRecord.setElectricTotal(electricity.getElectricity());
                            todayRecord.setElectricDay(electricity.getElectricity() - todayRecord.getElectricTotalLastDay());
                            electricStatisticsService.update(todayRecord);
                        } else {
                            ElectricStatistics newElectricStatistics = new ElectricStatistics();
                            newElectricStatistics.setId(IdGen.snowflakeId());
                            newElectricStatistics.setDeviceId(deviceId);
                            newElectricStatistics.setRegionCode(deviceVo.getRegionCode());
                            newElectricStatistics.setStatisticsDate(date);
                            newElectricStatistics.setDeviceModelId(deviceVo.getDeviceModelId());
                            newElectricStatistics.setProjectId(deviceVo.getProjectId());
                            if (lastRecord == null || electricity.getElectricity() < lastRecord.getElectricTotal()) { //过去未统计过或者今天的读数小于过去最近一天的读数
                                newElectricStatistics.setElectricTotalLastDay(electricity.getElectricity());
                            } else {
                                newElectricStatistics.setElectricTotalLastDay(lastRecord.getElectricTotal());
                            }
                            newElectricStatistics.setElectricTotal(electricity.getElectricity());
                            newElectricStatistics.setElectricDay(electricity.getElectricity() - newElectricStatistics.getElectricTotalLastDay());
                            electricStatisticsService.insert(newElectricStatistics);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(e.getMessage());
                }
            }
        }
    }
}
