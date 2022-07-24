package com.aswl.as.metadata.api.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
*
* 设备历史事件-电压Dto
* @author houzejun
* @date 2019-11-14 11:16
*/

@ApiModel(value = "EventHisEvoltageDto",description = "设备历史事件-电压Dto")
@Data
public class EventHisEvoltageDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
     * 统一事件ID
     */
    @ApiModelProperty(value = "统一事件ID",name="uEventId")
    private String uEventId;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;
    /**
     * 记录日期
     */
    @ApiModelProperty(value = "记录日期",name="recDate")
    private Date recDate;
    /**
     * 接收时间(从1970-01-01 08-00-00到现在的秒)
     */
    @ApiModelProperty(value = "接收时间(从1970-01-01 08-00-00到现在的秒)",name="recTime")
    private Integer recTime;
    /**
     * 温度
     */
    @ApiModelProperty(value = "温度",name="temperature")
    private String temperature;
    /**
     * 湿度
     */
    @ApiModelProperty(value = "湿度",name="humidity")
    private String humidity;
    /**
     * 电压值1
     */
    @ApiModelProperty(value = "电压值1",name="fld01")
    private Double fld01;
    /**
     * 电压值1设定
     */
    @ApiModelProperty(value = "电压值1设定",name="val01")
    private String val01;
    /**
     * 电压值2
     */
    @ApiModelProperty(value = "电压值2",name="fld02")
    private Double fld02;
    /**
     * 电压值2设定
     */
    @ApiModelProperty(value = "电压值2设定",name="val02")
    private String val02;
    /**
     * 电压值3
     */
    @ApiModelProperty(value = "电压值3",name="fld03")
    private Double fld03;
    /**
     * 电压值3设定
     */
    @ApiModelProperty(value = "电压值3设定",name="val03")
    private String val03;
    /**
     * 电压值4
     */
    @ApiModelProperty(value = "电压值4",name="fld04")
    private Double fld04;
    /**
     * 电压值4设定
     */
    @ApiModelProperty(value = "电压值4设定",name="val04")
    private String val04;
    /**
     * 电压值5
     */
    @ApiModelProperty(value = "电压值5",name="fld05")
    private Double fld05;
    /**
     * 电压值5设定
     */
    @ApiModelProperty(value = "电压值5设定",name="val05")
    private String val05;
    /**
     * 电压值6
     */
    @ApiModelProperty(value = "电压值6",name="fld06")
    private Double fld06;
    /**
     * 电压值6设定
     */
    @ApiModelProperty(value = "电压值6设定",name="val06")
    private String val06;
    /**
     * 电压值7
     */
    @ApiModelProperty(value = "电压值7",name="fld07")
    private Double fld07;
    /**
     * 电压值7设定
     */
    @ApiModelProperty(value = "电压值7设定",name="val07")
    private String val07;
    /**
     * 电压值8
     */
    @ApiModelProperty(value = "电压值8",name="fld08")
    private Double fld08;
    /**
     * 电压值8设定
     */
    @ApiModelProperty(value = "电压值8设定",name="val08")
    private String val08;
    /**
     * 电压值9
     */
    @ApiModelProperty(value = "电压值9",name="fld09")
    private Double fld09;
    /**
     * 电压值9设定
     */
    @ApiModelProperty(value = "电压值9设定",name="val09")
    private String val09;
    /**
     * 电压值9
     */
    @ApiModelProperty(value = "电压值9",name="fld10")
    private Double fld10;
    /**
     * 电压值9设定
     */
    @ApiModelProperty(value = "电压值9设定",name="val10")
    private String val10;
    /**
     * 电压值9
     */
    @ApiModelProperty(value = "电压值9",name="fld11")
    private Double fld11;
    /**
     * 电压值9设定
     */
    @ApiModelProperty(value = "电压值9设定",name="val11")
    private String val11;
    /**
     * 电压值9
     */
    @ApiModelProperty(value = "电压值9",name="fld12")
    private Double fld12;
    /**
     * 电压值9设定
     */
    @ApiModelProperty(value = "电压值9设定",name="val12")
    private String val12;
    /**
     * 电压值9
     */
    @ApiModelProperty(value = "电压值9",name="fld13")
    private Double fld13;
    /**
     * 电压值9设定
     */
    @ApiModelProperty(value = "电压值9设定",name="val13")
    private String val13;
    /**
     * 电压值9
     */
    @ApiModelProperty(value = "电压值9",name="fld14")
    private Double fld14;
    /**
     * 电压值9设定
     */
    @ApiModelProperty(value = "电压值9设定",name="val14")
    private String val14;
    /**
     * 电压值9
     */
    @ApiModelProperty(value = "电压值9",name="fld15")
    private Double fld15;
    /**
     * 电压值9设定
     */
    @ApiModelProperty(value = "电压值9设定",name="val15")
    private String val15;
    /**
     * 电压值9
     */
    @ApiModelProperty(value = "电压值9",name="fld16")
    private Double fld16;
    /**
     * 电压值9设定
     */
    @ApiModelProperty(value = "电压值9设定",name="val16")
    private String val16;
    /**
     * 存储时间
     */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;
}
