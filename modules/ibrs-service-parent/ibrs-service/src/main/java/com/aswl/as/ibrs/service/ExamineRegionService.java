package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ExamineRegion;
import com.aswl.as.ibrs.mapper.ExamineRegionMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ExamineRegionService extends CrudService<ExamineRegionMapper, ExamineRegion> {
    private final ExamineRegionMapper examineRegionMapper;

    /**
     * 新增考核区域关联表
     *
     * @param examineRegion
     * @return int
     */
    @Transactional
    @Override
    public int insert(ExamineRegion examineRegion) {
        return super.insert(examineRegion);
    }

    /**
     * 删除考核区域关联表
     *
     * @param examineRegion
     * @return int
     */
    @Transactional
    @Override
    public int delete(ExamineRegion examineRegion) {
        return super.delete(examineRegion);
    }


    public List<ExamineRegion> getListByStandardId(String standardId)
    {
        //根据 standardId 获取数据
        ExamineRegion tempR=new ExamineRegion();
        tempR.setExamineStandardId(standardId);
        return examineRegionMapper.findList(tempR);
    }

}