package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.AssetsInfoDto;
import com.aswl.as.ibrs.api.vo.AssetsInfoVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AssetsInfo;

import java.util.List;

/**
 * 资产信息Mapper
 *
 * @author df
 * @date 2022/01/14 15:54
 */

@Mapper
public interface AssetsInfoMapper extends CrudMapper<AssetsInfo> {

    /**
     * 分页查询所有资产信息
     * @param assetsInfoDto
     * @return list
     */
     List<AssetsInfoVo> findInfo(AssetsInfoDto assetsInfoDto);

}
