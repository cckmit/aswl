package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventEswitch;
import com.aswl.as.ibrs.mapper.EventEswitchMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Service
public class EventEswitchService extends CrudService<EventEswitchMapper, EventEswitch> {
private final EventEswitchMapper eventEswitchMapper;

/**
* 新增设备事件-电源开关
* @param  eventEswitch
* @return  int
*/
@Transactional
@Override
public int insert(EventEswitch eventEswitch) {
return super.insert(eventEswitch);
}

/**
* 删除设备事件-电源开关
* @param eventEswitch
* @return int
*/
@Transactional
@Override
public int delete(EventEswitch eventEswitch) {
return super.delete(eventEswitch);
}

    public void deleteByDeviceIds(String[] idsArr) {
        eventEswitchMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}