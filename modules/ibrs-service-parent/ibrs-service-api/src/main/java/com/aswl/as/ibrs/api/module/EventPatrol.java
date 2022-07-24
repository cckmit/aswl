package com.aswl.as.ibrs.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 设备蓝牙巡更Entity
 */
@ApiModel(value = "EventPatrol",description = "设备事件-巡更")
@Data
@Getter
@Setter
public class EventPatrol extends BaseEntity<EventPatrol> {

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
}
