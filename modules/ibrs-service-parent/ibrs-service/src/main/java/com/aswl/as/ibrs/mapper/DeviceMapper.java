package com.aswl.as.ibrs.mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.dto.DeviceAlarmConditionDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.vo.*;
import org.apache.ibatis.annotations.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
*
* 设备Mapper
* @author dingfei
* @date 2019-09-27 14:17
*/

@Mapper
public interface DeviceMapper extends CrudMapper<Device> {

    /**
     * 查询设备列表
     * @param deviceDto
     * @return list
     */
    List<DeviceVo> findDeviceInfo(DeviceDto deviceDto);

    /**
     * 根据id查询设备
     * @param id
     * @return DeviceVo
     */
    DeviceVo findById(@Param("id") String id);

    /**
     * 查询上级设备
     * @param parentId 父级ID
     * @return DeviceVo
     */
    DeviceVo getSuperiorDevice(@Param("parentId") String parentId);

    /**
     * 查询下级设备
     * @param id 设备id
     * @return DeviceVo
     */
    List<DeviceVo> getSubordinateDevice(@Param("id") String id);

    /**
     * 根据ids字符串查询设备
     * @param ids
     * @return
     */
    List<Device> getDevicesByIds(@Param("ids") String ids);

    List<Device> findByCondition(@Param("query") String query);

    /**
     * 根据经纬度查询区域设备
     * @param regionCode 区域编码
     * @param lonMin 经度最小值
     * @param lonMax 经度最大值
     * @param latMin 维度最小值
     * @param latMax 维度最大值
     * @param devType 设备类型
     * @param query 关键字
     * @param tenantCode 租户编码
     * @param projectId  项目ID
     * @param alarmLevels  报警级别数组
     * @param offlineFlag  设备监控列表离线标识
     * @param kind  设备种类 2,报障箱 3,摄像机
     * @return  list
     */
    List<DeviceVo> getDevicesByLonLat(@Param("regionCode") String regionCode,
                           @Param("lonMin") double lonMin,
                           @Param("lonMax") double lonMax,
                           @Param("latMin") double latMin,
                           @Param("latMax") double latMax,
                           @Param("devType") String devType,
                           @Param("query") String query,
                           @Param("tenantCode") String tenantCode,
                           @Param("projectId") String projectId ,
                           @Param("alarmLevels") String [] alarmLevels,
                          @Param("queryDebug") String queryDebug,
                          @Param("offlineFlag") String  offlineFlag,
                          @Param("kind") String  kind);

    /**
     *  根据区域获取设备数
     * @param regionCode 区域编码
     * @return Integer
     */
    Integer getDevicesCount(@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode,@Param("projectId") String projectId);


    List<VideoStreamVo> findVideoVo(@Param("id") String id);

    VideoStreamVo findVideoStreamVo(@Param("id") String id);


    /**
     * 根据设备id更新上级设备ID为空
     * @param id
     * @return
     */
    Integer updateParentDeviceId(@Param("id") String id);

    /**
     *  查询设备编码唯一
     * @param deviceCode 编码
     * @return Device
     */

    Device findUniqueDeviceCode(@Param("deviceCode") String deviceCode, @Param("projectId") String projectId);

    /**
     * 查询设备IP唯一
     * @param deviceId deviceId
     * @param ip ip
     * @return Device
     */
    int findUniqueIp(@Param("deviceId") String deviceId ,@Param("ip") String ip, @Param("projectId") String projectId);

    /**
     *根据区域获取设备Id集合
     */
    List<String> findDeviceIdsByRegionAndKind(@Param("regionCode") String regionCode,@Param("deviceKind") String deviceKind);

    List<DeviceVo> getDeviceNetworkState(@Param("ids")String ids);

    List<LinkedHashMap> openDeviceList(DeviceDto deviceDto);

    Long getDeviceCount(@Param("deviceKind") String deviceKind,@Param("regionCode") String regionCode,@Param("tenantCode") String tenantCode, @Param("projectId") String projectId,@Param("model") String model);

    List<String> findByRegionIds(@Param("regionIds") List<String> regionIds);

