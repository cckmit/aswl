package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备设置Entity
* @author dingfei
* @date 2019-12-18 16:24
*/

@ApiModel(value = "DeviceSettings",description = "设备设置Entity")
@Data
public class DeviceSettings extends BaseEntity<DeviceSettings> {
    /**
    * 设备ID
    */

    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
    * 设置类型
    */

    @ApiModelProperty(value = "设置类型",name="setType")
    private String setType;
    /**
    * 设置模式
    */

    @ApiModelProperty(value = "设置模式",name="mode")
    private String mode;
    /**
    * 设置值
    */

    @ApiModelProperty(value = "设置值",name="value")
    private String  value;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
    /**
    * 系统编码
    */

    @ApiModelProperty(value = "系统编码",name="applicationCode")
    private String applicationCode;
    /**
    * 租户编码
    */

    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;
}
