package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.LabelScopeDto;
import com.aswl.as.ibrs.api.dto.PeriodScopeDto;
import com.aswl.as.ibrs.api.module.ExamineStatisticsDeduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考核统计扣分Mapper
 */
@Mapper
public interface ExamineStatisticsDeductMapper extends CrudMapper<ExamineStatisticsDeduct> {

    void batchInsert(@Param("deductList") List<ExamineStatisticsDeduct> deductList);

    PeriodScopeDto periodTotalScope(@Param("standardId") String standardId, @Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd);

    /**
     * 每个周期的扣分和扣费
     * @param statisticId
     * @return
     */
    PeriodScopeDto totalScope(@Param("statisticId") String statisticId);

    /**
     * 每个周期的每个指标扣分和扣费
     * @param statisticId
     * @return
     */
    List<LabelScopeDto> LabelScope(@Param("statisticId") String statisticId);
}
