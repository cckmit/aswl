package com.aswl.as.metadata.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.OpenDoorRecodeDto;
import com.aswl.as.ibrs.api.module.OpenDoorRecode;
import com.aswl.as.ibrs.api.vo.OpenDoorVo;

/**
 * 开箱记录表Mapper
 *
 * @author com.aswl
 * @date 2019-12-18 17:06
 */

@Mapper
public interface OpenDoorRecodeMapper extends CrudMapper<OpenDoorRecode> {

    List<OpenDoorVo> findOpenDoorInfo(OpenDoorRecodeDto openDoorRecodeDto);

    int findOpenDoorTotal(OpenDoorRecodeDto openDoorRecodeDto);

    int findIllegalOpenDoorTotal(OpenDoorRecodeDto openDoorRecodeDto);

	List<OpenDoorRecode> findListByDeviceId(@Param("deviceId") String deviceId);
}
