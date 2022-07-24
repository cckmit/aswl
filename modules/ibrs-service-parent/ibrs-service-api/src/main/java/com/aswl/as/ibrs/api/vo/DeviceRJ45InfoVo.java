package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备网络口信息Vo
 */
@ApiModel(value = "DeviceRJ45InfoVo",description = "设备网络口信息Vo")
@Data
public class DeviceRJ45InfoVo implements Serializable {

    /**
     * 电口1
     */
    @ApiModelProperty(value = "网络口01信息（-9:禁用, 其他值:启用）",name = "fld01")
    private String fld01;
    /**
     * 电口2
     */
    @ApiModelProperty(value = "网络口02信息（-9:禁用, 其他值:启用）",name = "fld02")
    private String fld02;
    /**
     * 电口3
     */
    @ApiModelProperty(value = "网络口03信息（-9:禁用, 其他值:启用）",name = "fld03")
    private String fld03;
    /**
     * 电口4
     */
    @ApiModelProperty(value = "网络口04信息（-9:禁用, 其他值:启用）",name = "fld04")
    private String fld04;
    /**
     * 电口5
     */
    @ApiModelProperty(value = "网络口05信息（-9:禁用, 其他值:启用）",name = "fld05")
    private String fld05;
    /**
     * 电口6
     */
    @ApiModelProperty(value = "网络口06信息（-9:禁用, 其他值:启用）",name = "fld06")
    private String fld06;
    /**
     * 电口7
     */
    @ApiModelProperty(value = "网络口07信息（-9:禁用, 其他值:启用）",name = "fld07")
    private String fld07;
    /**
     * 电口8
     */
    @ApiModelProperty(value = "网络口08信息（-9:禁用, 其他值:启用）",name = "fld08")
    private String fld08;
    /**
     * 电口9
     */
    @ApiModelProperty(value = "网络口09信息（-9:禁用, 其他值:启用）",name = "fld09")
    private String fld09;
    /**
     * 电口10
     */
    @ApiModelProperty(value = "网络口10信息（-9:禁用, 其他值:启用）",name = "fld10")
    private String fld10;
    /**
     * 电口11
     */
    @ApiModelProperty(value = "网络口11信息（-9:禁用, 其他值:启用）",name = "fld11")
    private String fld11;
    /**
     * 电口12
     */
    @ApiModelProperty(value = "网络口12信息（-9:禁用, 其他值:启用）",name = "fld12")
    private String fld12;
    /**
     * 电口13
     */
    @ApiModelProperty(value = "网络口13信息（-9:禁用, 其他值:启用）",name = "fld13")
    private String fld13;
    /**
     * 电口14
     */
    @ApiModelProperty(value = "网络口14信息（-9:禁用, 其他值:启用）",name = "fld14")
    private String fld14;
    /**
     * 电口15
     */
    @ApiModelProperty(value = "网络口15信息（-9:禁用, 其他值:启用）",name = "fld15")
    private String fld15;
    /**
     * 电口16
     */
    @ApiModelProperty(value = "网络口16信息（-9:禁用, 其他值:启用）",name = "fld16")
    private String fld16;
}
