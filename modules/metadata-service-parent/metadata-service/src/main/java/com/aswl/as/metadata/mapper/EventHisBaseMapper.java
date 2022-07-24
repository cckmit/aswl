package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.metadata.api.module.EventHisBase;
import com.aswl.as.metadata.api.vo.EventHisBaseVO;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 设备历史事件-基础Mapper
* @author houzejun
* @date 2019-11-14 11:14
*/

@Mapper
public interface EventHisBaseMapper extends CrudMapper<EventHisBase> {

    int insert(EventHisBaseVO eventHisBaseVO);
}
