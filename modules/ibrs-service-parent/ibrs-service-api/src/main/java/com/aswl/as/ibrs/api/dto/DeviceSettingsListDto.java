package com.aswl.as.ibrs.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
*
* 设备设置Dto
* @author dingfei
* @date 2019-12-18 16:24
*/

@ApiModel(value = "DeviceSettingsListDto",description = "设备设置Dto")
@Data
public class DeviceSettingsListDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 设置类型
    */
    @ApiModelProperty(value = "设置类型",name="setType")
    private String type;

    /**
     * 设备设置Dto
     */
    @ApiModelProperty(value = "设备设置Dto",name="list")
    private List<DeviceSettingsDto> deviceSettingsDtoList=new ArrayList<>();


}
