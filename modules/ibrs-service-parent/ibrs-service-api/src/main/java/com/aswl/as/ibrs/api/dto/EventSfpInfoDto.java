package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * sfp信息Dto
 *
 * @author df
 * @date 2021/07/26 22:09
 */

@ApiModel(value = "EventSfpInfoDto", description = "sfp信息Dto")
@Data
public class EventSfpInfoDto implements Serializable {

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
     * 通道有效位
     */
    @ApiModelProperty(value = "通道有效位", name = "passway")
    private String passway;
    /**
     * 光口1信息
     */
    @ApiModelProperty(value = "光口1信息", name = "fld01")
    private String fld01;
    /**
     * 光2信息
     */
    @ApiModelProperty(value = "光2信息", name = "fld02")
    private String fld02;
    /**
     * 光口3信息
     */
    @ApiModelProperty(value = "光口3信息", name = "fld03")
    private String fld03;
    /**
     * 光口4信息
     */
    @ApiModelProperty(value = "光口4信息", name = "fld04")
    private String fld04;
    /**
     * 光口5信息
     */
    @ApiModelProperty(value = "光口5信息", name = "fld05")
    private String fld05;
    /**
     * 光口6信息
     */
    @ApiModelProperty(value = "光口6信息", name = "fld06")
    private String fld06;
    /**
     * 光口7信息
     */
    @ApiModelProperty(value = "光口7信息", name = "fld07")
    private String fld07;
    /**
     * 光口8信息
     */
    @ApiModelProperty(value = "光口8信息", name = "fld08")
    private String fld08;
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
     * SAAS租户编码
     */
    @ApiModelProperty(value = "SAAS租户编码", name = "tenantCode")
    private String tenantCode;
}
