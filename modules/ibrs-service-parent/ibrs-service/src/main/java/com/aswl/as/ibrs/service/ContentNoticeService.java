package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.ContentNotice;
import com.aswl.as.ibrs.mapper.ContentNoticeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class ContentNoticeService extends CrudService<ContentNoticeMapper, ContentNotice> {
    private final ContentNoticeMapper contentNoticeMapper;

    /**
     * 新增系统消息表
     *
     * @param contentNotice
     * @return int
     */
    @Transactional
    @Override
    public int insert(ContentNotice contentNotice) {
        return super.insert(contentNotice);
    }

    /**
     * 删除系统消息表
     *
     * @param contentNotice
     * @return int
     */
    @Transactional
    @Override
    public int delete(ContentNotice contentNotice) {
        return super.delete(contentNotice);
    }
}