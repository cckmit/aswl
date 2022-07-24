package com.aswl.as.user.service;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.user.api.dto.PositionInfoDto;
import com.aswl.as.user.api.module.Position;
import com.aswl.as.user.api.vo.PositionVo;
import com.aswl.as.user.mapper.PositionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @version 1.0.0
 * @Author ke
 * @create 2019/9/17 14:43
 */
@AllArgsConstructor
@Service
public class PositionService extends CrudService<PositionMapper, Position> {
    private final PositionMapper  positionMapper;

    public PageInfo<PositionVo> findPage(PageInfo<PositionInfoDto> page, PositionInfoDto positionInfoDto) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(positionMapper.findPositionInfo(positionInfoDto));
    }


}
