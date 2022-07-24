package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.DeviceEventAlarm;
import com.aswl.as.metadata.mapper.DeviceEventAlarmMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceEventAlarmService extends CrudService<DeviceEventAlarmMapper, DeviceEventAlarm> {
    private final DeviceEventAlarmMapper deviceEventAlarmMapper;

    /**
     * 根据设备ID获取当前报警数据
     * @param deviceId
     * @return
     */
    public DeviceEventAlarm findByDeviceId(String deviceId){
        return deviceEventAlarmMapper.findByDeviceId(deviceId);
    }
}