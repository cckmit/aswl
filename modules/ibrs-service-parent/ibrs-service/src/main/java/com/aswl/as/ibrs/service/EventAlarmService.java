package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventAlarm;
import com.aswl.as.ibrs.mapper.EventAlarmMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Service
public class EventAlarmService extends CrudService<EventAlarmMapper, EventAlarm> {
private final EventAlarmMapper eventAlarmMapper;

/**
* 新增设备事件-报警
* @param  eventAlarm
* @return  int
*/
@Transactional
@Override
public int insert(EventAlarm eventAlarm) {
return super.insert(eventAlarm);
}

/**
* 删除设备事件-报警
* @param eventAlarm
* @return int
*/
@Transactional
@Override
public int delete(EventAlarm eventAlarm) {
return super.delete(eventAlarm);
}

    public void deleteByDeviceIds(String[] idsArr) {
        eventAlarmMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}