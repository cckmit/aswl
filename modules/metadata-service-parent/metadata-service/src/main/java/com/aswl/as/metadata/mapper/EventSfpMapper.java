package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventSfp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;
import java.util.Set;

/**
*
* 设备事件-光口Mapper
* @author zgl
* @date 2019-11-01 14:04
*/

@Mapper
public interface EventSfpMapper extends CrudMapper<EventSfp> {

    @Select("select * from as_event_sfp where device_id = #{deviceId}")
    EventSfp findByDeviceId(String deviceId);

    Map<String,String> findSfpState(@Param("keySet") Set<String> keySet, @Param("deviceId") String deviceId);

}
