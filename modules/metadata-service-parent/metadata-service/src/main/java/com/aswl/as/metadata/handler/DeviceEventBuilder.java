package com.aswl.as.metadata.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.metadata.utils.GPSConverterUtils;
import com.aswl.iot.dto.*;
import com.aswl.iot.dto.DeviceEventAlarm;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设备事件构建器
 */
@Component("deviceEventBuilder")
public class DeviceEventBuilder {

    /**
     * 构建EventAlarm
     * @param deviceEventAlarm
     * @return
     */
    public EventAlarm buildEventAlarm(DeviceEventAlarm deviceEventAlarm){
       // EventAlarm eventAlarm = JSONObject.parseObject(JSON.toJSONString(deviceEventAlarm), EventAlarm.class);
    	EventAlarm eventAlarm = new EventAlarm();
    	eventAlarm.setPassway1(deviceEventAlarm.getPassway1());
    	eventAlarm.setPassway2(deviceEventAlarm.getPassway2());
    	eventAlarm.setFld01(deviceEventAlarm.getFld01());
    	eventAlarm.setFld02(deviceEventAlarm.getFld02());
    	eventAlarm.setFld03(deviceEventAlarm.getFld03());
    	eventAlarm.setFld04(deviceEventAlarm.getFld04());
    	eventAlarm.setFld05(deviceEventAlarm.getFld05());
    	eventAlarm.setFld06(deviceEventAlarm.getFld06());
    	eventAlarm.setFld07(deviceEventAlarm.getFld07());
    	eventAlarm.setFld08(deviceEventAlarm.getFld08());
    	eventAlarm.setFld09(deviceEventAlarm.getFld09());
    	eventAlarm.setFld10(deviceEventAlarm.getFld10());
    	eventAlarm.setFld11(deviceEventAlarm.getFld11());
    	eventAlarm.setFld12(deviceEventAlarm.getFld12());
    	eventAlarm.setFld13(deviceEventAlarm.getFld13());
    	eventAlarm.setFld14(deviceEventAlarm.getFld14());
    	eventAlarm.setFld15(deviceEventAlarm.getFld15());
    	eventAlarm.setFld16(deviceEventAlarm.getFld16());
        eventAlarm.setDeviceId(deviceEventAlarm.getDevice().getId());
        eventAlarm.setStoreTime(new Date());
        eventAlarm.setRecTime(deviceEventAlarm.getRecTime()/1000);
        if(eventAlarm.getFld09() == 1){
            eventAlarm.setFld08(2);    //短路掉电
        }
        if(eventAlarm.getFld10() == 1){
            eventAlarm.setFld08(3);    //漏电掉电
        }
        return eventAlarm;
    }

    /**
     * 构建EventEswitch
     * @param deviceEventEswitch
     * @return
     */
    public EventEswitch buildEventEswitch(DeviceEventEswitch deviceEventEswitch){
//        EventEswitch eventEswitch = JSONObject.parseObject(JSON.toJSONString(deviceEventEswitch), EventEswitch.class);
        EventEswitch eventEswitch = new EventEswitch();
        eventEswitch.setPassway1(deviceEventEswitch.getPassway1());
        eventEswitch.setPassway2(deviceEventEswitch.getPassway2());
        eventEswitch.setPassway3(deviceEventEswitch.getPassway3());
        eventEswitch.setFldx(deviceEventEswitch.getFldx());
        eventEswitch.setFldy(deviceEventEswitch.getFldy());
        eventEswitch.setFld01(deviceEventEswitch.getFld01());
        eventEswitch.setFld02(deviceEventEswitch.getFld02());
        eventEswitch.setFld03(deviceEventEswitch.getFld03());
        eventEswitch.setFld04(deviceEventEswitch.getFld04());
        eventEswitch.setFld05(deviceEventEswitch.getFld05());
        eventEswitch.setFld06(deviceEventEswitch.getFld06());
        eventEswitch.setFld07(deviceEventEswitch.getFld07());
        eventEswitch.setFld08(deviceEventEswitch.getFld08());
        eventEswitch.setFld09(deviceEventEswitch.getFld09());
        eventEswitch.setFld10(deviceEventEswitch.getFld10());
        eventEswitch.setFld11(deviceEventEswitch.getFld11());
        eventEswitch.setFld12(deviceEventEswitch.getFld12());
        eventEswitch.setFld13(deviceEventEswitch.getFld13());
        eventEswitch.setFld14(deviceEventEswitch.getFld14());
        eventEswitch.setFld15(deviceEventEswitch.getFld15());
        eventEswitch.setFld16(deviceEventEswitch.getFld16());
        eventEswitch.setRecTime(Integer.parseInt(deviceEventEswitch.getRecTime()/1000+""));
        eventEswitch.setDeviceId(deviceEventEswitch.getDevice().getId());
        eventEswitch.setStoreTime(new Date());
        return eventEswitch;
    }

    /**
     * 构建EventNetwork
     * @param deviceEventNetwork
     * @return
     */
    public EventNetwork buildEventNetwork(DeviceEventNetwork deviceEventNetwork){
        EventNetwork eventNetwork = JSONObject.parseObject(JSON.toJSONString(deviceEventNetwork), EventNetwork.class);
        eventNetwork.setDeviceId(deviceEventNetwork.getDevice().getId());
        eventNetwork.setStoreTime(new Date());
        return eventNetwork;
    }

