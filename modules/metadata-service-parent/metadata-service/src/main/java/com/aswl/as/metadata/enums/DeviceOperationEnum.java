package com.aswl.as.metadata.enums;

/**
 * 设备控制枚举类
 */
public enum DeviceOperationEnum {

    DC_POWER_OPEN("DCPowerOpen", "直流电源口开启"),
    DC_POWER_CLOSE("DCPowerClose", "直流电源口关闭"),
    DC_POWER_RESTART("DCPowerRestart", "直流电源口重启"),
    AC_POWER_OPEN("ACPowerOpen", "220V电源口开启"),
    AC_POWER_CLOSE("ACPowerClose", "220V电源口关闭"),
    AC_POWER_RESTART("ACPowerRestart", "220V电源口重启"),
    DELAYED_DC_POWER_RESTART("DelayedDCPowerRestart", "延时直流电源口重启"),
    POWER_RESTART("PowerRestart","系统重启"),
	TEMP_AUTO_MODE("TempAutoMode","自动模式"),
	TEMP_MANUAL_MODE("TempManualMode","手动模式"),
	FAN_TEMP_VAL_SET("FanTempValSet","风扇温度阈值设置"),
    MAIN_POWER_RESTART("MainPowerRestart","重合闸重启");
    private String operCode;
    private String title;

    DeviceOperationEnum(String operCode, String title) {
        this.operCode = operCode;
        this.title = title;
    }

    public String getOperCode() {
        return operCode;
    }

    public String getTitle() {
        return title;
    }

    public static DeviceOperationEnum getByOperCode(String operCode){
        DeviceOperationEnum deviceOperationEnum = null;
        for (DeviceOperationEnum e : DeviceOperationEnum.values()){
            if(e.getOperCode().equals(operCode)){
                deviceOperationEnum = e;
                break;
            }
        }
        return deviceOperationEnum;
    }

    public static class DeviceMfrs {
       public static final String WC_DEVICE = "WT_StatusTransferBox";
    }
}
