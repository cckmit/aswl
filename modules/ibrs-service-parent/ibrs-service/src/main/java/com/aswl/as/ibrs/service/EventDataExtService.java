package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventDataExt;
import com.aswl.as.ibrs.mapper.EventDataExtMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class EventDataExtService extends CrudService<EventDataExtMapper, EventDataExt> {
private final EventDataExtMapper eventDataExtMapper;

    /**
    * 新增设备事件数据扩展
    * @param  eventDataExt
    * @return  int
    */
    @Transactional
    @Override
    public int insert(EventDataExt eventDataExt) {
    return super.insert(eventDataExt);
    }

    /**
    * 删除设备事件数据扩展
    * @param eventDataExt
    * @return int
    */
    @Transactional
    @Override
    public int delete(EventDataExt eventDataExt) {
    return super.delete(eventDataExt);
    }

    /**
     * 根据设备ID获取数据
     * @param deviceId
     * @return
     */
    public EventDataExt findByDeviceId(String deviceId){
        EventDataExt eventDataExtSearch = new EventDataExt();
        eventDataExtSearch.setDeviceId(deviceId);
        List<EventDataExt> eventDataExtList  = this.findList(eventDataExtSearch);
        return (eventDataExtList != null && eventDataExtList.size() > 0) ? eventDataExtList.get(0) : null;
    }
}