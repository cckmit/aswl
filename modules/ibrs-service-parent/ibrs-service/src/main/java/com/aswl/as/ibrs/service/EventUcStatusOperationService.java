package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventUcStatusOperation;
import com.aswl.as.ibrs.mapper.EventUcStatusOperationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcStatusOperationService extends CrudService<EventUcStatusOperationMapper, EventUcStatusOperation> {
    private final EventUcStatusOperationMapper eventUcStatusOperationMapper;

    /**
     * 新增事件状态操作
     *
     * @param eventUcStatusOperation
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventUcStatusOperation eventUcStatusOperation) {
        return super.insert(eventUcStatusOperation);
    }

    /**
     * 删除事件状态操作
     *
     * @param eventUcStatusOperation
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventUcStatusOperation eventUcStatusOperation) {
        return super.delete(eventUcStatusOperation);
    }
}