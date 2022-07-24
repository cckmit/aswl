package com.aswl.as.ibrs.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceExtType;

/**
 * 外接设备类型表Mapper
 *
 * @author dingfei
 * @date 2019-11-09 10:10
 */

@Mapper
public interface DeviceExtTypeMapper extends CrudMapper<DeviceExtType> {

}
