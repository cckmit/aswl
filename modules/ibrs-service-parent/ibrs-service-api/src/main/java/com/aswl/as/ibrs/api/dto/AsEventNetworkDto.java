package com.aswl.as.ibrs.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel(value = "AsEventNetworkDto",description = "设备网络Dto")
public class AsEventNetworkDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "主键",name = "id")
    private String id;

    @ApiModelProperty(value = "设备ID",name = "deviceId")
    private Integer deviceId;

    @ApiModelProperty(value = "区域编码",name = "regionNo")
    private String regionNo;

    @ApiModelProperty(value = "网络状态",name = "networkState")
    private String networkState;

    @ApiModelProperty(value = "存储时间",name = "storeTime")
    private String storeTime;

    @ApiModelProperty(value = "系统编号",name = "applicationCode")
    private String applicationCode;

    @ApiModelProperty(value = "租户编码",name = "tenantCode")
    private String tenantCode;
}
