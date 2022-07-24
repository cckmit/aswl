package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ExamineDeductRule;
import com.aswl.as.ibrs.mapper.ExamineDeductRuleMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ExamineDeductRuleService extends CrudService<ExamineDeductRuleMapper, ExamineDeductRule> {
    private final ExamineDeductRuleMapper examineDeductRuleMapper;

    /**
     * 新增考核扣分规则
     *
     * @param examineDeductRule
     * @return int
     */
    @Transactional
    @Override
    public int insert(ExamineDeductRule examineDeductRule) {
        return super.insert(examineDeductRule);
    }

    /**
     * 删除考核扣分规则
     *
     * @param examineDeductRule
     * @return int
     */
    @Transactional
    @Override
    public int delete(ExamineDeductRule examineDeductRule) {
        return super.delete(examineDeductRule);
    }

    List<ExamineDeductRule> getRuleListByStandardId(String standardId)
    {
        return examineDeductRuleMapper.getRuleListByStandardId(standardId);
    }

    /**
     * 根据考核项Id查询扣分规则
     * @return
     */
    public List<ExamineDeductRule> getByExamineItemId(String examineItemId) {
        return examineDeductRuleMapper.getByExamineItemId(examineItemId);
    }
}