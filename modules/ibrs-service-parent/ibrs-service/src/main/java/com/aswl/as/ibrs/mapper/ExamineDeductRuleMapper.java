package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ExamineDeductRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 考核扣分规则Mapper
* @author hfx
* @date 2020-03-19 10:18
*/

@Mapper
public interface ExamineDeductRuleMapper extends CrudMapper<ExamineDeductRule> {

    List<ExamineDeductRule> getRuleListByStandardId(String standardId);


    /**
     * 根据考核项Id查询扣分规则
     * @return
     */
    List<ExamineDeductRule> getByExamineItemId(@Param("examineItemId") String examineItemId);


}
