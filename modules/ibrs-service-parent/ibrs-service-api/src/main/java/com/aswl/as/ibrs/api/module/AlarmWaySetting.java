package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 告警方式设置表Entity
* @author df
* @date 2021/07/10 17:16
*/

@ApiModel(value = "AlarmWaySetting",description = "告警方式设置表Entity")
@Data
public class AlarmWaySetting extends BaseEntity<AlarmWaySetting> {
    /**
    * 响铃(0,禁用,1,启用)
    */

    @ApiModelProperty(value = "响铃(0,禁用,1,启用)",name="bell")
    private Integer bell;
    /**
    * 语音(0,禁用,1,启用)
    */

    @ApiModelProperty(value = "语音(0,禁用,1,启用)",name="voice")
    private Integer voice;
    /**
    * 通知(0,禁用,1,启用)
    */

    @ApiModelProperty(value = "通知(0,禁用,1,启用)",name="notice")
    private Integer notice;
    /**
    * 短信(0,禁用,1,启用)
    */

    @ApiModelProperty(value = "短信(0,禁用,1,启用)",name="message")
    private Integer message;
    /**
    * 声光(0,禁用,1,启用)
    */

    @ApiModelProperty(value = "声光(0,禁用,1,启用)",name="soundLight")
    private Integer soundLight;
    /**
    * 弹窗(0,禁用,1,启用)
    */

    @ApiModelProperty(value = "弹窗(0,禁用,1,启用)",name="popupWindow")
    private Integer popupWindow;
    /**
    * 窗口抖动(0,禁用,1,启用)
    */

    @ApiModelProperty(value = "窗口抖动(0,禁用,1,启用)",name="windowTremble")
    private Integer windowTremble;
    /**
    * 移动终端振动(0,禁用,1,启用)
    */

    @ApiModelProperty(value = "移动终端振动(0,禁用,1,启用)",name="terminalVibration")
    private Integer terminalVibration;
    /**
    * 级别
    */

    @ApiModelProperty(value = "级别",name="alarmLevel")
    private Integer alarmLevel;
    /**
    * 区域编码
    */

    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;
}
