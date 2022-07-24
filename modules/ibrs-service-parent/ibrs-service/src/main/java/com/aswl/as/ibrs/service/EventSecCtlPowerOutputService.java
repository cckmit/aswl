package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerOutput;
import com.aswl.as.ibrs.mapper.EventSecCtlPowerOutputMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventSecCtlPowerOutputService extends CrudService<EventSecCtlPowerOutputMapper, EventSecCtlPowerOutput> {
    private final EventSecCtlPowerOutputMapper eventSecCtlPowerOutputMapper;

    /**
     * 新增设备分控板-电源输出
     *
     * @param eventSecCtlPowerOutput
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventSecCtlPowerOutput eventSecCtlPowerOutput) {
        return eventSecCtlPowerOutputMapper.insert(eventSecCtlPowerOutput);
    }

    /**
     * 删除设备分控板-电源输出
     *
     * @param eventSecCtlPowerOutput
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventSecCtlPowerOutput eventSecCtlPowerOutput) {
        return eventSecCtlPowerOutputMapper.delete(eventSecCtlPowerOutput);
    }
}