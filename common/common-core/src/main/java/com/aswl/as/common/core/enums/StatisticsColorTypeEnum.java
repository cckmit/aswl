package com.aswl.as.common.core.enums;

/**
 * 统计报表颜色设置类别枚举类
 */
public enum StatisticsColorTypeEnum {

    PROJECT_FAULT_PROPORTION(1, "项目/区域故障数据占比"),
    FAULT_TYPE_NUM_PROPORTION(2, "故障类型告警数占比"),
    FAULT_MODEL_PROPORTION(3, "设备型号故障数据占比"),
    PROJECT_DEVICE_PROPORTION(4, "项目/区域设备占比"),
    DEVICE_TYPE_PROPORTION(5, "设备类型占比"),
    DEVICE_MODEL_PROPORTION(6, "设备型号占比"),
    PROJECT_ONLINE_PROPORTION(7, "项目在线占比");

    /**
     * 类别
     */
    private int type;

    /**
     * 描述
     */
    private String desc;

    StatisticsColorTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
