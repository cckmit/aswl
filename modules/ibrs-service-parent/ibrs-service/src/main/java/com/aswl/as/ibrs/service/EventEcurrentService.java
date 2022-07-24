package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventEcurrent;
import com.aswl.as.ibrs.mapper.EventEcurrentMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Service
public class EventEcurrentService extends CrudService<EventEcurrentMapper, EventEcurrent> {
private final EventEcurrentMapper eventEcurrentMapper;

/**
* 新增设备事件-电源电流
* @param  eventEcurrent
* @return  int
*/
@Transactional
@Override
public int insert(EventEcurrent eventEcurrent) {
return super.insert(eventEcurrent);
}

/**
* 删除设备事件-电源电流
* @param eventEcurrent
* @return int
*/
@Transactional
@Override
public int delete(EventEcurrent eventEcurrent) {
return super.delete(eventEcurrent);
}

    public void deleteByDeviceIds(String[] idsArr) {
        eventEcurrentMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}