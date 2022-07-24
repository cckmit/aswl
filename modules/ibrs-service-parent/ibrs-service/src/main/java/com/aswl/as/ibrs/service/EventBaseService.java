package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventBase;
import com.aswl.as.ibrs.mapper.EventBaseMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Service
public class EventBaseService extends CrudService<EventBaseMapper, EventBase> {
private final EventBaseMapper eventBaseMapper;

/**
* 新增设备事件-基础
* @param  eventBase
* @return  int
*/
@Transactional
@Override
public int insert(EventBase eventBase) {
return super.insert(eventBase);
}

/**
* 删除设备事件-基础
* @param eventBase
* @return int
*/
@Transactional
@Override
public int delete(EventBase eventBase) {
return super.delete(eventBase);
}

    public void deleteByDeviceIds(String[] idsArr) {
        eventBaseMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}