package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 告警设置Dto
* @author hzj
* @date 2020/12/08 10:43
*/

@ApiModel(value = "AlarmSettingsDto",description = "告警设置Dto")
@Data
public class AlarmSettingsDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID
    */
    @ApiModelProperty(value = "ID",name="id")
    private String id;
    /**
    * 外接设备重启次数
    */
    @ApiModelProperty(value = "外接设备重启次数",name="times")
    private Integer times;
    /**
    * 重启间隔时间,单位秒
    */
    @ApiModelProperty(value = "重启间隔时间,单位秒",name="intervalTime")
    private Integer intervalTime;
    /**
    * 需要抓拍的告警类型,以逗号分隔
    */
    @ApiModelProperty(value = "需要抓拍的告警类型,以逗号分隔",name="alarmType")
    private String alarmType;
    /**
    * 存储时间
    */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
    /**
    * 系统编码
    */
    @ApiModelProperty(value = "系统编码",name="applicationCode")
    private String applicationCode;

    /**
     * 项目id
     */
    @ApiModelProperty(value = "项目id",name="projectId")
    private String projectId;

    /**
    * 租户编码
    */
    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;

    /**
     * 是否云平台
     */
    @ApiModelProperty(value = "是否云平台",name="isAsos")
    private String isAsos;
}
