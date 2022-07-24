package com.aswl.as.ibrs.api.dto;

import lombok.Data;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/7 15:00
 */
@Data
public class DeviceEventAlarmDto {

    private String deviceId;
    private String eventIds;
    private String alarmTypes;
    private String alarmTypeDes;
    private Integer alarmLevel;
    private String alarmLevels;
    private String alarmDates;
    private Integer isAlarm;
}
