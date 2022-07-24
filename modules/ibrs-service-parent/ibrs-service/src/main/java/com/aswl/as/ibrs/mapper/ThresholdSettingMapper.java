package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.ThresholdSetting;

/**
*
* 阈值设置表Mapper
* @author df
* @date 2021/09/28 09:47
*/

@Mapper
public interface ThresholdSettingMapper extends CrudMapper<ThresholdSetting> {

}
