package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

import java.util.Date;
@Data
public class EventAlarmVO extends BaseEntity<EventAlarmVO> {
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
     * 预留通道3
     */
    private String passway3;
    /**
     * 箱门
     */
    private Integer fld01;
    /**
     * 授权
     */
    private Integer fld02;
    /**
     * 风扇1
     */
    private Integer fld03;
    /**
     * 风扇2
     */
    private Integer fld04;
    /**
     * 加热
     */
    private Integer fld05;
    /**
     * 水浸
     */
    private Integer fld06;
    /**
     * 烟雾
     */
    private Integer fld07;
    /**
     * 供电
     */
    private Integer fld08;
    /**
     * 短路
     */
    private Integer fld09;
    /**
     * 漏电
     */
    private Integer fld10;
    /**
     * 电源模块
     */
    private Integer fld11;
    /**
     * 雷击保护
     */
    private Integer fld12;
    /**
     * 震动
     */
    private Integer fld13;
    /**
     * 门磁开关
     */
    private Integer fld14;
    /**
     * 预留1
     */
    private Integer fld15;
    /**
     * 预留2
     */
    private Integer fld16;
    /**
     * 预留3
     */
    private Integer fld17;
    /**
     * 预留4
     */
    private Integer fld18;
    /**
     * 预留5
     */
    private Integer fld19;
    /**
     * 预留6
     */
    private Integer fld20;
    /**
     * 预留7
     */
    private Integer fld21;
    /**
     * 预留8
     */
    private Integer fld22;
    /**
     * 预留9
     */
    private Integer fld23;
    /**
     * 预留10
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
     * 租户编码
     */
    private String tenantCode;
}