    /**
     * 构建EventEvoltage
     * @param deviceEventEvoltage
     * @return
     */
    public EventEvoltage buildEventEvoltage(DeviceEventEvoltage deviceEventEvoltage){
        //EventEvoltage eventEvoltage = JSONObject.parseObject(JSONObject.toJSONString(deviceEventEvoltage),EventEvoltage.class);
        EventEvoltage eventEvoltage = new EventEvoltage();
        eventEvoltage.setTemperature(String.valueOf(deviceEventEvoltage.getTemperature()));
        eventEvoltage.setHumidity(String.valueOf(deviceEventEvoltage.getHumidity()));
        eventEvoltage.setFld01(deviceEventEvoltage.getFld01());
        eventEvoltage.setVal01(deviceEventEvoltage.getVal01());
        eventEvoltage.setFld02(deviceEventEvoltage.getFld02());
        eventEvoltage.setVal02(deviceEventEvoltage.getVal02());
        eventEvoltage.setFld03(deviceEventEvoltage.getFld03());
        eventEvoltage.setVal03(deviceEventEvoltage.getVal03());
        eventEvoltage.setFld04(deviceEventEvoltage.getFld04());
        eventEvoltage.setVal04(deviceEventEvoltage.getVal04());
        eventEvoltage.setFld05(deviceEventEvoltage.getFld05());
        eventEvoltage.setVal05(deviceEventEvoltage.getVal05());
        eventEvoltage.setFld06(deviceEventEvoltage.getFld06());
        eventEvoltage.setVal06(deviceEventEvoltage.getVal06());
        eventEvoltage.setFld07(deviceEventEvoltage.getFld07());
        eventEvoltage.setVal07(deviceEventEvoltage.getVal07());
        eventEvoltage.setFld08(deviceEventEvoltage.getFld08());
        eventEvoltage.setVal08(deviceEventEvoltage.getVal08());
        eventEvoltage.setFld09(deviceEventEvoltage.getFld09());
        eventEvoltage.setVal09(deviceEventEvoltage.getVal09());
        eventEvoltage.setStoreTime(new Date());
        eventEvoltage.setDeviceId(deviceEventEvoltage.getDevice().getId());
        eventEvoltage.setRecTime(Integer.parseInt(deviceEventEvoltage.getRecTime()/1000 + ""));
        return eventEvoltage;
    }

    /**
     * 构建设备电口EventEoutlet
     * @return
     */
    public EventEoutlet builderEventEoutlet(DeviceEventEoutlet deviceEventEoutlet){
        EventEoutlet eventEoutlet = new EventEoutlet();
//        eventEoutlet.setPassway1(deviceEventEoutlet.getPassway1());
//        eventEoutlet.setPassway2(deviceEventEoutlet.getPassway2());
        eventEoutlet.setDeviceId(deviceEventEoutlet.getDevice().getId());
        eventEoutlet.setRecTime(Integer.parseInt(deviceEventEoutlet.getRecTime()/1000 + ""));
        eventEoutlet.setPassway1(deviceEventEoutlet.getPassway1());
        eventEoutlet.setPassway2(deviceEventEoutlet.getPassway2());
        String[] passway1 = deviceEventEoutlet.getPassway1().split("");
        String[] passway2 = deviceEventEoutlet.getPassway2().split("");
        if(passway1.length >= 8 && "1".equals(passway1[7]))
            eventEoutlet.setFld01(deviceEventEoutlet.getFld01());
        if(passway1.length >= 8 && "1".equals(passway1[6]))
            eventEoutlet.setFld02(deviceEventEoutlet.getFld02());
        if(passway1.length >= 8 && "1".equals(passway1[5]))
            eventEoutlet.setFld03(deviceEventEoutlet.getFld03());
        if(passway1.length >= 8 && "1".equals(passway1[4]))
            eventEoutlet.setFld04(deviceEventEoutlet.getFld04());
        if(passway1.length >= 8 && "1".equals(passway1[3]))
            eventEoutlet.setFld05(deviceEventEoutlet.getFld05());
        if(passway1.length >= 8 && "1".equals(passway1[2]))
            eventEoutlet.setFld06(deviceEventEoutlet.getFld06());
        if(passway1.length >= 8 && "1".equals(passway1[1]))
            eventEoutlet.setFld07(deviceEventEoutlet.getFld07());
        if(passway1.length >= 8 && "1".equals(passway1[0]))
            eventEoutlet.setFld08(deviceEventEoutlet.getFld08());
        if(passway2.length >= 8 && "1".equals(passway2[7]))
            eventEoutlet.setFld09(deviceEventEoutlet.getFld09());
        if(passway2.length >= 8 && "1".equals(passway2[6]))
            eventEoutlet.setFld10(deviceEventEoutlet.getFld10());
        if(passway2.length >= 8 && "1".equals(passway2[5]))
            eventEoutlet.setFld11(deviceEventEoutlet.getFld11());
        if(passway2.length >= 8 && "1".equals(passway2[4]))
            eventEoutlet.setFld12(deviceEventEoutlet.getFld12());
        if(passway2.length >= 8 && "1".equals(passway2[3]))
            eventEoutlet.setFld13(deviceEventEoutlet.getFld13());
        if(passway2.length >= 8 && "1".equals(passway2[2]))
            eventEoutlet.setFld14(deviceEventEoutlet.getFld14());
        if(passway2.length >= 8 && "1".equals(passway2[1]))
            eventEoutlet.setFld15(deviceEventEoutlet.getFld15());
        if(passway2.length >= 8 && "1".equals(passway2[0]))
            eventEoutlet.setFld16(deviceEventEoutlet.getFld16());
        eventEoutlet.setStoreTime(new Date());
        return eventEoutlet;
    }

