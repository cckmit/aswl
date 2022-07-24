package com.aswl.as.metadata.mapper;
import com.aswl.as.ibrs.api.module.EventDataExt;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;

/**
*
* 设备事件数据扩展Mapper
* @author zgl
* @date 2021-08-16 13:27
*/

@Mapper
public interface EventDataExtMapper extends CrudMapper<EventDataExt> {

}
