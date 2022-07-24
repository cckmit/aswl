package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AlarmWay;

/**
*
* 报警方式Mapper
* @author dingfei
* @date 2019-11-06 09:51
*/

@Mapper
public interface AlarmWayMapper extends CrudMapper<AlarmWay> {

}
