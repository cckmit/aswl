package com.aswl.as.ibrs.api.dto;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class AsEventHisAlarmDto {

    @ApiModelProperty(value = "表名",name="tableName")
    private String tableName;//表名
    @ApiModelProperty(value = "结束时间",name="endtime")
    private String endtime;//结束时间
    @ApiModelProperty(value = "主键",name="id")
    private String id;//主键
    @ApiModelProperty(value = "统一事件ID",name="ueventid")
    private String ueventid;//统一事件ID
    @ApiModelProperty(value = "设备ID",name="deviceid")
    private String deviceid;//设备ID
    @ApiModelProperty(value = "区域编码",name="regionno")
    private String regionno;//区域编码
    @ApiModelProperty(value = "接收时间",name="rectime")
    private int rectime;//接收时间(从1970-01-01 08-00-00到现在的秒)
    @ApiModelProperty(value = "记录日期",name="recdate")
    private Date recdate;//记录日期
    @ApiModelProperty(value = "通道有效位1",name="passway1")
    private String passway1;//通道有效位1
    @ApiModelProperty(value = "通道有效位2",name="passway2")
    private String passway2;//通道有效位2
    @ApiModelProperty(value = "预留通道3",name="passway3")
    private String passway3;//预留通道3
    @ApiModelProperty(value = "箱门",name="fld01")
    private String fld01;//箱门
    @ApiModelProperty(value = "授权",name="fld02")
    private String fld02;//授权
    @ApiModelProperty(value = "风扇1",name="fld03")
    private String fld03;//风扇1
    @ApiModelProperty(value = "风扇2",name="fld04")
    private String fld04;//风扇2
    @ApiModelProperty(value = "加热",name="fld05")
    private String fld05;//加热
    @ApiModelProperty(value = "水浸",name="fld06")
    private String fld06;//水浸
    @ApiModelProperty(value = "烟雾",name="fld07")
    private String fld07;//烟雾
    @ApiModelProperty(value = "供电",name="fld08")
    private String fld08;//供电
    @ApiModelProperty(value = "短路",name="fld09")
    private String fld09;//短路
    @ApiModelProperty(value = "漏电",name="fld10")
    private String fld10;//漏电
    @ApiModelProperty(value = "电源模块",name="fld11")
    private String fld11;//电源模块
    @ApiModelProperty(value = "雷击保护",name="fld12")
    private String fld12;//雷击保护
    @ApiModelProperty(value = "震动",name="fld13")
    private String fld13;//震动
    @ApiModelProperty(value = "门磁开关",name="fld14")
    private String fld14;//门磁开关
    @ApiModelProperty(value = "预留1",name="fld15")
    private String fld15;//预留1
    @ApiModelProperty(value = "预留2",name="fld16")
    private String fld16;//预留2
    @ApiModelProperty(value = "预留3",name="fld17")
    private String fld17;//预留3
    @ApiModelProperty(value = "预留4",name="fld18")
    private String fld18;//预留4
    @ApiModelProperty(value = "预留5",name="fld19")
    private String fld19;//预留5
    @ApiModelProperty(value = "预留6",name="fld20")
    private String fld20;//预留6
    @ApiModelProperty(value = "预留7",name="fld21")
    private String fld21;//预留7
    @ApiModelProperty(value = "预留8",name="fld22")
    private String fld22;//预留8
    @ApiModelProperty(value = "预留9",name="fld23")
    private String fld23;//预留9
    @ApiModelProperty(value = "预留10",name="fld24")
    private String fld24;//预留10
    @ApiModelProperty(value = "存储时间",name="storetime")
    private Date storetime;//存储时间
    @ApiModelProperty(value = "系统编号",name="applicationcode")
    private String applicationcode;//系统编号
    @ApiModelProperty(value = "租户编码",name="tenantcode")
    private String tenantcode;//租户编码

    @ApiModelProperty(value = "开始时间",name="startTime")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String startTime;
    @ApiModelProperty(value = "结束时间",name="endTime")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String endTime;
}
