package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.LabelScopeDto;
import com.aswl.as.ibrs.api.dto.PeriodScopeDto;
import com.aswl.as.ibrs.api.module.ExamineStatisticsDeduct;
import com.aswl.as.ibrs.mapper.ExamineStatisticsDeductMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 考核统计扣分Service
 */
@Service
@AllArgsConstructor
@Slf4j
public class ExamineStatisticsDeductService extends CrudService<ExamineStatisticsDeductMapper,ExamineStatisticsDeduct> {
    private final ExamineStatisticsDeductMapper examineStatisticsDeductMapper;

    public void batchInsert(List<ExamineStatisticsDeduct> deductList) {
        examineStatisticsDeductMapper.batchInsert(deductList);
    }

    public PeriodScopeDto periodTotalScope(String standardId, String periodStart,String periodEnd){
        return examineStatisticsDeductMapper.periodTotalScope(standardId,periodStart,periodEnd);
    }

    /**
     * 每个周期的扣分和扣费
     * @param statisticId
     * @return
     */
    public PeriodScopeDto totalScope(String statisticId) {
        return examineStatisticsDeductMapper.totalScope(statisticId);
    }

    /**
     * 每个周期内每个指标的扣分扣费
     * @param statisticId
     * @return
     */
    public List<LabelScopeDto> LabelScope(String statisticId) {
        return examineStatisticsDeductMapper.LabelScope(statisticId);
    }
}
