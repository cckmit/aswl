package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsContentMalfunction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 常见故障表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@Mapper
public interface AsContentMalfunctionMapper extends BaseMapper<AsContentMalfunction> {

    int countMalfunctions(Map<String, Object> params);

    List<AsContentMalfunction> queryMalfunctions(Map<String, Object> params);

}
