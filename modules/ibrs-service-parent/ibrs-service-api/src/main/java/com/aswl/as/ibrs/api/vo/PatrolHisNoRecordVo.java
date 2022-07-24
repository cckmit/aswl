package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;

/**
 * 未巡更记录Vo
 */
@ApiModel(value = "PatrolHisNoRecordVo",description = "未巡更记录Vo")
@Data
public class PatrolHisNoRecordVo implements Serializable {

    /**
     * 主键Id
     */
    @ApiModelProperty(value = "主键Id",name = "id")
    private String id;

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
     * 巡更周期开始
     */
    @ApiModelProperty(value = "巡更周期开始",name = "periodBeginDate")
    private String periodBeginDate;

    /**
     * 巡更周期结束
     */
    @ApiModelProperty(value = "巡更周期结束",name = "periodEndDate")
    private String periodEndDate;

    /**
     * 密文Id
     */
    @ApiModelProperty(value = "密文Id",name = "idCipher")
    private String idCipher;

    /**
     * 未巡更次数
     */
    @ApiModelProperty(value = "未巡更次数",name = "noPatrolNum")
    private Integer noPatrolNum;

    /**
     * 巡更周期
     */
    @ApiModelProperty(value = "巡更周期",name = "patrolPeriod")
    private String patrolPeriod;

    /**
     * 区域code
     */
    @ApiModelProperty(value = "区域code",name = "regionCode")
    private String regionCode;

    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目编码",name = "applicationCode")
    private String applicationCode;

    /**
     * 租户
     */
    @ApiModelProperty(value = "租户",name = "tenantCode")
    private String tenantCode;

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
