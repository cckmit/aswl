package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.AlarmWaySetting;
import com.aswl.as.ibrs.mapper.AlarmWaySettingMapper;
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
     * 批量保存更新告警方式设置表
     *
     * @param list list
     * @return int
     */
    @Transactional
    public int insertBath(List<AlarmWaySetting> list) {
        int update = 0 ;
        // 修改/新增数据
        for (AlarmWaySetting alarmWaySetting : list) {
            // 如果传过来的id为空、则添加
            if (alarmWaySetting.getId() == null) {
                alarmWaySetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),alarmWaySetting.getProjectId());
                alarmWaySettingMapper.insert(alarmWaySetting);
                update++;
            }else{
                alarmWaySettingMapper.update(alarmWaySetting);
                update ++ ;
            }
        }
        return update;
    }
}