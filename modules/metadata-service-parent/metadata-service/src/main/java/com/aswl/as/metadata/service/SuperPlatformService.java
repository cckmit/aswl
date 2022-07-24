package com.aswl.as.metadata.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.SuperPlatform;
import com.aswl.as.metadata.mapper.SuperPlatformMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class SuperPlatformService extends CrudService<SuperPlatformMapper, SuperPlatform> {
    private final SuperPlatformMapper superPlatformMapper;
    
	public List<SuperPlatform> findByRegionCode(String regioncode){
		return superPlatformMapper.findByRegionCode(regioncode);
	}
}