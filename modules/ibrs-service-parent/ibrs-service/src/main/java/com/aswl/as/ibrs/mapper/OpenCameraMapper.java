package com.aswl.as.ibrs.mapper;

import com.aswl.as.ibrs.api.dto.OpenCameraDto;
import com.aswl.as.ibrs.api.vo.OpenCameraVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.OpenCamera;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 开箱摄像机Mapper
 *
 * @author dingfei
 * @date 2019-12-16 16:05
 */

@Mapper
public interface OpenCameraMapper extends CrudMapper<OpenCamera> {

    /**
     * 查询开箱摄像机列表
     * @param openCameraDto
     * @return
     */
    List<OpenCameraVo> findOpenCameraInfo(OpenCameraDto openCameraDto);

    /**
     * 根据IP查询是否存在记录
     * @param ip ip
     * @return int
     */
    int findByIp(@Param("ip") String ip);

}
