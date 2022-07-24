package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.ThresholdSetting;
import com.aswl.as.ibrs.mapper.ThresholdSettingMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ThresholdSettingService extends CrudService<ThresholdSettingMapper, ThresholdSetting> {
    private final ThresholdSettingMapper thresholdSettingMapper;

    /**
     * 新增阈值设置表
     *
     * @param thresholdSetting
     * @return int
     */
    @Transactional
    @Override
    public int insert(ThresholdSetting thresholdSetting) {
        return thresholdSettingMapper.insert(thresholdSetting);
    }

    /**
     * 删除阈值设置表
     *
     * @param thresholdSetting
     * @return int
     */
    @Transactional
    @Override
    public int delete(ThresholdSetting thresholdSetting) {
        return thresholdSettingMapper.delete(thresholdSetting);
    }


    /**
     * 批量保存更新阈值设置表
     *
     * @param list list
     * @return int
     */
    @Transactional
    public int insertBath(List<ThresholdSetting> list) {
        int update = 0 ;
        // 修改/新增数据
        for (ThresholdSetting thresholdSetting : list) {
            // 如果传过来的id为空、则添加
            if (thresholdSetting.getId() == null) {
                thresholdSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode(),SysUtil.getProjectId());
                thresholdSettingMapper.insert(thresholdSetting);
                update++;
            }else{
                thresholdSettingMapper.update(thresholdSetting);
                update ++ ;
            }
        }
        return update;
    }
}