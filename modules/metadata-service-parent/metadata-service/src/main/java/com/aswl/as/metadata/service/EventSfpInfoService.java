package com.aswl.as.metadata.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSfpInfo;
import com.aswl.as.metadata.mapper.EventSfpInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventSfpInfoService extends CrudService<EventSfpInfoMapper, EventSfpInfo> {
private final EventSfpInfoMapper eventSfpInfoMapper;

    /**
    * 新增SFP信息
    * @param  eventSfpInfo
    * @return  int
    */
    @Transactional
    @Override
    public int insert(EventSfpInfo eventSfpInfo) {
    return super.insert(eventSfpInfo);
    }

    /**
    * 删除SFP信息
    * @param eventSfpInfo
    * @return int
    */
    @Transactional
    @Override
    public int delete(EventSfpInfo eventSfpInfo) {
    return super.delete(eventSfpInfo);
    }

    /**
     * 根据设备ID获取SFP信息
     * @param deviceId
     * @return
     */
    public EventSfpInfo findByDeviceId(String deviceId){
        return eventSfpInfoMapper.findByDeviceId(deviceId);
    }
}