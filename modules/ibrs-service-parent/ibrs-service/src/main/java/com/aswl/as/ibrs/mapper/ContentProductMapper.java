package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ContentProduct;
import org.apache.ibatis.annotations.Param;

/**
*
* 产品中心表Mapper
* @author hfx
* @date 2020-03-06 10:28
*/

@Mapper
public interface ContentProductMapper extends CrudMapper<ContentProduct> {

    int addClicks(String id);

    int addLikes(@Param("id")String id, @Param("addCount")Integer addCount);

}
