package com.aswl.as.metadata.websocket.push;

import com.aswl.as.ibrs.api.module.Device;

import java.io.Serializable;
import java.util.List;

/**
 * 推送事件数据
 */
public class PushEventData implements Serializable {

    private static final long serialVersionUID = 1L;

    private PushDevice pushDevice;

    private PushLevel pushLevel;

    private List<PushParam> pushParams;

    private String pushMessage;

    private String pushMessageEn;

    private String pushMessageTips;

    private String pushMessageTipsEn;

    private List<String> pushAlarmMethod;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public PushDevice getPushDevice() {
        return pushDevice;
    }

    public void setPushDevice(PushDevice pushDevice) {
        this.pushDevice = pushDevice;
    }

    public PushLevel getPushLevel() {
        return pushLevel;
    }

    public void setPushLevel(PushLevel pushLevel) {
        this.pushLevel = pushLevel;
    }

    public List<PushParam> getPushParams() {
        return pushParams;
    }

    public void setPushParams(List<PushParam> pushParams) {
        this.pushParams = pushParams;
    }

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }

    public String getPushMessageEn() {
        return pushMessageEn;
    }

    public void setPushMessageEn(String pushMessageEn) {
        this.pushMessageEn = pushMessageEn;
    }

    public String getPushMessageTips() {
        return pushMessageTips;
    }

    public void setPushMessageTips(String pushMessageTips) {
        this.pushMessageTips = pushMessageTips;
    }

    public String getPushMessageTipsEn() {
        return pushMessageTipsEn;
    }

    public void setPushMessageTipsEn(String pushMessageTipsEn) {
        this.pushMessageTipsEn = pushMessageTipsEn;
    }

    public List<String> getPushAlarmMethod() {
        return pushAlarmMethod;
    }

    public void setPushAlarmMethod(List<String> pushAlarmMethod) {
        this.pushAlarmMethod = pushAlarmMethod;
    }

    //用于创建语音消息
    public  String createPushMessage(Device device, String tips, String alarmLevelName){
        //传输箱202/192.168.1.202，提示（直流电源口[3]打开）
        String ip = device.getIp();
//		message = device.getDeviceName()+" "+ip.replace(".", "点")+"，"+alarmLevel.getAlarmLevelName()+"（"+tips+"）";
        this.pushMessage = device.getDeviceName()+"，"+alarmLevelName+"（"+tips+"）";
//		System.out.println("************语音message"+message);
        return this.pushMessage;
    }

    //用于创建语音消息(En)
    public  String createPushMessageEn(Device device, String tipsEn, String alarmLevelNameEn){
        //传输箱202/192.168.1.202，提示（直流电源口[3]打开）
        String ip = device.getIp();
//		message = device.getDeviceName()+" "+ip.replace(".", "点")+"，"+alarmLevel.getAlarmLevelName()+"（"+tips+"）";
        this.pushMessageEn = device.getDeviceName()+"，"+alarmLevelNameEn+"（"+tipsEn+"）";
//		System.out.println("************语音message"+message);
        return this.pushMessageEn;
    }
}
