package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 用电单位设备关联表Dto
* @author df
* @date 2021/06/01 20:56
*/

@ApiModel(value = "ElectricUnitDeviceDto",description = "用电单位设备关联表Dto")
@Data
public class ElectricUnitDeviceDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 用电单位ID
    */
    @ApiModelProperty(value = "用电单位ID",name="unitId")
    private String unitId;
    /**
    * 设备ID
    */
    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
}
