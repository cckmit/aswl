package com.aswl.as.metadata.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
*
* 设备历史事件-光口Entity
* @author houzejun
* @date 2019-11-14 11:17
*/

@ApiModel(value = "EventHisSfp",description = "设备历史事件-光口Entity")
@Data
public class EventHisSfp extends BaseEntity<EventHisSfp> {

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
    * 光口1
    */

    @ApiModelProperty(value = "光口1",name="fld01")
    private String fld01;
    /**
    * 光口2
    */

    @ApiModelProperty(value = "光口2",name="fld02")
    private String fld02;
    /**
    * 光口3
    */

    @ApiModelProperty(value = "光口3",name="fld03")
    private String fld03;
    /**
    * 光口4
    */

    @ApiModelProperty(value = "光口4",name="fld04")
    private String fld04;
    /**
    * 光口5
    */

    @ApiModelProperty(value = "光口5",name="fld05")
    private String fld05;
    /**
    * 光口6
    */

    @ApiModelProperty(value = "光口6",name="fld06")
    private String fld06;
    /**
    * 光口7
    */

    @ApiModelProperty(value = "光口7",name="fld07")
    private String fld07;
    /**
    * 光口8
    */

    @ApiModelProperty(value = "光口8",name="fld08")
    private String fld08;
    /**
    * 预留9
    */

    @ApiModelProperty(value = "预留9",name="fld09")
    private String fld09;
    /**
    * 预留10
    */

    @ApiModelProperty(value = "预留10",name="fld10")
    private String fld10;
    /**
    * 预留11
    */

    @ApiModelProperty(value = "预留11",name="fld11")
    private String fld11;
    /**
    * 预留12
    */

    @ApiModelProperty(value = "预留12",name="fld12")
    private String fld12;
    /**
    * 预留13
    */

    @ApiModelProperty(value = "预留13",name="fld13")
    private String fld13;
    /**
    * 预留14
    */

    @ApiModelProperty(value = "预留14",name="fld14")
    private String fld14;
    /**
    * 预留15
    */

    @ApiModelProperty(value = "预留15",name="fld15")
    private String fld15;
    /**
    * 预留16
    */

    @ApiModelProperty(value = "预留16",name="fld16")
    private String fld16;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
