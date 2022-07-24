package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.metadata.api.module.EventHisEcurrent;
import com.aswl.as.metadata.api.vo.EventHisEcurrentVO;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 设备历史事件-电流Mapper
* @author houzejun
* @date 2019-11-14 11:14
*/

@Mapper
public interface EventHisEcurrentMapper extends CrudMapper<EventHisEcurrent> {

    int insert(EventHisEcurrentVO eventHisEcurrentVO);
}
