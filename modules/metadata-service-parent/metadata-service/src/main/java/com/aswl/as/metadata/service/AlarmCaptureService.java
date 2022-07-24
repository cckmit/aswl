package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmCapture;
import com.aswl.as.metadata.mapper.AlarmCaptureMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmCaptureService extends CrudService<AlarmCaptureMapper, AlarmCapture> {
    private final AlarmCaptureMapper alarmCaptureMapper;

    /**
     * 新增ibrs
     *
     * @param alarmCapture
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmCapture alarmCapture) {
        return super.insert(alarmCapture);
    }

    /**
     * 删除ibrs
     *
     * @param alarmCapture
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmCapture alarmCapture) {
        return super.delete(alarmCapture);
    }
}