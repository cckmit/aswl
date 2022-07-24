package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.AssetsInfoDetail;
import com.aswl.as.ibrs.mapper.AssetsInfoDetailMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Slf4j
@Service
public class AssetsInfoDetailService extends CrudService<AssetsInfoDetailMapper, AssetsInfoDetail> {
    private final AssetsInfoDetailMapper assetsInfoDetailMapper;

    /**
     * 新增资产信息明细
     *
     * @param assetsInfoDetail
     * @return int
     */
    @Transactional
    @Override
    public int insert(AssetsInfoDetail assetsInfoDetail) {
        return assetsInfoDetailMapper.insert(assetsInfoDetail);
    }

    /**
     * 删除资产信息明细
     *
     * @param assetsInfoDetail
     * @return int
     */
    @Transactional
    @Override
    public int delete(AssetsInfoDetail assetsInfoDetail) {
        return assetsInfoDetailMapper.delete(assetsInfoDetail);
    }
}