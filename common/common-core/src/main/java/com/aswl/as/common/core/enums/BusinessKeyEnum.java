package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件上传业务枚举类
 */
@Getter
@AllArgsConstructor
public enum BusinessKeyEnum {
    
    SYS_LOGO("SYS_LOGO","系统logo");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;
    
    
    public static String getValueByKey(String key){
        try {
            for (BusinessKeyEnum type: BusinessKeyEnum.values()) {
                if (type.getType() == key){
                    return type.getType();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }
}
