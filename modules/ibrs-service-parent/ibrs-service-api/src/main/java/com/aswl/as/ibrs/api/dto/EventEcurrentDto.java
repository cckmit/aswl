package com.aswl.as.ibrs.api.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
*
* 设备事件-电源电流Dto
* @author zgl
* @date 2019-11-01 13:48
*/

@ApiModel(value = "EventEcurrentDto",description = "设备事件-电源电流Dto")
@Data
public class EventEcurrentDto implements Serializable {

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
    * 接收时间(从1970-01-01 08-00-00到现在的秒)
    */
    @ApiModelProperty(value = "接收时间(从1970-01-01 08-00-00到现在的秒)",name="recTime")
    private Integer recTime;
    /**
    * 通道有效位1
    */
    @ApiModelProperty(value = "通道有效位1",name="passway1")
    private String passway1;
    /**
    * 通道有效位2
    */
    @ApiModelProperty(value = "通道有效位2",name="passway2")
    private String passway2;
    /**
    * 通道有效位3
    */
    @ApiModelProperty(value = "",name="passway3")
    private String passway3;
    /**
    * 通道有效位4
    */
    @ApiModelProperty(value = "",name="passway4")
    private String passway4;
    /**
    * 电流X
    */
    @ApiModelProperty(value = "电流X",name="fldx")
    private Double fldx;
    /**
    * 电流Y
    */
    @ApiModelProperty(value = "电流Y",name="fldy")
    private Double fldy;
    /**
    * 总电流
    */
    @ApiModelProperty(value = "总电流",name="fldall")
    private Double fldall;
    /**
    * 电流1
    */
    @ApiModelProperty(value = "电流1",name="fld01")
    private Double fld01;
    /**
    * 电流2
    */
    @ApiModelProperty(value = "电流2",name="fld02")
    private Double fld02;
    /**
    * 电流3
    */
    @ApiModelProperty(value = "电流3",name="fld03")
    private Double fld03;
    /**
    * 电流4
    */
    @ApiModelProperty(value = "电流4",name="fld04")
    private Double fld04;
    /**
    * 电流5
    */
    @ApiModelProperty(value = "电流5",name="fld05")
    private Double fld05;
    /**
    * 电流6
    */
    @ApiModelProperty(value = "电流6",name="fld06")
    private Double fld06;
    /**
    * 电流17
    */
    @ApiModelProperty(value = "电流17",name="fld07")
    private Double fld07;
    /**
    * 电流8
    */
    @ApiModelProperty(value = "电流8",name="fld08")
    private Double fld08;
    /**
    * 电流9
    */
    @ApiModelProperty(value = "电流9",name="fld09")
    private Double fld09;
    /**
    * 电流10
    */
    @ApiModelProperty(value = "电流10",name="fld10")
    private Double fld10;
    /**
    * 电流11
    */
    @ApiModelProperty(value = "电流11",name="fld11")
    private Double fld11;
    /**
    * 电流12
    */
    @ApiModelProperty(value = "电流12",name="fld12")
    private Double fld12;
    /**
    * 电流13
    */
    @ApiModelProperty(value = "电流13",name="fld13")
    private Double fld13;
    /**
    * 电流14
    */
    @ApiModelProperty(value = "电流14",name="fld14")
    private Double fld14;
    /**
    * 电流15
    */
    @ApiModelProperty(value = "电流15",name="fld15")
    private Double fld15;
    /**
    * 电流16
    */
    @ApiModelProperty(value = "电流16",name="fld16")
    private Double fld16;
    /**
    * 预留17
    */
    @ApiModelProperty(value = "预留17",name="fld17")
    private Double fld17;
    /**
    * 预留18
    */
    @ApiModelProperty(value = "预留18",name="fld18")
    private Double fld18;
    /**
    * 预留19
    */
    @ApiModelProperty(value = "预留19",name="fld19")
    private Double fld19;
    /**
    * 预留20
    */
    @ApiModelProperty(value = "预留20",name="fld20")
    private Double fld20;
    /**
    * 预留21
    */
    @ApiModelProperty(value = "预留21",name="fld21")
    private Double fld21;
    /**
    * 预留22
    */
    @ApiModelProperty(value = "预留22",name="fld22")
    private Double fld22;
    /**
    * 预留23
    */
    @ApiModelProperty(value = "预留23",name="fld23")
    private Double fld23;
    /**
    * 预留24
    */
    @ApiModelProperty(value = "预留24",name="fld24")
    private Double fld24;
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
    * SAAS租户编码
    */
    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;
}
