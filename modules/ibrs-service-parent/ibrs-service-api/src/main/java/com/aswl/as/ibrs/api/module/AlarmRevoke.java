package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 报警撤销记录表Entity
* @author dingfei
* @date 2020-01-08 10:42
*/

@ApiModel(value = "AlarmRevoke",description = "报警撤销记录表Entity")
@Data
public class AlarmRevoke extends BaseEntity<AlarmRevoke> {
    /**
    * 统一事件ID
    */

    @ApiModelProperty(value = "统一事件ID",name="uEventId")
    private String uEventId;
    /**
    * 设备ID
    */

    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
    * 报警类型
    */

    @ApiModelProperty(value = "报警类型",name="alarmTypes")
    private String alarmTypes;
    /**
    * 报警类型中文描述
    */

    @ApiModelProperty(value = "报警类型中文描述",name="alarmTypesDes")
    private String alarmTypesDes;
    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remarks")
    private String remarks;
}
