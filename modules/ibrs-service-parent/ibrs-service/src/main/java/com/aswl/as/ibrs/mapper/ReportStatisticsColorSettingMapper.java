package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.ReportStatisticsColorSetting;

/**
 * 图形色标设置表Mapper
 *
 * @author df
 * @date 2021/07/12 16:54
 */

@Mapper
public interface ReportStatisticsColorSettingMapper extends CrudMapper<ReportStatisticsColorSetting> {

}
