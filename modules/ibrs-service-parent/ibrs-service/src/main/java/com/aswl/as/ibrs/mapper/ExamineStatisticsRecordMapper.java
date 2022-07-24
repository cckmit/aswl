package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.DeductScopeDto;
import com.aswl.as.ibrs.api.module.ExamineStatisticsRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 考核统计扣分记录Mapper
 */
@Mapper
public interface ExamineStatisticsRecordMapper extends CrudMapper<ExamineStatisticsRecord> {

    void batchInsert(@Param("recordList") List<ExamineStatisticsRecord> recordList);

    List<ExamineStatisticsRecord> getByPeriodAndStandardId(@Param("standardId") String standardId,
                                                           @Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd);

    List<DeductScopeDto> findDeductDetails(@Param("relateId") String relateId, @Param("hisTables") List<String> hisTables,
                                                       @Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd,
                                                   @Param("standardId") String standardId);

    List<DeductScopeDto> findDeductDetailsWithFlowRun(@Param("relateId") String relateId, @Param("periodStart") String periodStart, @Param("periodEnd") String periodEnd,
                                                              @Param("standardId") String standardId,@Param("itemId") String itemId);

    List<ExamineStatisticsRecord> detailsRecord(@Param("statisticId") String statisticId);

    List<DeductScopeDto> findDeductDetailsNoRecord(@Param("relateId") String relateId, @Param("hisTables") List<String> hisTables);

    List<DeductScopeDto> findDeductDetailsFlowRun(@Param("relateId") String relateId);
}
