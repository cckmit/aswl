package com.aswl.as.ibrs.api.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 每个型号的保障数
 */
@Data
@Getter
@Setter
public class DeviceModelAlarmCountVo {

    /**
     * 型号名称
     */
    private String deviceModelName;
    /**
     * 类型名称
     */

    private String deviceTypeName;

    /**
     * 故障设备数量
     */

    private String ModelAlarmCount;
}
