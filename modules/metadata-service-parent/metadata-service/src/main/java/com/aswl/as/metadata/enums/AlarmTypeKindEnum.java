package com.aswl.as.metadata.enums;

/**
 * 事件类型种类枚举类
 */
public enum AlarmTypeKindEnum {
    PROMPT("Prompt"),
    ALARM("Alarm");

    private String kind;

    public String getKind() {
        return kind;
    }

    AlarmTypeKindEnum(String kind) {
        this.kind = kind;
    }
}
