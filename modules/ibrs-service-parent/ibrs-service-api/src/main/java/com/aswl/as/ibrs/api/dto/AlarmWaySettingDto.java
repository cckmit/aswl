package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 告警方式设置表Dto
 *
 * @author df
 * @date 2021/07/10 17:16
 */

@ApiModel(value = "AlarmWaySettingDto", description = "告警方式设置表Dto")
@Data
public class AlarmWaySettingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", name = "id")
    private String id;
    /**
     * 响铃(0,禁用,1,启用)
     */
    @ApiModelProperty(value = "响铃(0,禁用,1,启用)", name = "bell")
    private Integer bell;
    /**
     * 语音(0,禁用,1,启用)
     */
    @ApiModelProperty(value = "语音(0,禁用,1,启用)", name = "voice")
    private Integer voice;
    /**
     * 通知(0,禁用,1,启用)
     */
    @ApiModelProperty(value = "通知(0,禁用,1,启用)", name = "notice")
    private Integer notice;
    /**
     * 短信(0,禁用,1,启用)
     */
    @ApiModelProperty(value = "短信(0,禁用,1,启用)", name = "message")
    private Integer message;
    /**
     * 声光(0,禁用,1,启用)
     */
    @ApiModelProperty(value = "声光(0,禁用,1,启用)", name = "soundLight")
    private Integer soundLight;
    /**
     * 弹窗(0,禁用,1,启用)
     */
    @ApiModelProperty(value = "弹窗(0,禁用,1,启用)", name = "popupWindow")
    private Integer popupWindow;
    /**
     * 窗口抖动(0,禁用,1,启用)
     */
    @ApiModelProperty(value = "窗口抖动(0,禁用,1,启用)", name = "windowTremble")
    private Integer windowTremble;
    /**
     * 移动终端振动(0,禁用,1,启用)
     */
    @ApiModelProperty(value = "移动终端振动(0,禁用,1,启用)", name = "terminalVibration")
    private Integer terminalVibration;
    /**
     * 级别
     */
    @ApiModelProperty(value = "级别", name = "alarmLevel")
    private Integer alarmLevel;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", name = "projectId")
    private String projectId;
    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码", name = "regionCode")
    private String regionCode;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    private String applicationCode;
    /**
     * SAAS租户编码
     */
    @ApiModelProperty(value = "SAAS租户编码", name = "tenantCode")
    private String tenantCode;
}
