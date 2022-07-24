package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ContentIndustry;
import com.aswl.as.ibrs.mapper.ContentIndustryMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class ContentIndustryService extends CrudService<ContentIndustryMapper, ContentIndustry> {
    private final ContentIndustryMapper contentIndustryMapper;

    /**
     * 新增行业资讯表
     *
     * @param contentIndustry
     * @return int
     */
    @Transactional
    @Override
    public int insert(ContentIndustry contentIndustry) {
        return super.insert(contentIndustry);
    }

    /**
     * 删除行业资讯表
     *
     * @param contentIndustry
     * @return int
     */
    @Transactional
    @Override
    public int delete(ContentIndustry contentIndustry) {
        return super.delete(contentIndustry);
    }

    /**
     * 行业资讯表 查看数+1
     *
     * @param id
     * @return int
     */
    @Transactional
    public int addClicks(String id) {
        return contentIndustryMapper.addClicks(id);
    }

    /**
     * 行业资讯表 点赞数+1
     *
     * @param id
     * @return int
     */
    @Transactional
    public int addLikes(String id,Integer addCount) {
        return contentIndustryMapper.addLikes(id,addCount);
    }

}