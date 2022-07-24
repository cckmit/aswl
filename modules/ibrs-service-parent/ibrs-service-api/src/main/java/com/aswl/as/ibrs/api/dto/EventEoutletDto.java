package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件-电源电口Dto
* @author zgl
* @date 2019-11-01 14:01
*/

@ApiModel(value = "EventEoutletDto",description = "设备事件-电源电口Dto")
@Data
public class EventEoutletDto implements Serializable {

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
    * 预留
    */
    @ApiModelProperty(value = "预留",name="passway3")
    private String passway3;
    /**
    * 电口1
    */
    @ApiModelProperty(value = "电口1",name="fld01")
    private String fld01 = "-1";
    /**
    * 电口2
    */
    @ApiModelProperty(value = "电口2",name="fld02")
    private String fld02 = "-1";
    /**
    * 电口3
    */
    @ApiModelProperty(value = "电口3",name="fld03")
    private String fld03 = "-1";
    /**
    * 电口4
    */
    @ApiModelProperty(value = "电口4",name="fld04")
    private String fld04 = "-1";
    /**
    * 电口5
    */
    @ApiModelProperty(value = "电口5",name="fld05")
    private String fld05 = "-1";
    /**
    * 电口6
    */
    @ApiModelProperty(value = "电口6",name="fld06")
    private String fld06 = "-1";
    /**
    * 电口7
    */
    @ApiModelProperty(value = "电口7",name="fld07")
    private String fld07 = "-1";
    /**
    * 电口8
    */
    @ApiModelProperty(value = "电口8",name="fld08")
    private String fld08 = "-1";
    /**
    * 电口9
    */
    @ApiModelProperty(value = "电口9",name="fld09")
    private String fld09 = "-1";
    /**
    * 电口10
    */
    @ApiModelProperty(value = "电口10",name="fld10")
    private String fld10 = "-1";
    /**
    * 电口11
    */
    @ApiModelProperty(value = "电口11",name="fld11")
    private String fld11 = "-1";
    /**
    * 电口12
    */
    @ApiModelProperty(value = "电口12",name="fld12")
    private String fld12 = "-1";
    /**
    * 电口13
    */
    @ApiModelProperty(value = "电口13",name="fld13")
    private String fld13 = "-1";
    /**
    * 电口14
    */
    @ApiModelProperty(value = "电口14",name="fld14")
    private String fld14 = "-1";
    /**
    * 电口15
    */
    @ApiModelProperty(value = "电口15",name="fld15")
    private String fld15 = "-1";
    /**
    * 电口16
    */
    @ApiModelProperty(value = "电口16",name="fld16")
    private String fld16 = "-1";
    /**
    * 预留17
    */
    @ApiModelProperty(value = "预留17",name="fld17")
    private String fld17 = "-1";
    /**
    * 预留18
    */
    @ApiModelProperty(value = "预留18",name="fld18")
    private String fld18 = "-1";
    /**
    * 预留19
    */
    @ApiModelProperty(value = "预留19",name="fld19")
    private String fld19 = "-1";
    /**
    * 预留20
    */
    @ApiModelProperty(value = "预留20",name="fld20")
    private String fld20 = "-1";
    /**
    * 预留21
    */
    @ApiModelProperty(value = "预留21",name="fld21")
    private String fld21 = "-1";
    /**
    * 预留22
    */
    @ApiModelProperty(value = "预留22",name="fld22")
    private String fld22 = "-1";
    /**
    * 预留23
    */
    @ApiModelProperty(value = "预留23",name="fld23")
    private String fld23 = "-1";
    /**
    * 预留24
    */
    @ApiModelProperty(value = "预留24",name="fld24")
    private String fld24 = "-1";
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
