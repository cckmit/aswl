package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.vo.*;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.ibrs.api.module.City;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
*
* 城市Mapper
* @author hzj
* @date 2021/01/13 14:51
*/

@Mapper
public interface CityMapper extends CrudMapper<City> {

    City findByCode(@Param("cityCode") String cityCode);
    /**
     * 区域项目导航城市在线率完好率信息
     * @param kind  设备类型 2 报障箱 3 摄像机
     * @param parentId 父级ID
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @param level 级别
     * @return list
     */
    List<CityVo> getCityRate(@Param("kind") String kind , @Param("parentId") String parentId ,@Param("cityCode") String cityCode,@Param("projectId") String projectId,@Param("level") String level);

    /**
     * 区域项目导航项目信息
     * @param kind 设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @return list
     */
    List<ProjectVo> findProjectRate(@Param("kind") String kind , @Param("cityCode") String cityCode);

    /**
     * 总体健康率
     * @param kind 设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return list
     */
    Map getHealthyRate(@Param("kind") String kind , @Param("cityCode") String cityCode,@Param("projectId") String projectId);

    /**
     * 故障类型排名
     * @param kind 设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return list
     */
    List<Map> getFaultType(@Param("kind") String kind, @Param("cityCode") String cityCode,@Param("projectId") String projectId);

    /**
     * 故障设备型号排名
     * @param kind 设备类型 2 报障箱 3 摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @return list
     */
    List<Map> getFaultModel(@Param("kind") String kind, @Param("cityCode") String cityCode, @Param("projectId") String projectId);


    /**
     * 获取地图设备列表
     * @param  kind 设备种类 2报障箱 3摄像机
     * @param cityCodes 城市编码集合
     * @param projectId 项目ID
     * @param longMin  经度最小值
     * @param longMax  经度最大值
     * @param latMin   维度最小值
     * @param latMax   维度最大值
     * @return list
     */

    List<DeviceVo> findDeviceList(@Param("kind") String kind,
                                  @Param("cityCodes") String cityCodes,
                                  @Param("projectId")String projectId,
                                  @Param("longMin") String longMin,
                                  @Param("longMax")String longMax,
                                  @Param("latMin")String latMin,
                                  @Param("latMax")String latMax);



    /**
     * 获取地图设备统计
     * @param  kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市编码
     * @param cityCodes 城市编码集合
     * @param projectId 项目ID
     * @param cityCodes 编码集合
     * @return map
     */

    Map findDevicesStatistics(@Param("kind")  String kind,@Param("cityCode")  String cityCode,@Param("cityCodes")  String cityCodes, @Param("projectId") String projectId,
                              @Param("longMin") String longMin,
                              @Param("longMax")String longMax,
                              @Param("latMin")String latMin,
                              @Param("latMax")String latMax);



    /**
     * 各厂商在线数/完好数数量 (当前)
     * @param  kind 设备种类 2报障箱 3摄像机
     * @param cityCode 城市编码
     * @param projectId 项目ID
     * @param groupName 分组字段
     * @return map
     */

    List<CityVo> findManufacturerDevicesStatistics(@Param("kind") String kind,@Param("cityCode")  String cityCode, @Param("projectId") String projectId,@Param("groupName") String groupName);


    /**
     * 资产统计
     *
     * @param kind     设备种类(2,报障箱 ,3,摄像机)
     * @param cityCode 城市编码
     * @param parentId 父级ID
     * @param projectId     项目ID
     * @param queryDate     查询日期
     * @param deviceType     设备类型
     * @param deviceModelName    设备型号
     * return list
     */
    List<Map> getAssetsStatistics(@Param("kind") String kind, @Param("cityCode") String cityCode,@Param("parentId") String parentId, @Param("projectId") String projectId,@Param("queryDate") String queryDate,@Param("deviceType") String deviceType,@Param("deviceModelName") String deviceModelName);


    /**
     * 查询网络拓扑图
     *
     * @param cityCodes 城市编码集合
     * @param projectId 项目ID
     * @return list
     */
     List<CityProjectDeviceVo> getCityTree(@Param("cityCodes") String cityCodes, @Param("projectId") String projectId);

}
