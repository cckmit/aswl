package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.CategoryTree;
import com.aswl.as.ibrs.mapper.CategoryTreeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class CategoryTreeService extends CrudService<CategoryTreeMapper, CategoryTree> {
    private final CategoryTreeMapper categoryTreeMapper;

    /**
     * 新增通用分类树表
     *
     * @param categoryTree
     * @return int
     */
    @Transactional
    @Override
    public int insert(CategoryTree categoryTree) {
        return super.insert(categoryTree);
    }

    /**
     * 删除通用分类树表
     *
     * @param categoryTree
     * @return int
     */
    @Transactional
    @Override
    public int delete(CategoryTree categoryTree) {
        return super.delete(categoryTree);
    }
}