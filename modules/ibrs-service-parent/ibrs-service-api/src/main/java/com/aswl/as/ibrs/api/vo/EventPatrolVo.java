package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 巡更Vo
 */
@Data
public class EventPatrolVo implements Serializable {

    /**
     * 负责人名字
     */
    @ApiModelProperty(value = "负责人id",name = "regionLeaderName")
    private String regionLeaderName;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域名",name = "regionName")
    private String regionName;

    /**
     * 设备名
     */
    @ApiModelProperty(value = "设备名",name = "deviceName")
    private String deviceName;

    /**
     * 巡更时间
     */
    @ApiModelProperty(value = "巡更时间",name = "patrolTime")
    private String patrolTime;

    /**
     * 密文Id
     */
    @ApiModelProperty(value = "密文Id",name = "idCipher")
    private String idCipher;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称",name = "projectName")
    private String projectName;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID",name = "deviceId")
    private String deviceId;
}
