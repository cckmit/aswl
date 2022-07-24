package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 用户关注的设备报警类型Dto
* @author dingfei
* @date 2019-10-31 13:58
*/

@ApiModel(value = "AlarmTypeDeviceFavoriteDto",description = "用户关注的设备报警类型Dto")
@Data
public class AlarmTypeDeviceFavoriteDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 
    */
    @ApiModelProperty(value = "",name="id")
    private String id;
    /**
    * 用户
    */
    @ApiModelProperty(value = "用户",name="userId")
    private String userId;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="deviceId")
    private String deviceId;
    /**
    * 报警类型
    */
    @ApiModelProperty(value = "报警类型",name="alarmType")
    private String alarmType;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="remark")
    private String remark;
}
