package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerSet;
import com.aswl.as.ibrs.mapper.EventSecCtlPowerSetMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventSecCtlPowerSetService extends CrudService<EventSecCtlPowerSetMapper, EventSecCtlPowerSet> {
    private final EventSecCtlPowerSetMapper eventSecCtlPowerSetMapper;

    /**
     * 新增设备分控板-电源设置
     *
     * @param eventSecCtlPowerSet
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventSecCtlPowerSet eventSecCtlPowerSet) {
        return eventSecCtlPowerSetMapper.insert(eventSecCtlPowerSet);
    }

    /**
     * 删除设备分控板-电源设置
     *
     * @param eventSecCtlPowerSet
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventSecCtlPowerSet eventSecCtlPowerSet) {
        return eventSecCtlPowerSetMapper.delete(eventSecCtlPowerSet);
    }
}