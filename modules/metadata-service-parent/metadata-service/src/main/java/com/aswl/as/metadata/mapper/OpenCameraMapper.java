package com.aswl.as.metadata.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.OpenCamera;

/**
 * 开箱摄像机Mapper
 *
 * @author dingfei
 * @date 2019-12-16 16:05
 */

@Mapper
public interface OpenCameraMapper extends CrudMapper<OpenCamera> {
	
	OpenCamera findOpenCameraByDeviceId(@Param("deviedId")String deviedId);

}
