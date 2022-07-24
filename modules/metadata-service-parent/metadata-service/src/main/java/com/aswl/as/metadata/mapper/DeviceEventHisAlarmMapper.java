package com.aswl.as.metadata.mapper;
import com.aswl.as.metadata.api.vo.DeviceEventHisAlarmVO;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceEventHisAlarm;

/**
*
* 设备事件历史报警Mapper
* @author zgl
* @date 2019-11-11 10:47
*/

@Mapper
public interface DeviceEventHisAlarmMapper extends CrudMapper<DeviceEventHisAlarm> {

    int insert(DeviceEventHisAlarmVO deviceEventHisAlarmVO);
}
