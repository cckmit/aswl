package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/8 16:48
 */
@Data
public class OpenDeviceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID",name = "id")
    private String id;


    /**
     * 设备类型
     */
    @ApiModelProperty(value = "设备类型",name = "type")
    private int type;

    /**
     * 设备种类名称
     */

    @ApiModelProperty(value = "设备种类名称",name = "deviceKindName")
    private String deviceKindName;

    /**
     * 设备类型名称
     */

    @ApiModelProperty(value = "设备类型名称",name = "deviceTypeName")
    private String deviceTypeName;

    /**
     * 设备型号
     */

    @ApiModelProperty(value = "设备型号",name = "deviceModelId")
    private String deviceModelId;

    /**
     * 设备型号名称
     */

    @ApiModelProperty(value = "设备型号名称",name = "deviceModelName")
    private String deviceModelName;

    /**
     * 编码
     */

    @ApiModelProperty(value = "编码",name = "deviceCode")
    private String deviceCode;
    /**
     * 名称
     */

    @ApiModelProperty(value = "名称",name = "deviceName")
    private String deviceName;
    /**
     * 地址
     */
    @ApiModelProperty(value = "地址",name = "address")
    private String address;

    @ApiModelProperty(value = "IP",name = "ip")
    private String ip;
    /**
     * 端口
     */

    @ApiModelProperty(value = "端口",name = "port")
    private Integer port;
    /**
     * 维度
     */
    @ApiModelProperty(value = "维度",name = "latitude")
    private Double latitude;
    /**
     * 经度
     */

    @ApiModelProperty(value = "经度",name = "longitude")
    private Double longitude;
    /**
     * 上级设备ID
     */

    @ApiModelProperty(value = "上级设备ID",name = "parentDeviceId")
    private String parentDeviceId;
    /**
     * 地区ID
     */

    @ApiModelProperty(value = "地区ID",name = "regionId")
    private String regionId;

    /**
     * 地区编码
     */

    @ApiModelProperty(value = "地区编码",name = "regionCode")
    private String regionCode;

    /**
     * 地区名称
     */

    @ApiModelProperty(value = "地区名称",name = "regionName")
    private String regionName;

    /**
     * 区域全称
     */
    @ApiModelProperty(value = "区域全称",name = "fullName")
    private String fullName;

    /**
     * 告警级别
     */

    @ApiModelProperty(value = "告警级别",name = "alarmLevel")
    private Integer alarmLevel;

    /**
     * 报警类型集合
     */

    @ApiModelProperty(value = "报警类型集合",name = "alarmTypes")
    private String alarmTypes;

    /**
     * 报警类型中文描述
     */

    @ApiModelProperty(value = "报警类型中文描述",name = "alarmTypesDes")
    private String alarmTypesDes;


    /**
     * 用户名
     */

    @ApiModelProperty(value = "用户名",name = "userName")
    private String userName;
    /**
     * 密码
     */

    @ApiModelProperty(value = "密码",name = "password")
    private String password;
    /**
     * 购买日期
     */

    @ApiModelProperty(value = "购买日期",name = "purchaseDate")
    private String purchaseDate;
    /**
     * 保修期
     */

    @ApiModelProperty(value = "保修期",name = "guaranteeTime")
    private Integer guaranteeTime;

    /**
     * 在线状态
     */
    @ApiModelProperty(value = "在线状态(0,null 离线,1,在线)",name = "networkState")
    private Integer networkState;

    /**
     * 图片路径
     */

    @ApiModelProperty(value = "图片路径",name = "picUrl")
    private String picUrl;

    /**
     * 型号描述
     */

    @ApiModelProperty(value = "型号描述",name = "description")
    private String description;

    /**
     * 是否报警
     */

    @ApiModelProperty(value = "是否报警",name = "isAlarm")
    private Integer isAlarm;

    /**
     * 是否有在线视频
     */
    @ApiModelProperty(value = "是否有在线视频",name = "isVideo")
    private Integer isVideo;
}
