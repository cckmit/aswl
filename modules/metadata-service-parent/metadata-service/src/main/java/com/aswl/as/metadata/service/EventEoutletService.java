package com.aswl.as.metadata.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventEoutlet;
import com.aswl.as.metadata.mapper.EventEoutletMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@Slf4j
@Service
public class EventEoutletService extends CrudService<EventEoutletMapper, EventEoutlet> {
    private final EventEoutletMapper eventEoutletMapper;

    /**
     * 新增设备事件-电源电口
     *
     * @param eventEoutlet
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventEoutlet eventEoutlet) {
        return super.insert(eventEoutlet);
    }

    /**
     * 删除设备事件-电源电口
     *
     * @param eventEoutlet
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventEoutlet eventEoutlet) {
        return super.delete(eventEoutlet);
    }
    /**
     * 更新设备事件-电源电口
     *
     * @param eventEoutlet
     * @return int
     */
    @Transactional
    @Override
    public int update(EventEoutlet eventEoutlet) {
        return eventEoutletMapper.update(eventEoutlet);
    }

    /**
     * 根据deviceId查询
     * @param deviceId
     * @return
     */
    public EventEoutlet findByDeviceId(String deviceId) {
        return eventEoutletMapper.findByDeviceId(deviceId);
    }

    /**
     * 有改变的电口状态
     * @param keySet
     * @return
     */
    public Map<String,String> findOutletState(Set<String> keySet,String deviceId) {
        return eventEoutletMapper.findOutletState(keySet,deviceId);
    }

    /**
     * 修改电口启用/禁用状态
     * @param fld
     * @param value
     * @param deviceId
     * @return
     */
   public int updateByDeviceIdAndFld(String fld , String value, String deviceId){
       return eventEoutletMapper.updateByDeviceIdAndFld(fld,value,deviceId);
   }
}