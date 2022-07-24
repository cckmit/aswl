package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ExamineTimePartConfig;
import com.aswl.as.ibrs.mapper.ExamineTimePartConfigMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ExamineTimePartConfigService extends CrudService<ExamineTimePartConfigMapper, ExamineTimePartConfig> {
    private final ExamineTimePartConfigMapper examineTimePartConfigMapper;

    /**
     * 新增考核时段设置
     *
     * @param examineTimePartConfig
     * @return int
     */
    @Transactional
    @Override
    public int insert(ExamineTimePartConfig examineTimePartConfig) {
        return super.insert(examineTimePartConfig);
    }

    /**
     * 删除考核时段设置
     *
     * @param examineTimePartConfig
     * @return int
     */
    @Transactional
    @Override
    public int delete(ExamineTimePartConfig examineTimePartConfig) {
        return super.delete(examineTimePartConfig);
    }

    public List<ExamineTimePartConfig> getConfigListByStandardId(String standardId)
    {
        // 编写sql
        return examineTimePartConfigMapper.getConfigListByStandardId(standardId);
    }

    /**
     * 考核项Id查询考核配置
     * @param examineItemId
     * @return
     */
    public List<ExamineTimePartConfig> getConfigListExamineItemId(String examineItemId) {
        return examineTimePartConfigMapper.getConfigListExamineItemId(examineItemId);
    }
}