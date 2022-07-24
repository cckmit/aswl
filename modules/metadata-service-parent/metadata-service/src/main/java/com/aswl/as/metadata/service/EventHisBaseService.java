package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.api.module.EventHisBase;
import com.aswl.as.metadata.mapper.EventHisBaseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventHisBaseService extends CrudService<EventHisBaseMapper, EventHisBase> {
private final EventHisBaseMapper eventHisBaseMapper;

/**
* 新增设备历史事件-基础
* @param  eventHisBase
* @return  int
*/
@Transactional
@Override
public int insert(EventHisBase eventHisBase) {
return super.insert(eventHisBase);
}

/**
* 删除设备历史事件-基础
* @param eventHisBase
* @return int
*/
@Transactional
@Override
public int delete(EventHisBase eventHisBase) {
return super.delete(eventHisBase);
}
}