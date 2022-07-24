package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件-电量Entity
* @author hzj
* @date 2021/06/01 19:05
*/

@ApiModel(value = "EventElectricity",description = "设备事件-电量Entity")
@Data
public class EventElectricity extends BaseEntity<EventElectricity> {

    /**
    * 主键
    */

    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 统一事件ID
    */

    @ApiModelProperty(value = "统一事件ID",name="uEventId")
    private String uEventId;
    /**
    * 设备ID
    */

    @ApiModelProperty(value = "设备ID",name="deviceId")
    private String deviceId;
    /**
    * 区域编码
    */

    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;
    /**
    * 接收时间(从1970-01-01 08-00-00到现在的秒)
    */

    @ApiModelProperty(value = "接收时间(从1970-01-01 08-00-00到现在的秒)",name="recTime")
    private Integer recTime;
    /**
    * 通道有效位
    */

    @ApiModelProperty(value = "通道有效位",name="passway")
    private String passway;
    /**
    * 漏电流
    */

    @ApiModelProperty(value = "漏电流",name="leakage")
    private Double leakage;
    /**
    * 合闸次数
    */

    @ApiModelProperty(value = "合闸次数",name="switchNum")
    private Integer switchNum;
    /**
    * 过载阈值
    */

    @ApiModelProperty(value = "过载阈值",name="overLoad")
    private Double overLoad;
    /**
    * 欠压阈值
    */

    @ApiModelProperty(value = "欠压阈值",name="underVoltage")
    private Double underVoltage;
    /**
    * 电量
    */

    @ApiModelProperty(value = "电量",name="electricity")
    private Double electricity;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
    /**
    * 系统编号
    */

    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
    * 租户编码
    */

    @ApiModelProperty(value = "租户编码",name="tenantCode")
    private String tenantCode;

}
