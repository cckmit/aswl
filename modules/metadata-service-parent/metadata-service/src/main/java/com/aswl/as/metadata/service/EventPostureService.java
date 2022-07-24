package com.aswl.as.metadata.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventPosture;
import com.aswl.as.metadata.mapper.EventPostureMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventPostureService extends CrudService<EventPostureMapper, EventPosture> {
private final EventPostureMapper eventPostureMapper;

    /**
    * 新增设备事件-姿态信息
    * @param  eventPosture
    * @return  int
    */
    @Transactional
    @Override
    public int insert(EventPosture eventPosture) {
    return super.insert(eventPosture);
    }

    /**
    * 删除设备事件-姿态信息
    * @param eventPosture
    * @return int
    */
    @Transactional
    @Override
    public int delete(EventPosture eventPosture) {
    return super.delete(eventPosture);
    }

    /**
     * 根据设备ID获取姿态信息
     * @param deviceId
     * @return
     */
    public EventPosture findByDeviceId(String deviceId){
        return eventPostureMapper.findByDeviceId(deviceId);
    }
}