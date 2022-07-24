package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmSettings;
import com.aswl.as.metadata.mapper.AlarmSettingsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmSettingsService extends CrudService<AlarmSettingsMapper, AlarmSettings> {
    private final AlarmSettingsMapper alarmSettingsMapper;

    /**
     * 新增ibrs
     *
     * @param alarmSettings
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmSettings alarmSettings) {
        return super.insert(alarmSettings);
    }

    /**
     * 删除ibrs
     *
     * @param alarmSettings
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmSettings alarmSettings) {
        return super.delete(alarmSettings);
    }

    public AlarmSettings getByTenantCode(String projectId,String tenantCode) {
        return alarmSettingsMapper.getByTenantCode(projectId,tenantCode);
    }
}