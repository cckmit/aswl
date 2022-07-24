package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.CityManagePlatformDto;
import com.aswl.as.ibrs.api.vo.CityManagePlatformVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.CityManagePlatform;

import java.util.List;

/**
 * 智能箱管理平台数据表Mapper
 *
 * @author df
 * @date 2021/07/26 14:33
 */

@Mapper
public interface CityManagePlatformMapper extends CrudMapper<CityManagePlatform> {

    /**
     * 分页查询智能箱管理平台数据表列表
     * @param cityManagePlatformDto
     * @return
     */
    List<CityManagePlatformVo> findPageInfo(CityManagePlatformDto cityManagePlatformDto);

}
