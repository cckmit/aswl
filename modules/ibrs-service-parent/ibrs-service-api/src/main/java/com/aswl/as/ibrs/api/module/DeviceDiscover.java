package com.aswl.as.ibrs.api.module;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备发现表
 */
@ApiModel(value = "DeviceDiscover",description = "设备发现Entity")
@Data
public class DeviceDiscover implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value = "主键",name = "id")
    private String id;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称",name = "deviceName")
    private String deviceName;

    /**
     * 出厂编码
     */
    @ApiModelProperty(value = "出厂编码",name = "deviceFactoryCode")
    private String deviceFactoryCode;

    /**
     * ip
     */
    @ApiModelProperty(value = "ip",name = "ip")
    private String ip;

    /**
    端口
     */
    @ApiModelProperty(value = "端口",name = "port")
    private String port;

    /**
     * 设备型号
     */
    @ApiModelProperty(value = "设备型号id",name = "deviceModelId")
    private String deviceModelId;

    /**
     * 存储时间
     */
    @ApiModelProperty(value = "存储时间",name = "storeTime")
    private Date storeTime;

    /**
     * 系统编码
     */
    @ApiModelProperty(value = "系统编码",name = "applicationCode")
    private String applicationCode;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码",name = "tenantCode")
    private String tenantCode;

    /**
     * 区域Id
     */
    @ApiModelProperty(value = "区域Id",name = "regionId")
    private String regionId;

    /**
     * 区域code
     */
    @ApiModelProperty(value = "区域code",name = "regionCode")
    private String regionCode;
}
