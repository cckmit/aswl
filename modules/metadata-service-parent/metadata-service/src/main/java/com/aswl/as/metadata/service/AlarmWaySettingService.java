package com.aswl.as.metadata.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.AlarmWaySetting;
import com.aswl.as.metadata.mapper.AlarmWaySettingMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmWaySettingService extends CrudService<AlarmWaySettingMapper, AlarmWaySetting> {
    private final AlarmWaySettingMapper alarmWaySettingMapper;

    /**
     * 新增告警方式设置表
     *
     * @param alarmWaySetting
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmWaySetting alarmWaySetting) {
        return alarmWaySettingMapper.insert(alarmWaySetting);
    }

    /**
     * 删除告警方式设置表
     *
     * @param alarmWaySetting
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmWaySetting alarmWaySetting) {
        return alarmWaySettingMapper.delete(alarmWaySetting);
    }

    /**
     * 根据项目ID和告警级别获取告警方式设置
     * @param projectId
     * @param alarmLevel
     * @return
     */
    public AlarmWaySetting findByProjectIdAndAlarmLevel(String projectId, int alarmLevel){
        AlarmWaySetting alarmWaySetting = new AlarmWaySetting();
        alarmWaySetting.setProjectId(projectId);
        alarmWaySetting.setAlarmLevel(alarmLevel);
        List<AlarmWaySetting> alarmWaySettingList = alarmWaySettingMapper.findList(alarmWaySetting);
        return (alarmWaySettingList != null && alarmWaySettingList.size() > 0) ? alarmWaySettingList.get(0) : null;
    }
}