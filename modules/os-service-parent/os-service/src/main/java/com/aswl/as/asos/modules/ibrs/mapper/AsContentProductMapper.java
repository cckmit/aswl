package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsContentProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产品中心表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@Mapper
public interface AsContentProductMapper extends BaseMapper<AsContentProduct> {

    int countProducts(Map<String, Object> params);

    List<AsContentProduct> queryProducts(Map<String, Object> params);

}
