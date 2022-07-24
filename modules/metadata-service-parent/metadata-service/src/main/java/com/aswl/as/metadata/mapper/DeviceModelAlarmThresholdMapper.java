package com.aswl.as.metadata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceModelAlarmThreshold;

@Mapper
public interface DeviceModelAlarmThresholdMapper extends CrudMapper<DeviceModelAlarmThreshold> {
	
	
	DeviceModelAlarmThreshold findStatusValueByCode(@Param("deviceModelId") String deviceModelId,@Param("statusValue") Double statusValue, @Param("tabCode") String tabCode, @Param("fldCode") String fldCode);
}
