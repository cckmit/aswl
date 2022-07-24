package com.aswl.as.metadata.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.metadata.api.module.EventUcId;

/**
*
* 事件统一ID自增Mapper
* @author zgl
* @date 2019-11-14 14:59
*/

@Mapper
public interface EventUcIdMapper extends CrudMapper<EventUcId> {

}
