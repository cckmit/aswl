package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmWay;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 报警方式Mapper
* @author dingfei
* @date 2019-11-06 09:51
*/

@Mapper
public interface AlarmWayMapper extends CrudMapper<AlarmWay> {

}
