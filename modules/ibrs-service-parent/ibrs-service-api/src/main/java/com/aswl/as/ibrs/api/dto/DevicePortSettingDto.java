package com.aswl.as.ibrs.api.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 *  设备端口控制Dto
 * @author df
 * @date 2021/06/05 15:29
 */
@ApiModel(value = "DevicePortSettingDto",description = "设备端口控制Dto")
@Data
public class DevicePortSettingDto implements Serializable {
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID",name = "deviceId")
    private String deviceId;

    /**
     * 标记(1、光纤口,2、网络口)
     */
    @ApiModelProperty(value = "标记(1、光纤口,2、网络口)",name = "flag")
    private String flag;

    /**
     * 状态(0、启用,1、禁用)
     */
    @ApiModelProperty(value = "状态(0、启用,1、禁用)",name = "status")
    private String status;

    /**
     * 更新字段
     */
    @ApiModelProperty(value = "更新字段",name = "fld")
    private String fld;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称",name = "fldName")
    private String fldName;
    
}
