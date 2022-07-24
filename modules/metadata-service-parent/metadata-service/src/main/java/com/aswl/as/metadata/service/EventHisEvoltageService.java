package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.api.module.EventHisEvoltage;
import com.aswl.as.metadata.mapper.EventHisEvoltageMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventHisEvoltageService extends CrudService<EventHisEvoltageMapper, EventHisEvoltage> {
private final EventHisEvoltageMapper eventHisEvoltageMapper;

/**
* 新增设备历史事件-电压
* @param  eventHisEvoltage
* @return  int
*/
@Transactional
@Override
public int insert(EventHisEvoltage eventHisEvoltage) {
return super.insert(eventHisEvoltage);
}

/**
* 删除设备历史事件-电压
* @param eventHisEvoltage
* @return int
*/
@Transactional
@Override
public int delete(EventHisEvoltage eventHisEvoltage) {
return super.delete(eventHisEvoltage);
}
}