package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.dto.DeviceModelAlarmThresholdDto;
import com.aswl.as.ibrs.api.dto.EventUcMetadataStatusOperationDto;
import com.aswl.as.ibrs.api.module.DeviceModelAlarmThreshold;
import com.aswl.as.ibrs.api.module.EventUcMetadataStatusOperation;
import com.aswl.as.ibrs.mapper.DeviceModelAlarmThresholdMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceModelAlarmThresholdService extends CrudService<DeviceModelAlarmThresholdMapper, DeviceModelAlarmThreshold> {
    private final DeviceModelAlarmThresholdMapper thresholdMapper;


    /**
     * 根据事件元数据-设备型号ID查询设备型号区间报警数据
     *
     * @param metadataModelId 设备型号ID
     * @return List<DeviceModelAlarmThreshold>
     */
    List<DeviceModelAlarmThreshold> findAlarmThresholdByMetadataModelId(String metadataModelId) {
       return  thresholdMapper.findAlarmThresholdByMetadataModelId(metadataModelId);

    }

    /**
     * 批量新增
     * @param list 集合
     * @return int 数量
     */
   public  int insertBatch(List<DeviceModelAlarmThresholdDto> list){
       List<DeviceModelAlarmThreshold> lists=new ArrayList<>();
       for (DeviceModelAlarmThresholdDto dto:list) {
           DeviceModelAlarmThreshold entity=new DeviceModelAlarmThreshold();
           BeanUtils.copyProperties(dto, entity);
           entity.setId(IdGen.snowflakeId());
           lists.add(entity);
       }
       return thresholdMapper.insertBatch(lists);
    }

    /**
     * 通过设备型号属性ID删除
     * @param eventMetadataModelId
     * @return int
     */
    public int deleteByEventMetadataModelId(String eventMetadataModelId){
        
        return thresholdMapper.deleteByEventMetadataModelId(eventMetadataModelId);
    }
}