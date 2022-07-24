package com.aswl.as.ibrs.mapper;

import com.aswl.as.ibrs.api.vo.EventUcStatusOperationVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventUcStatusOperation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 事件状态操作Mapper
 *
 * @author dingfei
 * @date 2019-11-12 10:24
 */

@Mapper
public interface EventUcStatusOperationMapper extends CrudMapper<EventUcStatusOperation> {

    /**
     *  根据 设备型号事件元数据-状态操作ID 查询选择可操作列表
     * @param metadataModelOperationId  状态操作ID
     * @return  List<EventUcStatusOperationVo>
     */
    List<EventUcStatusOperationVo> selectEventUcStatusOperation(@Param("metadataModelOperationId") String metadataModelOperationId );
}
