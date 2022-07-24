package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.AlarmSettingsDto;
import com.aswl.as.ibrs.api.module.AlarmSettings;
import com.aswl.as.ibrs.api.vo.AlarmSettingsVo;
import com.aswl.as.ibrs.mapper.AlarmSettingsMapper;
import com.aswl.as.ibrs.utils.BeanUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

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

    public AlarmSettings getByTenantCode(String projectId, String tenantCode) {
        return alarmSettingsMapper.getByTenantCode(projectId,tenantCode);
    }

    /**
     * 获取项目告警设置信息列表
     * @param alarmSettings
     * @return list
     */
    public List<AlarmSettingsVo> getProjectAlarmSettings(AlarmSettings alarmSettings){
        return alarmSettingsMapper.getProjectAlarmSettings(alarmSettings);
    }

    /**
     * 新增修改告警设置列表
     *
     * @param alarmSettingsDto
     * @return int
     */
    @Transactional
    public int inserUpdateAlarmSettings(AlarmSettingsDto alarmSettingsDto) {
        int update = 0;
        // 修改/新增数据
            // 如果传过来的id为空、则添加
            if (alarmSettingsDto.getId() == null) {
                AlarmSettings alarmSettings = new AlarmSettings();
                BeanUtils.copyProperties(alarmSettingsDto,alarmSettings);
                alarmSettings.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),alarmSettings.getProjectId());
                alarmSettings.setStoreTime(new Date());
                alarmSettingsMapper.insert(alarmSettings);
                update++;
            } else {
                AlarmSettings alarmSettings = new AlarmSettings();
                BeanUtils.copyProperties(alarmSettingsDto,alarmSettings);
                alarmSettingsMapper.update(alarmSettings);
                update++;
            }
        return update;
    }
}