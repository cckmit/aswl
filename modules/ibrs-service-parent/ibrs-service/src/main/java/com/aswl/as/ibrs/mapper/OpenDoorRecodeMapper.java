package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.OpenDoorRecodeDto;
import com.aswl.as.ibrs.api.vo.OpenDoorVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.OpenDoorRecode;

import java.util.List;

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
}
