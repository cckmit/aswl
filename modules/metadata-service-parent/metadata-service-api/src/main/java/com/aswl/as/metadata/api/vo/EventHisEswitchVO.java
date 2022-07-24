package com.aswl.as.metadata.api.vo;
import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 * 设备历史事件-电源开关VO
 * @author houzejun
 * @date 2019-11-14 11:15
 */

@ApiModel(value = "EventHisEswitchVO",description = "设备历史事件-电源开关VO")
@Data
public class EventHisEswitchVO extends BaseEntity<EventHisEswitchVO> {

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

    @ApiModelProperty(value = "记录日期",name="recDay")
    private Date recDay;
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

    @ApiModelProperty(value = "通道有效位3",name="passway3")
    private String passway3;
    /**
     * 预留
     */

    @ApiModelProperty(value = "预留",name="passway4")
    private String passway4;
    /**
     * 电源X
     */

    @ApiModelProperty(value = "电源X",name="fldx")
    private Integer fldx;
    /**
     * 电源Y
     */

    @ApiModelProperty(value = "电源Y",name="fldy")
    private Integer fldy;
    /**
     * 电源1
     */

    @ApiModelProperty(value = "电源1",name="fld01")
    private Integer fld01;
    /**
     * 电源2
     */

    @ApiModelProperty(value = "电源2",name="fld02")
    private Integer fld02;
    /**
     * 电源3
     */

    @ApiModelProperty(value = "电源3",name="fld03")
    private Integer fld03;
    /**
     * 电源4
     */

    @ApiModelProperty(value = "电源4",name="fld04")
    private Integer fld04;
    /**
     * 电源5
     */

    @ApiModelProperty(value = "电源5",name="fld05")
    private Integer fld05;
    /**
     * 电源6
     */

    @ApiModelProperty(value = "电源6",name="fld06")
    private Integer fld06;
    /**
     * 电源17
     */

    @ApiModelProperty(value = "电源17",name="fld07")
    private Integer fld07;
    /**
     * 电源8
     */

    @ApiModelProperty(value = "电源8",name="fld08")
    private Integer fld08;
    /**
     * 电源9
     */

    @ApiModelProperty(value = "电源9",name="fld09")
    private Integer fld09;
    /**
     * 电源10
     */

    @ApiModelProperty(value = "电源10",name="fld10")
    private Integer fld10;
    /**
     * 电源11
     */

    @ApiModelProperty(value = "电源11",name="fld11")
    private Integer fld11;
    /**
     * 电源12
     */

    @ApiModelProperty(value = "电源12",name="fld12")
    private Integer fld12;
    /**
     * 电源13
     */

    @ApiModelProperty(value = "电源13",name="fld13")
    private Integer fld13;
    /**
     * 电源14
     */

    @ApiModelProperty(value = "电源14",name="fld14")
    private Integer fld14;
    /**
     * 电源15
     */

    @ApiModelProperty(value = "电源15",name="fld15")
    private Integer fld15;
    /**
     * 电源16
     */

    @ApiModelProperty(value = "电源16",name="fld16")
    private Integer fld16;
    /**
     * 预留17
     */

    @ApiModelProperty(value = "预留17",name="fld17")
    private Integer fld17;
    /**
     * 预留18
     */

    @ApiModelProperty(value = "预留18",name="fld18")
    private Integer fld18;
    /**
     * 预留19
     */

    @ApiModelProperty(value = "预留19",name="fld19")
    private Integer fld19;
    /**
     * 预留20
     */

    @ApiModelProperty(value = "预留20",name="fld20")
    private Integer fld20;
    /**
     * 预留21
     */

    @ApiModelProperty(value = "预留21",name="fld21")
    private Integer fld21;
    /**
     * 预留22
     */

    @ApiModelProperty(value = "预留22",name="fld22")
    private Integer fld22;
    /**
     * 预留23
     */

    @ApiModelProperty(value = "预留23",name="fld23")
    private Integer fld23;
    /**
     * 预留24
     */

    @ApiModelProperty(value = "预留24",name="fld24")
    private Integer fld24;
    /**
     * 存储时间
     */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;

    /**
     * 年月（分表保存使用）
     */
    private String yearMonth;
}
