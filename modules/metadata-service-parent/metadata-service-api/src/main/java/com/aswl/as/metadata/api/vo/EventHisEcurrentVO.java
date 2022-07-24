package com.aswl.as.metadata.api.vo;
import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 * 设备历史事件-电流VO
 * @author houzejun
 * @date 2019-11-14 11:14
 */

@ApiModel(value = "EventHisEcurrentVO",description = "设备历史事件-电流VO")
@Data
public class EventHisEcurrentVO extends BaseEntity<EventHisEcurrentVO> {

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
     * 电源X
     */

    @ApiModelProperty(value = "电源X",name="fldx")
    private Double fldx;
    /**
     * 电源Y
     */

    @ApiModelProperty(value = "电源Y",name="fldy")
    private Double fldy;
    /**
     *
     */

    @ApiModelProperty(value = "",name="fldall")
    private Double fldall;
    /**
     * 电源1
     */

    @ApiModelProperty(value = "电源1",name="fld01")
    private Double fld01;
    /**
     * 电源2
     */

    @ApiModelProperty(value = "电源2",name="fld02")
    private Double fld02;
    /**
     * 电源3
     */

    @ApiModelProperty(value = "电源3",name="fld03")
    private Double fld03;
    /**
     * 电源4
     */

    @ApiModelProperty(value = "电源4",name="fld04")
    private Double fld04;
    /**
     * 电源5
     */

    @ApiModelProperty(value = "电源5",name="fld05")
    private Double fld05;
    /**
     * 电源6
     */

    @ApiModelProperty(value = "电源6",name="fld06")
    private Double fld06;
    /**
     * 电源17
     */

    @ApiModelProperty(value = "电源17",name="fld07")
    private Double fld07;
    /**
     * 电源8
     */

    @ApiModelProperty(value = "电源8",name="fld08")
    private Double fld08;
    /**
     * 电源9
     */

    @ApiModelProperty(value = "电源9",name="fld09")
    private Double fld09;
    /**
     * 电源10
     */

    @ApiModelProperty(value = "电源10",name="fld10")
    private Double fld10;
    /**
     * 电源11
     */

    @ApiModelProperty(value = "电源11",name="fld11")
    private Double fld11;
    /**
     * 电源12
     */

    @ApiModelProperty(value = "电源12",name="fld12")
    private Double fld12;
    /**
     * 电源13
     */

    @ApiModelProperty(value = "电源13",name="fld13")
    private Double fld13;
    /**
     * 电源14
     */

    @ApiModelProperty(value = "电源14",name="fld14")
    private Double fld14;
    /**
     * 电源15
     */

    @ApiModelProperty(value = "电源15",name="fld15")
    private Double fld15;
    /**
     * 电源16
     */

    @ApiModelProperty(value = "电源16",name="fld16")
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
     * 年月（分表保存使用）
     */
    private String yearMonth;
}
