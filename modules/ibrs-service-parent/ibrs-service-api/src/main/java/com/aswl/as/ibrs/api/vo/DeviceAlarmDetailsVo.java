package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/12/17 13:18
 */
@ApiModel(value = "DeviceAlarmVo",description = "设备报警Vo")
@Getter
@Setter
public class DeviceAlarmDetailsVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备id",name = "id")
    private String id;
    @ApiModelProperty(value = "事件id",name = "eventId")
    private String eventId;
    @ApiModelProperty(value = "设备报警类型中文描述",name = "id")
    private String alarmTypeName;
    @ApiModelProperty(value = "设备报警类型",name = "alarmType")
    private String alarmType;
    @ApiModelProperty(value = "设备报警类型",name = "alarmType")
    private String alarmLevel;
    @ApiModelProperty(value = "设备报警日期",name = "alarmDate")
    private String alarmDate;
    @ApiModelProperty(value = "是否派单",name = "isSendOrder",example = "0表示未派单,1表示已派单")
    private Integer isSendOrder;
    @ApiModelProperty(value = "工单id",name = "runId")
    private String runId;
}
