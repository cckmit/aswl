package com.aswl.as.asos.modules.sys.dao;

import com.aswl.as.asos.modules.sys.entity.SysBlackListEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统IP黑名单 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@Mapper
public interface SysBlackListDao extends BaseMapper<SysBlackListEntity> {

}
