package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
*
* 区域负责人Dto
* @author dingfei
* @date 2019-11-08 10:20
*/

@ApiModel(value = "RegionLeaderDto",description = "区域负责人Dto")
@Data
public class RegionLeaderDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID",name="id")
    private String id;
    /**
    * 用户ID，可空
    */
    @ApiModelProperty(value = "用户ID，可空",name="userId")
    private String userId;
    /**
    * 区域ID
    */
    @ApiModelProperty(value = "区域ID",name="regionId")
    private String regionId;
    /**
    * 区域编码
    */
    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;
    /**
    * 负责人姓名
    */
    @ApiModelProperty(value = "负责人姓名",name="userName")
    private String userName;
    /**
    * 负责人手机
    */
    @ApiModelProperty(value = "负责人手机",name="userMobile")
    private String userMobile;
    /**
    * 是否接收报警消息
    */
    @ApiModelProperty(value = "是否接收报警消息",name="isReceiveAlarm")
    private Integer isReceiveAlarm;
    /**
    * 接收消息开始时间
    */
    @ApiModelProperty(value = "接收消息开始时间",name="receiveStartAt")
    private String receiveStartAt;
    /**
    * 接收消息结束时间
    */
    @ApiModelProperty(value = "接收消息结束时间",name="receiveEndAt")
    private String receiveEndAt;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
    /**
    * 系统编号
    */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
    * SAAS租户编码
    */
    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;

    //TODO --------------- 下面字段只作为查询使用-------------------------------
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;


    /**
     * 是否巡更
     */
    @ApiModelProperty(value = "是否巡更",name = "isPatrol")
    private Integer isPatrol;

    /**
     * 巡更钥匙ID
     */
    @ApiModelProperty(value = "巡更钥匙ID",name = "patrolKeyId")
    private String patrolKeyId;

    /**
     * 巡更周期数量
     */
    @ApiModelProperty(value = "巡更周期数量",name = "patrolPeriodNum")
    private Integer patrolPeriodNum;

    /**
     * 巡更周期单位
     */
    @ApiModelProperty(value = "巡更周期单位",name = "patrolPeriodUnit")
    private String patrolPeriodUnit;

    /**
     * 巡更周期（时间戳，单位为秒）
     */
    @ApiModelProperty(value = "巡更周期（时间戳，单位为秒）",name = "patrolPeriod")
    private Integer patrolPeriod;

    /**
     * 巡更周期开始时间
     */
    @ApiModelProperty(value = "巡更周期开始时间",name = "patrolPeriodBeginTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date patrolPeriodBeginTime;

}
