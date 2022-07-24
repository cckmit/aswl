package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerOutput;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventSecCtlPowerSet;
import org.apache.ibatis.annotations.Param;

/**
 * 设备分控板-电源设置Mapper
 *
 * @author df
 * @date 2021/07/26 19:34
 */

@Mapper
public interface EventSecCtlPowerSetMapper extends CrudMapper<EventSecCtlPowerSet> {

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

    EventSecCtlPowerSet findByDeviceId(@Param("deviceId") String deviceId);
}
