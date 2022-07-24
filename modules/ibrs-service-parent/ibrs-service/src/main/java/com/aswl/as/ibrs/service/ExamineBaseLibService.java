package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.ExamineBaseLibDto;
import com.aswl.as.ibrs.api.module.ExamineBaseLib;
import com.aswl.as.ibrs.mapper.ExamineBaseLibMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ExamineBaseLibService extends CrudService<ExamineBaseLibMapper, ExamineBaseLib> {
    private final ExamineBaseLibMapper examineBaseLibMapper;

    /**
     * 新增考核指标库
     *
     * @param examineBaseLib
     * @return int
     */
    @Transactional
    @Override
    public int insert(ExamineBaseLib examineBaseLib) {
        return super.insert(examineBaseLib);
    }

    /**
     * 删除考核指标库
     *
     * @param examineBaseLib
     * @return int
     */
    @Transactional
    @Override
    public int delete(ExamineBaseLib examineBaseLib) {
        return super.delete(examineBaseLib);
    }

    /**
     * 查询指标库
     * @param standardId
     */
    public List<ExamineBaseLib> getByStandardId(String standardId) {
        return examineBaseLibMapper.getByStandardId(standardId);
    }

    public ExamineBaseLib getBy(String baseLibId) {
        ExamineBaseLib examineBaseLib = new ExamineBaseLib();
        examineBaseLib.setId(baseLibId);
        return examineBaseLibMapper.get(examineBaseLib);
    }

    /**
     * 所有指标库
     * @return
     */
    public List<String> findAllTitleByStandardId(String standardId) {
        return examineBaseLibMapper.findAllTitleByStandardId(standardId);
    }
}