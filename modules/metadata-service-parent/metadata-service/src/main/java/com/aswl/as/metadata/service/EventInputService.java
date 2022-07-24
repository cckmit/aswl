package com.aswl.as.metadata.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventInput;
import com.aswl.as.metadata.mapper.EventInputMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventInputService extends CrudService<EventInputMapper, EventInput> {
    private final EventInputMapper eventInputMapper;

    /**
     * 新增设备事件-输入
     *
     * @param eventInput
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventInput eventInput) {
        return eventInputMapper.insert(eventInput);
    }

    /**
     * 删除设备事件-输入
     *
     * @param eventInput
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventInput eventInput) {
        return eventInputMapper.delete(eventInput);
    }

    /**
     * 通过设备ID获取设备事件-输入信息
     * @param deviceId
     * @return EventInput
     */

   public  EventInput findByDeviceId( String deviceId){
       return eventInputMapper.findByDeviceId(deviceId);
    }
    
}