package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.CategoryTree;

/**
*
* 通用分类树表Mapper
* @author hfx
* @date 2020-03-06 09:16
*/

@Mapper
public interface CategoryTreeMapper extends CrudMapper<CategoryTree> {

}
