package com.aswl.as.metadata.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.metadata.api.module.EventUcId;
import com.aswl.as.metadata.mapper.EventUcIdMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcIdService extends CrudService<EventUcIdMapper, EventUcId> {
private final EventUcIdMapper eventUcIdMapper;

/**
* 新增事件统一ID自增
* @param  eventUcId
* @return  int
*/
@Transactional
@Override
public int insert(EventUcId eventUcId) {
return super.insert(eventUcId);
}

/**
* 删除事件统一ID自增
* @param eventUcId
* @return int
*/
@Transactional
@Override
public int delete(EventUcId eventUcId) {
return super.delete(eventUcId);
}
}