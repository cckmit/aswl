package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.api.module.EventHisAlarm;
import com.aswl.as.metadata.mapper.EventHisAlarmMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventHisAlarmService extends CrudService<EventHisAlarmMapper, EventHisAlarm> {
private final EventHisAlarmMapper eventHisAlarmMapper;

/**
* 新增设备历史事件-报警
* @param  eventHisAlarm
* @return  int
*/
@Transactional
@Override
public int insert(EventHisAlarm eventHisAlarm) {
return super.insert(eventHisAlarm);
}

/**
* 删除设备历史事件-报警
* @param eventHisAlarm
* @return int
*/
@Transactional
@Override
public int delete(EventHisAlarm eventHisAlarm) {
return super.delete(eventHisAlarm);
}
}