package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ExamineDeductRule;
import com.aswl.as.ibrs.api.module.ExamineItem;
import com.aswl.as.ibrs.api.module.ExamineTimePartConfig;
import com.aswl.as.ibrs.mapper.ExamineItemMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ExamineItemService extends CrudService<ExamineItemMapper, ExamineItem> {
    private final ExamineItemMapper examineItemMapper;

    private final ExamineTimePartConfigService examineTimePartConfigService;

    private final ExamineDeductRuleService examineDeductRuleService;

    /**
     * 新增考核项
     *
     * @param examineItem
     * @return int
     */
    @Transactional
    @Override
    public int insert(ExamineItem examineItem) {
        return super.insert(examineItem);
    }

    /**
     * 删除考核项
     *
     * @param examineItem
     * @return int
     */
    @Transactional
    @Override
    public int delete(ExamineItem examineItem) {
        return super.delete(examineItem);
    }


    public List<ExamineItem> getItemListByStandardId(String standardId)
    {
        ExamineItem temp=new ExamineItem();
        temp.setExamineStandardId(standardId);

        List<ExamineItem> itemList=examineItemMapper.findList(temp);

        //获取对应的数据，然后移除掉对应的数据
        List<ExamineTimePartConfig> configList=examineTimePartConfigService.getConfigListByStandardId(standardId);
        List<ExamineDeductRule> ruleList=examineDeductRuleService.getRuleListByStandardId(standardId);

        temp.setTimePartConfigList(new ArrayList<>());
        temp.setRuleList(new ArrayList<>());
        for(ExamineItem item:itemList)
        {
            item.setRuleList(new ArrayList<>());
            item.setTimePartConfigList(new ArrayList<>());

            for(ExamineTimePartConfig config:configList)
            {
                if(item.getId().equals(config.getExamineItemId()))
                {
                    item.getTimePartConfigList().add(config);
                }
            }
            for(ExamineDeductRule rule:ruleList)
            {
                if(item.getId().equals(rule.getExamineItemId()))
                {
                    item.getRuleList().add(rule);
                }
            }
        }
        return itemList;
    }

    public ExamineItem getItemByStandardIdAndBaseLibId(String standardId, String examineBaseLibId) {
        return examineItemMapper.getItemByStandardIdAndBaseLibId(standardId,examineBaseLibId);
    }
}