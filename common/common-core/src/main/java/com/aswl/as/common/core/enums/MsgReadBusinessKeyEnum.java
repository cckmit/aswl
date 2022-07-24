package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 
 */
@Getter
@AllArgsConstructor
public enum MsgReadBusinessKeyEnum {
    MSG_READ_ALARM("1","msg_read_alarm","告警"),
    MSG_READ_FLOW_RUN("2","msg_read_flow_run","工单"),
    MSG_READ_SYS("3","msg_read_sys","系统");

    /**
     * 值
     */
    private String value;

    /**
     * 标识
     */
    private String code;

    /**
     * 描述
     */
    private String description;
}
