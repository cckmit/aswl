package com.aswl.as.metadata.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.module.Device;
import com.aswl.as.ibrs.api.module.EventAlarm;
import com.aswl.as.ibrs.api.module.EventBase;
import com.aswl.as.ibrs.api.module.EventEcurrent;
import com.aswl.as.ibrs.api.module.EventEoutlet;
import com.aswl.as.ibrs.api.module.EventEswitch;
import com.aswl.as.ibrs.api.module.EventEvoltage;
import com.aswl.as.ibrs.api.module.EventNetwork;
import com.aswl.as.ibrs.api.module.EventSfp;
import com.aswl.as.metadata.api.module.EventUcId;
import com.aswl.as.metadata.api.vo.EventHisAlarmVO;
import com.aswl.as.metadata.api.vo.EventHisBaseVO;
import com.aswl.as.metadata.api.vo.EventHisEcurrentVO;
import com.aswl.as.metadata.api.vo.EventHisEoutletVO;
import com.aswl.as.metadata.api.vo.EventHisEswitchVO;
import com.aswl.as.metadata.api.vo.EventHisEvoltageVO;
import com.aswl.as.metadata.api.vo.EventHisNetworkVO;
import com.aswl.as.metadata.api.vo.EventHisSfpVO;
import com.aswl.as.metadata.mapper.DeviceEventMapper;
import com.aswl.as.metadata.mapper.EventAlarmMapper;
import com.aswl.as.metadata.mapper.EventBaseMapper;
import com.aswl.as.metadata.mapper.EventEcurrentMapper;
import com.aswl.as.metadata.mapper.EventEoutletMapper;
import com.aswl.as.metadata.mapper.EventEswitchMapper;
import com.aswl.as.metadata.mapper.EventEvoltageMapper;
import com.aswl.as.metadata.mapper.EventHisAlarmMapper;
import com.aswl.as.metadata.mapper.EventHisBaseMapper;
import com.aswl.as.metadata.mapper.EventHisEcurrentMapper;
import com.aswl.as.metadata.mapper.EventHisEoutletMapper;
import com.aswl.as.metadata.mapper.EventHisEswitchMapper;
import com.aswl.as.metadata.mapper.EventHisEvoltageMapper;
import com.aswl.as.metadata.mapper.EventHisNetworkMapper;
import com.aswl.as.metadata.mapper.EventHisSfpMapper;
import com.aswl.as.metadata.mapper.EventNetworkMapper;
import com.aswl.as.metadata.mapper.EventSfpMapper;
import com.aswl.as.metadata.utils.DateUtils;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceEventService extends CrudService<DeviceEventMapper, EventAlarm> {

    private DeviceEventMapper deviceEventMapper;

    private EventAlarmMapper eventAlarmMapper;
    private EventBaseMapper eventBaseMapper;
    private EventEcurrentMapper eventEcurrentMapper;
    private EventEoutletMapper eventEoutletMapper;
    private EventEswitchMapper eventEswitchMapper;
    private EventEvoltageMapper eventEvoltageMapper;
    private EventNetworkMapper eventNetworkMapper;
    private EventSfpMapper eventSfpMapper;

    private EventHisAlarmMapper eventHisAlarmMapper;
    private EventHisBaseMapper eventHisBaseMapper;
    private EventHisEcurrentMapper eventHisEcurrentMapper;
    private EventHisEoutletMapper eventHisEoutletMapper;
    private EventHisEswitchMapper eventHisEswitchMapper;
    private EventHisEvoltageMapper eventHisEvoltageMapper;
    private EventHisNetworkMapper eventHisNetworkMapper;
    private EventHisSfpMapper eventHisSfpMapper;

    public EventAlarm findEventAlarmByDeviceId(String deviceId){
        if(StringUtils.isEmpty(deviceId)){
            return null;
        }
        return deviceEventMapper.findEventAlarmByDeviceId(deviceId);
    }

    /**
     * 生成事件历史记录
     * @param device
     */
    @Transactional
    public void generateEventHisRecord(Device device,EventUcId eventUcId){
        if(device == null){
            return;
        }
        //获取所有事件
        EventAlarm eventAlarm = eventAlarmMapper.findByDeviceId(device.getId());
        EventBase eventBase = eventBaseMapper.findByDeviceId(device.getId());
        EventEcurrent eventEcurrent = eventEcurrentMapper.findByDeviceId(device.getId());
        EventEoutlet eventEoutlet = eventEoutletMapper.findByDeviceId(device.getId());
        EventEswitch eventEswitch = eventEswitchMapper.findByDeviceId(device.getId());
        EventEvoltage eventEvoltage = eventEvoltageMapper.findByDeviceId(device.getId());
        EventNetwork eventNetwork = eventNetworkMapper.findByDeviceId(device.getId());
        EventSfp eventSfp = eventSfpMapper.findByDeviceId(device.getId());

        //获取
        String yearMonth = DateUtils.formatDate(new Date(), "yyyyMM");
        //保存到历史记录
        EventHisAlarmVO eventHisAlarmVO = createEventHisAlarm(device, eventAlarm, eventUcId);
        EventHisBaseVO eventHisBaseVO = createEventHisBase(device, eventBase, eventUcId);
        EventHisEcurrentVO eventHisEcurrentVO = createEventHisEcurrent(device, eventEcurrent, eventUcId);
        EventHisEoutletVO eventHisEoutletVO = createEventHisEoutlet(device, eventEoutlet, eventUcId);
        EventHisEswitchVO eventHisEswitchVO = createEventHisEswitch(device, eventEswitch, eventUcId);
        EventHisEvoltageVO eventHisEvoltageVO = createEventHisEvoltage(device, eventEvoltage, eventUcId);
        EventHisNetworkVO eventHisNetworkVO = createEventHisNetwork(device, eventNetwork, eventUcId);
        EventHisSfpVO eventHisSfpVO = createEventHisSfp(device, eventSfp, eventUcId);
        eventHisAlarmVO.setYearMonth(yearMonth);
        eventHisBaseVO.setYearMonth(yearMonth);
        eventHisEcurrentVO.setYearMonth(yearMonth);
        eventHisEoutletVO.setYearMonth(yearMonth);
        eventHisEswitchVO.setYearMonth(yearMonth);
        eventHisEvoltageVO.setYearMonth(yearMonth);
        eventHisNetworkVO.setYearMonth(yearMonth);
        eventHisSfpVO.setYearMonth(yearMonth);
        eventHisAlarmMapper.insert(eventHisAlarmVO);
        eventHisBaseMapper.insert(eventHisBaseVO);
        eventHisEcurrentMapper.insert(eventHisEcurrentVO);
        eventHisEoutletMapper.insert(eventHisEoutletVO);
        eventHisEswitchMapper.insert(eventHisEswitchVO);
        eventHisEvoltageMapper.insert(eventHisEvoltageVO);
        eventHisNetworkMapper.insert(eventHisNetworkVO);
        eventHisSfpMapper.insert(eventHisSfpVO);
    }

    public EventHisAlarmVO createEventHisAlarm(Device device, EventAlarm eventAlarm, EventUcId eventUcId){
        EventHisAlarmVO eventHisAlarm = new EventHisAlarmVO();
        eventHisAlarm.setUEventId(eventUcId.getId());
        eventHisAlarm.setDeviceId(device.getId());
        eventHisAlarm.setRegionNo(device.getRegionCode());
        eventHisAlarm.setPassway1(eventAlarm.getPassway1());
        eventHisAlarm.setPassway2(eventAlarm.getPassway2());
        eventHisAlarm.setPassway3(eventAlarm.getPassway3());
        eventHisAlarm.setFld01(eventAlarm.getFld01());
        eventHisAlarm.setFld02(eventAlarm.getFld02());
        eventHisAlarm.setFld03(eventAlarm.getFld03());
        eventHisAlarm.setFld04(eventAlarm.getFld04());
        eventHisAlarm.setFld05(eventAlarm.getFld05());
        eventHisAlarm.setFld06(eventAlarm.getFld06());
        eventHisAlarm.setFld07(eventAlarm.getFld07());
        eventHisAlarm.setFld08(eventAlarm.getFld08());
        eventHisAlarm.setFld09(eventAlarm.getFld09());
        eventHisAlarm.setFld10(eventAlarm.getFld10());
        eventHisAlarm.setFld11(eventAlarm.getFld11());
        eventHisAlarm.setFld12(eventAlarm.getFld12());
        eventHisAlarm.setFld13(eventAlarm.getFld13());
        eventHisAlarm.setFld14(eventAlarm.getFld14());
        eventHisAlarm.setFld15(eventAlarm.getFld15());
        eventHisAlarm.setFld16(eventAlarm.getFld16());
        eventHisAlarm.setFld17(eventAlarm.getFld17());
        eventHisAlarm.setFld18(eventAlarm.getFld18());
        eventHisAlarm.setFld19(eventAlarm.getFld19());
        eventHisAlarm.setFld20(eventAlarm.getFld20());
        eventHisAlarm.setFld21(eventAlarm.getFld21());
        eventHisAlarm.setFld22(eventAlarm.getFld22());
        eventHisAlarm.setFld23(eventAlarm.getFld23());
        eventHisAlarm.setFld24(eventAlarm.getFld24());
        eventHisAlarm.setRecTime(eventAlarm.getRecTime().intValue());
        eventHisAlarm.setRecDate(new Date());
        eventHisAlarm.setStoreTime(new Date());
        eventHisAlarm.setCommonValue_meta("admin", device.getApplicationCode(), device.getTenantCode(),device.getProjectId());
        return eventHisAlarm;
    }

    public EventHisBaseVO createEventHisBase(Device device, EventBase eventBase, EventUcId eventUcId){
        EventHisBaseVO eventHisBase = new EventHisBaseVO();
        eventHisBase.setUEventId(eventUcId.getId());
        eventHisBase.setDeviceId(device.getId());
        eventHisBase.setRegionNo(device.getRegionCode());
        eventHisBase.setUseStatus(eventBase.getUseStatus());
        eventHisBase.setIotStatus(eventBase.getIotStatus());
        eventHisBase.setKeyMac(eventBase.getKeyMac());
        eventHisBase.setCiphertextId(eventBase.getCiphertextId());
        eventHisBase.setAuthTime(eventBase.getAuthTime());
        eventHisBase.setAuthStatus(eventBase.getAuthStatus());
        eventHisBase.setGateSwitch(eventBase.getGateSwitch());
        eventHisBase.setLightingSwitch(eventBase.getLightingSwitch());
        eventHisBase.setUtcTime(eventBase.getUtcTime());
        eventHisBase.setLng(eventBase.getLng());
        eventHisBase.setLat(eventBase.getLat());
        eventHisBase.setAltitude(eventBase.getAltitude());
        eventHisBase.setFld01(eventBase.getFld01());
        eventHisBase.setFld02(eventBase.getFld02());
        eventHisBase.setFld03(eventBase.getFld03());
        eventHisBase.setFld04(eventBase.getFld04());
        eventHisBase.setFld05(eventBase.getFld05());
        eventHisBase.setFld06(eventBase.getFld06());
        eventHisBase.setFld07(eventBase.getFld07());
        eventHisBase.setFld08(eventBase.getFld08());
        eventHisBase.setRecTime(eventBase.getRecTime());
        eventHisBase.setRecDate(new Date());
        eventHisBase.setStoreTime(new Date());
        eventHisBase.setCommonValue_meta("admin", device.getApplicationCode(), device.getTenantCode(),device.getProjectId());
        return eventHisBase;
    }

    public EventHisEcurrentVO createEventHisEcurrent(Device device, EventEcurrent eventEcurrent, EventUcId eventUcId){
        EventHisEcurrentVO eventHisEcurrent = new EventHisEcurrentVO();
        eventHisEcurrent.setUEventId(eventUcId.getId());
        eventHisEcurrent.setDeviceId(device.getId());
        eventHisEcurrent.setRegionNo(device.getRegionCode());
        eventHisEcurrent.setPassway1(eventEcurrent.getPassway1());
        eventHisEcurrent.setPassway2(eventEcurrent.getPassway2());
        eventHisEcurrent.setPassway3(eventEcurrent.getPassway3());
        eventHisEcurrent.setPassway4(eventEcurrent.getPassway4());
        eventHisEcurrent.setFld01(eventEcurrent.getFld01());
        eventHisEcurrent.setFld02(eventEcurrent.getFld02());
        eventHisEcurrent.setFld03(eventEcurrent.getFld03());
        eventHisEcurrent.setFld04(eventEcurrent.getFld04());
        eventHisEcurrent.setFld05(eventEcurrent.getFld05());
        eventHisEcurrent.setFld06(eventEcurrent.getFld06());
        eventHisEcurrent.setFld07(eventEcurrent.getFld07());
        eventHisEcurrent.setFld08(eventEcurrent.getFld08());
        eventHisEcurrent.setFld09(eventEcurrent.getFld09());
        eventHisEcurrent.setFld10(eventEcurrent.getFld10());
        eventHisEcurrent.setFld11(eventEcurrent.getFld11());
        eventHisEcurrent.setFld12(eventEcurrent.getFld12());
        eventHisEcurrent.setFld13(eventEcurrent.getFld13());
        eventHisEcurrent.setFld14(eventEcurrent.getFld14());
        eventHisEcurrent.setFld15(eventEcurrent.getFld15());
        eventHisEcurrent.setFld16(eventEcurrent.getFld16());
        eventHisEcurrent.setFld17(eventEcurrent.getFld17());
        eventHisEcurrent.setFld18(eventEcurrent.getFld18());
        eventHisEcurrent.setFld19(eventEcurrent.getFld19());
        eventHisEcurrent.setFld20(eventEcurrent.getFld20());
        eventHisEcurrent.setFld21(eventEcurrent.getFld21());
        eventHisEcurrent.setFld22(eventEcurrent.getFld22());
        eventHisEcurrent.setFld23(eventEcurrent.getFld23());
        eventHisEcurrent.setFld24(eventEcurrent.getFld24());
        eventHisEcurrent.setRecTime(eventEcurrent.getRecTime());
        eventHisEcurrent.setRecDate(new Date());
        eventHisEcurrent.setStoreTime(new Date());
        eventHisEcurrent.setCommonValue_meta("admin", device.getApplicationCode(), device.getTenantCode(),device.getProjectId());
        return eventHisEcurrent;
    }

    public EventHisEoutletVO createEventHisEoutlet(Device device, EventEoutlet eventEoutlet, EventUcId eventUcId){
        EventHisEoutletVO eventHisEoutlet = new EventHisEoutletVO();
        eventHisEoutlet.setUEventId(eventUcId.getId());
        eventHisEoutlet.setDeviceId(device.getId());
        eventHisEoutlet.setRegionNo(device.getRegionCode());
        eventHisEoutlet.setPassway1(eventEoutlet.getPassway1());
        eventHisEoutlet.setPassway2(eventEoutlet.getPassway2());
        eventHisEoutlet.setPassway3(eventEoutlet.getPassway3());
        eventHisEoutlet.setFld01(eventEoutlet.getFld01());
        eventHisEoutlet.setFld02(eventEoutlet.getFld02());
        eventHisEoutlet.setFld03(eventEoutlet.getFld03());
        eventHisEoutlet.setFld04(eventEoutlet.getFld04());
        eventHisEoutlet.setFld05(eventEoutlet.getFld05());
        eventHisEoutlet.setFld06(eventEoutlet.getFld06());
        eventHisEoutlet.setFld07(eventEoutlet.getFld07());
        eventHisEoutlet.setFld08(eventEoutlet.getFld08());
        eventHisEoutlet.setFld09(eventEoutlet.getFld09());
        eventHisEoutlet.setFld10(eventEoutlet.getFld10());
        eventHisEoutlet.setFld11(eventEoutlet.getFld11());
        eventHisEoutlet.setFld12(eventEoutlet.getFld12());
        eventHisEoutlet.setFld13(eventEoutlet.getFld13());
        eventHisEoutlet.setFld14(eventEoutlet.getFld14());
        eventHisEoutlet.setFld15(eventEoutlet.getFld15());
        eventHisEoutlet.setFld16(eventEoutlet.getFld16());
        eventHisEoutlet.setFld17(eventEoutlet.getFld17());
        eventHisEoutlet.setFld18(eventEoutlet.getFld18());
        eventHisEoutlet.setFld19(eventEoutlet.getFld19());
        eventHisEoutlet.setFld20(eventEoutlet.getFld20());
        eventHisEoutlet.setFld21(eventEoutlet.getFld21());
        eventHisEoutlet.setFld22(eventEoutlet.getFld22());
        eventHisEoutlet.setFld23(eventEoutlet.getFld23());
        eventHisEoutlet.setFld24(eventEoutlet.getFld24());
        eventHisEoutlet.setRecTime(eventEoutlet.getRecTime());
        eventHisEoutlet.setRecDate(new Date());
        eventHisEoutlet.setStoreTime(new Date());
        eventHisEoutlet.setCommonValue_meta("admin", device.getApplicationCode(), device.getTenantCode(),device.getProjectId());
        return eventHisEoutlet;
    }

    public EventHisEswitchVO createEventHisEswitch(Device device, EventEswitch eventEswitch, EventUcId eventUcId){
        EventHisEswitchVO eventHisEswitch = new EventHisEswitchVO();
        eventHisEswitch.setUEventId(eventUcId.getId());
        eventHisEswitch.setDeviceId(device.getId());
        eventHisEswitch.setRegionNo(device.getRegionCode());
        eventHisEswitch.setPassway1(eventEswitch.getPassway1());
        eventHisEswitch.setPassway2(eventEswitch.getPassway2());
        eventHisEswitch.setPassway3(eventEswitch.getPassway3());
        eventHisEswitch.setPassway4(eventEswitch.getPassway4());
        eventHisEswitch.setFld01(eventEswitch.getFld01());
        eventHisEswitch.setFld02(eventEswitch.getFld02());
        eventHisEswitch.setFld03(eventEswitch.getFld03());
        eventHisEswitch.setFld04(eventEswitch.getFld04());
        eventHisEswitch.setFld05(eventEswitch.getFld05());
        eventHisEswitch.setFld06(eventEswitch.getFld06());
        eventHisEswitch.setFld07(eventEswitch.getFld07());
        eventHisEswitch.setFld08(eventEswitch.getFld08());
        eventHisEswitch.setFld09(eventEswitch.getFld09());
        eventHisEswitch.setFld10(eventEswitch.getFld10());
        eventHisEswitch.setFld11(eventEswitch.getFld11());
        eventHisEswitch.setFld12(eventEswitch.getFld12());
        eventHisEswitch.setFld13(eventEswitch.getFld13());
        eventHisEswitch.setFld14(eventEswitch.getFld14());
        eventHisEswitch.setFld15(eventEswitch.getFld15());
        eventHisEswitch.setFld16(eventEswitch.getFld16());
        eventHisEswitch.setFld17(eventEswitch.getFld17());
        eventHisEswitch.setFld18(eventEswitch.getFld18());
        eventHisEswitch.setFld19(eventEswitch.getFld19());
        eventHisEswitch.setFld20(eventEswitch.getFld20());
        eventHisEswitch.setFld21(eventEswitch.getFld21());
        eventHisEswitch.setFld22(eventEswitch.getFld22());
        eventHisEswitch.setFld23(eventEswitch.getFld23());
        eventHisEswitch.setFld24(eventEswitch.getFld24());
        eventHisEswitch.setRecTime(eventEswitch.getRecTime());
        eventHisEswitch.setRecDay(new Date());
        eventHisEswitch.setStoreTime(new Date());
        eventHisEswitch.setCommonValue_meta("admin", device.getApplicationCode(), device.getTenantCode(),device.getProjectId());
        return eventHisEswitch;
    }

    public EventHisEvoltageVO createEventHisEvoltage(Device device, EventEvoltage eventEvoltage, EventUcId eventUcId){
        EventHisEvoltageVO eventHisEvoltage = new EventHisEvoltageVO();
        eventHisEvoltage.setUEventId(eventUcId.getId());
        eventHisEvoltage.setDeviceId(device.getId());
        eventHisEvoltage.setRegionNo(device.getRegionCode());
        eventHisEvoltage.setTemperature(eventEvoltage.getTemperature());
        eventHisEvoltage.setHumidity(eventEvoltage.getHumidity());
        eventHisEvoltage.setFld01(eventEvoltage.getFld01());
        eventHisEvoltage.setVal01(eventEvoltage.getVal01());
        eventHisEvoltage.setFld02(eventEvoltage.getFld02());
        eventHisEvoltage.setVal02(eventEvoltage.getVal02());
        eventHisEvoltage.setFld03(eventEvoltage.getFld03());
        eventHisEvoltage.setVal03(eventEvoltage.getVal03());
        eventHisEvoltage.setFld04(eventEvoltage.getFld04());
        eventHisEvoltage.setVal04(eventEvoltage.getVal04());
        eventHisEvoltage.setFld05(eventEvoltage.getFld05());
        eventHisEvoltage.setVal05(eventEvoltage.getVal05());
        eventHisEvoltage.setFld06(eventEvoltage.getFld06());
        eventHisEvoltage.setVal06(eventEvoltage.getVal06());
        eventHisEvoltage.setFld07(eventEvoltage.getFld07());
        eventHisEvoltage.setVal07(eventEvoltage.getVal07());
        eventHisEvoltage.setFld08(eventEvoltage.getFld08());
        eventHisEvoltage.setVal08(eventEvoltage.getVal08());
        eventHisEvoltage.setFld09(eventEvoltage.getFld09());
        eventHisEvoltage.setVal09(eventEvoltage.getVal09());
        eventHisEvoltage.setFld10(eventEvoltage.getFld10());
        eventHisEvoltage.setVal10(eventEvoltage.getVal10());
        eventHisEvoltage.setFld11(eventEvoltage.getFld11());
        eventHisEvoltage.setVal11(eventEvoltage.getVal11());
        eventHisEvoltage.setFld12(eventEvoltage.getFld12());
        eventHisEvoltage.setVal12(eventEvoltage.getVal12());
        eventHisEvoltage.setFld13(eventEvoltage.getFld13());
        eventHisEvoltage.setVal13(eventEvoltage.getVal13());
        eventHisEvoltage.setFld14(eventEvoltage.getFld14());
        eventHisEvoltage.setVal14(eventEvoltage.getVal14());
        eventHisEvoltage.setFld15(eventEvoltage.getFld15());
        eventHisEvoltage.setVal15(eventEvoltage.getVal15());
        eventHisEvoltage.setFld16(eventEvoltage.getFld16());
        eventHisEvoltage.setVal16(eventEvoltage.getVal16());
        Long times = System.currentTimeMillis();
        eventHisEvoltage.setRecTime(eventEvoltage.getRecTime()==0 ? (times.intValue())/1000:eventEvoltage.getRecTime());
        eventHisEvoltage.setRecDate(new Date());
        eventHisEvoltage.setStoreTime(new Date());
        eventHisEvoltage.setCommonValue_meta("admin", device.getApplicationCode(), device.getTenantCode(),device.getProjectId());
        return eventHisEvoltage;
    }

    public EventHisNetworkVO createEventHisNetwork(Device device, EventNetwork eventNetwork, EventUcId eventUcId){
        EventHisNetworkVO eventHisNetwork = new EventHisNetworkVO();
        eventHisNetwork.setUEventId(eventUcId.getId());
        eventHisNetwork.setDeviceId(device.getId());
        eventHisNetwork.setRegionNo(device.getRegionCode());
        eventHisNetwork.setNetworkState(eventNetwork.getNetworkState());
        eventHisNetwork.setRecDate(new Date());
        eventHisNetwork.setStoreTime(new Date());
        eventHisNetwork.setCommonValue_meta("admin", device.getApplicationCode(), device.getTenantCode(),device.getProjectId());
        return eventHisNetwork;
    }

    public EventHisSfpVO createEventHisSfp(Device device, EventSfp eventSfp, EventUcId eventUcId){
        EventHisSfpVO eventHisSfp = new EventHisSfpVO();
        eventHisSfp.setUEventId(eventUcId.getId());
        eventHisSfp.setDeviceId(device.getId());
        eventHisSfp.setRegionNo(device.getRegionCode());
        eventHisSfp.setPassway1(eventSfp.getPassway1());
        eventHisSfp.setPassway2(eventSfp.getPassway2());
        eventHisSfp.setFld01(eventSfp.getFld01());
        eventHisSfp.setFld02(eventSfp.getFld02());
        eventHisSfp.setFld03(eventSfp.getFld03());
        eventHisSfp.setFld04(eventSfp.getFld04());
        eventHisSfp.setFld05(eventSfp.getFld05());
        eventHisSfp.setFld06(eventSfp.getFld06());
        eventHisSfp.setFld07(eventSfp.getFld07());
        eventHisSfp.setFld08(eventSfp.getFld08());
        eventHisSfp.setFld09(eventSfp.getFld09());
        eventHisSfp.setFld10(eventSfp.getFld10());
        eventHisSfp.setFld11(eventSfp.getFld11());
        eventHisSfp.setFld12(eventSfp.getFld12());
        eventHisSfp.setFld14(eventSfp.getFld14());
        eventHisSfp.setFld15(eventSfp.getFld15());
        eventHisSfp.setFld16(eventSfp.getFld16());
        eventHisSfp.setRecTime(eventSfp.getRecTime());
        eventHisSfp.setRecDate(new Date());
        eventHisSfp.setStoreTime(new Date());
        eventHisSfp.setCommonValue_meta("admin", device.getApplicationCode(), device.getTenantCode(),device.getProjectId());
        return eventHisSfp;
    }
    
    @Transactional
    @Override
    public int update(EventAlarm eventAlarm) {
        return eventAlarmMapper.update(eventAlarm);
    }
}
