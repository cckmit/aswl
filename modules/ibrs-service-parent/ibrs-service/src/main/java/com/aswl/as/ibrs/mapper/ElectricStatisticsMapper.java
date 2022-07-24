package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.ElectricStatisticsDto;
import com.aswl.as.ibrs.api.vo.UnitElectricStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.ElectricStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 电量统计表Mapper
 *
 * @author df
 * @date 2021/06/01 20:18
 */

@Mapper
public interface ElectricStatisticsMapper extends CrudMapper<ElectricStatistics> {

    /**
     * 最近一条记录
     * @param deviceId
     * @return ElectricStatistics
     */
    ElectricStatistics findLastRecord(String deviceId);

    /**
     * 当天的一条记录
     * @param deviceId
     * @param date
     * @return ElectricStatistics
     */

    ElectricStatistics findTodayRecord(@Param("deviceId")String deviceId, @Param("date")Date date);
    
    /** 
     *首页--用电量
     * @param regionCode
     * @param projectId
     * @param tenantCode
     * @param unitId
     * @param type
     * @return list
     */
     Map getElectricNum(@Param("regionCode") String regionCode,@Param("projectId") String projectId,@Param("tenantCode") String tenantCode,@Param("unitId") String unitId, @Param("type") String type);

    /**
     * 用电量统计--用电对比
     * @param dto
     * @return list
     */
    List<Map> getElectricContrast(ElectricStatisticsDto dto);

    /**
     * 用电量统计--各单位用电对比
     * @param dto
     * @return list
     */
    List<UnitElectricStatisticsVo> getUnitElectricContrast(ElectricStatisticsDto dto);

    /**
     * 用电量统计
     * @param regionCode
     * @param projectId
     * @param tenantCode
     * @param startTime
     *  @param endTime
     * @return UnitElectricStatisticsVo
     */
    UnitElectricStatisticsVo  getElectricByTime(@Param("regionCode") String regionCode,@Param("projectId") String projectId,@Param("tenantCode") String tenantCode,@Param("startTime") String startTime ,@Param("endTime") String endTime);

    /**
     * 导出电量报表
     * @param dto
     * @return list
     */
    List<UnitElectricStatisticsVo> exportUnitElectric(ElectricStatisticsDto dto);
    
    
}

