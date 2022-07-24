package com.aswl.as.common.core.enums;

/**
 * 八个状态的表明枚举类
 */
public enum EventTableEnum {

    SFP("as_event_sfp","光纤口事件"),
    OUTLET("as_event_eoutlet","电口事件"),
    NETWORK("as_event_network","网络事件"),
    ALARM("as_event_alarm","告警事件"),
    BASE("as_event_base","基础事件"),
    CURRENT("as_event_ecurrent","电流事件"),
    SWITCH("as_event_eswitch","电源开关事件"),
    VOLTAGE("as_event_evoltage","电压事件");




    private String tableName;

    private String des;

    EventTableEnum() {
    }

    EventTableEnum(String tableName, String des) {
        this.tableName = tableName;
        this.des = des;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
