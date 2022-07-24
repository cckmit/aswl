package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.ReportStatisticsColorSetting;
import com.aswl.as.ibrs.mapper.ReportStatisticsColorSettingMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ReportStatisticsColorSettingService extends CrudService<ReportStatisticsColorSettingMapper, ReportStatisticsColorSetting> {
    private final ReportStatisticsColorSettingMapper reportStatisticsColorSettingMapper;

    /**
     * 新增图形色标设置表
     *
     * @param reportStatisticsColorSetting
     * @return int
     */
    @Transactional
    @Override
    public int insert(ReportStatisticsColorSetting reportStatisticsColorSetting) {
        return reportStatisticsColorSettingMapper.insert(reportStatisticsColorSetting);
    }

    /**
     * 删除图形色标设置表
     *
     * @param reportStatisticsColorSetting
     * @return int
     */
    @Transactional
    @Override
    public int delete(ReportStatisticsColorSetting reportStatisticsColorSetting) {
        return reportStatisticsColorSettingMapper.delete(reportStatisticsColorSetting);
    }

    /**
     * 批量保存图形色标设置
     * @param reportStatisticsColorSettingList
     * @return
     */
    @Transactional
    public int batchSave(List<ReportStatisticsColorSetting> reportStatisticsColorSettingList){
        int result = 0;
        for(ReportStatisticsColorSetting reportStatisticsColorSetting : reportStatisticsColorSettingList){
            if(StringUtils.isEmpty(reportStatisticsColorSetting.getId())){
                reportStatisticsColorSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                result = result + this.insert(reportStatisticsColorSetting);
            }else{
                reportStatisticsColorSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), reportStatisticsColorSetting.getTenantCode());
                result = result + this.update(reportStatisticsColorSetting);
            }
        }
        return result;
    }
}