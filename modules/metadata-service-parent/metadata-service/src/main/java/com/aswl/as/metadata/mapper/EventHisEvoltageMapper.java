package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.metadata.api.module.EventHisEvoltage;
import com.aswl.as.metadata.api.vo.EventHisEvoltageVO;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 设备历史事件-电压Mapper
* @author houzejun
* @date 2019-11-14 11:16
*/

@Mapper
public interface EventHisEvoltageMapper extends CrudMapper<EventHisEvoltage> {

    int insert(EventHisEvoltageVO eventHisEvoltageVO);
}
