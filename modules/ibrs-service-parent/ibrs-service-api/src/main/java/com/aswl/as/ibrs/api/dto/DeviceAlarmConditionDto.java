package com.aswl.as.ibrs.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/11/8 16:54
 */
@ApiModel(value = "DeviceAlarmConditionDto",description = "设备报警条件Dto")
@Setter@Getter
public class DeviceAlarmConditionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "查询条件",name="query")
    private String query;
    private String[] queries;
    @ApiModelProperty(value = "报警级别",name="alarmLevel")
    private String alarmLevel;
    private String[] alarmLevels;
    @ApiModelProperty(value = "开始时间",name="startTime")
    private String startTime;
    @ApiModelProperty(value = "结束时间",name="endTime")
    private String endTime;
    /**
     * 0  未报警状态
     * 1  报警状态
     */
    @ApiModelProperty(value = "设备状态",name="status")
    private Integer status;
    private String alarmType;//离线标记
    private Integer flag; //离线,报警级别,设备树标记
    /**
     * 1 光纤
     * 2 传输箱
     * 3 摄像机
     */
    @ApiModelProperty(value = "设备种类",name="kind")
    private String kind;
    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;
    @ApiModelProperty(value = "设备父id",name="parentId")
    private String parentId;
    /**
     * 表名集合
     */
    private List<String> tableNames;

    //下面只作传输输出使用，并不保存到数据库
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;

    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;

    @ApiModelProperty(value = "设备监控列表离线标识",name = "offlineFlag")
    private String offlineFlag;

    @ApiModelProperty(value = "经度",name = "longitude")
    private String longitude;

    @ApiModelProperty(value = "维度",name = "latitude")
    private String latitude;

    @ApiModelProperty(value = "是否调试(0:非调试,1:调试中)",name = "debug")
    private String queryDebug;

    @ApiModelProperty(value = "项目ID",name = "queryProjectId")
    private String queryProjectId;

}
