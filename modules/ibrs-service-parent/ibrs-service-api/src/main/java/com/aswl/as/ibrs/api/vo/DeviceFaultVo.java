package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author aswl.com
 * @version 1.0.0
 * @create 2019/10/21 17:48
 */
@ApiModel(value = "DeviceFaultVo",description = "设备故障维护Vo")
@Setter@Getter
public class DeviceFaultVo {
    @ApiModelProperty(value = "工单流程实例id",name="runId")
    private String id;
    @ApiModelProperty(value = "设备id",name="deviceId")
    private String deviceId;
    @ApiModelProperty(value = "设备经度",name="longitude")
    private BigDecimal longitude;
    @ApiModelProperty(value = "设备设备维度",name="latitude")
    private BigDecimal latitude;
    @ApiModelProperty(value = "设备地址",name="address")
    private String address;
    @ApiModelProperty(value = "工单标题",name="title")
    private String title;
    @ApiModelProperty(value = "工单流水号",name="runNo")
    private String runNo;
    @ApiModelProperty(value = "报警设备ip",name="ip")
    private String ip;
    @ApiModelProperty(value = "报警设备名称",name="deviceName")
    private String deviceName;
    @ApiModelProperty(value = "报警设备编码",name="deviceCode")
    private String deviceCode;
    @ApiModelProperty(value = "区域名称",name="regionName")
    private String regionName;
    @ApiModelProperty(value = "工单报警时间",name="beginTime")
    private String beginTime;
    @ApiModelProperty(value = "元数据id",name="eventId")
    private String eventId;
    @ApiModelProperty(value = "历史报警表",name="hisTable")
    private String hisTable;
    @ApiModelProperty(value = "设备报警级别",name="alarmLevel")
    private String alarmLevel;
    @ApiModelProperty(value = "设备报警工单类型",name="type")
    private Integer type;
    @ApiModelProperty(value = "设备报警工单紧急程度",name="priority")
    private Integer priority;
    @ApiModelProperty(value = "设备报警时间",name="alarmTime")
    private String alarmTime;
    @ApiModelProperty(value = "报警间隔时长",name = "intervalTime")
    private String intervalTime;
    @ApiModelProperty(value = "维修时长",name = "repairTime")
    private String repairTime;
    @ApiModelProperty(value = "维修人员",name="repairer")
    private String repairer;
    @ApiModelProperty(value = "派单员",name="assigner")
    private String assigner;
    @ApiModelProperty(value = "维修人员电话",name="repairerMobile")
    private String repairerMobile;
    private String curUserId;
    @ApiModelProperty(value = "工单状态",name="status")
    private Integer status;
    private FlowLogVo children;
    /*以下为查询日志的实体类*/
    @ApiModelProperty(value = "工单意见",name="comment")
    private String comment;
    @ApiModelProperty(value = "工单处理时间",name="handleTime")
    private String handleTime;
    @ApiModelProperty(value = "工单处理人",name="name")
    private String handler;
    @ApiModelProperty(value = "处理人用户Id",name="userId")
    private String userId;
    @ApiModelProperty(value = "工单状态",name="statusValue")
    private String statusValue;
    @ApiModelProperty(value = "工单备注",name="remark")
    private String remark;

    // 新加字段
    @ApiModelProperty(value = "故障类型",name="alarmTypeName")
    private String alarmTypeName;

    @ApiModelProperty(value = "故障类型英文",name="alarmType")
    private String alarmType;

    @ApiModelProperty(value = "设备种类",name="2,报障箱 3,摄像机")
    private String model;


    @ApiModelProperty(value = "预估时间",name="estimatedTime")
    private Integer estimatedTime;

    @ApiModelProperty(value = "自动处理已修复工单 0-自动 1-手动",name="orderHandleType")
    private Integer orderHandleType;

    @ApiModelProperty(value = "处理的管理人员，使用用户id",name="handleUserId")
    private String handleUserId;

    @ApiModelProperty(value = "项目名称",name="projectName")
    private String projectName;

}
