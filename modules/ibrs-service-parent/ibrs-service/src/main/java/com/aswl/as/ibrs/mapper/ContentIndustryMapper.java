package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ContentIndustry;
import org.apache.ibatis.annotations.Param;

/**
*
* 行业资讯表Mapper
* @author hfx
* @date 2020-03-06 09:57
*/

@Mapper
public interface ContentIndustryMapper extends CrudMapper<ContentIndustry> {

    int addClicks(String id);

    int addLikes(@Param("id")String id, @Param("addCount")Integer addCount);

}
