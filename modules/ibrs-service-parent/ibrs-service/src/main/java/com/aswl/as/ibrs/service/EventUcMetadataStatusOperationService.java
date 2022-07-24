package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.dto.EventUcMetadataStatusOperationDto;
import com.aswl.as.ibrs.api.module.EventUcMetadataStatusOperation;
import com.aswl.as.ibrs.mapper.EventUcMetadataStatusOperationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcMetadataStatusOperationService extends CrudService<EventUcMetadataStatusOperationMapper, EventUcMetadataStatusOperation> {
    private final EventUcMetadataStatusOperationMapper eventUcMetadataStatusOperationMapper;

    /**
     * 批量新增事件元数据-状态操作
     *
     * @param list 集合
     * @return int 数量
     */
    @Transactional
    public int insertBatch(List<EventUcMetadataStatusOperationDto> list) {
        List<EventUcMetadataStatusOperation> lists=new ArrayList<>();
        for (EventUcMetadataStatusOperationDto operationDto:list) {
            EventUcMetadataStatusOperation operation=new EventUcMetadataStatusOperation();
            BeanUtils.copyProperties(operationDto, operation);
            operation.setId(IdGen.snowflakeId());
            lists.add(operation);
        }
        return eventUcMetadataStatusOperationMapper.insertBatch(lists);
    }

    /**
     *  根据事件元数据ID删除事件元数据-状态操作
     * @param eventMetadataId 事件元数据ID
     * @return 数量
     */
    public int deleteByEventMetadataId(String eventMetadataId){

        return eventUcMetadataStatusOperationMapper.deleteByEventMetadataId(eventMetadataId);
    }
}