package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 设备设置类型枚举
 * @author dingfei
 * @date 2019-12-18 19:43
 * @Version V1
 */

@Getter
@AllArgsConstructor
public enum DeviceSettingsType {
    TEMPERATURE_MANUAL("TEMPERATURE","MANUAL","手动"),
    TEMPERATURE_AUTO("TEMPERATURE","AUTO","自动"),
    DOOR_DEPLOY("DOOR","DEPLOY","布防"),
    DOOR_REVOKE("DOOR","REVOKE","撤防"),
    DOOR_TEMPORARY_REVOKE("DOOR","TEMPORARY_REVOKE","临时撤防");

    private String mode;

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;



}
