package com.aswl.as.metadata.service;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.AlarmStatistics;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.metadata.mapper.AlarmStatisticsMapper;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmStatisticsService extends CrudService<AlarmStatisticsMapper, AlarmStatistics> {
    private final AlarmStatisticsMapper statisticsMapper;

    public AlarmStatistics findByDeviceIdWithDate(String deviceId, String alarmType, Date date){
        return statisticsMapper.findByDeviceIdWithDate(deviceId, alarmType, date);
    }
}