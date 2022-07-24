package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.dto.EventUcMetadataModelOperationDto;
import com.aswl.as.ibrs.api.dto.EventUcMetadataStatusOperationDto;
import com.aswl.as.ibrs.api.module.EventUcMetadataModelOperation;
import com.aswl.as.ibrs.api.module.EventUcMetadataStatusOperation;
import com.aswl.as.ibrs.mapper.EventUcMetadataModelOperationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcMetadataModelOperationService extends CrudService<EventUcMetadataModelOperationMapper, EventUcMetadataModelOperation> {
    private final EventUcMetadataModelOperationMapper eventUcMetadataModelOperationMapper;

    /**
     * 通过设备型号ID删除
     *
     * @param eventMetadataModelId 设备型号ID
     * @return int
     */
    public int deleteByEventMetadataModelId(@Param("eventMetadataModelId") String eventMetadataModelId) {
        return eventUcMetadataModelOperationMapper.deleteByEventMetadataModelId(eventMetadataModelId);
    }


    /**
     * 批量新增事件元数据-状态操作
     *
     * @param list 集合
     * @return int 数量
     */
    @Transactional
    public int insertBatch(List<EventUcMetadataModelOperationDto> list) {
        List<EventUcMetadataModelOperation> lists=new ArrayList<>();
        for (EventUcMetadataModelOperationDto operationDto:list) {
            EventUcMetadataModelOperation operation=new EventUcMetadataModelOperation();
            BeanUtils.copyProperties(operationDto, operation);
            operation.setId(IdGen.snowflakeId());
            lists.add(operation);
        }
        return eventUcMetadataModelOperationMapper.insertBatch(lists);
    }
}