package com.aswl.as.iface.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 设备控制类
 */
@Data
public class ControlDevice implements Serializable {

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 操作编码
     */
    private String operCode;

    /**
     * 端口号
     */
    private Integer portSerial;

    /**
     * 风扇1温度值
     */
    private Integer fanTempVal1;

    /**
     * 风扇2温度值
     */
    private Integer fanTempVal2;

}
