package com.aswl.as.ibrs.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.AssetsInfoDetail;
import org.apache.ibatis.annotations.Param;

/**
 * 资产信息明细Mapper
 *
 * @author df
 * @date 2022/03/11 13:38
 */

@Mapper
public interface AssetsInfoDetailMapper extends CrudMapper<AssetsInfoDetail> {

    /**
     * 根据资产ID删除详情
     * @param assetsInfoId
     * @return int
     */
    int deleteByAssetsInfoId(@Param("assetsInfoId") String assetsInfoId);

}
