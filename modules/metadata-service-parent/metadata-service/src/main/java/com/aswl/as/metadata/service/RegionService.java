package com.aswl.as.metadata.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.metadata.mapper.RegionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class RegionService extends CrudService<RegionMapper, Region> {
    private final RegionMapper regionMapper;

    public Region findByRegionCode(String regionCode) {
        return regionMapper.findByRegionCode(regionCode);
    }
}