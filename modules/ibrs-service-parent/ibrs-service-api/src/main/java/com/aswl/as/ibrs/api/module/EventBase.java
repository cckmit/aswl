package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备事件-基础Entity
* @author zgl
* @date 2019-11-01 11:59
*/

@ApiModel(value = "EventBase",description = "设备事件-基础Entity")
@Data
public class EventBase extends BaseEntity<EventBase> {

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
    * 使能状态
    */

    @ApiModelProperty(value = "使能状态",name="useStatus")
    private Integer useStatus;
    /**
    * 物联网连接状态
    */

    @ApiModelProperty(value = "物联网连接状态",name="iotStatus")
    private Integer iotStatus;
    /**
    * 钥匙MAC
    */

    @ApiModelProperty(value = "钥匙MAC",name="keyMac")
    private String keyMac;
    /**
    * ID密文
    */

    @ApiModelProperty(value = "ID密文",name="ciphertextId")
    private String ciphertextId;
    /**
    * 授权时间
    */

    @ApiModelProperty(value = "授权时间",name="authTime")
    private Integer authTime;
    /**
    * 授权状态
    */

    @ApiModelProperty(value = "授权状态",name="authStatus")
    private Integer authStatus;
    /**
    * 门磁开关
    */

    @ApiModelProperty(value = "门磁开关",name="gateSwitch")
    private Integer gateSwitch;
    /**
    * 箱内照明开关
    */

    @ApiModelProperty(value = "箱内照明开关",name="lightingSwitch")
    private Integer lightingSwitch;
    /**
    * UTC时间
    */

    @ApiModelProperty(value = "UTC时间",name="utcTime")
    private Integer utcTime;
    /**
    * 经度
    */

    @ApiModelProperty(value = "经度",name="lng")
    private Double lng;
    /**
    * 纬度
    */

    @ApiModelProperty(value = "纬度",name="lat")
    private Double lat;
    /**
    * 海拔
    */

    @ApiModelProperty(value = "海拔",name="altitude")
    private Double altitude;
    /**
    * 预留1
    */

    @ApiModelProperty(value = "预留1",name="fld01")
    private String fld01;
    /**
    * 预留1
    */

    @ApiModelProperty(value = "预留1",name="fld02")
    private String fld02;
    /**
    * 预留3
    */

    @ApiModelProperty(value = "预留3",name="fld03")
    private String fld03;
    /**
    * 预留4
    */

    @ApiModelProperty(value = "预留4",name="fld04")
    private String fld04;
    /**
    * 预留5
    */

    @ApiModelProperty(value = "预留5",name="fld05")
    private String fld05;
    /**
    * 预留6
    */

    @ApiModelProperty(value = "预留6",name="fld06")
    private String fld06;
    /**
    * 预留7
    */

    @ApiModelProperty(value = "预留7",name="fld07")
    private String fld07;
    /**
    * 预留8
    */

    @ApiModelProperty(value = "预留8",name="fld08")
    private String fld08;
    /**
    * 存储时间
    */

    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
