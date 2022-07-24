package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSfp;
import com.aswl.as.ibrs.mapper.EventSfpMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@AllArgsConstructor
@Slf4j
@Service
public class EventSfpService extends CrudService<EventSfpMapper, EventSfp> {
private final EventSfpMapper eventSfpMapper;

/**
* 新增设备事件-光口
* @param  eventSfp
* @return  int
*/
@Transactional
@Override
public int insert(EventSfp eventSfp) {
return super.insert(eventSfp);
}

/**
* 删除设备事件-光口
* @param eventSfp
* @return int
*/
@Transactional
@Override
public int delete(EventSfp eventSfp) {
return super.delete(eventSfp);
}

    public void deleteByDeviceIds(String[] idsArr) {
            eventSfpMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}