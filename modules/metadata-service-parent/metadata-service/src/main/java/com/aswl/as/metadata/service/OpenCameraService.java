package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.OpenCamera;
import com.aswl.as.metadata.mapper.OpenCameraMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class OpenCameraService extends CrudService<OpenCameraMapper, OpenCamera> {
	
	private final OpenCameraMapper openCameraMapper;
	
	public OpenCamera findOpenCameraByDeviceId(String deviedId) {
		return openCameraMapper.findOpenCameraByDeviceId(deviedId);
	}
}