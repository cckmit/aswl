package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.FlowInfo;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 流程信息Mapper
* @author dingfei
* @date 2019-10-25 16:49
*/

@Mapper
public interface FlowInfoMapper extends CrudMapper<FlowInfo> {

}
