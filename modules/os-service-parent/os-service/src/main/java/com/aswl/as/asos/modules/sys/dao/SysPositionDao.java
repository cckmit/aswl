package com.aswl.as.asos.modules.sys.dao;

import com.aswl.as.asos.modules.sys.entity.SysPositionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 职位表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-12-17
 */
@Mapper
public interface SysPositionDao extends BaseMapper<SysPositionEntity> {

    int countPositions(Map<String, Object> params);

    List<SysPositionEntity> queryPositions(Map<String, Object> params);

}
