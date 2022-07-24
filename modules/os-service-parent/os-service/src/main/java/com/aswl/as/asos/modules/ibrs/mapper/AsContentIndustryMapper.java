package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsContentIndustry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 行业资讯表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@Mapper
public interface AsContentIndustryMapper extends BaseMapper<AsContentIndustry> {

    int countIndustrys(Map<String, Object> params);

    List<AsContentIndustry> queryIndustrys(Map<String, Object> params);

}
