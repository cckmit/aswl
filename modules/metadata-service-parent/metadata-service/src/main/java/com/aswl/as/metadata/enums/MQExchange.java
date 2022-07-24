package com.aswl.as.metadata.enums;

public enum MQExchange {

    DEVICE_CONTROL("deviceCtrlExchange");

    private String mqFanoutExchange;

    MQExchange(String mqFanoutExchange) {
        this.mqFanoutExchange = mqFanoutExchange;
    }

    public String getMqFanoutExchange() {
        return mqFanoutExchange;
    }
}
