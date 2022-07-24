package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class EventEcurrentVo extends BaseEntity<EventEcurrentVo> {

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
     * 通道有效位1
     */
    private String passway1;
    /**
     * 通道有效位2
     */
    private String passway2;
    /**
     *
     */
    private String passway3;
    /**
     *
     */
    private String passway4;
    /**
     * 电流X
     */
    private Integer fldx;
    /**
     * 电流Y
     */
    private Integer fldy;
    /**
     * 总电流
     */
    private Integer fldall;
    /**
     * 电流1
     */
    private Integer fld01;
    /**
     * 电流2
     */
    private Integer fld02;
    /**
     * 电流3
     */
    private Integer fld03;
    /**
     * 电流4
     */
    private Integer fld04;
    /**
     * 电流5
     */
    private Integer fld05;
    /**
     * 电流6
     */
    private Integer fld06;
    /**
     * 电流17
     */
    private Integer fld07;
    /**
     * 电流8
     */
    private Integer fld08;
    /**
     * 电流9
     */
    private Integer fld09;
    /**
     * 电流10
     */
    private Integer fld10;
    /**
     * 电流11
     */
    private Integer fld11;
    /**
     * 电流12
     */
    private Integer fld12;
    /**
     * 电流13
     */
    private Integer fld13;
    /**
     * 电流14
     */
    private Integer fld14;
    /**
     * 电流15
     */
    private Integer fld15;
    /**
     * 电流16
     */
    private Integer fld16;
    /**
     * 预留17
     */
    private Integer fld17;
    /**
     * 预留18
     */
    private Integer fld18;
    /**
     * 预留19
     */
    private Integer fld19;
    /**
     * 预留20
     */
    private Integer fld20;
    /**
     * 预留21
     */
    private Integer fld21;
    /**
     * 预留22
     */
    private Integer fld22;
    /**
     * 预留23
     */
    private Integer fld23;
    /**
     * 预留24
     */
    private Integer fld24;
    /**
     * 存储时间
     */
    private Date storeTime;
    /**
     * 系统编号
     */
    private String applicationCode;
    /**
     * SAAS租户编码
     */
    private String tenantCode;
}
