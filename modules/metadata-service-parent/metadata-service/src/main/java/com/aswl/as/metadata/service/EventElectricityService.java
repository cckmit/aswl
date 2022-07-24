package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventElectricity;
import com.aswl.as.metadata.mapper.EventElectricityMapper;
import com.aswl.as.metadata.mapper.EventElectricityMapper;
import com.aswl.iot.dto.DeviceElectricity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventElectricityService extends CrudService<EventElectricityMapper, EventElectricity> {
    private final EventElectricityMapper eventElectricityMapper;

    /**
     * 新增设备事件-电量
     *
     * @param eventElectricity
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventElectricity eventElectricity) {
        return super.insert(eventElectricity);
    }

    /**
     * 删除设备事件-电量
     *
     * @param eventElectricity
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventElectricity eventElectricity) {
        return super.delete(eventElectricity);
    }

    public EventElectricity findByDeviceId(String deviceId) {
        return eventElectricityMapper.findByDeviceId(deviceId);
    }
}