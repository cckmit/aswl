package com.aswl.as.ibrs.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*
* 用户关注的设备报警类型Entity
* @author dingfei
* @date 2019-10-31 13:58
*/

@ApiModel(value = "AlarmTypeDeviceFavorite",description = "用户关注的设备报警类型Entity")
@Data
public class AlarmTypeDeviceFavorite extends BaseEntity<AlarmTypeDeviceFavorite> {
    /**
    * 用户
    */

    @ApiModelProperty(value = "用户id",name="userId")
    private String userId;
    /**
    * 设备id
    */

    @ApiModelProperty(value = "设备id",name="deviceId")
    private String deviceId;
    /**
    * 报警类型
    */

    @ApiModelProperty(value = "报警类型",name="alarmType")
    private String alarmType;
    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
}
