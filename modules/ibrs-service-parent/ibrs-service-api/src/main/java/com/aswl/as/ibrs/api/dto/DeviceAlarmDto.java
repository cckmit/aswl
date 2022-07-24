package com.aswl.as.ibrs.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/10/24 18:49
 */
@ApiModel(value = "DeviceAlarmDto",description = "设备报警Dto")
@Getter@Setter
public class DeviceAlarmDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id",name="id")
    private String id;
    @ApiModelProperty(value = "设备名称",name="deviceName")
    private String deviceName;
    @ApiModelProperty(value = "设备编码",name="deviceCode")
    private String deviceCode;
    @ApiModelProperty(value = "设备ip",name="ip")
    private String ip;
    @ApiModelProperty(value = "区域名称",name="regionName")
    private String regionName;
    @ApiModelProperty(value = "设备种类",name="kind")
    private String kind;
    @ApiModelProperty(value = "设备类型",name="type")
    private String type;
    @ApiModelProperty(value = "设备型号",name="model")
    private String model;
    @ApiModelProperty(value = "开始时间",name="startTime")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String startTime;
    @ApiModelProperty(value = "结束时间",name="endTime")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String endTime;
    @ApiModelProperty(value = "报警级别",name="alarmLevel")
    private String alarmLevel;
    private String[] alarmLevels;
    @ApiModelProperty(value = "报警类型名称",name="alarmTypeName")
    private String alarmTypeName;
    @ApiModelProperty(value = "报警类型名称(以组区分)",name="alarmTypeNameGroup")
    private String alarmTypeNameGroup;
    private String[] alarmNames;
    private List<String> alarmTypeNames;
    private List<String> promptTypeNames;
    private List<String> alarmTypeNameGroups;
    private String  deviceIds;
    /**
     * 区域编码
     */
    private String regionCode;
    /**
     *是否报警 1-->true
     *         0-->false
     */
    @ApiModelProperty(value = "报警状态",name="status")
    private String status;
    /**
     * 网络状态
     * 1-->在线
     * 0-->离线
     */
    @ApiModelProperty(value = "网络状态",name="networkStatus")
    private Integer networkStatus;
    private List<String> tableNames;
    /**
     * 当前和历史记录导出的flag
     * 1-->历史记录
     * 0-->当前记录
     */
    @ApiModelProperty(value = "导出flag",name="flag")
    private String flag;
    @ApiModelProperty(value = "查询天数",name="day")
    private Integer day;
    @ApiModelProperty(value = "告警类型查询方式(0:且, 1:或)",name="filter")
    private Integer filter;
    @ApiModelProperty(value = "负责人",name="userName")
    private String userName;

    //------------------下面的字段只作查询用途----------------
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;

    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;

    //-----------------在线设备趋势图时间显示单位-----------------
    @ApiModelProperty(value = "显示单位",name = "timeUnit")
    private String timeUnit;

    //-----------------查询年度---------------------------
    @ApiModelProperty(value = "查询年度",name = "year")
    private String year;

    //----------------当前日期------------------
    @ApiModelProperty(value = "当前日期",name = "currentDate")
    private String currentDate;

    //----------------筛选调试设备------------------
    @ApiModelProperty(value = "筛选调试设备",name = "queryDebug")
    private Integer queryDebug;

    //----------------强制查询某一个项目------------------
    @ApiModelProperty(value = "强制查询某一个项目",name = "queryProjectId")
    private String queryProjectId;

    //----------------工单过滤------------------
    @ApiModelProperty(value = "工单过滤",name = "workOrderFilter")
    private String  workOrderFilter;

    @ApiModelProperty(value = "多条件查询",name = "query")
    private String  query;

    @ApiModelProperty(value = "区域ID",name = "regionId")
    private String  regionId;

    @ApiModelProperty(value = "分组字段区别型号、类型分组",name = "groupFlag")
    private Integer  groupFlag;

    @ApiModelProperty(value = "排序字段",name = "order")
    private String  order;

    @ApiModelProperty(value = "告警时间",name = "alarmTime")
    private String  alarmTime;
    
    @ApiModelProperty(value = "统一事件Id",name = "uEventId")
    @JsonProperty(value = "uEventId")
    private String uEventId;

    @ApiModelProperty(value = "是否后台(1,后台)",name = "isManager")
    private String isManager;

    @ApiModelProperty(value = "是否首页",name = "isPage")
    private String isPage;

    @ApiModelProperty(value = "用户ID",name = "userId")
    private String userId;

}
