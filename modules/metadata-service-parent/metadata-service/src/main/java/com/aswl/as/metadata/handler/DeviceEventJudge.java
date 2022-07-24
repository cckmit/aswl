package com.aswl.as.metadata.handler;

import com.aswl.as.common.core.enums.PortSettingEnum;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.iot.dto.DeviceEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备事件判断
 */
@Component("deviceEventJudge")
public class DeviceEventJudge {

    /**
     * 设备报警事件改变判断
     * @param newEventAlarm
     * @param oldEvetAlarm
     * @return
     */
    public Map<String, Object> eventAlarmJudge(EventAlarm newEventAlarm, EventAlarm oldEvetAlarm){
        //判断区别
        Map<String, Object> changeEventMap = new HashMap();
        if(!newEventAlarm.getFld01().equals(oldEvetAlarm.getFld01())){
            changeEventMap.put("fld01", newEventAlarm.getFld01());
        }
        if(!newEventAlarm.getFld02().equals(oldEvetAlarm.getFld02())){
            changeEventMap.put("fld02", newEventAlarm.getFld02());
        }
        if(!newEventAlarm.getFld03().equals(oldEvetAlarm.getFld03())){
            changeEventMap.put("fld03", newEventAlarm.getFld03());
        }
        if(!newEventAlarm.getFld04().equals(oldEvetAlarm.getFld04())){
            changeEventMap.put("fld04", newEventAlarm.getFld04());
        }
        if(!newEventAlarm.getFld05().equals(oldEvetAlarm.getFld05())){
            changeEventMap.put("fld05", newEventAlarm.getFld05());
        }
        if(!newEventAlarm.getFld06().equals(oldEvetAlarm.getFld06())){
            changeEventMap.put("fld06", newEventAlarm.getFld06());
        }
        if(!newEventAlarm.getFld07().equals(oldEvetAlarm.getFld07())){
            changeEventMap.put("fld07", newEventAlarm.getFld07());
        }
        if(!newEventAlarm.getFld08().equals(oldEvetAlarm.getFld08())){
            changeEventMap.put("fld08", newEventAlarm.getFld08());
        }
        if(!newEventAlarm.getFld09().equals(oldEvetAlarm.getFld09())){
            changeEventMap.put("fld09", newEventAlarm.getFld09());
        }
        if(!newEventAlarm.getFld10().equals(oldEvetAlarm.getFld10())){
            changeEventMap.put("fld10", newEventAlarm.getFld10());
        }
        if(!newEventAlarm.getFld11().equals(oldEvetAlarm.getFld11())){
            changeEventMap.put("fld11", newEventAlarm.getFld11());
        }
        if(!newEventAlarm.getFld12().equals(oldEvetAlarm.getFld12())){
            changeEventMap.put("fld12", newEventAlarm.getFld12());
        }
        if(!newEventAlarm.getFld13().equals(oldEvetAlarm.getFld13())){
            changeEventMap.put("fld13", newEventAlarm.getFld13());
        }
        if(!newEventAlarm.getFld14().equals(oldEvetAlarm.getFld14())){
            changeEventMap.put("fld14", newEventAlarm.getFld14());
        }
        if(!newEventAlarm.getFld15().equals(oldEvetAlarm.getFld15())){
            changeEventMap.put("fld15", newEventAlarm.getFld15());
        }
        if(!newEventAlarm.getFld16().equals(oldEvetAlarm.getFld16())){
            changeEventMap.put("fld16", newEventAlarm.getFld16());
        }
        return changeEventMap;
    }

