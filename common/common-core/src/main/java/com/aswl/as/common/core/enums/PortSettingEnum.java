package com.aswl.as.common.core.enums;

/**
 * 设备端口设置枚举类
 */
public enum PortSettingEnum {
    /** 启用 */
    Enable("-1", "启用"),
    /** 禁用 */
    Disable("-9", "禁用");

    private String value;
    private String name;

    PortSettingEnum(String value, String name){
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static PortSettingEnum getByCode(String value){
        PortSettingEnum labelStatus = null;
        for(PortSettingEnum e : PortSettingEnum.values()){
            if(e.value.equals(value)){
                labelStatus = e;
                break;
            }
        }
        return labelStatus;
    }
}
