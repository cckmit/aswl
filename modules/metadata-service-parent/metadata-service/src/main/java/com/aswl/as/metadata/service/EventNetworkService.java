package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventNetwork;
import com.aswl.as.metadata.mapper.EventNetworkMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class EventNetworkService extends CrudService<EventNetworkMapper,EventNetwork> {

    private final EventNetworkMapper eventNetworkMapper;

    public int update(EventNetwork eventNetwork){
        return eventNetworkMapper.update(eventNetwork);
    }

    public EventNetwork findByDeviceId(String deviceId){
        return eventNetworkMapper.findByDeviceId(deviceId);
    }
}