    public Map<String, Object> eventEcurrentJudge(EventEcurrent newEventEcurrent, EventEcurrent oldEventEcurrent){
        Map<String, Object> changeEventMap = new HashMap();
        if(!newEventEcurrent.getFldx().equals(oldEventEcurrent.getFldx())){
            changeEventMap.put("fldx", newEventEcurrent.getFldx());
        }
        if(!newEventEcurrent.getFldy().equals(oldEventEcurrent.getFldy())){
            changeEventMap.put("fldy", newEventEcurrent.getFldy());
        }
        if(!newEventEcurrent.getFldall().equals(oldEventEcurrent.getFldall())){
            changeEventMap.put("fldall", newEventEcurrent.getFldall());
        }
        if(!newEventEcurrent.getFld01().equals(oldEventEcurrent.getFld01())){
            changeEventMap.put("fld01", newEventEcurrent.getFld01());
        }
        if(!newEventEcurrent.getFld02().equals(oldEventEcurrent.getFld02())){
            changeEventMap.put("fld02", newEventEcurrent.getFld02());
        }
        if(!newEventEcurrent.getFld03().equals(oldEventEcurrent.getFld03())){
            changeEventMap.put("fld03", newEventEcurrent.getFld03());
        }
        if(!newEventEcurrent.getFld04().equals(oldEventEcurrent.getFld04())){
            changeEventMap.put("fld04", newEventEcurrent.getFld04());
        }
        if(!newEventEcurrent.getFld05().equals(oldEventEcurrent.getFld05())){
            changeEventMap.put("fld05", newEventEcurrent.getFld05());
        }
        if(!newEventEcurrent.getFld06().equals(oldEventEcurrent.getFld06())){
            changeEventMap.put("fld06", newEventEcurrent.getFld06());
        }
        if(!newEventEcurrent.getFld07().equals(oldEventEcurrent.getFld07())){
            changeEventMap.put("fld07", newEventEcurrent.getFld07());
        }
        if(!newEventEcurrent.getFld08().equals(oldEventEcurrent.getFld08())){
            changeEventMap.put("fld08", newEventEcurrent.getFld08());
        }
        if(!newEventEcurrent.getFld09().equals(oldEventEcurrent.getFld09())){
            changeEventMap.put("fld09", newEventEcurrent.getFld09());
        }
        if(!newEventEcurrent.getFld10().equals(oldEventEcurrent.getFld10())){
            changeEventMap.put("fld10", newEventEcurrent.getFld10());
        }
        if(!newEventEcurrent.getFld11().equals(oldEventEcurrent.getFld11())){
            changeEventMap.put("fld11", newEventEcurrent.getFld11());
        }
        if(!newEventEcurrent.getFld12().equals(oldEventEcurrent.getFld12())){
            changeEventMap.put("fld12", newEventEcurrent.getFld12());
        }
        if(!newEventEcurrent.getFld13().equals(oldEventEcurrent.getFld13())){
            changeEventMap.put("fld13", newEventEcurrent.getFld13());
        }
        if(!newEventEcurrent.getFld14().equals(oldEventEcurrent.getFld14())){
            changeEventMap.put("fld14", newEventEcurrent.getFld14());
        }
        if(!newEventEcurrent.getFld15().equals(oldEventEcurrent.getFld15())){
            changeEventMap.put("fld15", newEventEcurrent.getFld15());
        }
        if(!newEventEcurrent.getFld16().equals(oldEventEcurrent.getFld16())){
            changeEventMap.put("fld16", newEventEcurrent.getFld16());
        }
        return changeEventMap;
    }

