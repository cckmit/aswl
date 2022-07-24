package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.api.module.EventHisNetwork;
import com.aswl.as.metadata.mapper.EventHisNetworkMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventHisNetworkService extends CrudService<EventHisNetworkMapper, EventHisNetwork> {
private final EventHisNetworkMapper eventHisNetworkMapper;

/**
* 新增设备历史事件-网络
* @param  eventHisNetwork
* @return  int
*/
@Transactional
@Override
public int insert(EventHisNetwork eventHisNetwork) {
return super.insert(eventHisNetwork);
}

/**
* 删除设备历史事件-网络
* @param eventHisNetwork
* @return int
*/
@Transactional
@Override
public int delete(EventHisNetwork eventHisNetwork) {
return super.delete(eventHisNetwork);
}
}