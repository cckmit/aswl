package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.DeviceModelAlarmThreshold;
import com.aswl.as.metadata.mapper.DeviceModelAlarmThresholdMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceModelAlarmThresholdService extends CrudService<DeviceModelAlarmThresholdMapper, DeviceModelAlarmThreshold> {
	
	 private final DeviceModelAlarmThresholdMapper deviceModelAlarmThresholdMapper;
	
	public DeviceModelAlarmThreshold findStatusValueByCode(String deviceModelId,Double statusValue,String tabCode,String fldCode) {
		
		return deviceModelAlarmThresholdMapper.findStatusValueByCode(deviceModelId, statusValue, tabCode, fldCode);
	}
   
}