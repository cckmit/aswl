package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ExamineTimePartConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 考核时段设置Mapper
* @author hfx
* @date 2020-03-19 10:20
*/

@Mapper
public interface ExamineTimePartConfigMapper extends CrudMapper<ExamineTimePartConfig> {

    List<ExamineTimePartConfig> getConfigListByStandardId(String standardId);

    /**
     * itemId获取考核配置
     * @param examineItemId
     * @return
     */
    List<ExamineTimePartConfig> getConfigListExamineItemId(@Param("examineItemId") String examineItemId);
}
