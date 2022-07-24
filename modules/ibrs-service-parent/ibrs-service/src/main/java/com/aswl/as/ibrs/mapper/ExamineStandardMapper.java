package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ExamineStandard;

/**
*
* 考核标准Mapper
* @author hfx
* @date 2020-03-19 10:19
*/

@Mapper
public interface ExamineStandardMapper extends CrudMapper<ExamineStandard> {

}
