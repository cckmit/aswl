package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 设备分控板-电源Dto
 *
 * @author df
 * @date 2021/07/26 19:28
 */

@ApiModel(value = "EventSecCtlPowerDto", description = "设备分控板-电源Dto")
@Data
public class EventSecCtlPowerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    @ApiModelProperty(value = "ID主键", name = "id")
    private String id;
    /**
     * 统一事件ID
     */
    @ApiModelProperty(value = "统一事件ID", name = "uEventId")
    private String uEventId;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID", name = "deviceId")
    private String deviceId;
    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码", name = "regionNo")
    private String regionNo;
    /**
     * 接收时间(从1970-01-01 08-00-00到现在的秒)
     */
    @ApiModelProperty(value = "接收时间(从1970-01-01 08-00-00到现在的秒)", name = "recTime")
    private Integer recTime;
    /**
     * 电源1信息
     */
    @ApiModelProperty(value = "电源1信息", name = "fld01")
    private String fld01;
    /**
     * 电源2信息
     */
    @ApiModelProperty(value = "电源2信息", name = "fld02")
    private String fld02;
    /**
     * 电源3信息
     */
    @ApiModelProperty(value = "电源3信息", name = "fld03")
    private String fld03;
    /**
     * 电源4信息
     */
    @ApiModelProperty(value = "电源4信息", name = "fld04")
    private String fld04;
    /**
     * 电源5信息
     */
    @ApiModelProperty(value = "电源5信息", name = "fld05")
    private String fld05;
    /**
     * 电源6信息
     */
    @ApiModelProperty(value = "电源6信息", name = "fld06")
    private String fld06;
    /**
     * 电源7信息
     */
    @ApiModelProperty(value = "电源7信息", name = "fld07")
    private String fld07;
    /**
     * 电源8信息
     */
    @ApiModelProperty(value = "电源8信息", name = "fld08")
    private String fld08;
    /**
     * 电源9信息
     */
    @ApiModelProperty(value = "电源9信息", name = "fld09")
    private String fld09;
    /**
     * 电源10信息
     */
    @ApiModelProperty(value = "电源10信息", name = "fld10")
    private String fld10;
    /**
     * 电源11信息
     */
    @ApiModelProperty(value = "电源11信息", name = "fld11")
    private String fld11;
    /**
     * 电源12信息
     */
    @ApiModelProperty(value = "电源12信息", name = "fld12")
    private String fld12;
    /**
     * 电源13信息
     */
    @ApiModelProperty(value = "电源13信息", name = "fld13")
    private String fld13;
    /**
     * 电源14信息
     */
    @ApiModelProperty(value = "电源14信息", name = "fld14")
    private String fld14;
    /**
     * 电源15信息
     */
    @ApiModelProperty(value = "电源15信息", name = "fld15")
    private String fld15;
    /**
     * 电源16信息
     */
    @ApiModelProperty(value = "电源16信息", name = "fld16")
    private String fld16;
    /**
     * 电源1告警
     */
    @ApiModelProperty(value = "电源1告警", name = "alarm01")
    private String alarm01;
    /**
     * 电源2告警
     */
    @ApiModelProperty(value = "电源2告警", name = "alarm02")
    private String alarm02;
    /**
     * 电源3告警
     */
    @ApiModelProperty(value = "电源3告警", name = "alarm03")
    private String alarm03;
    /**
     * 电源4告警
     */
    @ApiModelProperty(value = "电源4告警", name = "alarm04")
    private String alarm04;
    /**
     * 电源5告警
     */
    @ApiModelProperty(value = "电源5告警", name = "alarm05")
    private String alarm05;
    /**
     * 电源6告警
     */
    @ApiModelProperty(value = "电源6告警", name = "alarm06")
    private String alarm06;
    /**
     * 电源7告警
     */
    @ApiModelProperty(value = "电源7告警", name = "alarm07")
    private String alarm07;
    /**
     * 电源8告警
     */
    @ApiModelProperty(value = "电源8告警", name = "alarm08")
    private String alarm08;
    /**
     * 电源9告警
     */
    @ApiModelProperty(value = "电源9告警", name = "alarm09")
    private String alarm09;
    /**
     * 电源10告警
     */
    @ApiModelProperty(value = "电源10告警", name = "alarm10")
    private String alarm10;
    /**
     * 电源11告警
     */
    @ApiModelProperty(value = "电源11告警", name = "alarm11")
    private String alarm11;
    /**
     * 电源12告警
     */
    @ApiModelProperty(value = "电源12告警", name = "alarm12")
    private String alarm12;
    /**
     * 电源13告警
     */
    @ApiModelProperty(value = "电源13告警", name = "alarm13")
    private String alarm13;
    /**
     * 电源14告警
     */
    @ApiModelProperty(value = "电源14告警", name = "alarm14")
    private String alarm14;
    /**
     * 电源15告警
     */
    @ApiModelProperty(value = "电源15告警", name = "alarm15")
    private String alarm15;
    /**
     * 电源16告警
     */
    @ApiModelProperty(value = "电源16告警", name = "alarm16")
    private String alarm16;
    /**
     * 存储时间
     */
    @ApiModelProperty(value = "存储时间", name = "storeTime")
    private Date storeTime;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    private String applicationCode;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码", name = "tenantCode")
    private String tenantCode;
}
