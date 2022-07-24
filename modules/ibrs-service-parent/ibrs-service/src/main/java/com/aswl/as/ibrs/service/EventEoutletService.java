package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventEoutlet;
import com.aswl.as.ibrs.mapper.EventEoutletMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Service
public class EventEoutletService extends CrudService<EventEoutletMapper, EventEoutlet> {
private final EventEoutletMapper eventEoutletMapper;

/**
* 新增设备事件-电源电口
* @param  eventEoutlet
* @return  int
*/
@Transactional
@Override
public int insert(EventEoutlet eventEoutlet) {
return super.insert(eventEoutlet);
}

/**
* 删除设备事件-电源电口
* @param eventEoutlet
* @return int
*/
@Transactional
@Override
public int delete(EventEoutlet eventEoutlet) {
return super.delete(eventEoutlet);
}

    public void deleteByDeviceIds(String[] idsArr) {
        eventEoutletMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}