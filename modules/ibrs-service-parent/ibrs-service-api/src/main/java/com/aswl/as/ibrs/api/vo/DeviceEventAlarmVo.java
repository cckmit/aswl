package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-30 11:52
 * @Version V1
 */
@Data
public class DeviceEventAlarmVo implements Serializable {

    /**
     * 告警数量
     */
    @ApiModelProperty(value = "告警数",name = "total")
    private Integer total;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID",name = "deviceId")
    private String deviceId;

    /**
     * 报警时间
     */
    @ApiModelProperty(value = "报警时间",name = "alarmTime")
    private String alarmTime;

    /**
     * 告警级别
     */
    @ApiModelProperty(value = "告警级别",name = "alarmLevel")
    private String alarmLevel;


    /**
     * 告警类型
     */
    @ApiModelProperty(value = "告警类型",name = "alarmTypes")
    private String alarmTypes;


    /**
     * 告警类型名称
     */
    @ApiModelProperty(value = "告警类型名称",name = "alarmTypesDes")
    private String alarmTypesDes;

    /**
     * 流程实例状态 0 审批中（ 待处理）1已审批（已完成） 2 未通过（未修复） 3 已撤销
     */
    @ApiModelProperty(value = "流程实例状态 0 审批中（ 待处理）1已审批（已完成） 2 未通过（未修复） 3 已撤销",name = "status")
    private Integer status;

    @ApiModelProperty(value = "状态描述",name = "statusDes")
    private String statusDes;
    /**
     * 告警类型名称
     */
    @ApiModelProperty(value = "告警级别",name = "alarmLevels")
    private String alarmLevels;

    /**
     * 告警类型名称
     */
    @ApiModelProperty(value = "告警时间",name = "alarmDates")
    private String alarmDates;

    /**
     * 告警类型名称
     */
    @ApiModelProperty(value = "事件id",name = "eventIds")
    private String eventIds;

}
