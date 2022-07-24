package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件-报警Entity
* @author zgl
* @date 2019-11-01 11:29
*/

@ApiModel(value = "EventAlarm",description = "设备事件-报警Entity")
@Data
public class EventAlarm extends BaseEntity<EventAlarm> {

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
    private Long recTime;
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
    * 预留通道3
    */

    @ApiModelProperty(value = "预留通道3",name="passway3")
    private String passway3;
    /**
    * 箱门
    */

    @ApiModelProperty(value = "箱门",name="fld01")
    private Integer fld01;
    /**
    * 授权
    */

    @ApiModelProperty(value = "授权",name="fld02")
    private Integer fld02;
    /**
    * 风扇1
    */

    @ApiModelProperty(value = "风扇1",name="fld03")
    private Integer fld03;
    /**
    * 风扇2
    */

    @ApiModelProperty(value = "风扇2",name="fld04")
    private Integer fld04;
    /**
    * 加热
    */

    @ApiModelProperty(value = "加热",name="fld05")
    private Integer fld05;
    /**
    * 水浸
    */

    @ApiModelProperty(value = "水浸",name="fld06")
    private Integer fld06;
    /**
    * 烟雾
    */

    @ApiModelProperty(value = "烟雾",name="fld07")
    private Integer fld07;
    /**
    * 供电
    */

    @ApiModelProperty(value = "供电",name="fld08")
    private Integer fld08;
    /**
    * 短路
    */

    @ApiModelProperty(value = "短路",name="fld09")
    private Integer fld09;
    /**
    * 漏电
    */

    @ApiModelProperty(value = "漏电",name="fld10")
    private Integer fld10;
    /**
    * 电源模块
    */

    @ApiModelProperty(value = "电源模块",name="fld11")
    private Integer fld11;
    /**
    * 雷击保护
    */

    @ApiModelProperty(value = "雷击保护",name="fld12")
    private Integer fld12;
    /**
    * 震动
    */

    @ApiModelProperty(value = "震动",name="fld13")
    private Integer fld13;
    /**
    * 门磁开关
    */

    @ApiModelProperty(value = "门磁开关",name="fld14")
    private Integer fld14;
    /**
    * 预留1
    */

    @ApiModelProperty(value = "预留1",name="fld15")
    private Integer fld15;
    /**
    * 预留2
    */

    @ApiModelProperty(value = "预留2",name="fld16")
    private Integer fld16;
    /**
    * 预留3
    */

    @ApiModelProperty(value = "预留3",name="fld17")
    private Integer fld17;
    /**
    * 预留4
    */

    @ApiModelProperty(value = "预留4",name="fld18")
    private Integer fld18;
    /**
    * 预留5
    */

    @ApiModelProperty(value = "预留5",name="fld19")
    private Integer fld19;
    /**
    * 预留6
    */

    @ApiModelProperty(value = "预留6",name="fld20")
    private Integer fld20;
    /**
    * 预留7
    */

    @ApiModelProperty(value = "预留7",name="fld21")
    private Integer fld21;
    /**
    * 预留8
    */

    @ApiModelProperty(value = "预留8",name="fld22")
    private Integer fld22;
    /**
    * 预留9
    */

    @ApiModelProperty(value = "预留9",name="fld23")
    private Integer fld23;
    /**
    * 预留10
    */

    @ApiModelProperty(value = "预留10",name="fld24")
    private Integer fld24;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
