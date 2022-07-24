package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 用户关注的系统报警类型Dto
* @author dingfei
* @date 2019-10-31 15:03
*/

@ApiModel(value = "AlarmTypeUserFavoriteDto",description = "用户关注的系统报警类型Dto")
@Data
public class AlarmTypeUserFavoriteDto implements Serializable {

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
     * 报警类型
     */
    @ApiModelProperty(value = "报警类型",name="alarmType")
    private String alarmType;

    /**
     * APP告警类型订阅
     */
    @ApiModelProperty(value = "APP告警类型订阅",name="appAlarmType")
    private String appAlarmType;

    /**
     * 短信告警类型订阅
     */
    @ApiModelProperty(value = "报警类型",name="smsAlarmType")
    private String smsAlarmType;
    
    /**
    * 
    */
    @ApiModelProperty(value = "",name="remark")
    private String remark;
}
