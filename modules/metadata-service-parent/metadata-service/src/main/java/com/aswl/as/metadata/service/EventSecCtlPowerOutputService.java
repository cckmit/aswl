package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerOutput;
import com.aswl.as.metadata.mapper.EventSecCtlPowerOutputMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventSecCtlPowerOutputService extends CrudService<EventSecCtlPowerOutputMapper, EventSecCtlPowerOutput> {
    private final EventSecCtlPowerOutputMapper eventSecCtlPowerOutputMapper;

    /**
     * 新增设备分控板-电源输出
     *
     * @param eventSecCtlPowerOutput
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventSecCtlPowerOutput eventSecCtlPowerOutput) {
        return super.insert(eventSecCtlPowerOutput);
    }

    /**
     * 删除设备分控板-电源输出
     *
     * @param eventSecCtlPowerOutput
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventSecCtlPowerOutput eventSecCtlPowerOutput) {
        return super.delete(eventSecCtlPowerOutput);
    }

    /**
     * 根据设备ID获取分控板-电源输出
     * @param deviceId
     * @return
     */
    public EventSecCtlPowerOutput findByDeviceId(String deviceId){
        return eventSecCtlPowerOutputMapper.findByDeviceId(deviceId);
    }
}