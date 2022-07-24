package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmTerminal;
import com.aswl.as.ibrs.mapper.AlarmTerminalMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmTerminalService extends CrudService<AlarmTerminalMapper, AlarmTerminal> {
    private final AlarmTerminalMapper alarmTerminalMapper;

    /**
     * 新增报警终端设备
     *
     * @param alarmTerminal
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmTerminal alarmTerminal) {
        return super.insert(alarmTerminal);
    }

    /**
     * 删除报警终端设备
     *
     * @param alarmTerminal
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmTerminal alarmTerminal) {
        return super.delete(alarmTerminal);
    }
}