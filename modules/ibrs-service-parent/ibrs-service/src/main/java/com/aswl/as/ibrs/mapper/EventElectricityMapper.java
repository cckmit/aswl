package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.EventElectricity;
import org.apache.ibatis.annotations.Param;

/**
*
* 设备事件-电量Mapper
* @author hzj
* @date 2021/06/01 19:05
*/

@Mapper
public interface EventElectricityMapper extends CrudMapper<EventElectricity> {

    @Delete("delete from as_event_electricity where device_id in (${deviceIds})")
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);
}
