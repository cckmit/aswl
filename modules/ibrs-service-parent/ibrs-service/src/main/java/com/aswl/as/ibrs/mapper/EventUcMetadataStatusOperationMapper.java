package com.aswl.as.ibrs.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventUcMetadataStatusOperation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事件元数据-状态操作Mapper
 *
 * @author dingfei
 * @date 2019-11-13 11:50
 */

@Mapper
public interface EventUcMetadataStatusOperationMapper extends CrudMapper<EventUcMetadataStatusOperation> {

    /**
     * 批量新增
     * @param list 集合
     * @return int 数量
     */
    int insertBatch(List<EventUcMetadataStatusOperation> list);

    /**
     * 根据事件元数据ID删除事件元数据-状态操作
     * @param eventMetadataId  事件元数据ID
     * @return int 数量
     */
    int deleteByEventMetadataId(@Param("eventMetadataId") String eventMetadataId);

}
