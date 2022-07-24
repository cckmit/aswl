package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ContentBanner;
import org.apache.ibatis.annotations.Param;

/**
*
* banner管理Mapper
* @author hfx
* @date 2020-03-11 13:28
*/

@Mapper
public interface ContentBannerMapper extends CrudMapper<ContentBanner> {

    int addClicks(String id);

    int addLikes(@Param("id")String id, @Param("addCount")Integer addCount);

}
