package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
*
* 区域负责人Entity
* @author dingfei
* @date 2019-11-08 10:20
*/

@ApiModel(value = "RegionLeader",description = "区域负责人Entity")
@Data
public class RegionLeader extends BaseEntity<RegionLeader> {
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
