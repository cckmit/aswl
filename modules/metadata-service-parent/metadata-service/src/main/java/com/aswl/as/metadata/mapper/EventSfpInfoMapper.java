package com.aswl.as.metadata.mapper;
import com.aswl.as.ibrs.api.module.EventSfpInfo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
*
* SFP信息Mapper
* @author zgl
* @date 2021-07-26 23:28
*/

@Mapper
public interface EventSfpInfoMapper extends CrudMapper<EventSfpInfo> {

    /**
     * 根据设备ID获取SFP信息
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_sfp_info where device_id = #{deviceId}")
    EventSfpInfo findByDeviceId(@Param("deviceId") String deviceId);
}
