package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.RatingStandardDto;
import com.aswl.as.ibrs.api.module.RatingStandard;
import com.aswl.as.ibrs.mapper.RatingStandardMapper;
import com.aswl.as.ibrs.utils.BeanUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class RatingStandardService extends CrudService<RatingStandardMapper, RatingStandard> {
    private final RatingStandardMapper ratingStandardMapper;

    /**
     * 新增告警评级设置
     *
     * @param ratingStandard
     * @return int
     */
    @Transactional
    @Override
    public int insert(RatingStandard ratingStandard) {
        return super.insert(ratingStandard);
    }

    /**
     * 删除告警评级设置
     *
     * @param ratingStandard
     * @return int
     */
    @Transactional
    @Override
    public int delete(RatingStandard ratingStandard) {
        return super.delete(ratingStandard);
    }

    /**
     * 更新数据
     * @param ratingStandard
     * @return
     */
    @Transactional
    @Override
    public int update(RatingStandard ratingStandard) {
        return super.update(ratingStandard);
    }

    public List<RatingStandard> findAll(){
        return ratingStandardMapper.findAll();
    }

    /**
     * 更新告警评级,修改一个,其他评级也会改变,也要修改
     * @param ratingStandardDtos
     * @return
     */
    @Transactional
    public boolean updateRating(List<RatingStandardDto> ratingStandardDtos) {
        int count = 0;
        for (RatingStandardDto ratingStandardDto : ratingStandardDtos) {
            RatingStandard ratingStandard = new RatingStandard();
            BeanUtils.copyProperties(ratingStandardDto, ratingStandard);
            ratingStandard.setApplicationCode(SysUtil.getSysCode());
            ratingStandard.setTenantCode(SysUtil.getTenantCode());
            int update = ratingStandardMapper.update(ratingStandard);
            count+=update;
        }
        if(count >= ratingStandardDtos.size()){
            return true;
        }else {
            throw new CommonException("更新失败");
        }
    }
}