package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author df
 * @date 2021/07/14 15:28
 */
@Data
public class AlarmSettingsVo {

    /**
     * 项目告警设置表ID
     */
    @ApiModelProperty(value = "项目告警设置表ID",name="id")
    private String id;
    
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "外接设备重启次数",name="projectName")
    private String projectName;
    /**
     * 外接设备重启次数
     */

    @ApiModelProperty(value = "外接设备重启次数",name="times",example = "1")
    private Integer times;
    /**
     * 重启间隔时间,单位秒
     */

    @ApiModelProperty(value = "重启间隔时间,单位秒",name="intervalTime",example = "30")
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
    private String storeTime;
}
