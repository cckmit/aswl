package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.DeductScopeDto;
import com.aswl.as.ibrs.api.module.ExamineStatisticsRecord;
import com.aswl.as.ibrs.mapper.ExamineStatisticsRecordMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 考核统计扣分记录Service
 */
@Service
@AllArgsConstructor
@Slf4j
public class ExamineStatisticsRecordService extends CrudService<ExamineStatisticsRecordMapper,ExamineStatisticsRecord> {
    private final ExamineStatisticsRecordMapper examineStatisticsRecordMapper;

    public void batchInsert(List<ExamineStatisticsRecord> recordList) {
        examineStatisticsRecordMapper.batchInsert(recordList);
    }

    /**
     * 当前周期所有的扣分记录
     * @param standardId
     * @param periodStart
     * @param periodEnd
     * @return
     */
    public List<ExamineStatisticsRecord> getByPeriodAndStandardId(String standardId, String periodStart, String periodEnd) {
        return examineStatisticsRecordMapper.getByPeriodAndStandardId(standardId,periodStart,periodEnd);
    }

    /**
     * 未巡更的关联扣分明细
     * @param relateId
     * @param hisTables
     * @param periodStart
     * @param periodEnd
     * @return
     */
    public List<DeductScopeDto> findDeductDetailsWithNoRecord(String relateId, List<String> hisTables, String periodStart, String periodEnd,String standardId) {
        return examineStatisticsRecordMapper.findDeductDetails(relateId, hisTables, periodStart, periodEnd,standardId);
    }

    /**
     * 工单的扣分详细
     * @param relateId
     * @param periodStart
     * @param periodEnd
     * @return
     */
    public List<DeductScopeDto> findDeductDetailsWithFlowRun(String relateId, String periodStart, String periodEnd,String standardId,String itemId) {
        return examineStatisticsRecordMapper.findDeductDetailsWithFlowRun(relateId,periodStart,periodEnd,standardId,itemId);
    }

    /**
     * 某个周期内的详细扣分记录
     * @param statisticId
     * @return
     */
    public List<ExamineStatisticsRecord> detailsRecord(String statisticId) {
        return examineStatisticsRecordMapper.detailsRecord(statisticId);
    }

    public List<DeductScopeDto> findDeductDetailsNoRecord(String relateId, List<String> hisTables) {
        return examineStatisticsRecordMapper.findDeductDetailsNoRecord(relateId,hisTables);
    }

    public List<DeductScopeDto> findDeductDetailsFlowRun(String relateId) {
        return examineStatisticsRecordMapper.findDeductDetailsFlowRun(relateId);
    }
}
