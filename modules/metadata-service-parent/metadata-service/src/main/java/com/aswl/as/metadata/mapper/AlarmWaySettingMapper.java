package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmWaySetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * 告警方式设置表Mapper
 *
 * @author df
 * @date 2021/07/10 17:16
 */

@Mapper
public interface AlarmWaySettingMapper extends CrudMapper<AlarmWaySetting> {

}
