package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.dto.ElectricSupplyUnitDto;
import com.aswl.as.ibrs.api.module.ElectricSupplyUnit;
import com.aswl.as.ibrs.api.vo.ElectricSupplyUnitVo;
import com.aswl.as.ibrs.api.vo.UnitDeviceVo;
import com.aswl.as.ibrs.mapper.ElectricSupplyUnitMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ElectricSupplyUnitService extends CrudService<ElectricSupplyUnitMapper, ElectricSupplyUnit> {
    private final ElectricSupplyUnitMapper electricSupplyUnitMapper;

    /**
     * 新增供电单位
     *
     * @param electricSupplyUnit
     * @return int
     */
    @Transactional
    @Override
    public int insert(ElectricSupplyUnit electricSupplyUnit) {
        return electricSupplyUnitMapper.insert(electricSupplyUnit);
    }

    /**
     * 删除供电单位
     *
     * @param electricSupplyUnit
     * @return int
     */
    @Transactional
    @Override
    public int delete(ElectricSupplyUnit electricSupplyUnit) {
        return electricSupplyUnitMapper.delete(electricSupplyUnit);
    }

    /**
     * 获取供电单位数据集合
     * @param electricSupplyUnitDto
     * @return
     */
    public List<ElectricSupplyUnitVo> getElectricSupplyUnitList(ElectricSupplyUnitDto electricSupplyUnitDto){
        return electricSupplyUnitMapper.getElectricSupplyUnitList(electricSupplyUnitDto);
    }

    /**
     * 分页获取供电单位数据
     * @param pageInfo
     * @param electricSupplyUnitDto
     * @return
     */
    public PageInfo<ElectricSupplyUnitVo> getElectriSupplyUnitPage(PageInfo<ElectricSupplyUnitVo> pageInfo, ElectricSupplyUnitDto electricSupplyUnitDto){
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        return new PageInfo(this.getElectricSupplyUnitList(electricSupplyUnitDto));
    }

    /**
     * 根据单位ID查询点位用电量
     * @param unitId
     * @param deviceName
     * @param startTime
     * @param endTime
     * @return list
     */
    public List<UnitDeviceVo> findByUnitId(String unitId,String deviceName,String startTime ,String endTime){
        
        return  electricSupplyUnitMapper.findByUnitId(unitId,deviceName,startTime,endTime);
    }
}