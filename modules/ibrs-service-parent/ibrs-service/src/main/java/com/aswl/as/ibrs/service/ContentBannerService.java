package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ContentBanner;
import com.aswl.as.ibrs.mapper.ContentBannerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class ContentBannerService extends CrudService<ContentBannerMapper, ContentBanner> {
    private final ContentBannerMapper contentBannerMapper;

    /**
     * 新增banner管理
     *
     * @param contentBanner
     * @return int
     */
    @Transactional
    @Override
    public int insert(ContentBanner contentBanner) {
        return super.insert(contentBanner);
    }

    /**
     * 删除banner管理
     *
     * @param contentBanner
     * @return int
     */
    @Transactional
    @Override
    public int delete(ContentBanner contentBanner) {
        return super.delete(contentBanner);
    }

    /**
     * banner管理 查看数+1
     *
     * @param id
     * @return int
     */
    @Transactional
    public int addClicks(String id) {
        return contentBannerMapper.addClicks(id);
    }

    /**
     * banner管理 点赞数+1
     *
     * @param id
     * @return int
     */
    @Transactional
    public int addLikes(String id,Integer addCount) {
        return contentBannerMapper.addLikes(id,addCount);
    }

}