package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsCategoryTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 通用分类树表，普通的树可以直接用一个type辨别来获取 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-03-03
 */
@Mapper
public interface AsCategoryTreeMapper extends BaseMapper<AsCategoryTree> {

}
