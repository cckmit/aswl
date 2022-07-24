package com.aswl.as.common.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dtu数据发送常量
 */
public class DtuPushConstant {

    /**
     * Dtu数据标识枚举类
     */
    @Getter
    @AllArgsConstructor
    public enum DtuFlagEnum {

        /** 发送短信-告警 */
        SEND_SMS_ALARM("sendSmsAlarm"),
        /** 发送短信-工单 */
        SEND_SMS_WORK_ORDER("sendSmsWorkOrder"),
        /** 推送APP-告警 */
        PUSH_APP_ALARM("pushAppAlarm"),
        /** 推送APP-工单 */
        PUSH_APP_WORK_ORDER("pushAppWorkOrder");

        String flag;
    }

}
