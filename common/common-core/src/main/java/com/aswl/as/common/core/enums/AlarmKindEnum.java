package com.aswl.as.common.core.enums;

/**
 * 报警类型种类
 */
public enum AlarmKindEnum {

    /** 异常 */
    Alarm("Alarm", "异常"),
    /** 正常 */
    Prompt("Prompt", "正常");

    private String code;
    private String name;

    AlarmKindEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static AlarmKindEnum getByCode(String code){
        AlarmKindEnum alarmKindEnum = null;
        for(AlarmKindEnum e : AlarmKindEnum.values()){
            if(e.code.equals(code)){
                alarmKindEnum = e;
                break;
            }
        }
        return alarmKindEnum;
    }
}
