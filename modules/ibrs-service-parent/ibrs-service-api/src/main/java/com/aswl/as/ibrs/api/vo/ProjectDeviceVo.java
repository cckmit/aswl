package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/4/21 16:34
 */
@Data
@ApiModel(value = "ProjectDeviceVo",description = "项目设备")
public class ProjectDeviceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备名称",name="deviceName")
    private String deviceName;

    @ApiModelProperty(value = "设备编码",name="deviceCode")
    private String deviceCode;

    @ApiModelProperty(value = "区域名称",name="regionName")
    private String regionName;

    @ApiModelProperty(value = "设备种类",name="deviceKindName")
    private String deviceKindName;

    @ApiModelProperty(value = "设备类型",name="deviceTypeName")
    private String deviceTypeName;

    @ApiModelProperty(value = "设备型号",name="deviceModelName")
    private String deviceModelName;

    @ApiModelProperty(value = "厂家负责人",name="vendorLeader")
    private String vendorLeader;

    @ApiModelProperty(value = "售后负责人",name="afterSaleLeader")
    private String afterSaleLeader;

    @ApiModelProperty(value = "售后电话",name="afterSalePhone")
    private String afterSalePhone;

    @ApiModelProperty(value = "设备参数",name="description")
    private String description;

    @ApiModelProperty(value = "设备参数标记",name="flag",example = "1 代表有设备参数,0代表没有设备参数")
    private Integer flag;
}
