package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.ElectricSupplyUnitDto;
import com.aswl.as.ibrs.api.vo.ElectricSupplyUnitVo;
import com.aswl.as.ibrs.api.vo.UnitDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.ElectricSupplyUnit;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
*
* 供电单位Mapper
* @author df
* @date 2021/06/01 20:25
*/

@Mapper
public interface ElectricSupplyUnitMapper extends CrudMapper<ElectricSupplyUnit> {

    /**
     * 获取供电单位数据集合
     * @param electricSupplyUnitDto
     * @return
     */
    List<ElectricSupplyUnitVo> getElectricSupplyUnitList(ElectricSupplyUnitDto electricSupplyUnitDto);

    /**
     * 根据单位ID查询点位用电量
     * @param unitId
     * @param deviceName
     * @param startTime
     * @param endTime
     * @return list
     */
    List<UnitDeviceVo> findByUnitId(@Param("unitId") String unitId,@Param("deviceName") String deviceName, @Param("startTime") String startTime,@Param("endTime") String endTime);
}
