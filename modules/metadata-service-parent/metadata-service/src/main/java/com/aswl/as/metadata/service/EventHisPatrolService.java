package com.aswl.as.metadata.service;

import com.aswl.as.ibrs.api.module.EventPatrol;
import com.aswl.as.metadata.api.module.EventHisPatrol;
import com.aswl.as.metadata.mapper.EventHisPatrolMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EventHisPatrolService {
    private final EventHisPatrolMapper eventHisPatrolMapper;

    public void save(EventHisPatrol eventHisPatrol, String hisTableName) {
        eventHisPatrolMapper.save(eventHisPatrol,hisTableName);
    }
}
