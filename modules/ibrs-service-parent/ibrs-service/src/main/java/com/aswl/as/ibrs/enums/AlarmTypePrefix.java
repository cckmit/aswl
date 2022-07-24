package com.aswl.as.ibrs.enums;

public enum AlarmTypePrefix {

    DCPOWER_EXT("DCPowerExt","电源口异常"),
    VOLTAGE_VALUE_EXT("VoltageValueExt","电压异常"),
    FIBRE_OPTICAL_EXT("FibreOpticalExt","光纤口异常"),
    RJ45_EXT("RJ45Ext","网络口异常"),
    FAN_EXT("FanExt","风扇异常");

    private String prefix;

    private String prefixDes;

    AlarmTypePrefix(String prefix, String prefixDes) {
        this.prefix = prefix;
        this.prefixDes = prefixDes;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefixDes() {
        return prefixDes;
    }

    public void setPrefixDes(String prefixDes) {
        this.prefixDes = prefixDes;
    }
}
