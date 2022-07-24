package com.aswl.as.metadata.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventNetwork;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/**
 *
 * 摄像头Mapper
 * @author liuliepan
 * @date 2019-09-276 14:01
 */
@Mapper
public interface EventNetworkMapper extends CrudMapper<EventNetwork> {

//    @Update({"update as_event_network set device_id = #{deviceId}, region_no = #{regionNo}, network_state = #{networkState}, " +
//            "store_time = #{storeTime}, application_code = #{applicationCode}, tenant_code = #{tenantCode} where id = #{id}"})
    int update(EventNetwork asEventNetwork);

    /**
     * 根据设备ID查询设备网络
     * @param deviceId
     * @return
     */
    @Select("select * from as_event_network where device_id = #{deviceId}")
    EventNetwork findByDeviceId(@Param("deviceId") String deviceId);
}