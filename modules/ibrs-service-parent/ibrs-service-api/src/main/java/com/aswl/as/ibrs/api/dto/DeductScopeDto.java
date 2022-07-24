package com.aswl.as.ibrs.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 扣分详细记录
 */
@Data
public class DeductScopeDto implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 考核指标
     */
    private String title;

    /**
     * 区域
     */
    private String regionName;

    /**
     * 事件
     */
    private String event;

    /**
     * 时间
     */
    private String time;

    /**
     * 设备
     */
    private String deviceName;

    /**
     * 告警类型
     */
    private String alarmTypeName;

    /**
     * 告警时间
     */
    private String alarmTime;

    /**
     * 响应时长
     */
    private long responseTime;

    /**
     * 延误时长
     */
    private String delayTime;

    /**
     * 处理时间
     */
    private String handleTime;
}
