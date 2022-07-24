package com.aswl.as.ibrs.mapper;


import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.LabelScopeDto;
import com.aswl.as.ibrs.api.dto.PeriodScopeDto;
import com.aswl.as.ibrs.api.module.ExamineStatistics;
import com.aswl.as.ibrs.api.vo.ExamineStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 考核统计Mapper
 */
@Mapper
public interface ExamineStatisticsMapper extends CrudMapper<ExamineStatistics> {

    ExamineStatistics findByStandardId(@Param("standardId") String standardId,@Param("year") String year,
                                       @Param("periodStart") String periodStart,@Param("periodEnd") String periodEnd);

    /**
     * 批量新增考核统计
     * 每个周期每个考核标准新增一条
     * @param Statistics
     */
    void batchInsert(@Param("Statistics") List<ExamineStatistics> Statistics);

    PeriodScopeDto periodTotalScope(@Param("standardId") String standardId, @Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd);

    List<LabelScopeDto> periodLabelScope(@Param("standardId") String standardId,@Param("periodStart") String periodStart,@Param("periodEnd") String periodEnd);

    List<ExamineStatistics> getByStandardIDAndYear(@Param("standardId") String standardId,@Param("year") String year,@Param("regionCode") String regionCode);

    ExamineStatistics findById(@Param("statisticsId") String statisticsId);
}
