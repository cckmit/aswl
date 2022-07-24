package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.api.module.EventHisSfp;
import com.aswl.as.metadata.mapper.EventHisSfpMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventHisSfpService extends CrudService<EventHisSfpMapper, EventHisSfp> {
private final EventHisSfpMapper eventHisSfpMapper;

/**
* 新增设备历史事件-光口
* @param  eventHisSfp
* @return  int
*/
@Transactional
@Override
public int insert(EventHisSfp eventHisSfp) {
return super.insert(eventHisSfp);
}

/**
* 删除设备历史事件-光口
* @param eventHisSfp
* @return int
*/
@Transactional
@Override
public int delete(EventHisSfp eventHisSfp) {
return super.delete(eventHisSfp);
}
}