    /**
     * 构建设备光口事件
     * @param deviceEventSfp
     * @return
     */
    public EventSfp builderEventSfp(DeviceEventSfp deviceEventSfp){
        EventSfp eventSfp = new EventSfp();
        eventSfp.setPassway1(deviceEventSfp.getPassway1());
        String[] passway1 = deviceEventSfp.getPassway1().split("");
        if(passway1.length >= 8){
            if("1".equals(passway1[7]))
                eventSfp.setFld01(deviceEventSfp.getFld01());
            if("1".equals(passway1[6]))
                eventSfp.setFld02(deviceEventSfp.getFld02());
            if("1".equals(passway1[5]))
                eventSfp.setFld03(deviceEventSfp.getFld03());
            if("1".equals(passway1[4]))
                eventSfp.setFld04(deviceEventSfp.getFld04());
            if("1".equals(passway1[3]))
                eventSfp.setFld05(deviceEventSfp.getFld05());
            if("1".equals(passway1[2]))
                eventSfp.setFld06(deviceEventSfp.getFld06());
            if("1".equals(passway1[1]))
                eventSfp.setFld07(deviceEventSfp.getFld07());
            if("1".equals(passway1[0]))
                eventSfp.setFld08(deviceEventSfp.getFld08());
        }
        eventSfp.setDeviceId(deviceEventSfp.getDevice().getId());
        eventSfp.setRecTime(Integer.parseInt(deviceEventSfp.getRecTime()/1000 + ""));
        eventSfp.setStoreTime(new Date());
        return eventSfp;
    }

    /**
     * 构建设备物联网事件
     * @param deviceEventIot
     * @return
     */
    public EventBase builderEventBase(DeviceEventIot deviceEventIot){
        EventBase eventBase = new EventBase();
        eventBase.setDeviceId(deviceEventIot.getDevice().getId());
        eventBase.setUseStatus(deviceEventIot.getUseStatus());
        eventBase.setIotStatus(deviceEventIot.getConnectStatus());
        eventBase.setRecTime(Integer.parseInt(deviceEventIot.getRecTime()/1000 + ""));
        eventBase.setStoreTime(new Date());
        return eventBase;
    }

    /**
     * 构建设备GPS事件
     */
    public EventBase builderEventBase(DeviceEventGPS deviceEventGPS){
        EventBase eventBase = new EventBase();
        eventBase.setDeviceId(deviceEventGPS.getDevice().getId());
        eventBase.setAltitude(deviceEventGPS.getAltitude());
//        eventBase.setUtcTime(Integer.parseInt(deviceEventGPS.getUtcTime()));
        eventBase.setUtcTime(0);
        GPSConverterUtils.GPS bdGps = GPSConverterUtils.gps84_To_bd09(deviceEventGPS.getLat(), deviceEventGPS.getLng());
        eventBase.setLat(bdGps.getLat());
        eventBase.setLng(bdGps.getLon());
        eventBase.setStoreTime(new Date());
        eventBase.setRecTime(Integer.parseInt(deviceEventGPS.getRecTime()/1000 + ""));
        return eventBase;
    }

    public EventEcurrent builderEventEcurrent(DeviceEventEcurrent deviceEventEcurrent){
        EventEcurrent eventEcurrent = new EventEcurrent();
        eventEcurrent.setDeviceId(deviceEventEcurrent.getDevice().getId());
        eventEcurrent.setStoreTime(new Date());
        eventEcurrent.setRecTime(Integer.parseInt(deviceEventEcurrent.getRecTime()/1000 + ""));
        eventEcurrent.setPassway1(deviceEventEcurrent.getPassway1());
        eventEcurrent.setPassway2(deviceEventEcurrent.getPassway2());
        eventEcurrent.setPassway3(deviceEventEcurrent.getPassway3());
        eventEcurrent.setFldx(deviceEventEcurrent.getFldx());
        eventEcurrent.setFldy(deviceEventEcurrent.getFldy());
        eventEcurrent.setFldall(deviceEventEcurrent.getFldall());
        eventEcurrent.setFld01(deviceEventEcurrent.getFld01());
        eventEcurrent.setFld02(deviceEventEcurrent.getFld02());
        eventEcurrent.setFld03(deviceEventEcurrent.getFld03());
        eventEcurrent.setFld04(deviceEventEcurrent.getFld04());
        eventEcurrent.setFld05(deviceEventEcurrent.getFld05());
        eventEcurrent.setFld06(deviceEventEcurrent.getFld06());
        eventEcurrent.setFld07(deviceEventEcurrent.getFld7());
        eventEcurrent.setFld08(deviceEventEcurrent.getFld08());
        eventEcurrent.setFld09(deviceEventEcurrent.getFld09());
        eventEcurrent.setFld10(deviceEventEcurrent.getFld10());
        eventEcurrent.setFld11(deviceEventEcurrent.getFld11());
        eventEcurrent.setFld12(deviceEventEcurrent.getFld12());
        eventEcurrent.setFld13(deviceEventEcurrent.getFld13());
        eventEcurrent.setFld14(deviceEventEcurrent.getFld14());
        eventEcurrent.setFld15(deviceEventEcurrent.getFld15());
        eventEcurrent.setFld16(deviceEventEcurrent.getFld16());
        eventEcurrent.setStoreTime(new Date());
        eventEcurrent.setFld01(deviceEventEcurrent.getFld01());

        return eventEcurrent;
    }

