package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ExamineRegion;

/**
*
* 考核区域关联表Mapper
* @author hfx
* @date 2020-03-19 10:19
*/

@Mapper
public interface ExamineRegionMapper extends CrudMapper<ExamineRegion> {

}
