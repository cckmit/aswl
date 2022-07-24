package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.api.module.EventHisEcurrent;
import com.aswl.as.metadata.mapper.EventHisEcurrentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventHisEcurrentService extends CrudService<EventHisEcurrentMapper, EventHisEcurrent> {
private final EventHisEcurrentMapper eventHisEcurrentMapper;

/**
* 新增设备历史事件-电流
* @param  eventHisEcurrent
* @return  int
*/
@Transactional
@Override
public int insert(EventHisEcurrent eventHisEcurrent) {
return super.insert(eventHisEcurrent);
}

/**
* 删除设备历史事件-电流
* @param eventHisEcurrent
* @return int
*/
@Transactional
@Override
public int delete(EventHisEcurrent eventHisEcurrent) {
return super.delete(eventHisEcurrent);
}
}