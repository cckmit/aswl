package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件-姿态信息Entity
* @author zgl
* @date 2022-07-15 16:36
*/

@ApiModel(value = "EventPosture",description = "设备事件-姿态信息Entity")
@Data
public class EventPosture extends BaseEntity<EventPosture> {

    /**
    * ID主键
    */

    @ApiModelProperty(value = "ID主键",name="id")
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
    * X轴偏移（单位：°）
    */

    @ApiModelProperty(value = "X轴偏移（单位：°）",name="offsetX")
    private Float offsetX;
    /**
    * Y轴偏移（单位：°）
    */

    @ApiModelProperty(value = "Y轴偏移（单位：°）",name="offsetY")
    private Float offsetY;
    /**
    * Z轴偏移（单位：°）
    */

    @ApiModelProperty(value = "Z轴偏移（单位：°）",name="offsetZ")
    private Float offsetZ;
    /**
    * X轴加速度（单位：g）
    */

    @ApiModelProperty(value = "X轴加速度（单位：g）",name="accelSpeedX")
    private Float accelSpeedX;
    /**
    * Y轴加速度（单位：g）
    */

    @ApiModelProperty(value = "Y轴加速度（单位：g）",name="accelSpeedY")
    private Float accelSpeedY;
    /**
    * Z轴加速度（单位：g）
    */

    @ApiModelProperty(value = "Z轴加速度（单位：g）",name="accelSpeedZ")
    private Float accelSpeedZ;
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
