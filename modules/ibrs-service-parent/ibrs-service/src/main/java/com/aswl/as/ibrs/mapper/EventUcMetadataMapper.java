package com.aswl.as.ibrs.mapper;
import com.aswl.as.ibrs.api.dto.AlarmTypeDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmConditionDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.ibrs.api.vo.*;
import org.apache.ibatis.annotations.Mapper;
import com.aswl.as.common.core.persistence.CrudMapper;
import com.aswl.as.ibrs.api.module.EventUcMetadata;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
*
* 事件元数据Mapper
* @author dingfei
* @date 2019-10-22 11:42
*/

@Mapper
public interface EventUcMetadataMapper extends CrudMapper<EventUcMetadata> {


    /**
     * 分页查询事件元数据列表
     * @param eventUcMetadata 
     * @return list
     */
    List<EventUcMetadataVo> findPageInfo(EventUcMetadata eventUcMetadata);
    

    /**
     * 分页条件查询设备报警列表
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceAlarmVo> findDeviceAlarmInfo(DeviceAlarmDto deviceAlarmDto);


    /**
     * 获取设备告警明细记录
     * @param id
     * @return
     */
    List<DeviceAlarmDetailsVo> findDeviceInfo(@Param("id") String id,@Param("tenantCode") String tenantCode,@Param("projectId")String projectId);

    /**
     * 查询设备历史报警列表
     * @param deviceAlarmDto
     * @return
     */
    List<DeviceAlarmVo> findDeviceHistoryStatusAlarmInfo(DeviceAlarmDto deviceAlarmDto);

    List<EventUcMetadata> findEventUcMetadataByIds(@Param("list") List<String> list);

    UserWatchVo findGroup(@Param("alarmType") String alarmType);

    List<DeviceAlarmVo> findDeviceAlarmInfoByCondition(DeviceAlarmConditionDto deviceAlarmConditionDto);

    /**
     *  根据元数据ID查询已设置操作事件列表
     * @param id
     * @return List
     */
    List<EventUcStatusOperationVo> findSeletedOperationByMetadataId(@Param("id") String id);

    /**
     * 根据设备型号ID获取元数据
     * @return List<EventUcMetadata>
     */
    List<EventUcMetadataVo> findEventUcMetadataByDeviceModelId(@Param("deviceModelId") String deviceModelId);

    List<DeviceAlarmVo> findHistoryByPage(DeviceAlarmConditionDto deviceAlarmConditionDto);

    List<DeviceAlarmVo>  findHistoryMsg(DeviceAlarmDto deviceAlarmDto);

    /**
     * 根据alarmType获取事件元素
     * @param alarmType
     * @return
     */
    @Select("SELECT m.* FROM as_event_uc_metadata m LEFT JOIN as_alarm_type a ON a.event_metadata_id = m.id WHERE a.alarm_type = #{alarmType}")
    EventUcMetadata findByAlarmType(@Param("alarmType") String alarmType);

    /**
     * 根据事件元素和设备ID获取AlarmType
     * @param metadata
     * @param deviceId
     * @return
     */
    @Select("SELECT a.* FROM as_alarm_type a LEFT JOIN as_event_uc_metadata m ON m.id = a.event_metadata_id WHERE m.id = #{metadata.id} " +
                "AND a.status_value = (SELECT t.${metadata.fldCode}  FROM ${metadata.tabCode} t WHERE device_id = #{deviceId})")
    AlarmType findAlarmTypeByMetadataAndDeviceId(@Param("metadata") EventUcMetadata metadata, @Param("deviceId") String deviceId);

    /**
     * 更新指定设备ID和事件元素的当前状态值
     * @param metadata
     * @param deviceId
     * @param val
     * @return
     */
    @Update("UPDATE ${metadata.tabCode} SET ${metadata.fldCode} = #{val} WHERE device_id = #{deviceId}")
    int updateDynamicTableValByDeviceId(@Param("metadata") EventUcMetadata metadata,@Param("deviceId") String deviceId,@Param("val") String val);

    /**
     * 正常的类型信息
     * @param id
     * @param tenantCode
     * @param projectId
     * @return
     */
    List<DeviceAlarmDetailsVo> findPromptInfo(@Param("id") String id, @Param("tenantCode") String tenantCode, @Param("projectId") String projectId);

    /**
     * 告警明细记录
     * @param id
     * @return
     */
    List<DeviceAlarmDetailsVo> findAlarmInfo(@Param("id") String id,@Param("tenantCode") String tenantCode,@Param("projectId")String projectId);


    /**
     * 根据事件元数据表名和字段名获取AlarmType
     * @param tabCode
     * @param fldCode
     * @return
     */
    @Select("SELECT a.* FROM as_alarm_type a LEFT JOIN as_event_uc_metadata m ON m.id = a.event_metadata_id WHERE m.tab_code = #{tabCode} AND m.fld_code = #{fldCode} ")
    List<AlarmType> findAlarmTypesByTabFld(@Param("tabCode") String tabCode, @Param("fldCode") String fldCode);
}
