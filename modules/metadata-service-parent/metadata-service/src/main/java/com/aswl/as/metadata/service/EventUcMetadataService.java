package com.aswl.as.metadata.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventUcMetadata;
import com.aswl.as.metadata.mapper.EventUcMetadataMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcMetadataService extends CrudService<EventUcMetadataMapper, EventUcMetadata> {
    private final EventUcMetadataMapper metadataMapper;


    /**
     * 新增事件元数据
     *
     * @param eventUcMetadata
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventUcMetadata eventUcMetadata) {
        return super.insert(eventUcMetadata);
    }

    /**
     * 删除事件元数据
     *
     * @param eventUcMetadata
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventUcMetadata eventUcMetadata) {
        return super.delete(eventUcMetadata);
    }
    
    /**
     * 根据状态值及元数据表名、字段查询数据
     * @param statusValue
     * @param tabCode
     * @param fldCode
     * @return
     */
    public EventUcMetadata findByStatusName(String statusName){
        return metadataMapper.findByStatusName(statusName);
    }


    /**
     * 指定型号指定协议的fld
     * @param deviceModelId
     * @param tableCode
     * @return
     */
    public String findByModelTab(String deviceModelId, String tableCode) {
        return metadataMapper.findByModelTab(deviceModelId,tableCode);
    }
}