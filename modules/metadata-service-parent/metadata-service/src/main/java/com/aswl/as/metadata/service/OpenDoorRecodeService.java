package com.aswl.as.metadata.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.OpenDoorRecode;
import com.aswl.as.metadata.mapper.OpenDoorRecodeMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class OpenDoorRecodeService extends CrudService<OpenDoorRecodeMapper, OpenDoorRecode> {

	
	private final OpenDoorRecodeMapper openDoorRecodeMapper;

	/**
	 * 新增开箱记录表
	 *
	 * @param openDoorRecode
	 * @return int
	 */
	@Transactional
	@Override
	public int insert(OpenDoorRecode openDoorRecode) {
		return openDoorRecodeMapper.insert(openDoorRecode);
	}
	
	public List<OpenDoorRecode> findListByDeviceId(String deviceId){
		return openDoorRecodeMapper.findListByDeviceId(deviceId);
	}
}