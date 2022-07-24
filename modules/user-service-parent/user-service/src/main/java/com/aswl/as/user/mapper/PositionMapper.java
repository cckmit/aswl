package com.aswl.as.user.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.user.api.dto.PositionDto;
import com.aswl.as.user.api.dto.PositionInfoDto;
import com.aswl.as.user.api.module.Position;
import com.aswl.as.user.api.vo.PositionVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PositionMapper extends CrudMapper<Position> {
     /**
      * 查询列表
      * @param positionInfoDto
      * @return list
      */
     List<PositionVo> findPositionInfo(PositionInfoDto positionInfoDto);

     /**
      * 根据租户编码删除
      * @param tenantCode
      * @return int
      */
     int deleteByTenantCode(@Param("tenantCode") String tenantCode);
}
