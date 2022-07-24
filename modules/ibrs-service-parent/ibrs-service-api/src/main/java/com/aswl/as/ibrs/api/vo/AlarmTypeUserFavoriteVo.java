package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-29 17:01
 * @Version V1
 */
@Data
public class AlarmTypeUserFavoriteVo implements java.io.Serializable {

    /**
     * 用户ID
     */
    @ApiModelProperty(value ="用户ID" ,name = "userId")
    private String userId;

    /**
     * 报警类型
     */
    @ApiModelProperty(value ="报警类型" ,name = "alarmType")
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
     * 报警类型名称
     */
    @ApiModelProperty(value ="报警类型名称" ,name = "alarmTypeName")
    private String alarmTypeName;
}
