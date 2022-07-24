package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.CityRunTimeStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 市级平台各时段工单趋势统计Mapper
 *
 * @author hzj
 * @date 2021/01/22 15:57
 */

@Mapper
public interface CityRunTimeStatisticsMapper extends CrudMapper<CityRunTimeStatistics> {

    CityRunTimeStatistics findByCondition(@Param("cityCode") String cityCode, @Param("deviceKind") String deviceKind,
                                          @Param("statisticsDate") Date statisticsDate, @Param("projectId") String projectId);

    /**
     * 时间段工单数
     * @param cityAlarmStatisticsDto
     * @return
     */
    CityRunTimeStatistics periodRun(CityAlarmStatisticsDto cityAlarmStatisticsDto);

}
