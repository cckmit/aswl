package com.aswl.as.ibrs.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/10/28 16:16
 */
@Setter@Getter
public class WorkOrderDto {
    @ApiModelProperty(value = "流程实例id",name="runId")
    private String id;
    @ApiModelProperty(value = "设备id",name="deviceId")
    private String deviceId;
    @ApiModelProperty(value = "设备报警类型",name="alarmType")
    private String alarmType;
    @ApiModelProperty(value = "设备报警级别",name="alarmLevel")
    private String alarmLevel;
    @ApiModelProperty(value = "用户id",name="userId")
    private String userId;
    private String prcsId;
    @ApiModelProperty(value = "处理意见",name="comment")
    private String comment;
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
    @ApiModelProperty(value = "流程实例状态",name="status")
    private Integer status;

    //只作传递参数使用
    @ApiModelProperty(value = "预估时间",name="estimatedTime")
    private Integer estimatedTime;
}
