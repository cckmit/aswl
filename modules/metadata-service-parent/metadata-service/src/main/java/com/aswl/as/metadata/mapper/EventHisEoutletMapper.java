package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.metadata.api.module.EventHisEoutlet;
import com.aswl.as.metadata.api.vo.EventHisEoutletVO;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 设备历史事件-电口Mapper
* @author houzejun
* @date 2019-11-14 11:15
*/

@Mapper
public interface EventHisEoutletMapper extends CrudMapper<EventHisEoutlet> {

    int insert(EventHisEoutletVO eventHisEoutletVO);
}
