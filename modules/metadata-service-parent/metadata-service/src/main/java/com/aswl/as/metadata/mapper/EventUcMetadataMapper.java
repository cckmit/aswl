package com.aswl.as.metadata.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventUcMetadata;

/**
*
* 事件元数据Mapper
* @author dingfei
* @date 2019-10-22 11:42
*/

@Mapper
public interface EventUcMetadataMapper extends CrudMapper<EventUcMetadata> {

	EventUcMetadata findByStatusName(@Param("statusName")String statusName);

    String findByModelTab(@Param("deviceModelId") String deviceModelId,@Param("tableCode") String tableCode);
}