    public EventPatrol buildEventEPatrol(DeviceEventPatrol deviceEventPatrol) throws ParseException {
        EventPatrol eventPatrol = new EventPatrol();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        eventPatrol.setAuthTime(sdf.parse(deviceEventPatrol.getAuthTime()));
        eventPatrol.setDeviceId(deviceEventPatrol.getDevice().getId());
        eventPatrol.setIdCipher(deviceEventPatrol.getIdCipher());
        eventPatrol.setKeyMac(deviceEventPatrol.getKeyMac());
        eventPatrol.setRecTime(Integer.parseInt(deviceEventPatrol.getRecTime()/1000 + ""));
        eventPatrol.setStoreTime(new Date());
        return eventPatrol;
    }

    public EventElectricity buildEventElectricity(DeviceElectricity deviceElectricity){
        EventElectricity eventElectricity = new EventElectricity();
        eventElectricity.setDeviceId(deviceElectricity.getDevice().getId());
        eventElectricity.setRecTime(Integer.parseInt(deviceElectricity.getRecTime()/1000 + ""));
        eventElectricity.setPassway(deviceElectricity.getPassway());
        eventElectricity.setLeakage(deviceElectricity.getLeakage());
        eventElectricity.setSwitchNum(deviceElectricity.getSwitchNum());
        eventElectricity.setOverLoad(deviceElectricity.getOverLoad());
        eventElectricity.setUnderVoltage(deviceElectricity.getUnderVoltage());
        eventElectricity.setElectricity(deviceElectricity.getElectricity());
        return eventElectricity;
    }

    /**
     * 构建EventSfpInfo
     * @param deviceEventSfpInfo
     * @return
     */
    public EventSfpInfo buildEventSfpInfo(DeviceEventSfpInfo deviceEventSfpInfo){
        EventSfpInfo eventSfpInfo = new EventSfpInfo();
        eventSfpInfo.setPassway(deviceEventSfpInfo.getPassway());
        eventSfpInfo.setFld01(deviceEventSfpInfo.getSfp01());
        eventSfpInfo.setFld02(deviceEventSfpInfo.getSfp02());
        eventSfpInfo.setFld03(deviceEventSfpInfo.getSfp03());
        eventSfpInfo.setFld04(deviceEventSfpInfo.getSfp04());
        eventSfpInfo.setFld05(deviceEventSfpInfo.getSfp05());
        eventSfpInfo.setFld06(deviceEventSfpInfo.getSfp06());
        eventSfpInfo.setFld07(deviceEventSfpInfo.getSfp07());
        eventSfpInfo.setFld08(deviceEventSfpInfo.getSfp08());
        eventSfpInfo.setDeviceId(deviceEventSfpInfo.getDevice().getId());
        eventSfpInfo.setRecTime(Integer.parseInt(deviceEventSfpInfo.getRecTime()/1000 + ""));
        return eventSfpInfo;
    }

    /**
     * 构建EventSecCtlPower (根据DeviceSecCtlPower)
     * @param deviceSecCtlPower
     * @return
     */
    public EventSecCtlPower buildSecCtlPower(DeviceSecCtlPower deviceSecCtlPower){
        EventSecCtlPower eventSecCtlPower = new EventSecCtlPower();
        eventSecCtlPower.setFld01(deviceSecCtlPower.getPower01Txt());
        eventSecCtlPower.setFld02(deviceSecCtlPower.getPower02Txt());
        eventSecCtlPower.setFld03(deviceSecCtlPower.getPower03Txt());
        eventSecCtlPower.setFld04(deviceSecCtlPower.getPower04Txt());
        eventSecCtlPower.setFld05(deviceSecCtlPower.getPower05Txt());
        eventSecCtlPower.setFld06(deviceSecCtlPower.getPower06Txt());
        eventSecCtlPower.setFld07(deviceSecCtlPower.getPower07Txt());
        eventSecCtlPower.setFld08(deviceSecCtlPower.getPower08Txt());
        eventSecCtlPower.setFld09(deviceSecCtlPower.getPower09Txt());
        eventSecCtlPower.setFld10(deviceSecCtlPower.getPower10Txt());
        eventSecCtlPower.setFld11(deviceSecCtlPower.getPower11Txt());
        eventSecCtlPower.setFld12(deviceSecCtlPower.getPower12Txt());
        eventSecCtlPower.setFld13(deviceSecCtlPower.getPower13Txt());
        eventSecCtlPower.setFld14(deviceSecCtlPower.getPower14Txt());
        eventSecCtlPower.setFld15(deviceSecCtlPower.getPower15Txt());
        eventSecCtlPower.setFld16(deviceSecCtlPower.getPower16Txt());
        eventSecCtlPower.setDeviceId(deviceSecCtlPower.getDevice().getId());
        eventSecCtlPower.setRecTime(Integer.parseInt(deviceSecCtlPower.getRecTime()/1000 + ""));
        return eventSecCtlPower;
    }

