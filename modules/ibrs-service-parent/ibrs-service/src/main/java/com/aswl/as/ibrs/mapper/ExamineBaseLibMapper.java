package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ExamineBaseLib;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 考核指标库Mapper
* @author hfx
* @date 2020-03-19 10:17
*/

@Mapper
public interface ExamineBaseLibMapper extends CrudMapper<ExamineBaseLib> {

    List<ExamineBaseLib> getByStandardId(@Param("standardId") String standardId);

    List<String> findAllTitleByStandardId(@Param("standardId") String standardId);

}
