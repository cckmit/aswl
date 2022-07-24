package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventUcMetadataModelOperation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 设备型号事件元数据-状态操作Mapper
* @author dingfei
* @date 2019-12-02 10:44
*/

@Mapper
public interface EventUcMetadataModelOperationMapper extends CrudMapper<EventUcMetadataModelOperation> {

    /**
     *  通过设备型号ID删除
     * @param eventMetadataModelId  设备型号ID
     * @return int
     */
    int deleteByEventMetadataModelId(@Param("eventMetadataModelId") String eventMetadataModelId);

    /**
     * 批量新增
     * @param list 集合
     * @return int 数量
     */
    int insertBatch(List<EventUcMetadataModelOperation> list);
}
