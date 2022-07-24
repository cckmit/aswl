package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ContentMalfunction;
import org.apache.ibatis.annotations.Param;

/**
 * 常见故障表Mapper
 *
 * @author hfx
 * @date 2020-03-06 10:20
 */

@Mapper
public interface ContentMalfunctionMapper extends CrudMapper<ContentMalfunction> {

    int addClicks(String id);

    int addLikes(@Param("id")String id, @Param("addCount")Integer addCount);

}
