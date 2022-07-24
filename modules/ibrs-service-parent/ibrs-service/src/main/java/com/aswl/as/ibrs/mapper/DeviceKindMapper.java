package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.DeviceKind;

/**
*
* 设备种类Mapper
* @author dingfei
* @date 2019-09-26 14:33
*/

@Mapper
public interface DeviceKindMapper extends CrudMapper<DeviceKind> {

}
