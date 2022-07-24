package com.aswl.as.metadata.service;

import com.aswl.as.ibrs.api.module.EventPatrol;
import com.aswl.as.metadata.mapper.EventPatrolMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EventPatrolService {

    private final EventPatrolMapper eventPatrolMapper;

    public EventPatrol findByDeviceId(String deviceId) {
        return eventPatrolMapper.findByDeviceId(deviceId);
    }

    public void save(EventPatrol eventPatrol) {
        eventPatrolMapper.save(eventPatrol);
    }

    public void update(EventPatrol eventPatrol) {
        eventPatrolMapper.update(eventPatrol);
    }
}