    /**
     * 构建EventSecCtlPower (根据DeviceSecCtlPowerAlarm)
     * @param deviceSecCtlPowerAlarm
     * @return
     */
    public EventSecCtlPower buildSecCtlPower(DeviceSecCtlPowerAlarm deviceSecCtlPowerAlarm){
        EventSecCtlPower eventSecCtlPower = new EventSecCtlPower();
        eventSecCtlPower.setAlarm01(String.valueOf(deviceSecCtlPowerAlarm.getAlarm01()));
        eventSecCtlPower.setAlarm02(String.valueOf(deviceSecCtlPowerAlarm.getAlarm02()));
        eventSecCtlPower.setAlarm03(String.valueOf(deviceSecCtlPowerAlarm.getAlarm03()));
        eventSecCtlPower.setAlarm04(String.valueOf(deviceSecCtlPowerAlarm.getAlarm04()));
        eventSecCtlPower.setAlarm05(String.valueOf(deviceSecCtlPowerAlarm.getAlarm05()));
        eventSecCtlPower.setAlarm06(String.valueOf(deviceSecCtlPowerAlarm.getAlarm06()));
        eventSecCtlPower.setAlarm07(String.valueOf(deviceSecCtlPowerAlarm.getAlarm07()));
        eventSecCtlPower.setAlarm08(String.valueOf(deviceSecCtlPowerAlarm.getAlarm08()));
        eventSecCtlPower.setAlarm09(String.valueOf(deviceSecCtlPowerAlarm.getAlarm09()));
        eventSecCtlPower.setAlarm10(String.valueOf(deviceSecCtlPowerAlarm.getAlarm10()));
        eventSecCtlPower.setAlarm11(String.valueOf(deviceSecCtlPowerAlarm.getAlarm11()));
        eventSecCtlPower.setAlarm12(String.valueOf(deviceSecCtlPowerAlarm.getAlarm12()));
        eventSecCtlPower.setAlarm13(String.valueOf(deviceSecCtlPowerAlarm.getAlarm13()));
        eventSecCtlPower.setAlarm14(String.valueOf(deviceSecCtlPowerAlarm.getAlarm14()));
        eventSecCtlPower.setAlarm15(String.valueOf(deviceSecCtlPowerAlarm.getAlarm15()));
        eventSecCtlPower.setAlarm16(String.valueOf(deviceSecCtlPowerAlarm.getAlarm16()));
        eventSecCtlPower.setDeviceId(deviceSecCtlPowerAlarm.getDevice().getId());
        eventSecCtlPower.setRecTime(Integer.parseInt(deviceSecCtlPowerAlarm.getRecTime()/1000 + ""));
        return eventSecCtlPower;
    }

    /**
     * 构建EventSecCtlPowerOutput (根据DeviceSecCtlPowerVol)
     * @param deviceSecCtlPowerVol
     * @return
     */
    public EventSecCtlPowerOutput buildSecCtlPowerOutput(DeviceSecCtlPowerVol deviceSecCtlPowerVol){
        EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
        eventSecCtlPowerOutput.setVol01(deviceSecCtlPowerVol.getVol01());
        eventSecCtlPowerOutput.setVol02(deviceSecCtlPowerVol.getVol02());
        eventSecCtlPowerOutput.setVol03(deviceSecCtlPowerVol.getVol03());
        eventSecCtlPowerOutput.setVol04(deviceSecCtlPowerVol.getVol04());
        eventSecCtlPowerOutput.setVol05(deviceSecCtlPowerVol.getVol05());
        eventSecCtlPowerOutput.setVol06(deviceSecCtlPowerVol.getVol06());
        eventSecCtlPowerOutput.setVol07(deviceSecCtlPowerVol.getVol07());
        eventSecCtlPowerOutput.setVol08(deviceSecCtlPowerVol.getVol08());
        eventSecCtlPowerOutput.setVol09(deviceSecCtlPowerVol.getVol09());
        eventSecCtlPowerOutput.setVol10(deviceSecCtlPowerVol.getVol10());
        eventSecCtlPowerOutput.setVol11(deviceSecCtlPowerVol.getVol11());
        eventSecCtlPowerOutput.setVol12(deviceSecCtlPowerVol.getVol12());
        eventSecCtlPowerOutput.setVol13(deviceSecCtlPowerVol.getVol13());
        eventSecCtlPowerOutput.setVol14(deviceSecCtlPowerVol.getVol14());
        eventSecCtlPowerOutput.setVol15(deviceSecCtlPowerVol.getVol15());
        eventSecCtlPowerOutput.setVol16(deviceSecCtlPowerVol.getVol16());
        eventSecCtlPowerOutput.setDeviceId(deviceSecCtlPowerVol.getDevice().getId());
        eventSecCtlPowerOutput.setRecTime(Integer.parseInt(deviceSecCtlPowerVol.getRecTime()/1000 + ""));
        return eventSecCtlPowerOutput;
    }

