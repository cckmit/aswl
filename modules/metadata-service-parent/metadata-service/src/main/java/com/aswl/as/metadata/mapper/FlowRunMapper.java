package com.aswl.as.metadata.mapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.FlowRun;

/**
*
* 流程实例基本信息Mapper
* @author zgl
* @date 2019-11-13 19:09
*/

@Mapper
public interface FlowRunMapper extends CrudMapper<FlowRun> {

}
