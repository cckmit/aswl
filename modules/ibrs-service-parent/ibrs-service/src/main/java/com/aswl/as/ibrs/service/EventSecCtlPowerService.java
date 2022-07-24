package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSecCtlPower;
import com.aswl.as.ibrs.mapper.EventSecCtlPowerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventSecCtlPowerService extends CrudService<EventSecCtlPowerMapper, EventSecCtlPower> {
    private final EventSecCtlPowerMapper eventSecCtlPowerMapper;

    /**
     * 新增设备分控板-电源
     *
     * @param eventSecCtlPower
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventSecCtlPower eventSecCtlPower) {
        return eventSecCtlPowerMapper.insert(eventSecCtlPower);
    }

    /**
     * 删除设备分控板-电源
     *
     * @param eventSecCtlPower
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventSecCtlPower eventSecCtlPower) {
        return eventSecCtlPowerMapper.delete(eventSecCtlPower);
    }
}