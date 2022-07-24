package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EventEoutletVo extends BaseEntity<EventEoutletVo> {
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
     * 预留
     */
    private String passway3;
    /**
     * 电口1
     */
    private String fld01;
    /**
     * 电口2
     */
    private String fld02;
    /**
     * 电口3
     */
    private String fld03;
    /**
     * 电口4
     */
    private String fld04;
    /**
     * 电口5
     */
    private String fld05;
    /**
     * 电口6
     */
    private String fld06;
    /**
     * 电口7
     */
    private String fld07;
    /**
     * 电口8
     */
    private String fld08;
    /**
     * 电口9
     */
    private String fld09;
    /**
     * 电口10
     */
    private String fld10;
    /**
     * 电口11
     */
    private String fld11;
    /**
     * 电口12
     */
    private String fld12;
    /**
     * 电口13
     */
    private String fld13;
    /**
     * 电口14
     */
    private String fld14;
    /**
     * 电口15
     */
    private String fld15;
    /**
     * 电口16
     */
    private String fld16;
    /**
     * 预留17
     */
    private String fld17;
    /**
     * 预留18
     */
    private String fld18;
    /**
     * 预留19
     */
    private String fld19;
    /**
     * 预留20
     */
    private String fld20;
    /**
     * 预留21
     */
    private String fld21;
    /**
     * 预留22
     */
    private String fld22;
    /**
     * 预留23
     */
    private String fld23;
    /**
     * 预留24
     */
    private String fld24;
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
