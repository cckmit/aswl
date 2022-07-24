package com.aswl.as.ibrs.api.module;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/2 16:37
 */
@Data
public class DynamicMessage implements Serializable {
    private String deviceId;
    private String statusName;
    private String deviceName;
    private String regionName;
    private String alarmType;
    private String alarmTime;
    private Integer alarmLevel;
    private String alarmLevelName;
    private String intervalTime;
    private String tenantCode;
    private String projectId;
    private String regionCode;
    private String deviceKindType;
    private String deviceCode;
    private String ip;
}
