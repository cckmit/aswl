package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventUcMetadataModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 事件元数据-设备型号Mapper
* @author dingfei
* @date 2019-10-25 14:44
*/

@Mapper
public interface EventUcMetadataModelMapper extends CrudMapper<EventUcMetadataModel> {

    /**
     * 根据型号ID获取事件元数据-设备型号
     * @return
     */
    List<EventUcMetadataModel> findByUcMetadataByModelId(@Param("modelId") String modelId);

    /**
     * 根据型号ID和元数据ID查询事件元数据-设备型号
     * @param deviceModelId 型号ID
     * @param eventMetadataId 元数据ID
     * @return EventUcMetadataModel
     */
    EventUcMetadataModel findEventUcMetadataModel(@Param("deviceModelId") String deviceModelId,@Param("eventMetadataId") String eventMetadataId );


    /**
     * 根据设备型号ID删除
     * @param deviceModelId
     * @return
     */
    int deleteByDeviceModelId(@Param("deviceModelId") String deviceModelId);

}
