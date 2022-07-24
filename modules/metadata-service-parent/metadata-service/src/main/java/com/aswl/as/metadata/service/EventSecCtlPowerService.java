package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSecCtlPower;
import com.aswl.as.metadata.mapper.EventSecCtlPowerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventSecCtlPowerService extends CrudService<EventSecCtlPowerMapper, EventSecCtlPower> {
    private final EventSecCtlPowerMapper eventSecCtlPowerMapper;

    /**
     * 新增设备分控板-电源
     *
     * @param eventSecCtlPower
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventSecCtlPower eventSecCtlPower) {
        return super.insert(eventSecCtlPower);
    }

    /**
     * 删除设备分控板-电源
     *
     * @param eventSecCtlPower
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventSecCtlPower eventSecCtlPower) {
        return super.delete(eventSecCtlPower);
    }

    /**
     * 根据设备ID获取分控板-电源
     * @param deviceId
     * @return
     */
    public EventSecCtlPower findByDeviceId(String deviceId){
        return eventSecCtlPowerMapper.findByDeviceId(deviceId);
    }
}