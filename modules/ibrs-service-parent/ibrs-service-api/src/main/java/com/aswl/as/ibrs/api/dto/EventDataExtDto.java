package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件数据扩展Dto
* @author zgl
* @date 2021-08-16 13:57
*/

@ApiModel(value = "EventDataExtDto",description = "设备事件数据扩展Dto")
@Data
public class EventDataExtDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID主键
    */
    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 设备ID
    */
    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
    * 数据
    */
    @ApiModelProperty(value = "数据",name="data")
    private String data;
}
