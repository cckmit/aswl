package com.aswl.as.asos.modules.test.mapper;

import com.aswl.as.asos.modules.test.entity.Test;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 测试表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
@Mapper
public interface TestMapper extends BaseMapper<Test> {

}
