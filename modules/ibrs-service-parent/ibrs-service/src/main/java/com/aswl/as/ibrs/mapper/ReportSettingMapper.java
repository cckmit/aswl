package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.ReportSettingDto;
import com.aswl.as.ibrs.api.vo.ReportSettingVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.ReportSetting;

import java.util.List;

/**
 * 报送人、报送人配置表Mapper
 *
 * @author df
 * @date 2021/07/08 18:12
 */

@Mapper
public interface ReportSettingMapper extends CrudMapper<ReportSetting> {

    /**
     * 分页查询报送人、报送人配置表信息
     * @param reportSettingDto
     * @return
     */
    List<ReportSettingVo> findPageInfo(ReportSettingDto reportSettingDto);

}
