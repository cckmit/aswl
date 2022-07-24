package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.FlowRunLog;
import com.aswl.as.metadata.mapper.FlowRunLogMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class FlowRunLogService extends CrudService<FlowRunLogMapper, FlowRunLog> {
    private final FlowRunLogMapper flowRunLogMapper;

    /**
     * 新增流程实例日志
     *
     * @param flowRunLog
     * @return int
     */
    @Transactional
    @Override
    public int insert(FlowRunLog flowRunLog) {
        return flowRunLogMapper.insert(flowRunLog);
    }

    /**
     * 删除流程实例日志
     *
     * @param flowRunLog
     * @return int
     */
    @Transactional
    @Override
    public int delete(FlowRunLog flowRunLog) {
        return super.delete(flowRunLog);
    }
}