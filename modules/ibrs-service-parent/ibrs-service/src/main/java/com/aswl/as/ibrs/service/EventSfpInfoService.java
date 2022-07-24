package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.EventSfpInfo;
import com.aswl.as.ibrs.mapper.EventSfpInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class EventSfpInfoService extends CrudService<EventSfpInfoMapper, EventSfpInfo> {
    private final EventSfpInfoMapper eventSfpInfoMapper;

    /**
     * 新增sfp信息
     *
     * @param eventSfpInfo
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventSfpInfo eventSfpInfo) {
        return eventSfpInfoMapper.insert(eventSfpInfo);
    }

    /**
     * 删除sfp信息
     *
     * @param eventSfpInfo
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventSfpInfo eventSfpInfo) {
        return eventSfpInfoMapper.delete(eventSfpInfo);
    }
}