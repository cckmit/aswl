package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ContentLike;

/**
 * 用户点赞表Mapper
 *
 * @author hfx
 * @date 2020-03-16 10:26
 */

@Mapper
public interface ContentLikeMapper extends CrudMapper<ContentLike> {

}
