package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 设备类型Mapper
* @author dingfei
* @date 2019-09-26 15:29
*/

@Mapper
public interface DeviceTypeMapper extends CrudMapper<DeviceType> {

    /**
     *  根据设备类型查询设备类型是否存在
     * @param deviceType
     * @return
     */
    DeviceType findByDeviceType(@Param("deviceType") String deviceType);

}
