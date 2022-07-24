package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ExamineItem;
import org.apache.ibatis.annotations.Param;

/**
*
* 考核项Mapper
* @author hfx
* @date 2020-03-19 10:18
*/

@Mapper
public interface ExamineItemMapper extends CrudMapper<ExamineItem> {

    ExamineItem getItemByStandardIdAndBaseLibId(@Param("standardId") String standardId, @Param("examineBaseLibId") String examineBaseLibId);

}
