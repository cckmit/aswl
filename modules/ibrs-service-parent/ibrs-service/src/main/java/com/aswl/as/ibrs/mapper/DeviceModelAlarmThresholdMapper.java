package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.module.EventUcMetadataStatusOperation;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceModelAlarmThreshold;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 设备型号区间报警Mapper
* @author dingfei
* @date 2019-10-26 14:24
*/

@Mapper
public interface DeviceModelAlarmThresholdMapper extends CrudMapper<DeviceModelAlarmThreshold> {

    /**
     * 根据设备型号元数据ID和fldCode值查询报警阀值判断是否正常
     * @param list
     * @param fldCode
     * @return
     */
    List<DeviceModelAlarmThreshold> findByMetadataModelId(@Param("list") List<String> list,@Param("fldCode") String fldCode);

    /**
     * 根据事件元数据-设备型号ID查询设备型号区间报警数据
     * @param metadataModelId  设备型号ID
     * @return List<DeviceModelAlarmThreshold>
     */
    List<DeviceModelAlarmThreshold> findAlarmThresholdByMetadataModelId(@Param("metadataModelId") String metadataModelId);


    /**
     * 批量新增
     * @param list 集合
     * @return int 数量
     */
    int insertBatch(List<DeviceModelAlarmThreshold> list);

    /**
     * 通过设备型号属性ID删除
     * @param eventMetadataModelId
     * @return int
     */
    int deleteByEventMetadataModelId(@Param("eventMetadataModelId") String eventMetadataModelId);

}
