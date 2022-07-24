package com.aswl.as.metadata.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.metadata.api.module.EventHisAlarm;
import com.aswl.as.metadata.api.vo.EventHisAlarmVO;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 设备历史事件-报警Mapper
* @author houzejun
* @date 2019-11-14 11:13
*/

@Mapper
public interface EventHisAlarmMapper extends CrudMapper<EventHisAlarm> {

    int insert(EventHisAlarmVO eventHisAlarmVO);
}
