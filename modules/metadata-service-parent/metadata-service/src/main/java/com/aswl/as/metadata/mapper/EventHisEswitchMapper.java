package com.aswl.as.metadata.mapper;
import com.aswl.as.metadata.api.vo.EventHisEswitchVO;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventHisEswitch;

/**
*
* 设备历史事件-电源开关Mapper
* @author zgl
* @date 2019-11-11 11:07
*/

@Mapper
public interface EventHisEswitchMapper extends CrudMapper<EventHisEswitch> {

    int insert(EventHisEswitchVO eventHisEswitchVO);
}