    Integer getDevicesBoxCount(@Param("kind") String kind,@Param("regionCode") String regionCode, @Param("tenantCode") String tenantCode, @Param("projectId") String projectId);

    List<String> getDeviceModelByProjectId(@Param("projectId") String projectId);


    /**
     * 设备监控列表多条件(前pageSize条父级设备)
     * @param deviceAlarmConditionDto
     * @return
     */
    List<DeviceAlarmVo> monitorDeviceList(@Param("deviceAlarmConditionDto") DeviceAlarmConditionDto deviceAlarmConditionDto,@Param("parentOrChildren") String parentOrChildren);


    /**
     * 当前告警分页查询
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceAlarmVo> getCurrentAlarm(DeviceAlarmDto deviceAlarmDto);


    /**
     * 当前状态分页查询
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceAlarmVo> getCurrentStatus(DeviceAlarmDto deviceAlarmDto);

    /**
     * 历史告警分页查询
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceAlarmVo> getHistoryAlarm(DeviceAlarmDto deviceAlarmDto);

    /**
     * 历史状态分页查询
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceAlarmVo> getHistoryStatus(DeviceAlarmDto deviceAlarmDto);

    /**
     * 根据设备编码获取设备
     * @param deviceCode
     * @return
     */
    com.aswl.iot.dto.Device getByDeviceCode(String deviceCode);

    /**
     * 根据ids查询租户
     * @param ids
     * @return
     */
    List<Map<String,String>> getTenantCodeByIds(@Param("ids") List ids);

    /**
     *
     * @param id
     * @param port
     * @param number
     * @return
     */
    List<Map> portDevice(@Param("id") String id,@Param("port") String port, @Param("number") Integer number);

    /**
     * 获取项目设备清单型号信息
     * @param projectId
     * @return list
     */
    List<ModelStatisticsVo> findModelStatistics (String projectId);

    /**
     * 手机端关联设备
     * @param dto
     * @return
     */
     int relationDevice ( DeviceDto dto);


    /**
     * 上级设备和端口查询下级设备
     * @param id
     * @param port
     * @param portNum
     * @return
     */
    Device findSubsetDevice(@Param("id") String id, @Param("port") String port, @Param("portNum") Integer portNum);

    /**
     *根据设备二维码获取设备信息
      * @param qrcode
     * @return
     */
    DeviceVo findDeviceByQrCode (@Param("qrcode") String qrcode);

    Map getMapDeviceCount(@Param("deviceAlarmConditionDto") DeviceAlarmConditionDto deviceAlarmConditionDto);

    /**
     * 根据Ip和项目编码查询局域网设备
     */
    @Select("select a.* from as_device as a left join as_project b on a.project_id = b.project_id where a.ip = #{ip} and b.project_code = #{projectCode}")
    Device findByIpAndProjectCode(Device device);

    /**
     * 调试的设备
     */
    List<DeviceVo> debugDevice(DeviceDto deviceDto);


    @Select("select a.*, b.device_type, c.project_code from as_device a LEFT JOIN as_device_model b ON a.device_model_id = b.id left join as_project c on a.project_id = c.project_id where a.id = #{id}")
    Device getDeviceVo(@Param("id") String id);

    /**
     *  获取当前健康指数
     * @param deviceAlarmDto
     * @return map
     */
    List<Map> getCurrentHealthyCount(DeviceAlarmDto deviceAlarmDto);

    /**
     * 根据区域ID查询设备IP为空的数据
     * @param  regionId
     * @return list
     */
    List<Device> findDeviceByIpNull(@Param("regionId") String regionId);


    /**
     * 根据区域ID查询设备IP不为空的数据
     * @param  regionId
     * @return list
     */
    int  findDeviceByIpNotNull(@Param("regionId") String regionId,@Param("tag") String tag);

    /**
     * 更新设备地址（地址、经纬度）
     * @param device
     * @return
     */
    int updateAddress(Device device);

    /**
     * 根据项目ID和设备IP查询
     * @param ip ip
     * @param projectId projectId      
     * @return Device
     */
    Device findDevice(@Param("ip") String ip, @Param("projectId") String projectId);
    
}