    public Map<String, Object> eventEswitchJudge(EventEswitch newEventEswitch, EventEswitch oldEventEswitch){
        Map<String, Object> changeEventMap = new HashMap();

        if(!newEventEswitch.getFldx().equals(oldEventEswitch.getFldx())){
            changeEventMap.put("fldx", newEventEswitch.getFldx());
        }
        if(!newEventEswitch.getFldy().equals(oldEventEswitch.getFldy())){
            changeEventMap.put("fldy", newEventEswitch.getFldy());
        }
        if(!newEventEswitch.getFld01().equals(oldEventEswitch.getFld01())){
            changeEventMap.put("fld01", newEventEswitch.getFld01());
        }
        if(!newEventEswitch.getFld02().equals(oldEventEswitch.getFld02())){
            changeEventMap.put("fld02", newEventEswitch.getFld02());
        }
        if(!newEventEswitch.getFld03().equals(oldEventEswitch.getFld03())){
            changeEventMap.put("fld03", newEventEswitch.getFld03());
        }
        if(!newEventEswitch.getFld04().equals(oldEventEswitch.getFld04())){
            changeEventMap.put("fld04", newEventEswitch.getFld04());
        }
        if(!newEventEswitch.getFld05().equals(oldEventEswitch.getFld05())){
            changeEventMap.put("fld05", newEventEswitch.getFld05());
        }
        if(!newEventEswitch.getFld06().equals(oldEventEswitch.getFld06())){
            changeEventMap.put("fld06", newEventEswitch.getFld06());
        }
        if(!newEventEswitch.getFld07().equals(oldEventEswitch.getFld07())){
            changeEventMap.put("fld07", newEventEswitch.getFld07());
        }
        if(!newEventEswitch.getFld08().equals(oldEventEswitch.getFld08())){
            changeEventMap.put("fld08", newEventEswitch.getFld08());
        }
        if(!newEventEswitch.getFld09().equals(oldEventEswitch.getFld09())){
            changeEventMap.put("fld09", newEventEswitch.getFld09());
        }
        if(!newEventEswitch.getFld10().equals(oldEventEswitch.getFld10())){
            changeEventMap.put("fld10", newEventEswitch.getFld10());
        }
        if(!newEventEswitch.getFld11().equals(oldEventEswitch.getFld11())){
            changeEventMap.put("fld11", newEventEswitch.getFld11());
        }
        if(!newEventEswitch.getFld12().equals(oldEventEswitch.getFld12())){
            changeEventMap.put("fld12", newEventEswitch.getFld12());
        }
        if(!newEventEswitch.getFld13().equals(oldEventEswitch.getFld13())){
            changeEventMap.put("fld13", newEventEswitch.getFld13());
        }
        if(!newEventEswitch.getFld14().equals(oldEventEswitch.getFld14())){
            changeEventMap.put("fld14", newEventEswitch.getFld14());
        }
        if(!newEventEswitch.getFld15().equals(oldEventEswitch.getFld15())){
            changeEventMap.put("fld15", newEventEswitch.getFld15());
        }
        if(!newEventEswitch.getFld16().equals(oldEventEswitch.getFld16())){
            changeEventMap.put("fld16", newEventEswitch.getFld16());
        }
//        if(!newEventEswitch.getFld17().equals(oldEventEswitch.getFld17())){
//            changeEventMap.put("fld17", newEventEswitch.getFld17());
//        }
//        if(!newEventEswitch.getFld18().equals(oldEventEswitch.getFld18())){
//            changeEventMap.put("fld18", newEventEswitch.getFld18());
//        }
//        if(!newEventEswitch.getFld19().equals(oldEventEswitch.getFld19())){
//            changeEventMap.put("fld19", newEventEswitch.getFld19());
//        }
//        if(!newEventEswitch.getFld20().equals(oldEventEswitch.getFld20())){
//            changeEventMap.put("fld20", newEventEswitch.getFld20());
//        }
//        if(!newEventEswitch.getFld21().equals(oldEventEswitch.getFld21())){
//            changeEventMap.put("fld21", newEventEswitch.getFld21());
//        }
//        if(!newEventEswitch.getFld22().equals(oldEventEswitch.getFld22())){
//            changeEventMap.put("fld22", newEventEswitch.getFld22());
//        }
//        if(!newEventEswitch.getFld23().equals(oldEventEswitch.getFld23())){
//            changeEventMap.put("fld23", newEventEswitch.getFld23());
//        }
//        if(!newEventEswitch.getFld24().equals(oldEventEswitch.getFld24())){
//            changeEventMap.put("fld24", newEventEswitch.getFld24());
//        }

        return changeEventMap;
    }

