package com.aswl.as.iface.model.consumer;

import lombok.Data;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/13 10:15
 */
@Data
public class ControlDeviceDto implements java.io.Serializable {

    private String deviceCode;

    private String operCode;

    private Integer portSerial;

    private Integer fanTempVal1;

    private Integer fanTempVal2;
}
