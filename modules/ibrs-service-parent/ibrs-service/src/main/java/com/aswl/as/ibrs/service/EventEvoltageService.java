package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventEvoltage;
import com.aswl.as.ibrs.mapper.EventEvoltageMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Service
public class EventEvoltageService extends CrudService<EventEvoltageMapper, EventEvoltage> {
private final EventEvoltageMapper eventEvoltageMapper;

/**
* 新增设备事件-电压
* @param  eventEvoltage
* @return  int
*/
@Transactional
@Override
public int insert(EventEvoltage eventEvoltage) {
return super.insert(eventEvoltage);
}

/**
* 删除设备事件-电压
* @param eventEvoltage
* @return int
*/
@Transactional
@Override
public int delete(EventEvoltage eventEvoltage) {
return super.delete(eventEvoltage);
}

    public void deleteByDeviceIds(String[] idsArr) {
        eventEvoltageMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}