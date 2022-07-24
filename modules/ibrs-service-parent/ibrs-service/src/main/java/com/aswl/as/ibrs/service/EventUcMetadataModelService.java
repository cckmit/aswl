package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventUcMetadataModel;
import com.aswl.as.ibrs.mapper.EventUcMetadataModelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcMetadataModelService extends CrudService<EventUcMetadataModelMapper, EventUcMetadataModel> {
    private final EventUcMetadataModelMapper eventUcMetadataModelMapper;

    /**
     * 新增事件元数据-设备型号
     *
     * @param eventUcMetadataModel
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventUcMetadataModel eventUcMetadataModel) {
        return super.insert(eventUcMetadataModel);
    }

    /**
     * 删除事件元数据-设备型号
     *
     * @param eventUcMetadataModel
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventUcMetadataModel eventUcMetadataModel) {
        return super.delete(eventUcMetadataModel);
    }

    /**
     * 根据型号ID获取事件元数据-设备型号
     * @param modelId
     * @return
     */
    public List<EventUcMetadataModel> findByUcMetadataByModelId(String modelId){
        
        return eventUcMetadataModelMapper.findByUcMetadataByModelId(modelId);
    }

    /**
     * 根据设备型号ID删除
     * @param deviceModelId
     * @return
     */
    public int deleteByDeviceModelId( String deviceModelId){
        
        return eventUcMetadataModelMapper.deleteByDeviceModelId(deviceModelId);
    }
}