package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.CityOnlineStatistics;
import com.aswl.as.ibrs.mapper.CityOnlineStatisticsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class CityOnlineStatisticsService extends CrudService<CityOnlineStatisticsMapper, CityOnlineStatistics> {
    private final CityOnlineStatisticsMapper cityOnlineStatisticsMapper;

    /**
     * 新增市级平台在线统计表
     *
     * @param cityOnlineStatistics
     * @return int
     */
    @Transactional
    @Override
    public int insert(CityOnlineStatistics cityOnlineStatistics) {
        return super.insert(cityOnlineStatistics);
    }

    /**
     * 删除市级平台在线统计表
     *
     * @param cityOnlineStatistics
     * @return int
     */
    @Transactional
    @Override
    public int delete(CityOnlineStatistics cityOnlineStatistics) {
        return super.delete(cityOnlineStatistics);
    }

    public CityOnlineStatistics findByCondition(Date statisticsDate, String deviceKind, String cityCode, String projectId, String deviceModelId) {
        return cityOnlineStatisticsMapper.findByCondition(statisticsDate,deviceKind,cityCode,projectId,deviceModelId);
    }
}