package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ContentProduct;
import com.aswl.as.ibrs.mapper.ContentProductMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class ContentProductService extends CrudService<ContentProductMapper, ContentProduct> {
    private final ContentProductMapper contentProductMapper;

    /**
     * 新增产品中心表
     *
     * @param contentProduct
     * @return int
     */
    @Transactional
    @Override
    public int insert(ContentProduct contentProduct) {
        return super.insert(contentProduct);
    }

    /**
     * 删除产品中心表
     *
     * @param contentProduct
     * @return int
     */
    @Transactional
    @Override
    public int delete(ContentProduct contentProduct) {
        return super.delete(contentProduct);
    }


    /**
     * 产品中心表查看数+1
     *
     * @param id
     * @return int
     */
    @Transactional
    public int addClicks(String id) {
        return contentProductMapper.addClicks(id);
    }

    /**
     * 产品中心表 点赞数+1
     *
     * @param id
     * @return int
     */
    @Transactional
    public int addLikes(String id,Integer addCount) {
        return contentProductMapper.addLikes(id,addCount);
    }

}