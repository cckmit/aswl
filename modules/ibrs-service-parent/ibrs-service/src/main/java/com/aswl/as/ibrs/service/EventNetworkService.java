package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.module.CityAlarmStatistics;
import com.aswl.as.ibrs.api.module.CityOnlineStatistics;
import com.aswl.as.ibrs.api.module.DeviceEventAlarm;
import com.aswl.as.ibrs.api.module.EventNetwork;
import com.aswl.as.ibrs.mapper.EventNetworkMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class EventNetworkService extends CrudService<EventNetworkMapper, EventNetwork> {
private final EventNetworkMapper eventNetworkMapper;

/**
* 新增设备事件-网络
* @param  eventNetwork
* @return  int
*/
@Transactional
@Override
public int insert(EventNetwork eventNetwork) {
return super.insert(eventNetwork);
}

/**
* 删除设备事件-网络
* @param eventNetwork
* @return int
*/
@Transactional
@Override
public int delete(EventNetwork eventNetwork) {
return super.delete(eventNetwork);
}

    /**
     * 在线数
     * @param type
     * @return
     */
    public List<CityOnlineStatistics > onlineCount(String type, String day,String hisTable) {
        return eventNetworkMapper.onlineCount(type,day,hisTable);
    }

    public EventNetwork findByDeviceId(String deviceId) {
        return eventNetworkMapper.findByDeviceId(deviceId);
    }

    public void deleteByDeviceIds(String[] idsArr) {
        eventNetworkMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}