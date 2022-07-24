package com.aswl.as.metadata.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.metadata.api.module.EventHisSfp;
import com.aswl.as.metadata.api.vo.EventHisSfpVO;
import org.apache.ibatis.annotations.Mapper;

/**
*
* 设备历史事件-光口Mapper
* @author houzejun
* @date 2019-11-14 11:17
*/

@Mapper
public interface EventHisSfpMapper extends CrudMapper<EventHisSfp> {

    int insert(EventHisSfpVO eventHisSfpVO);
}