    public Map<String, Object> eventEvoltageJudge(EventEvoltage newEventEoutlet, EventEvoltage oldEventEoutlet){
        Map<String, Object> changeEventMap = new HashMap();
        if(!newEventEoutlet.getFld01().equals(oldEventEoutlet.getFld01())){
            changeEventMap.put("fld01",newEventEoutlet.getFld01());
        }
        if(!newEventEoutlet.getFld02().equals(oldEventEoutlet.getFld02())){
            changeEventMap.put("fld02",newEventEoutlet.getFld02());
        }
        if(!newEventEoutlet.getFld03().equals(oldEventEoutlet.getFld03())){
            changeEventMap.put("fld03",newEventEoutlet.getFld03());
        }
        if(!newEventEoutlet.getFld04().equals(oldEventEoutlet.getFld04())){
            changeEventMap.put("fld04",newEventEoutlet.getFld04());
        }
        if(!newEventEoutlet.getFld05().equals(oldEventEoutlet.getFld05())){
            changeEventMap.put("fld05",newEventEoutlet.getFld05());
        }
        if(!newEventEoutlet.getFld06().equals(oldEventEoutlet.getFld06())){
            changeEventMap.put("fld06",newEventEoutlet.getFld06());
        }
        if(!newEventEoutlet.getFld07().equals(oldEventEoutlet.getFld07())){
            changeEventMap.put("fld07",newEventEoutlet.getFld07());
        }
        if(!newEventEoutlet.getFld08().equals(oldEventEoutlet.getFld08())){
            changeEventMap.put("fld08",newEventEoutlet.getFld08());
        }
        if(!newEventEoutlet.getHumidity().equals(oldEventEoutlet.getHumidity())){
            changeEventMap.put("humidity",newEventEoutlet.getHumidity());
        }
        if(!newEventEoutlet.getTemperature().equals(oldEventEoutlet.getTemperature())){
        	changeEventMap.put("temperature",newEventEoutlet.getTemperature());
        }
		/*
		 * if(!newEventEoutlet.getFld10().equals(oldEventEoutlet.getFld10())){
		 * changeEventMap.put("fld10",newEventEoutlet.getFld10()); }
		 * if(!newEventEoutlet.getFld11().equals(oldEventEoutlet.getFld11())){
		 * changeEventMap.put("fld11",newEventEoutlet.getFld11()); }
		 * if(!newEventEoutlet.getFld12().equals(oldEventEoutlet.getFld12())){
		 * changeEventMap.put("fld12",newEventEoutlet.getFld12()); }
		 * if(!newEventEoutlet.getFld13().equals(oldEventEoutlet.getFld13())){
		 * changeEventMap.put("fld13",newEventEoutlet.getFld13()); }
		 * if(!newEventEoutlet.getFld14().equals(oldEventEoutlet.getFld14())){
		 * changeEventMap.put("fld14",newEventEoutlet.getFld14()); }
		 * if(!newEventEoutlet.getFld15().equals(oldEventEoutlet.getFld15())){
		 * changeEventMap.put("fld15",newEventEoutlet.getFld15()); }
		 * if(!newEventEoutlet.getFld16().equals(oldEventEoutlet.getFld16())){
		 * changeEventMap.put("fld16",newEventEoutlet.getFld16()); }
		 */

        return changeEventMap;
    }

