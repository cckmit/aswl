package com.aswl.as.ibrs.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*
* 用户关注的系统报警类型Entity
* @author dingfei
* @date 2019-10-29 15:54
*/

@ApiModel(value = "AlarmTypeUserFavorite",description = "用户关注的系统报警类型Entity")
@Data
public class AlarmTypeUserFavorite extends BaseEntity<AlarmTypeUserFavorite> {
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
    @ApiModelProperty(value = "短信告警类型订阅",name="smsAlarmType")
    private String smsAlarmType;
    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
}
