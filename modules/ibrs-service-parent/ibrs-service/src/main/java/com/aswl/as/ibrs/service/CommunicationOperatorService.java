package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.CommunicationOperator;
import com.aswl.as.ibrs.mapper.CommunicationOperatorMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class CommunicationOperatorService extends CrudService<CommunicationOperatorMapper, CommunicationOperator> {
    private final CommunicationOperatorMapper communicationOperatorMapper;

    /**
     * 新增通讯运营商
     *
     * @param communicationOperator
     * @return int
     */
    @Transactional
    @Override
    public int insert(CommunicationOperator communicationOperator) {
        return communicationOperatorMapper.insert(communicationOperator);
    }

    /**
     * 删除通讯运营商
     *
     * @param communicationOperator
     * @return int
     */
    @Transactional
    @Override
    public int delete(CommunicationOperator communicationOperator) {
        return communicationOperatorMapper.delete(communicationOperator);
    }
}