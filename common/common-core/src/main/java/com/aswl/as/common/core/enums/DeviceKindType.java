package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** 设备种类枚举
 * @author dingfei
 * @date 2020-01-05 16:05
 */

@Getter
@AllArgsConstructor
public enum DeviceKindType {
    TRANSCEIVER("1","光纤收发器"),
    BOX("2","传输箱"),
    CAMERA("3","摄像机");

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String description;



}
