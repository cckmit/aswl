package com.aswl.as.asos.modules.ibrs.mapper;

import com.aswl.as.asos.modules.ibrs.entity.AsProject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 项目表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-11-15
 */
@Mapper
public interface AsProjectMapper extends BaseMapper<AsProject> {

    AsProject selectProjectForProjectCode(AsProject asProject);

}
