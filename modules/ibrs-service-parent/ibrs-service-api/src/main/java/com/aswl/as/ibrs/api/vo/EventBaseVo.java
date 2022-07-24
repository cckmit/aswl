package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 设备事件-基础Vo
 *
 * @author liuliepan
 * @date 2019-10-22 15:29
 */
@Data
public class EventBaseVo extends BaseEntity<EventBaseVo> {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;
    /**
     * 统一事件ID
     */
    private Integer uEventId;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 区域编码
     */
    private String regionNo;
    /**
     * 接收时间(从1970-01-01 08-00-00到现在的秒)
     */
    private Integer recTime;
    /**
     * 使能状态
     */
    private Integer useStatus;
    /**
     * 物联网连接状态
     */
    private Integer iotStatus;
    /**
     * 钥匙MAC
     */
    private String keyMac;
    /**
     * ID密文
     */
    private String ciphertextId;
    /**
     * 授权时间
     */
    private Integer authTime;
    /**
     * 授权状态
     */
    private Integer authStatus;
    /**
     * 门磁开关
     */
    private Integer gateSwitch;
    /**
     * 箱内照明开关
     */
    private Integer lightingSwitch;
    /**
     * UTC时间
     */
    private Integer utcTime;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 海拔
     */
    private Double altitude;
    /**
     * 预留1
     */
    private String fld01;
    /**
     * 预留1
     */
    private String fld02;
    /**
     * 预留3
     */
    private String fld03;
    /**
     * 预留4
     */
    private String fld04;
    /**
     * 预留5
     */
    private String fld05;
    /**
     * 预留6
     */
    private String fld06;
    /**
     * 预留7
     */
    private String fld07;
    /**
     * 预留8
     */
    private String fld08;
    /**
     * 存储时间
     */
    private Date storeTime;
    /**
     * 系统编号
     */
    private String applicationCode;
    /**
     *
     */
    private String tenantCode;

}
