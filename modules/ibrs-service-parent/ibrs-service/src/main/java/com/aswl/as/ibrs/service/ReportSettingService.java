package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.ReportSettingDto;
import com.aswl.as.ibrs.api.module.ReportSetting;
import com.aswl.as.ibrs.api.vo.ReportSettingVo;
import com.aswl.as.ibrs.mapper.ReportSettingMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ReportSettingService extends CrudService<ReportSettingMapper, ReportSetting> {
    private final ReportSettingMapper reportSettingMapper;


    /**
     * 分页查询新增报送人、报送人配置表信息
     *
     * @param page
     * @param reportSettingDto
     * @return ReportSettingVo
     */
    public PageInfo<ReportSettingVo> findPage(PageInfo<ReportSettingDto> page, ReportSettingDto reportSettingDto) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(reportSettingMapper.findPageInfo(reportSettingDto));
    }

    /**
     * 新增报送人、报送人配置表
     *
     * @param reportSetting
     * @return int
     */
    @Transactional
    @Override
    public int insert(ReportSetting reportSetting) {
        return reportSettingMapper.insert(reportSetting);
    }

    /**
     * 删除报送人、报送人配置表
     *
     * @param reportSetting
     * @return int
     */
    @Transactional
    @Override
    public int delete(ReportSetting reportSetting) {
        return reportSettingMapper.delete(reportSetting);
    }


    /**
     * 批量新增报送人、报送人配置表信息
     *
     * @param list list
     * @return int
     */
    @Transactional
    public int insertBath(List<ReportSetting> list) {
        int update = 0;
        // 修改/新增数据
        for (ReportSetting reportSetting : list) {
            // 如果传过来的id为空、则添加
            if (reportSetting.getId() == null) {
                reportSetting.setCommonValue(SysUtil.getUser(), SysUtil.getSysCode(), SysUtil.getTenantCode());
                reportSettingMapper.insert(reportSetting);
                update++;
            } else {
                reportSettingMapper.update(reportSetting);
                update++;
            }
        }
        return update;
    }
}