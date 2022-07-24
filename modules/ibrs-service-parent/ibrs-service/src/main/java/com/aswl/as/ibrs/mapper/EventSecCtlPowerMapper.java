package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventSecCtlPower;
import org.apache.ibatis.annotations.Param;

/**
 * 设备分控板-电源Mapper
 *
 * @author df
 * @date 2021/07/26 19:28
 */

@Mapper
public interface EventSecCtlPowerMapper extends CrudMapper<EventSecCtlPower> {
    /**
     * 通过设备ID删除数据
     * @param deviceIds
     * @return
     */
    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);

    /**
     * 通过设备ID获取设备分控板电源信息
     * @param deviceId
     * @return EventSecCtlPower
     */

    EventSecCtlPower findByDeviceId(@Param("deviceId") String deviceId);
}
