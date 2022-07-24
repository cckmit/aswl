package com.aswl.as.ibrs.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceExt;

/**
 * 外接设备表Mapper
 *
 * @author dingfei
 * @date 2019-11-12 15:57
 */

@Mapper
public interface DeviceExtMapper extends CrudMapper<DeviceExt> {

}
