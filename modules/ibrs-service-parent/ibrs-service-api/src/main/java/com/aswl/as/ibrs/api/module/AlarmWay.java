package com.aswl.as.ibrs.api.module;

import java.util.Date;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报警方式Entity
 *
 * @author dingfei
 * @date 2019-11-06 09:51
 */

@ApiModel(value = "AlarmWay", description = "报警方式Entity")
@Data
public class AlarmWay extends BaseEntity<AlarmWay> {
    /**
     * 报警方式
     */

    @ApiModelProperty(value = "报警方式", name = "alarmMethod")
    private String alarmMethod;
    /**
     * 报警方式名
     */

    @ApiModelProperty(value = "报警方式名", name = "alarmMethodName")
    private String alarmMethodName;
    /**
     *
     */

    @ApiModelProperty(value = "", name = "isOn")
    private Integer isOn;
    /**
     * 优先权
     */

    @ApiModelProperty(value = "优先权", name = "priority")
    private Integer priority;
    /**
     * 终端类型组
     */

    @ApiModelProperty(value = "终端类型组", name = "terminalTypeGroup")
    private String terminalTypeGroup;
    /**
     *
     */

    @ApiModelProperty(value = "", name = "createTerminal")
    private String createTerminal;
    /**
     * 修改终端
     */

    @ApiModelProperty(value = "修改终端", name = "modifyTerminal")
    private String modifyTerminal;
    /**
     *
     */

    @ApiModelProperty(value = "", name = "lowestAlarmLevel")
    private Integer lowestAlarmLevel;
    /**
     * /**
     * 备注
     */

    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
}
