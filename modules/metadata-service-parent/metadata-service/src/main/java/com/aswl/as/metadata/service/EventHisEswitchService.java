package com.aswl.as.metadata.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventHisEswitch;
import com.aswl.as.metadata.mapper.EventHisEswitchMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventHisEswitchService extends CrudService<EventHisEswitchMapper, EventHisEswitch> {
private final EventHisEswitchMapper eventHisEswitchMapper;

/**
* 新增设备历史事件-电源开关
* @param  eventHisEswitch
* @return  int
*/
@Transactional
@Override
public int insert(EventHisEswitch eventHisEswitch) {
return super.insert(eventHisEswitch);
}

/**
* 删除设备历史事件-电源开关
* @param eventHisEswitch
* @return int
*/
@Transactional
@Override
public int delete(EventHisEswitch eventHisEswitch) {
return super.delete(eventHisEswitch);
}
}