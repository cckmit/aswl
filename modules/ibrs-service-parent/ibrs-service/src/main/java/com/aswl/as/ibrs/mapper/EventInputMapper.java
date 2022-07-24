package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventInput;
import org.apache.ibatis.annotations.Param;

/**
 * 设备事件-输入Mapper
 *
 * @author df
 * @date 2022/07/07 15:06
 */

@Mapper
public interface EventInputMapper extends CrudMapper<EventInput> {

    /**
     * 通过设备ID获取设备事件-输入信息
     * @param deviceId
     * @return EventSecCtlPower
     */

    EventInput findByDeviceId(@Param("deviceId") String deviceId);

    /**
     *  通过设备ID删除数据
     * @param deviceIds
     * @return
     */

    int deleteByDeviceIds(@Param("deviceIds") String deviceIds);

}
