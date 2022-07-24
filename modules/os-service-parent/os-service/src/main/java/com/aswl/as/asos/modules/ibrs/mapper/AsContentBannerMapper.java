package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsContentBanner;
import com.aswl.as.asos.modules.ibrs.entity.AsContentIndustry;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * banner管理 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-03-12
 */
@Mapper
public interface AsContentBannerMapper extends BaseMapper<AsContentBanner> {

    int countBanners(Map<String, Object> params);

    List<AsContentBanner> queryBanners(Map<String, Object> params);

}