    /**
     * 构建EventSecCtlPowerOutput (根据DeviceSecCtlPowerRate)
     * @param deviceSecCtlPowerRate
     * @return
     */
    public EventSecCtlPowerOutput buildSecCtlPowerOutput(DeviceSecCtlPowerRate deviceSecCtlPowerRate){
        EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
        eventSecCtlPowerOutput.setRate01(deviceSecCtlPowerRate.getOutputPower01());
        eventSecCtlPowerOutput.setRate02(deviceSecCtlPowerRate.getOutputPower02());
        eventSecCtlPowerOutput.setRate03(deviceSecCtlPowerRate.getOutputPower03());
        eventSecCtlPowerOutput.setRate04(deviceSecCtlPowerRate.getOutputPower04());
        eventSecCtlPowerOutput.setRate05(deviceSecCtlPowerRate.getOutputPower05());
        eventSecCtlPowerOutput.setRate06(deviceSecCtlPowerRate.getOutputPower06());
        eventSecCtlPowerOutput.setRate07(deviceSecCtlPowerRate.getOutputPower07());
        eventSecCtlPowerOutput.setRate08(deviceSecCtlPowerRate.getOutputPower08());
        eventSecCtlPowerOutput.setRate09(deviceSecCtlPowerRate.getOutputPower09());
        eventSecCtlPowerOutput.setRate10(deviceSecCtlPowerRate.getOutputPower10());
        eventSecCtlPowerOutput.setRate11(deviceSecCtlPowerRate.getOutputPower11());
        eventSecCtlPowerOutput.setRate12(deviceSecCtlPowerRate.getOutputPower12());
        eventSecCtlPowerOutput.setRate13(deviceSecCtlPowerRate.getOutputPower13());
        eventSecCtlPowerOutput.setRate14(deviceSecCtlPowerRate.getOutputPower14());
        eventSecCtlPowerOutput.setRate15(deviceSecCtlPowerRate.getOutputPower15());
        eventSecCtlPowerOutput.setRate16(deviceSecCtlPowerRate.getOutputPower16());
        eventSecCtlPowerOutput.setDeviceId(deviceSecCtlPowerRate.getDevice().getId());
        eventSecCtlPowerOutput.setRecTime(Integer.parseInt(deviceSecCtlPowerRate.getRecTime()/1000 + ""));
        return eventSecCtlPowerOutput;
    }

    /**
     * 构建EventSecCtlPowerOutput (DeviceSecCtlPowerElectric)
     * @param deviceSecCtlPowerElectric
     * @return
     */
    public EventSecCtlPowerOutput buildSecCtlPowerOutput(DeviceSecCtlPowerElectric deviceSecCtlPowerElectric){
        EventSecCtlPowerOutput eventSecCtlPowerOutput = new EventSecCtlPowerOutput();
        eventSecCtlPowerOutput.setElec01(deviceSecCtlPowerElectric.getElectric01());
        eventSecCtlPowerOutput.setElec02(deviceSecCtlPowerElectric.getElectric02());
        eventSecCtlPowerOutput.setElec03(deviceSecCtlPowerElectric.getElectric03());
        eventSecCtlPowerOutput.setElec04(deviceSecCtlPowerElectric.getElectric04());
        eventSecCtlPowerOutput.setElec05(deviceSecCtlPowerElectric.getElectric05());
        eventSecCtlPowerOutput.setElec06(deviceSecCtlPowerElectric.getElectric06());
        eventSecCtlPowerOutput.setElec07(deviceSecCtlPowerElectric.getElectric07());
        eventSecCtlPowerOutput.setElec08(deviceSecCtlPowerElectric.getElectric08());
        eventSecCtlPowerOutput.setElec09(deviceSecCtlPowerElectric.getElectric09());
        eventSecCtlPowerOutput.setElec10(deviceSecCtlPowerElectric.getElectric10());
        eventSecCtlPowerOutput.setElec11(deviceSecCtlPowerElectric.getElectric11());
        eventSecCtlPowerOutput.setElec12(deviceSecCtlPowerElectric.getElectric12());
        eventSecCtlPowerOutput.setElec13(deviceSecCtlPowerElectric.getElectric13());
        eventSecCtlPowerOutput.setElec14(deviceSecCtlPowerElectric.getElectric14());
        eventSecCtlPowerOutput.setElec15(deviceSecCtlPowerElectric.getElectric15());
        eventSecCtlPowerOutput.setElec16(deviceSecCtlPowerElectric.getElectric16());
        eventSecCtlPowerOutput.setDeviceId(deviceSecCtlPowerElectric.getDevice().getId());
        eventSecCtlPowerOutput.setRecTime(Integer.parseInt(deviceSecCtlPowerElectric.getRecTime()/1000 + ""));
        return eventSecCtlPowerOutput;
    }

