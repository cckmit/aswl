package com.aswl.as.ibrs.api.vo;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class EventEvoltageVo extends BaseEntity<EventEvoltageVo> {
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
     * 温度
     */
    private String temperature;
    /**
     * 湿度
     */
    private String humidity;
    /**
     * 电压值1设定
     */
    private String fld01;
    /**
     * 电压值1
     */
    private Integer val01;
    /**
     * 电压值2设定
     */
    private String fld02;
    /**
     * 电压值2
     */
    private Integer val02;
    /**
     * 电压值3设定
     */
    private String fld03;
    /**
     * 电压值3
     */
    private Integer val03;
    /**
     * 电压值4设定
     */
    private String fld04;
    /**
     * 电压值4设定
     */
    private Integer val04;
    /**
     * 电压值5设定
     */
    private String fld05;
    /**
     * 电压值5设定
     */
    private Integer val05;
    /**
     * 电压值6设定
     */
    private String fld06;
    /**
     * 电压值6设定
     */
    private Integer val06;
    /**
     * 电压值7设定
     */
    private String fld07;
    /**
     * 电压值7
     */
    private Integer val07;
    /**
     * 电压值8设定
     */
    private String fld08;
    /**
     * 电压值8
     */
    private Integer val08;
    /**
     * 电压值9
     */
    private String fld09;
    /**
     * 电压值9设定
     */
    private Integer val09;
    /**
     * 电压值9
     */
    private String fld10;
    /**
     * 电压值9设定
     */
    private Integer val10;
    /**
     * 电压值9
     */
    private String fld11;
    /**
     * 电压值9设定
     */
    private Integer val11;
    /**
     * 电压值9
     */
    private String fld12;
    /**
     * 电压值9设定
     */
    private Integer val12;
    /**
     * 电压值9
     */
    private String fld13;
    /**
     * 电压值9设定
     */
    private Integer val13;
    /**
     * 电压值9
     */
    private String fld14;
    /**
     * 电压值9设定
     */
    private Integer val14;
    /**
     * 电压值9
     */
    private String fld15;
    /**
     * 电压值9设定
     */
    private Integer val15;
    /**
     * 电压值9
     */
    private String fld16;
    /**
     * 电压值9设定
     */
    private Integer val16;
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
