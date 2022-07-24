package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ContentLike;
import com.aswl.as.ibrs.mapper.ContentLikeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class ContentLikeService extends CrudService<ContentLikeMapper, ContentLike> {

    private final ContentLikeMapper contentLikeMapper;

    /**
     * 新增用户点赞表
     *
     * @param contentLike
     * @return int
     */
    @Transactional
    @Override
    public int insert(ContentLike contentLike) {
        return super.insert(contentLike);
    }

    /**
     * 删除用户点赞表
     *
     * @param contentLike
     * @return int
     */
    @Transactional
    @Override
    public int delete(ContentLike contentLike) {
        return super.delete(contentLike);
    }
}