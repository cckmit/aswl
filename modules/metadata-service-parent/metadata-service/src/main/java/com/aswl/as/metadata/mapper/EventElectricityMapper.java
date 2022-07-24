package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventElectricity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
*
* 设备事件-电量Mapper
* @author hzj
* @date 2021/06/01 19:05
*/

@Mapper
public interface EventElectricityMapper extends CrudMapper<EventElectricity> {

    EventElectricity findByDeviceId(String deviceId);
}
