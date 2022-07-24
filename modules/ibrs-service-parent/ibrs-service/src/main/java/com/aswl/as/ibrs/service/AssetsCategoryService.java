package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AssetsCategory;
import com.aswl.as.ibrs.mapper.AssetsCategoryMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AssetsCategoryService extends CrudService<AssetsCategoryMapper, AssetsCategory> {
    private final AssetsCategoryMapper assetsCategoryMapper;

    /**
     * 新增资产分类
     *
     * @param assetsCategory
     * @return int
     */
    @Transactional
    @Override
    public int insert(AssetsCategory assetsCategory) {
        return assetsCategoryMapper.insert(assetsCategory);
    }

    /**
     * 删除资产分类
     *
     * @param assetsCategory
     * @return int
     */
    @Transactional
    @Override
    public int delete(AssetsCategory assetsCategory) {
        return assetsCategoryMapper.delete(assetsCategory);
    }
}