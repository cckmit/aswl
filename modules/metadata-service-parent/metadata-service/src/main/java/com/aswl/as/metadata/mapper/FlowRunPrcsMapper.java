package com.aswl.as.metadata.mapper;
import org.apache.ibatis.annotations.Mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.FlowRunPrcs;

/**
*
* 流程实例步骤批注信息Mapper
* @author cheney
* @date 2019-11-13 19:09
*/

@Mapper
public interface FlowRunPrcsMapper extends CrudMapper<FlowRunPrcs> {

}
