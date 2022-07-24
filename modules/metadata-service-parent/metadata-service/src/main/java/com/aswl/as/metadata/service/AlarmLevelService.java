package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AlarmLevel;
import com.aswl.as.metadata.mapper.AlarmLevelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Slf4j
@Service
public class AlarmLevelService extends CrudService<AlarmLevelMapper, AlarmLevel> {
    private final AlarmLevelMapper alarmLevelMapper;

    /**
     * 新增设备报警级别
     *
     * @param alarmLevel
     * @return int
     */
    @Transactional
    @Override
    public int insert(AlarmLevel alarmLevel) {
        return super.insert(alarmLevel);
    }

    /**
     * 删除设备报警级别
     *
     * @param alarmLevel
     * @return int
     */
    @Transactional
    @Override
    public int delete(AlarmLevel alarmLevel) {
        return super.delete(alarmLevel);
    }

    public List<Map> findAlarmLevel() {
        return alarmLevelMapper.findAlarmLevel();
    }

    public AlarmLevel findByAlarmLevel(Integer alarmLevel, String tenantCode){
        return alarmLevelMapper.findByAlarmLevel(alarmLevel, tenantCode);
    }

    /**
     * 根据alarmType集合获取告警最小级别（最高级）
     * @param alarmTypes
     * @return
     */
    public AlarmLevel loadMinLevelByAlarmTypes(@Param("alarmTypes") List<String> alarmTypes, String tenantCode){
        return alarmLevelMapper.loadMinLevelByAlarmTypes(alarmTypes, tenantCode);
    }
}