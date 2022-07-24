package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmWay;
import com.aswl.as.ibrs.mapper.AlarmWayMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmWayService extends CrudService<AlarmWayMapper, AlarmWay> {
    private final AlarmWayMapper alarmWayMapper;

    /**
     * 新增报警方式
     *
     * @param alarmWay
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmWay alarmWay) {
        return super.insert(alarmWay);
    }

    /**
     * 删除报警方式
     *
     * @param alarmWay
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmWay alarmWay) {
        return super.delete(alarmWay);
    }
}