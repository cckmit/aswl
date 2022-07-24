package com.aswl.as.ibrs.mapper;

import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.api.vo.*;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
*
* 区域Mapper
* @author dingfei
* @date 2019-09-27 14:01
*/

@Mapper
public interface RegionMapper extends CrudMapper<Region> {
    /**
     * 根据父级id查询最新一条记录
     * @param parentId
     * @return Region
     */
    List<Region> findRegionByParentId(@Param("parentId") String parentId);

    /**
     * 条件查询区域设备
     * @param parentId
     * @param query
     * @return
     */
    List<RegionDeviceVo> findByParentId(@Param("parentId") String parentId,@Param("query") String query,@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId,@Param("kind")String kind);

    /**
     * 查询区域设备拓扑图
     * @param parentId
     * @param regionCode
     * @return
     */
    List<RegionDeviceTree> getRegionTree(@Param("parentId") String parentId,@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);

    /**
     * 查询区域
     * @param parentId
     * @param query
     * @param regionCode
     * @return
     */
    List<Map> getRegions(@Param("parentId") String parentId, @Param("query") String query, @Param("regionCode") String regionCode);

    List<KindVo> findDeviceModelKind();

    List<RegionVo> findRegionTree(@Param("regionCode") String regionCode,@Param("tenantCode")String tenantCode,@Param("projectId")String projectId);

    String findRegionId(@Param("regionCode") String regionCode,@Param("tenantCode")String tenantCode,@Param("projectId")String projectId);

    Region findRegionByProjectIdAndName(@Param("projectId")String projectId,@Param("regionName") String regionName );

    Region findRegionByName(@Param("regionName") String regionName );

    List<String> getAllRegion();

    List<RegionDeviceVo> findVideo(@Param("id") String id);

    List<LinkedHashMap> findRegionList(@Param("regionName") String regionName);

    List<Region> findRegionListByParentId(@Param("parentId") String parentId);

    Region findRegionByIpBetween(Region region);

    /**
     * 有设备的区域
     * @return
     */
    List<String> getHaveDeviceRegion(DeviceAlarmDto deviceAlarmDto);

    List<KindVo> findDeviceModelKind1();

    List<Region> findListAll(Region region);

    List<Region> findByPage(Region region);

    /**
     * APP首页各区智能箱/摄像机在线数完好数和数量
     * @param type
     * @param regionCode
     * @param projectId
     * @param tenantCode
     * @return list
     */
    List<RegionDeviceListVo> getAppOnlineAndIntactList( @Param("type") String type,@Param("regionCode")String regionCode,@Param("projectId")String projectId,@Param("tenantCode")String tenantCode);


    /**
     * 根据区域编码删除区域
     * @param regionCode
     * @return
     */
    int deleteByRegionCode(@Param("regionCode") String regionCode);


    /**
     * 健康监测---> 各区域在线率完好率(当前)
     * @param deviceAlarmDto
     * @return list
     */
    List<RegionDeviceListVo> getRegionCurrentOnlineIntactList(DeviceAlarmDto deviceAlarmDto);


    /**
     * 健康监测---> 各区域在线率完好率(历史)
     * @param deviceAlarmDto
     * @return list
     */
    List<RegionDeviceListVo> getRegionHistoryOnlineIntactList(DeviceAlarmDto deviceAlarmDto);

    /**
     * 根据项目ID删除区域
     * @param projectId
     * @return int
     */
    int deleteByProjectId(@Param("projectId") String projectId);


    /**
     *  根据项目ID查询区域下智能箱数量/摄像机数量
     * @param projectIds
     * @return
     */
    Map findByProjectId(@Param("projectIds") List<String> projectIds);

    /**
     * 根据区域编码获取区域
     * @param regionCode
     * @return region
     */
    Region findByRegionCode(@Param("regionCode") String regionCode);

    /**
     * 查询区域级别
     * @param region
     * @return list
     */
    List<RegionVo> findRegionAlarmLevel(Region region);

}