    /**
     * 构建buildSecCtlPowerSet (DeviceSecCtlPowerOverVol)
     * @param deviceSecCtlPowerOverVol
     * @return
     */
    public EventSecCtlPowerSet buildSecCtlPowerSet(DeviceSecCtlPowerOverVol deviceSecCtlPowerOverVol){
        EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
        eventSecCtlPowerSet.setOverVol01(deviceSecCtlPowerOverVol.getOverVolVal01());
        eventSecCtlPowerSet.setOverVol02(deviceSecCtlPowerOverVol.getOverVolVal02());
        eventSecCtlPowerSet.setOverVol03(deviceSecCtlPowerOverVol.getOverVolVal03());
        eventSecCtlPowerSet.setOverVol04(deviceSecCtlPowerOverVol.getOverVolVal04());
        eventSecCtlPowerSet.setOverVol05(deviceSecCtlPowerOverVol.getOverVolVal05());
        eventSecCtlPowerSet.setOverVol06(deviceSecCtlPowerOverVol.getOverVolVal06());
        eventSecCtlPowerSet.setOverVol07(deviceSecCtlPowerOverVol.getOverVolVal07());
        eventSecCtlPowerSet.setOverVol08(deviceSecCtlPowerOverVol.getOverVolVal08());
        eventSecCtlPowerSet.setOverVol09(deviceSecCtlPowerOverVol.getOverVolVal09());
        eventSecCtlPowerSet.setOverVol10(deviceSecCtlPowerOverVol.getOverVolVal10());
        eventSecCtlPowerSet.setOverVol11(deviceSecCtlPowerOverVol.getOverVolVal11());
        eventSecCtlPowerSet.setOverVol12(deviceSecCtlPowerOverVol.getOverVolVal12());
        eventSecCtlPowerSet.setOverVol13(deviceSecCtlPowerOverVol.getOverVolVal13());
        eventSecCtlPowerSet.setOverVol14(deviceSecCtlPowerOverVol.getOverVolVal14());
        eventSecCtlPowerSet.setOverVol15(deviceSecCtlPowerOverVol.getOverVolVal15());
        eventSecCtlPowerSet.setOverVol16(deviceSecCtlPowerOverVol.getOverVolVal16());
        eventSecCtlPowerSet.setDeviceId(deviceSecCtlPowerOverVol.getDevice().getId());
        eventSecCtlPowerSet.setRecTime(Integer.parseInt(deviceSecCtlPowerOverVol.getRecTime()/1000 + ""));
        return eventSecCtlPowerSet;
    }

    /**
     * 构建buildSecCtlPowerSet (DeviceSecCtlPowerUnderVol)
     * @param deviceSecCtlPowerUnderVol
     * @return
     */
    public EventSecCtlPowerSet buildSecCtlPowerSet(DeviceSecCtlPowerUnderVol deviceSecCtlPowerUnderVol){
        EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
        eventSecCtlPowerSet.setUnderVol01(deviceSecCtlPowerUnderVol.getUnderVolVal01());
        eventSecCtlPowerSet.setUnderVol02(deviceSecCtlPowerUnderVol.getUnderVolVal02());
        eventSecCtlPowerSet.setUnderVol03(deviceSecCtlPowerUnderVol.getUnderVolVal03());
        eventSecCtlPowerSet.setUnderVol04(deviceSecCtlPowerUnderVol.getUnderVolVal04());
        eventSecCtlPowerSet.setUnderVol05(deviceSecCtlPowerUnderVol.getUnderVolVal05());
        eventSecCtlPowerSet.setUnderVol06(deviceSecCtlPowerUnderVol.getUnderVolVal06());
        eventSecCtlPowerSet.setUnderVol07(deviceSecCtlPowerUnderVol.getUnderVolVal07());
        eventSecCtlPowerSet.setUnderVol08(deviceSecCtlPowerUnderVol.getUnderVolVal08());
        eventSecCtlPowerSet.setUnderVol09(deviceSecCtlPowerUnderVol.getUnderVolVal09());
        eventSecCtlPowerSet.setUnderVol10(deviceSecCtlPowerUnderVol.getUnderVolVal10());
        eventSecCtlPowerSet.setUnderVol11(deviceSecCtlPowerUnderVol.getUnderVolVal11());
        eventSecCtlPowerSet.setUnderVol12(deviceSecCtlPowerUnderVol.getUnderVolVal12());
        eventSecCtlPowerSet.setUnderVol13(deviceSecCtlPowerUnderVol.getUnderVolVal13());
        eventSecCtlPowerSet.setUnderVol14(deviceSecCtlPowerUnderVol.getUnderVolVal14());
        eventSecCtlPowerSet.setUnderVol15(deviceSecCtlPowerUnderVol.getUnderVolVal15());
        eventSecCtlPowerSet.setUnderVol16(deviceSecCtlPowerUnderVol.getUnderVolVal16());
        eventSecCtlPowerSet.setDeviceId(deviceSecCtlPowerUnderVol.getDevice().getId());
        eventSecCtlPowerSet.setRecTime(Integer.parseInt(deviceSecCtlPowerUnderVol.getRecTime()/1000 + ""));
        return eventSecCtlPowerSet;
    }

    /**
     * 构建buildSecCtlPowerSet (DeviceSecCtlPowerOverElec)
     * @param deviceSecCtlPowerOverElec
     * @return
     */
    public EventSecCtlPowerSet buildSecCtlPowerSet(DeviceSecCtlPowerOverElec deviceSecCtlPowerOverElec){
        EventSecCtlPowerSet eventSecCtlPowerSet = new EventSecCtlPowerSet();
        eventSecCtlPowerSet.setUnderVol01(deviceSecCtlPowerOverElec.getOverElecVal01());
        eventSecCtlPowerSet.setUnderVol02(deviceSecCtlPowerOverElec.getOverElecVal02());
        eventSecCtlPowerSet.setUnderVol03(deviceSecCtlPowerOverElec.getOverElecVal03());
        eventSecCtlPowerSet.setUnderVol04(deviceSecCtlPowerOverElec.getOverElecVal04());
        eventSecCtlPowerSet.setUnderVol05(deviceSecCtlPowerOverElec.getOverElecVal05());
        eventSecCtlPowerSet.setUnderVol06(deviceSecCtlPowerOverElec.getOverElecVal06());
        eventSecCtlPowerSet.setUnderVol07(deviceSecCtlPowerOverElec.getOverElecVal07());
        eventSecCtlPowerSet.setUnderVol08(deviceSecCtlPowerOverElec.getOverElecVal08());
        eventSecCtlPowerSet.setUnderVol09(deviceSecCtlPowerOverElec.getOverElecVal09());
        eventSecCtlPowerSet.setUnderVol10(deviceSecCtlPowerOverElec.getOverElecVal10());
        eventSecCtlPowerSet.setUnderVol11(deviceSecCtlPowerOverElec.getOverElecVal11());
        eventSecCtlPowerSet.setUnderVol12(deviceSecCtlPowerOverElec.getOverElecVal12());
        eventSecCtlPowerSet.setUnderVol13(deviceSecCtlPowerOverElec.getOverElecVal13());
        eventSecCtlPowerSet.setUnderVol14(deviceSecCtlPowerOverElec.getOverElecVal14());
        eventSecCtlPowerSet.setUnderVol15(deviceSecCtlPowerOverElec.getOverElecVal15());
        eventSecCtlPowerSet.setUnderVol16(deviceSecCtlPowerOverElec.getOverElecVal16());
        eventSecCtlPowerSet.setDeviceId(deviceSecCtlPowerOverElec.getDevice().getId());
        eventSecCtlPowerSet.setRecTime(Integer.parseInt(deviceSecCtlPowerOverElec.getRecTime()/1000 + ""));
        return eventSecCtlPowerSet;
    }

