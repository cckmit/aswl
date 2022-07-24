package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerSet;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventSfpInfo;
import org.apache.ibatis.annotations.Param;

/**
 * sfp信息Mapper
 *
 * @author df
 * @date 2021/07/26 22:09
 */

@Mapper
public interface EventSfpInfoMapper extends CrudMapper<EventSfpInfo> {

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

    EventSfpInfo findByDeviceId(@Param("deviceId") String deviceId);
}
