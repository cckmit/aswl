package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.vo.DeviceFaultVo;
import com.aswl.as.ibrs.api.vo.FlowLogVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.FlowRunLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 流程实例日志Mapper
* @author dingfei
* @date 2019-10-28 15:34
*/

@Mapper
public interface FlowRunLogMapper extends CrudMapper<FlowRunLog> {

    List<DeviceFaultVo> findWorkOrderHistory(@Param("id") String id);

    FlowLogVo findUserByRunId(@Param("id") String id);

    FlowLogVo findUserByRunIdAndRunStatus(@Param("id") String id,@Param("runStatus") Integer runStatus);
    
    int deleteByRunId(List<String> runIds);


}
