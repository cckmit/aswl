package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.api.module.EventHisEoutlet;
import com.aswl.as.metadata.mapper.EventHisEoutletMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventHisEoutletService extends CrudService<EventHisEoutletMapper, EventHisEoutlet> {
private final EventHisEoutletMapper eventHisEoutletMapper;

/**
* 新增设备历史事件-电口
* @param  eventHisEoutlet
* @return  int
*/
@Transactional
@Override
public int insert(EventHisEoutlet eventHisEoutlet) {
return super.insert(eventHisEoutlet);
}

/**
* 删除设备历史事件-电口
* @param eventHisEoutlet
* @return int
*/
@Transactional
@Override
public int delete(EventHisEoutlet eventHisEoutlet) {
return super.delete(eventHisEoutlet);
}
}