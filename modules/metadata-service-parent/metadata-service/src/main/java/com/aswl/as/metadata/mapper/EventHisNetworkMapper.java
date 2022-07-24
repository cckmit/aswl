package com.aswl.as.metadata.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.metadata.api.module.EventHisNetwork;
import com.aswl.as.metadata.api.vo.EventHisNetworkVO;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 设备历史事件-网络Mapper
* @author houzejun
* @date 2019-11-14 11:16
*/

@Mapper
public interface EventHisNetworkMapper extends CrudMapper<EventHisNetwork>{

    int insert(EventHisNetworkVO eventHisNetworkVO);
}
