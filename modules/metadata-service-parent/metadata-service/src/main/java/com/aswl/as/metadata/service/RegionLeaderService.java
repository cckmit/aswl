package com.aswl.as.metadata.service;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.RegionLeader;
import com.aswl.as.metadata.datasource.annotation.DataSource;
import com.aswl.as.metadata.mapper.RegionLeaderMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class RegionLeaderService extends CrudService<RegionLeaderMapper, RegionLeader> {
    private final RegionLeaderMapper regionLeaderMapper;

    public List<RegionLeader> getRegionLeaderByPatrolKeyId(String patrolKeyId) {
        RegionLeader regionLeader = new RegionLeader();
        // 需要根据租户和项目过滤
        regionLeader.setTenantCode(SysUtil.getTenantCode());
        regionLeader.setProjectId(SysUtil.getProjectId());
        regionLeader.setPatrolKeyId(patrolKeyId);
        return regionLeaderMapper.findList(regionLeader);
    }

    public List<RegionLeader> getRegionLeaderByRegionId(String regionCode) {
        RegionLeader regionLeader = new RegionLeader();
        regionLeader.setRegionCode(regionCode);
        return regionLeaderMapper.findList(regionLeader);
    }
}