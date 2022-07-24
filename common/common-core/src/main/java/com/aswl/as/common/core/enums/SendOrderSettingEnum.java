package com.aswl.as.common.core.enums;

/**
 * 派单设置枚举类
 */
public enum SendOrderSettingEnum {

    /** 派单方式-自动 */
    SEND_ORDER_TYPE_AUTO(1, "自动派单"),
    /** 派单方式-手动 */
    SEND_ORDER_TYPE_MANUAL(2, "手动派单"),
    /** 派单方式-不派单 */
    SEND_ORDER_TYPE_NONE(3, "不派单"),

    /** 工单处理方式-自动 */
    ORDER_HANDLE_TYPE_AUTO(0, "自动处理工单"),
    /** 工单处理方式-手动 */
    ORDER_HANDLE_TYPE_MANUAL(1, "手动处理工单");

    private int value;
    private String desc;

    SendOrderSettingEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
