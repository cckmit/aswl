package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.CityOnlineStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 市级平台在线统计表Mapper
 *
 * @author hzj
 * @date 2021/01/23 17:26
 */

@Mapper
public interface CityOnlineStatisticsMapper extends CrudMapper<CityOnlineStatistics> {

    CityOnlineStatistics findByCondition(@Param("statisticsDate") Date statisticsDate, @Param("deviceKind") String deviceKind,
                                               @Param("cityCode") String cityCode, @Param("projectId") String projectId, @Param("deviceModelId") String deviceModelId);

    Map onlineRate(CityAlarmStatisticsDto cityAlarmStatisticsDto);
}
