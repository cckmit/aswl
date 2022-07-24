package com.aswl.as.metadata.enums;

/**
 * 电源开关枚举类
 */
public enum PowerSwitchEnum {

    /**
     * 电源口打开
     */
    POWER_ON(1),
    /**
     * 电源口关闭
     */
    POWER_OFF(2),
    /**
     * 电源口重启
     */
    POWER_RESTART(3);

    private int switchType;

    PowerSwitchEnum(int switchType) {
        this.switchType = switchType;
    }

    public int getSwitchType() {
        return switchType;
    }
}
