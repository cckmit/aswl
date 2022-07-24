package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerSet;
import com.aswl.as.metadata.mapper.EventSecCtlPowerSetMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventSecCtlPowerSetService extends CrudService<EventSecCtlPowerSetMapper, EventSecCtlPowerSet> {
    private final EventSecCtlPowerSetMapper eventSecCtlPowerSetMapper;

    /**
     * 新增设备分控板-电源设置
     *
     * @param eventSecCtlPowerSet
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventSecCtlPowerSet eventSecCtlPowerSet) {
        return super.insert(eventSecCtlPowerSet);
    }

    /**
     * 删除设备分控板-电源设置
     *
     * @param eventSecCtlPowerSet
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventSecCtlPowerSet eventSecCtlPowerSet) {
        return super.delete(eventSecCtlPowerSet);
    }

    /**
     * 根据设备ID获取分控板-电源设置
     * @param deviceId
     * @return
     */
    public EventSecCtlPowerSet findByDeviceId(String deviceId){
        return eventSecCtlPowerSetMapper.findByDeviceId(deviceId);
    }
}