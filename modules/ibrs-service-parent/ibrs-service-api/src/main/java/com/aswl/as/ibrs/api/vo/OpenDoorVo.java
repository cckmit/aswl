package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/12/18 19:38
 */
@Data
@ApiModel(value = "OpenDoorVo",description = "开箱记录")
public class OpenDoorVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "开箱记录id",name="id")
    private String id;
    @ApiModelProperty(value = "设备id",name="deviceId")
    private String deviceId;
    @ApiModelProperty(value = "设备名称",name="deviceName")
    private String deviceName;
    @ApiModelProperty(value = "设备编码",name="deviceCode")
    private String deviceCode;
    @ApiModelProperty(value = "区域",name="regionName")
    private String regionName;
    @ApiModelProperty(value = "告警类型",name="alarmType")
    private String alarmType;
    @ApiModelProperty(value = "开箱时间",name="openTime")
    private String openTime;
    @ApiModelProperty(value = "关箱时间",name="closeTime")
    private String closeTime;
    @ApiModelProperty(value = "开箱与关箱间隔时长",name="intervalTime")
    private String intervalTime;
    @ApiModelProperty(value = "区域责任人",name="owner")
    private String owner;
    @ApiModelProperty(value = "区域责任人电话",name="mobile")
    private String mobile;
    @ApiModelProperty(value = "图片",name="picUrl")
    private String picUrl;
    @ApiModelProperty(value = "视频",name="videoUrl")
    private String videoUrl;
    @ApiModelProperty(value = "ip",name="ip")
    private String ip;
    @ApiModelProperty(value = "项目名称",name="projectName")
    private String projectName;
}
