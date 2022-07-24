package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 报警撤销记录表Dto
 *
 * @author dingfei
 * @date 2020-01-08 10:42
 */

@ApiModel(value = "AlarmRevokeDto", description = "报警撤销记录表Dto")
@Data
public class AlarmRevokeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", name = "id")
    private String id;
    /**
     * 统一事件ID
     */
    @ApiModelProperty(value = "统一事件ID", name = "uEventId")
    private String uEventId;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID", name = "deviceId")
    private String deviceId;
    /**
     * 报警类型
     */
    @ApiModelProperty(value = "报警类型", name = "alarmTypes")
    private String alarmTypes;
    /**
     * 报警类型中文描述
     */
    @ApiModelProperty(value = "报警类型中文描述", name = "alarmTypesDes")
    private String alarmTypesDes;
    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人", name = "creator")
    private String creator;
    /**
     * 操作时间
     */
    @ApiModelProperty(value = "操作时间", name = "createDate")
    private Date createDate;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remarks")
    private String remarks;
}
