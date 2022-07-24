package com.aswl.as.asos.modules.asuser.mapper;

import com.aswl.as.asos.modules.asuser.entity.AsUserSysDept;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 部门表 Mapper 接口
 * </p>
 *
 * @author hfx
 * @since 2019-11-21
 */
@Mapper
public interface AsUserSysDeptMapper extends BaseMapper<AsUserSysDept> {

    AsUserSysDept findDeptByParentId(@Param("parentId") String parentId);

}
