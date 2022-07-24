package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.module.CityRunTimeStatistics;
import com.aswl.as.ibrs.mapper.CityRunTimeStatisticsMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@AllArgsConstructor
@Slf4j
@Service
public class CityRunTimeStatisticsService extends CrudService<CityRunTimeStatisticsMapper, CityRunTimeStatistics> {
    private final CityRunTimeStatisticsMapper cityRunTimeStatisticsMapper;

    /**
     * 新增市级平台各时段工单趋势统计
     *
     * @param cityRunTimeStatistics
     * @return int
     */
    @Transactional
    @Override
    public int insert(CityRunTimeStatistics cityRunTimeStatistics) {
        return super.insert(cityRunTimeStatistics);
    }

    /**
     * 删除市级平台各时段工单趋势统计
     *
     * @param cityRunTimeStatistics
     * @return int
     */
    @Transactional
    @Override
    public int delete(CityRunTimeStatistics cityRunTimeStatistics) {
        return super.delete(cityRunTimeStatistics);
    }

    /**
     *
     * @param cityCode
     * @param deviceKind
     * @param statisticsDate
     * @param projectId
     * @return
     */
    public CityRunTimeStatistics findByCondition(String cityCode, String deviceKind, Date statisticsDate, String projectId) {
        return cityRunTimeStatisticsMapper.findByCondition(cityCode,deviceKind,statisticsDate,projectId);
    }

    /**
     * 个时间段工单
     * @param cityAlarmStatisticsDto
     * @return
     */
    public CityRunTimeStatistics periodRun(CityAlarmStatisticsDto cityAlarmStatisticsDto) {
        return cityRunTimeStatisticsMapper.periodRun(cityAlarmStatisticsDto);
    }
}