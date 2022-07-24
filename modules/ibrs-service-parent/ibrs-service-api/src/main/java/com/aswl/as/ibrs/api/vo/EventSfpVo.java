package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EventSfpVo extends BaseEntity<EventSfpVo> {

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
     * 光口1
     */
    private String fld01;
    /**
     * 光口2
     */
    private String fld02;
    /**
     * 光口3
     */
    private String fld03;
    /**
     * 光口4
     */
    private String fld04;
    /**
     * 光口5
     */
    private String fld05;
    /**
     * 光口6
     */
    private String fld06;
    /**
     * 光口7
     */
    private String fld07;
    /**
     * 光口8
     */
    private String fld08;
    /**
     * 预留9
     */
    private String fld09;
    /**
     * 预留10
     */
    private String fld10;
    /**
     * 预留11
     */
    private String fld11;
    /**
     * 预留12
     */
    private String fld12;
    /**
     * 预留13
     */
    private String fld13;
    /**
     * 预留14
     */
    private String fld14;
    /**
     * 预留15
     */
    private String fld15;
    /**
     * 预留16
     */
    private String fld16;
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