    public Map<String, Object> eventEoutletJudge(EventEoutlet newEventEoutlet, EventEoutlet oldEventEoutlet){
        Map<String, Object> changeEventMap = new HashMap();
        //通道有效位
        String[] passway1 = newEventEoutlet.getPassway1() != null ? newEventEoutlet.getPassway1().split("") : new String[]{};
        String[] passway2 = newEventEoutlet.getPassway1() != null ? newEventEoutlet.getPassway2().split("") : new String[]{};
        if(passway1.length >= 8 && "1".equals(passway1[7]) && newEventEoutlet.getFld01() != null){
            if(newEventEoutlet.getFld01() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld01())){  //如果原本是禁用且实时状态是连通，则把原状态置为启用 20220623
                oldEventEoutlet.setFld01(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld01().equals(oldEventEoutlet.getFld01())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld01())){
                changeEventMap.put("fld01",newEventEoutlet.getFld01());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[6]) && newEventEoutlet.getFld02() != null){
            if(newEventEoutlet.getFld02() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld02())){  //如果原本是禁用且实时状态是连通，则把原状态置为启用 20220623
                oldEventEoutlet.setFld02(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld02().equals(oldEventEoutlet.getFld02())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld02())){
                changeEventMap.put("fld02",newEventEoutlet.getFld02());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[5]) && newEventEoutlet.getFld03() != null){
            if(newEventEoutlet.getFld03() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld03())){  //如果原本是禁用且实时状态是连通，则把原状态置为启用 20220623
                oldEventEoutlet.setFld03(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld03().equals(oldEventEoutlet.getFld03())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld03())){
                changeEventMap.put("fld03",newEventEoutlet.getFld03());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[4]) && newEventEoutlet.getFld04() != null){
            if(newEventEoutlet.getFld04() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld04())){  //如果原本是禁用且实时状态是连通，则把原状态置为启用 20220623
                oldEventEoutlet.setFld04(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld04().equals(oldEventEoutlet.getFld04())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld04())){
                changeEventMap.put("fld04", newEventEoutlet.getFld04());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[3]) && newEventEoutlet.getFld05() != null){
            if(newEventEoutlet.getFld05() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld05())){  //如果原本是禁用且实时状态是连通，则把原状态置为启用 20220623
                oldEventEoutlet.setFld05(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld05().equals(oldEventEoutlet.getFld05())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld05())){
                changeEventMap.put("fld05", newEventEoutlet.getFld05());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[2]) && newEventEoutlet.getFld06() != null){
            if(newEventEoutlet.getFld06() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld06())){  //如果原本是禁用且实时状态是连通，则把原状态置为启用 20220623
                oldEventEoutlet.setFld06(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld06().equals(oldEventEoutlet.getFld06())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld06())) {
                changeEventMap.put("fld06", newEventEoutlet.getFld06());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[1]) && newEventEoutlet.getFld07() != null){
            if(newEventEoutlet.getFld07() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld07())){  //如果原本是禁用且实时状态是连通，则把原状态置为启用 20220623
                oldEventEoutlet.setFld07(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld07().equals(oldEventEoutlet.getFld07())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld07())) {
                changeEventMap.put("fld07", newEventEoutlet.getFld07());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[0]) && newEventEoutlet.getFld08() != null){
            if(newEventEoutlet.getFld08() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld08())){  //如果原本是禁用且实时状态是连通，则把原状态置为启用 20220623
                oldEventEoutlet.setFld08(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld08().equals(oldEventEoutlet.getFld08())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld08())) {
                changeEventMap.put("fld08", newEventEoutlet.getFld08());
            }
        }
        if(passway2.length >= 8 && "1".equals(passway1[7]) && newEventEoutlet.getFld09() != null){
            if(newEventEoutlet.getFld09() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld09())){
                oldEventEoutlet.setFld09(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld09().equals(oldEventEoutlet.getFld09())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld09())) {
                changeEventMap.put("fld09", newEventEoutlet.getFld09());
            }
        }
        if(passway2.length >= 8 && "1".equals(passway1[6]) && newEventEoutlet.getFld10() != null){
            if(newEventEoutlet.getFld10() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld10())){
                oldEventEoutlet.setFld10(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld10().equals(oldEventEoutlet.getFld10())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld10())) {
                changeEventMap.put("fld10", newEventEoutlet.getFld10());
            }
        }
        if(passway2.length >= 8 && "1".equals(passway1[5]) && newEventEoutlet.getFld11() != null){
            if(newEventEoutlet.getFld11() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld11())){
                oldEventEoutlet.setFld11(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld11().equals(oldEventEoutlet.getFld11())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld11())) {
                changeEventMap.put("fld11", newEventEoutlet.getFld11());
            }
        }
        if(passway2.length >= 8 && "1".equals(passway1[4]) && newEventEoutlet.getFld12() != null){
            if(newEventEoutlet.getFld12() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld12())){
                oldEventEoutlet.setFld12(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld12().equals(oldEventEoutlet.getFld12())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld12())) {
                changeEventMap.put("fld12", newEventEoutlet.getFld12());
            }
        }
        if(passway2.length >= 8 && "1".equals(passway1[3]) && newEventEoutlet.getFld13() != null){
            if(newEventEoutlet.getFld13() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld13())){
                oldEventEoutlet.setFld13(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld13().equals(oldEventEoutlet.getFld13())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld13())) {
                changeEventMap.put("fld13", newEventEoutlet.getFld13());
            }
        }
        if(passway2.length >= 8 && "1".equals(passway1[2]) && newEventEoutlet.getFld14() != null){
            if(newEventEoutlet.getFld14() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld14())){
                oldEventEoutlet.setFld14(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld14().equals(oldEventEoutlet.getFld14())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld14())) {
                changeEventMap.put("fld14", newEventEoutlet.getFld14());
            }
        }
        if(passway2.length >= 8 && "1".equals(passway1[1]) && newEventEoutlet.getFld15() != null){
            if(newEventEoutlet.getFld15() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld15())){
                oldEventEoutlet.setFld15(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld15().equals(oldEventEoutlet.getFld15())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld15())) {
                changeEventMap.put("fld15", newEventEoutlet.getFld15());
            }
        }
        if(passway2.length >= 8 && "1".equals(passway1[0]) && newEventEoutlet.getFld16() != null){
            if(newEventEoutlet.getFld16() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld16())){
                oldEventEoutlet.setFld16(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if(!newEventEoutlet.getFld16().equals(oldEventEoutlet.getFld16())
                    && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventEoutlet.getFld16())) {
                changeEventMap.put("fld16", newEventEoutlet.getFld16());
            }
        }
        return changeEventMap;
    }

    public Map<String, Object> eventSfpJudge(EventSfp newEventSfp, EventSfp oldEventSfp){
        Map<String, Object> changeEventMap = new HashMap();
        //通道有效位
        String[] passway1 = newEventSfp.getPassway1() != null ? newEventSfp.getPassway1().split("") : new String[]{};
        if(passway1.length >= 8 && "1".equals(passway1[7]) && newEventSfp.getFld01() != null) {
            if(newEventSfp.getFld01() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld01())){
                oldEventSfp.setFld01(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if (!newEventSfp.getFld01().equals(oldEventSfp.getFld01()) && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld01())) {
                changeEventMap.put("fld01", newEventSfp.getFld01());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[6]) && newEventSfp.getFld02() != null) {
            if (newEventSfp.getFld02() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld02())) {
                oldEventSfp.setFld02(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if (!newEventSfp.getFld02().equals(oldEventSfp.getFld02()) && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld02())) {
                changeEventMap.put("fld02", newEventSfp.getFld02());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[5]) && newEventSfp.getFld03() != null) {
            if (newEventSfp.getFld03() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld03())) {
                oldEventSfp.setFld03(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if (!newEventSfp.getFld03().equals(oldEventSfp.getFld03()) && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld03())) {
                changeEventMap.put("fld03", newEventSfp.getFld03());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[4]) && newEventSfp.getFld04() != null) {
            if (newEventSfp.getFld04() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld04())) {
                oldEventSfp.setFld04(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if (!newEventSfp.getFld04().equals(oldEventSfp.getFld04()) && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld04())) {
                changeEventMap.put("fld04", newEventSfp.getFld04());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[3]) && newEventSfp.getFld05() != null) {
            if (newEventSfp.getFld05() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld05())) {
                oldEventSfp.setFld05(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if (!newEventSfp.getFld05().equals(oldEventSfp.getFld05()) && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld05())) {
                changeEventMap.put("fld05", newEventSfp.getFld05());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[2]) && newEventSfp.getFld06() != null) {
            if (newEventSfp.getFld06() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld06())) {
                oldEventSfp.setFld06(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if (!newEventSfp.getFld06().equals(oldEventSfp.getFld06()) && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld06())) {
                changeEventMap.put("fld06", newEventSfp.getFld06());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[1]) && newEventSfp.getFld07() != null) {
            if (newEventSfp.getFld07() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld07())) {
                oldEventSfp.setFld07(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if (!newEventSfp.getFld07().equals(oldEventSfp.getFld07()) && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld07())) {
                changeEventMap.put("fld07", newEventSfp.getFld07());
            }
        }
        if(passway1.length >= 8 && "1".equals(passway1[0]) && newEventSfp.getFld08() != null) {
            if (newEventSfp.getFld08() == 1 && Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld08())) {
                oldEventSfp.setFld08(Integer.valueOf(PortSettingEnum.Enable.getValue()));
            }
            if (!newEventSfp.getFld08().equals(oldEventSfp.getFld08()) && !Integer.valueOf(PortSettingEnum.Disable.getValue()).equals(oldEventSfp.getFld08())) {
                changeEventMap.put("fld08", newEventSfp.getFld08());
            }
        }
        return changeEventMap;
    }

    /**
     * 判断物联网连接状态最新的和数据库数据的状态的变化
     * @return
     */
    public Map<String, Object> eventIotJudge(EventBase newEventBase, EventBase oldEventBase){
        Map<String, Object> changeEventMap = new HashMap();
        if(!newEventBase.getIotStatus().equals(oldEventBase.getIotStatus())){
            changeEventMap.put("iotStatus",newEventBase.getIotStatus());
        }
        if(!newEventBase.getUseStatus().equals(oldEventBase.getUseStatus())){
            changeEventMap.put("useStatus",newEventBase.getUseStatus());
        }
        return changeEventMap;
    }

    /**
     * 判断最新的GPS和数据库数据的变化
     * @return
     */
    public Map<String,Object> eventGPSJudge(EventBase newEventBase,EventBase oldEventBase){
        Map<String,Object> changeEventMap = new HashMap<>();
        if(newEventBase.getLat() != 0L && !newEventBase.getLat().equals(oldEventBase.getLat())){
            changeEventMap.put("lat",newEventBase.getLat());
        }
        if(newEventBase.getLng() != 0L && !newEventBase.getLng().equals(oldEventBase.getLng())){
            changeEventMap.put("lng",newEventBase.getLng());
        }
        if(newEventBase.getAltitude() != 0L && !newEventBase.getAltitude().equals(oldEventBase.getAltitude())){
            changeEventMap.put("altitude",newEventBase.getAltitude());
        }
        return changeEventMap;
    }
    /**
     * 判断network
     */
    public Map<String,Object> eventNetworkJudge(EventNetwork newEventNetwork,EventNetwork oldeventNetwork){
        Map<String,Object> changeEventMap = new HashMap<>();
        int networkState = oldeventNetwork.getNetworkState() == 1 ? oldeventNetwork.getNetworkState() : 0;
        if(networkState != newEventNetwork.getNetworkState().intValue()){
            changeEventMap.put("network_state",newEventNetwork.getNetworkState());
        }
        return changeEventMap;
    }

    /**
     * 返回所有value为-9且实时状态不为1(禁用的字段)
     */
    public List<String> ignoreField(Object newDeviceEvent, Object dbDeviceEvent){
        List<String> ignoreList = new ArrayList<>();
        if(dbDeviceEvent instanceof EventEoutlet){
            EventEoutlet newEventEoutlet = (EventEoutlet)newDeviceEvent;
            EventEoutlet dbEventEoutlet = (EventEoutlet)dbDeviceEvent;
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld01().intValue() && newEventEoutlet.getFld01().intValue() != 1){
                ignoreList.add("fld01");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld02().intValue() && newEventEoutlet.getFld02().intValue() != 1){
                ignoreList.add("fld02");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld03().intValue() && newEventEoutlet.getFld03().intValue() != 1){
                ignoreList.add("fld03");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld04().intValue() && newEventEoutlet.getFld04().intValue() != 1){
                ignoreList.add("fld04");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld05().intValue() && newEventEoutlet.getFld05().intValue() != 1){
                ignoreList.add("fld05");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld06().intValue() && newEventEoutlet.getFld06().intValue() != 1){
                ignoreList.add("fld06");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld07().intValue() && newEventEoutlet.getFld07().intValue() != 1){
                ignoreList.add("fld07");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld08().intValue() && newEventEoutlet.getFld08().intValue() != 1){
                ignoreList.add("fld08");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld09().intValue() && newEventEoutlet.getFld09().intValue() != 1){
                ignoreList.add("fld09");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld10().intValue() && newEventEoutlet.getFld10().intValue() != 1){
                ignoreList.add("fld10");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld11().intValue() && newEventEoutlet.getFld11().intValue() != 1){
                ignoreList.add("fld11");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld12().intValue() && newEventEoutlet.getFld12().intValue() != 1){
                ignoreList.add("fld12");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld13().intValue() && newEventEoutlet.getFld13().intValue() != 1){
                ignoreList.add("fld13");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld14().intValue() && newEventEoutlet.getFld14().intValue() != 1){
                ignoreList.add("fld14");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld15().intValue() && newEventEoutlet.getFld15().intValue() != 1){
                ignoreList.add("fld15");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld16().intValue() && newEventEoutlet.getFld16().intValue() != 1){
                ignoreList.add("fld16");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld17().intValue() && newEventEoutlet.getFld17().intValue() != 1){
                ignoreList.add("fld17");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld18().intValue() && newEventEoutlet.getFld18().intValue() != 1){
                ignoreList.add("fld18");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld19().intValue() && newEventEoutlet.getFld19().intValue() != 1){
                ignoreList.add("fld19");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld20().intValue() && newEventEoutlet.getFld20().intValue() != 1){
                ignoreList.add("fld20");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld21().intValue() && newEventEoutlet.getFld21().intValue() != 1){
                ignoreList.add("fld21");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld22().intValue() && newEventEoutlet.getFld22().intValue() != 1){
                ignoreList.add("fld22");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld23().intValue() && newEventEoutlet.getFld23().intValue() != 1){
                ignoreList.add("fld23");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventEoutlet.getFld24().intValue() && newEventEoutlet.getFld24().intValue() != 1){
                ignoreList.add("fld24");
            }
        }
        if(dbDeviceEvent instanceof EventSfp){
            EventSfp newEventSfp = (EventSfp)newDeviceEvent;
            EventSfp dbEventSfp = (EventSfp)dbDeviceEvent;
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld01().intValue() && newEventSfp.getFld01().intValue() != 1){
                ignoreList.add("fld01");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld02().intValue() && newEventSfp.getFld02().intValue() != 1){
                ignoreList.add("fld02");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld03().intValue() && newEventSfp.getFld03().intValue() != 1){
                ignoreList.add("fld03");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld04().intValue() && newEventSfp.getFld04().intValue() != 1){
                ignoreList.add("fld04");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld05().intValue() && newEventSfp.getFld05().intValue() != 1){
                ignoreList.add("fld05");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld06().intValue() && newEventSfp.getFld06().intValue() != 1){
                ignoreList.add("fld06");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld07().intValue() && newEventSfp.getFld07().intValue() != 1){
                ignoreList.add("fld07");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld08().intValue() && newEventSfp.getFld08().intValue() != 1){
                ignoreList.add("fld08");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld09().intValue() && newEventSfp.getFld09().intValue() != 1){
                ignoreList.add("fld09");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld10().intValue() && newEventSfp.getFld10().intValue() != 1){
                ignoreList.add("fld10");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld11().intValue() && newEventSfp.getFld11().intValue() != 1){
                ignoreList.add("fld11");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld12().intValue() && newEventSfp.getFld12().intValue() != 1){
                ignoreList.add("fld12");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld13().intValue() && newEventSfp.getFld13().intValue() != 1){
                ignoreList.add("fld13");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld14().intValue() && newEventSfp.getFld14().intValue() != 1){
                ignoreList.add("fld14");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld15().intValue() && newEventSfp.getFld15().intValue() != 1){
                ignoreList.add("fld15");
            }
            if(Integer.valueOf(PortSettingEnum.Disable.getValue()).intValue() == dbEventSfp.getFld16().intValue() && newEventSfp.getFld16().intValue() != 1){
                ignoreList.add("fld16");
            }
        }
        return ignoreList;
    }
}
