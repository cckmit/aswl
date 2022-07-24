package com.aswl.as.ibrs.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventDataExt;

/**
*
* 设备事件数据扩展Mapper
* @author zgl
* @date 2021-08-16 13:57
*/

@Mapper
public interface EventDataExtMapper extends CrudMapper<EventDataExt> {

}