    /**
     * 构建EventPosture (根据DeviceEventPosture)
     * @param deviceEventPosture
     * @return
     */
    public EventPosture buildEventPosture(DeviceEventPosture deviceEventPosture){
        EventPosture eventPosture = new EventPosture();
        eventPosture.setOffsetX(deviceEventPosture.getOffsetX());
        eventPosture.setOffsetY(deviceEventPosture.getOffsetY());
        eventPosture.setOffsetZ(deviceEventPosture.getOffsetZ());
        eventPosture.setAccelSpeedX(deviceEventPosture.getAccelSpeedX());
        eventPosture.setAccelSpeedY(deviceEventPosture.getAccelSpeedY());
        eventPosture.setAccelSpeedZ(deviceEventPosture.getAccelSpeedZ());
        eventPosture.setDeviceId(deviceEventPosture.getDevice().getId());
        eventPosture.setRecTime(Integer.parseInt(deviceEventPosture.getRecTime()/1000 + ""));
        eventPosture.setStoreTime(new Date());
        return eventPosture;
    }

    /**
     * 构建event_input (根据DeviceEventInputStatus)
     * @param deviceEventInputStatus
     * @return
     */
    public EventInput buildEventInput(DeviceEventInputStatus deviceEventInputStatus){
        EventInput eventInput = new EventInput();

        String[] passway1 = deviceEventInputStatus.getPassway1().split("");
        String[] passway2 = deviceEventInputStatus.getPassway2().split("");
        if(passway1.length >= 8 && "1".equals(passway1[7]))
            eventInput.setInput01(deviceEventInputStatus.getInput01());
        if(passway1.length >= 8 && "1".equals(passway1[6]))
            eventInput.setInput02(deviceEventInputStatus.getInput02());
        if(passway1.length >= 8 && "1".equals(passway1[5]))
            eventInput.setInput03(deviceEventInputStatus.getInput03());
        if(passway1.length >= 8 && "1".equals(passway1[4]))
            eventInput.setInput04(deviceEventInputStatus.getInput04());
        if(passway1.length >= 8 && "1".equals(passway1[3]))
            eventInput.setInput05(deviceEventInputStatus.getInput05());
        if(passway1.length >= 8 && "1".equals(passway1[2]))
            eventInput.setInput06(deviceEventInputStatus.getInput06());
        if(passway1.length >= 8 && "1".equals(passway1[1]))
            eventInput.setInput07(deviceEventInputStatus.getInput07());
        if(passway1.length >= 8 && "1".equals(passway1[0]))
            eventInput.setInput08(deviceEventInputStatus.getInput08());
        if(passway2.length >= 8 && "1".equals(passway2[7]))
            eventInput.setInput09(deviceEventInputStatus.getInput09());
        if(passway2.length >= 8 && "1".equals(passway2[6]))
            eventInput.setInput10(deviceEventInputStatus.getInput10());
        if(passway2.length >= 8 && "1".equals(passway2[5]))
            eventInput.setInput11(deviceEventInputStatus.getInput11());
        if(passway2.length >= 8 && "1".equals(passway2[4]))
            eventInput.setInput12(deviceEventInputStatus.getInput12());
        if(passway2.length >= 8 && "1".equals(passway2[3]))
            eventInput.setInput13(deviceEventInputStatus.getInput13());
        if(passway2.length >= 8 && "1".equals(passway2[2]))
            eventInput.setInput14(deviceEventInputStatus.getInput14());
        if(passway2.length >= 8 && "1".equals(passway2[1]))
            eventInput.setInput15(deviceEventInputStatus.getInput15());
        if(passway2.length >= 8 && "1".equals(passway2[0]))
            eventInput.setInput16(deviceEventInputStatus.getInput16());
        eventInput.setPassway1(deviceEventInputStatus.getPassway1());
        eventInput.setPassway2(deviceEventInputStatus.getPassway2());
        eventInput.setDeviceId(deviceEventInputStatus.getDevice().getId());
        eventInput.setRecTime(Integer.parseInt(deviceEventInputStatus.getRecTime()/1000 + ""));
        return eventInput;
    }
}
