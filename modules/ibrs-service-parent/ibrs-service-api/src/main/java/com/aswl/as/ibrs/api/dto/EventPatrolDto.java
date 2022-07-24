package com.aswl.as.ibrs.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 设备蓝牙巡更Entity
 */
@ApiModel(value = "EventPatrolDto",description = "设备事件-巡更Dto")
@Data
@Getter
@Setter
public class EventPatrolDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */

    @ApiModelProperty(value = "主键", name = "id")
    protected String id;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;

    /**
     * 接收时间(从1970-01-01 08-00-00到现在的秒)
     */
    @ApiModelProperty(value = "接收时间",name="recTime")
    private long recTime;

    /**
     * 钥匙MAC
     */
    @ApiModelProperty(value = "钥匙MAC",name="keyMac")
    private String keyMac;

    /**
     * ID密文
     */
    @ApiModelProperty(value = "ID密文",name="idCipher")
    private String idCipher;

    /**
     * 授权时间
     */
    @ApiModelProperty(value = "授权时间",name="authTime")
    private Date authTime;

    /**
     * 存储时间
     */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;

    /**
     * 负责人id
     */
    @ApiModelProperty(value = "负责人id",name = "regionLeaderId")
    private String regionLeaderId;

    /**
     * 负责人名字
     */
    @ApiModelProperty(value = "负责人id",name = "regionLeaderName")
    private String regionLeaderName;

    /**
     * 设备名
     */
    @ApiModelProperty(value = "设备名",name = "deviceName")
    private String deviceName;

    /**
     * 巡更开始时间
     */
    @ApiModelProperty(value = "巡更开始时间",name = "patrolStartTime")
    private String patrolStartTime;

    /**
     * 巡更结束时间
     */
    @ApiModelProperty(value = "巡更结束时间",name = "patrolEndTime")
    private String patrolEndTime;

    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码",name = "tenantCode")
    private String tenantCode;

    /**
     *项目id
     */
    @ApiModelProperty(value = "项目id",name = "projectId")
    private String projectId;

    /**
     * 系统编码
     */
    @ApiModelProperty(value = "系统编码",name = "applicationCode")
    private String applicationCode;

    /**
     *查询的表
     */
    @ApiModelProperty(value = "查询的表",name = "tableNames")
    private List<String> tableNames;

    /**
     * 项目过滤
     */
    @ApiModelProperty(value = "项目过滤",name = "queryProjectId")
    private String queryProjectId;

    /**
     * 多条件查询参数
     */
    @ApiModelProperty(value = "多条件查询参数",name = "query")
    private String query;
}
