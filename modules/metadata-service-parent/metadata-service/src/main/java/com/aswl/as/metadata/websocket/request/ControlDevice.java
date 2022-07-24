package com.aswl.as.metadata.websocket.request;

import java.io.Serializable;

/**
 * 设备控制类
 */
public class ControlDevice implements Serializable {

    private String deviceId;

    private String operCode;

    private String portSerial;

    private Integer fanTempVal1;

    private Integer fanTempVal2;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getOperCode() {
        return operCode;
    }

    public void setOperCode(String operCode) {
        this.operCode = operCode;
    }

    public String getPortSerial() {
        return portSerial;
    }

    public void setPortSerial(String portSerial) {
        this.portSerial = portSerial;
    }

    public Integer getFanTempVal1() {
        return fanTempVal1;
    }

    public void setFanTempVal1(Integer fanTempVal1) {
        this.fanTempVal1 = fanTempVal1;
    }

    public Integer getFanTempVal2() {
        return fanTempVal2;
    }

    public void setFanTempVal2(Integer fanTempVal2) {
        this.fanTempVal2 = fanTempVal2;
    }
}
