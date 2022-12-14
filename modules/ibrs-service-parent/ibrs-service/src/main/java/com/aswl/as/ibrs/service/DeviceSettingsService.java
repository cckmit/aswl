package com.aswl.as.ibrs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.*;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.DevicePortSettingDto;
import com.aswl.as.ibrs.api.dto.DeviceSettingsDto;
import com.aswl.as.ibrs.api.dto.DeviceSettingsListDto;
import com.aswl.as.ibrs.api.module.AlarmType;
import com.aswl.as.ibrs.api.module.DeviceEventAlarm;
import com.aswl.as.ibrs.api.module.DeviceOperationLog;
import com.aswl.as.ibrs.api.module.DeviceSettings;
import com.aswl.as.ibrs.api.vo.DeviceVo;
import com.aswl.as.ibrs.enums.MQExchange;
import com.aswl.as.ibrs.mapper.*;
import com.aswl.as.ibrs.mq.MQSender;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.iot.dto.DeviceCommand;
import com.aswl.iot.dto.QueryDeviceDataDto;
import com.aswl.iot.dto.constant.MQConstants;
import com.aswl.iot.dto.constant.OperationMoudle;
import com.aswl.iot.dto.enums.DoorDeploySettingEnum;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceSettingsService extends CrudService<DeviceSettingsMapper, DeviceSettings> {
    private final DeviceSettingsMapper deviceSettingsMapper;
    private final EventEoutletMapper eventEoutletMapper;
    private final EventSfpMapper eventSfpMapper;
    private EventUcMetadataMapper eventUcMetadataMapper;
    private DeviceEventAlarmMapper deviceEventAlarmMapper;
    private DeviceMapper deviceMapper;
    private MQSender mqSender;
    private UserServiceClient userServiceClient;
    private DeviceOperationLogMapper deviceOperationLogMapper;
    private final AlarmTypeService alarmTypeService;

    /**
     * ??????????????????
     *
     * @param deviceSettings
     * @return int
     */
    @Transactional
    @Override
    public int insert(DeviceSettings deviceSettings) {
        return super.insert(deviceSettings);
    }

    /**
     * ??????????????????
     *
     * @param deviceSettings
     * @return int
     */
    @Transactional
    @Override
    public int delete(DeviceSettings deviceSettings) {
        return super.delete(deviceSettings);
    }


    /**
     * ????????????????????????
     *
     * @param list ??????
     * @return int ??????
     */
    @Transactional
    public int insertBatch(List<DeviceSettingsDto> list) {
        // ??????????????????
        UserInfoDto userInfo = null;
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        if (info.getStatus() == 200){
            userInfo = info.getData();
        }
        List<DeviceSettings> lists=new ArrayList<>();
        if (list!=null && list.size()>0) {
            for (DeviceSettingsDto dto : list) {
                DeviceSettings settings = new DeviceSettings();
                BeanUtils.copyProperties(dto, settings);
                settings.setId(IdGen.snowflakeId());
                settings.setStoreTime(new Date());
                settings.setApplicationCode(SysUtil.getSysCode());
                settings.setTenantCode(SysUtil.getTenantCode());
                lists.add(settings);
                //??????????????????
                DeviceOperationLog log = new DeviceOperationLog();
                log.setId(IdGen.snowflakeId());
                log.setDeviceId(dto.getDeviceId());
                log.setOperationCommand("??????");
                // ????????????
                if (dto.getSetType().equals(DeviceSettingsType.DOOR_DEPLOY.getMode())){
                    if (dto.getMode().equals(DeviceSettingsType.DOOR_DEPLOY.getType())){
                        log.setOperationContent("????????????????????????");
                        log.setFullContent("???????????????????????????" + DeviceSettingsType.DOOR_DEPLOY.getDescription());
                    }else{
                        log.setOperationContent("????????????????????????");
                        log.setFullContent("???????????????????????????" + DeviceSettingsType.DOOR_REVOKE.getDescription());
                    }
                 //????????????
                }else{
                    log.setOperationContent("????????????");
                    //????????????
                    if (dto.getMode().equals(DeviceSettingsType.TEMPERATURE_AUTO.getType())){
                        log.setOperationContent(dto.getFldName());
                        //????????????
                    }else{
                        log.setOperationContent(dto.getFldName());
                    }
                }
                log.setUserId(userInfo.getId());
                log.setUserName(userInfo.getName());
                log.setCreateDate(new Date());
                log.setApplicationCode(SysUtil.getSysCode());
                log.setTenantCode(SysUtil.getTenantCode());
                deviceOperationLogMapper.insert(log);
            }
        }
        return deviceSettingsMapper.insertBatch(lists);
    }

    /**
     *  ??????????????????????????????
     * @param setType
     * @return int
     */
   public  int deleteByMode(String setType){
        return deviceSettingsMapper.deleteByMode(setType);
    }


    /**
     *  ????????????ID????????????
     * @param list
     * @return int
     */
    public  int deleteBath(List list){
        return deviceSettingsMapper.deleteBath(list);
    }

    /**
     *  ????????????id?????????????????????
     * @return
     */
    public DeviceSettings findDeviceSettings(String deviceId,String setType){

        return deviceSettingsMapper.findDeviceSettings(deviceId,setType);
    }

    /**
     * ???????????????????????????????????????
     *
     * @param list
     * @return int
     */
    @Transactional
    public int updatePortSettings(List<DevicePortSettingDto> list) {
        int update = 0;
        List<String> enableSfpDeviceIds = new ArrayList<>();
        List<String> enableOutletDeviceIds = new ArrayList<>();
        for (DevicePortSettingDto deviceSettingsDto: list) {
            String flag = deviceSettingsDto.getFlag();
            String status = deviceSettingsDto.getStatus();
            String fld = deviceSettingsDto.getFld();
            String fldName =deviceSettingsDto.getFldName();
            String deviceId = deviceSettingsDto.getDeviceId();
            //?????????
            if ("1".equals(flag)) {
                // ??????
                if ("0".equals(status)) {
                    eventSfpMapper.updateBydeviceIdAndFld(fld, PortSettingEnum.Enable.getValue(),deviceId);
//                    enableDevicePort(deviceId, EventTableEnum.SFP.getTableName());
                    enableSfpDeviceIds.add(deviceId);
                    //??????????????????
                    insertLog(flag,status,deviceId,fldName);
                } else {
                     eventSfpMapper.updateBydeviceIdAndFld(fld, PortSettingEnum.Disable.getValue(),deviceId);
                    forbiddenDevicePort(deviceId, EventTableEnum.SFP.getTableName(), fld);
                    //??????????????????
                    insertLog(flag,status,deviceId,fldName);
                }

            } else {
                // ??????
                if ("0".equals(status)) {
                    update = eventEoutletMapper.updateBydeviceIdAndFld(fld, PortSettingEnum.Enable.getValue(),deviceId);
//                    enableDevicePort(deviceId, EventTableEnum.OUTLET.getTableName());
                    enableOutletDeviceIds.add(deviceId);
                    //??????????????????
                    insertLog(flag,status,deviceId,fldName);
                } else {
                    update = eventEoutletMapper.updateBydeviceIdAndFld(fld, PortSettingEnum.Disable.getValue(),deviceId);
                    forbiddenDevicePort(deviceId, EventTableEnum.OUTLET.getTableName(), fld);
                    //??????????????????
                    insertLog(flag,status,deviceId,fldName);
                }
            }
            update ++ ;
        }

        if(enableSfpDeviceIds != null && enableSfpDeviceIds.size() > 0){
            enableDevicePort(enableSfpDeviceIds, EventTableEnum.SFP.getTableName());    //??????????????????
        }
        if(enableOutletDeviceIds != null && enableOutletDeviceIds.size() > 0){
            enableDevicePort(enableOutletDeviceIds, EventTableEnum.OUTLET.getTableName());  //??????????????????
        }
        return update;
    }

    /**
     * ??????????????????
     * @param flag ??????(1????????????,2????????????)
     * @param status ??????(0?????????,1?????????)
     * @param deviceId ??????ID
     * @param fldName ????????????
     */
    public void insertLog(String flag,String status,String deviceId,String fldName){
        // ??????????????????
        UserInfoDto userInfo = null;
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        if (info.getStatus() == 200){
            userInfo = info.getData();
        }
        DeviceOperationLog log = new DeviceOperationLog();
        log.setId(IdGen.snowflakeId());
        log.setDeviceId(deviceId);
        log.setOperationCommand("0".equals(status) ? "??????":"??????");
        log.setOperationContent("1".equals(flag) ? "?????????":"?????????");
        log.setFullContent(fldName);
        log.setUserId(userInfo.getId());
        log.setUserName(userInfo.getName());
        log.setCreateDate(new Date());
        log.setApplicationCode(SysUtil.getSysCode());
        log.setTenantCode(SysUtil.getTenantCode());
        deviceOperationLogMapper.insert(log);
    }

    public List<DeviceSettings> findDeviceSettingsByDeviceId(String deviceIds,String setType){
        return deviceSettingsMapper.findDeviceSettingsByDeviceId(deviceIds,setType);
    }

    /**
     * ??????????????????
     * @param deviceId  ??????ID
     * @param eventTabCode ????????????
     * @param fld   ???????????????
     */
    public void forbiddenDevicePort(String deviceId, String eventTabCode, String fld){
        //?????????????????????AlarmType
        List<AlarmType> alarmTypeList = eventUcMetadataMapper.findAlarmTypesByTabFld(eventTabCode, fld);

        //???????????????????????????DeviceEventAlarm
        DeviceEventAlarm deviceEventAlarm = deviceEventAlarmMapper.findByDeviceId(deviceId);
        if(deviceEventAlarm != null){
            //???????????????????????????????????????
            List<String> uEventIdsList = deviceEventAlarm.getUEventId() == null ? new ArrayList<>()
                    : new ArrayList<>(Arrays.asList(deviceEventAlarm.getUEventId().split(CommonConstant.EventAlarmSeparate.SEMICOLON)));
            List<String> alarmTypesList = deviceEventAlarm.getAlarmTypes() == null ? new ArrayList<>()
                    : new ArrayList<>(Arrays.asList(deviceEventAlarm.getAlarmTypes().split(CommonConstant.EventAlarmSeparate.COMMA)));
            List<String> alarmTypesDesList = deviceEventAlarm.getAlarmTypesDes() == null ? new ArrayList<>()
                    : new ArrayList<>(Arrays.asList(deviceEventAlarm.getAlarmTypesDes().split(CommonConstant.EventAlarmSeparate.SEMICOLON)));
            List<String> alarmLevelsList = deviceEventAlarm.getAlarmLevels() == null ? new ArrayList<>()
                    : new ArrayList<>(Arrays.asList(deviceEventAlarm.getAlarmLevels().split(CommonConstant.EventAlarmSeparate.SEMICOLON)));
            List<String> alarmDatesList = deviceEventAlarm.getAlarmDates() == null ? new ArrayList<>()
                    : new ArrayList<>(Arrays.asList(deviceEventAlarm.getAlarmDates().split(CommonConstant.EventAlarmSeparate.SEMICOLON)));
            List<String> promptTypesList = deviceEventAlarm.getPromptTypes() == null ? new ArrayList<>()
                    : new ArrayList<>(Arrays.asList(deviceEventAlarm.getPromptTypes().split(CommonConstant.EventAlarmSeparate.COMMA)));

            //?????????????????????
            for(AlarmType alarmType : alarmTypeList){
                if(AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())){  //?????????
                    for (int i = 0; i < alarmTypesList.size(); i++) {
                        String alarm = alarmTypesList.get(i);
                        if(alarm.equals(alarmType.getAlarmType())){ //???????????????????????????
                            alarmTypesList.remove(i);   //????????????????????????

                            if(uEventIdsList.size() >= i){
                                uEventIdsList.remove(i);
                            }
                            if(alarmTypesDesList.size() >= i){
                                alarmTypesDesList.remove(i);
                            }
                            if(alarmLevelsList.size() >= i){
                                alarmLevelsList.remove(i);
                            }
                            if(alarmDatesList.size() >= i){
                                alarmDatesList.remove(i);
                            }
                            break;
                        }
                    }
                }else{  //?????????
                    for (int i = 0; i < promptTypesList.size(); i++) {
                        String prompt = promptTypesList.get(i);
                        if(prompt.equals(alarmType.getAlarmType())){
                            promptTypesList.remove(i);
                            break;
                        }
                    }
                }
            }

            //??????????????????
            deviceEventAlarm.setUEventId(String.join(CommonConstant.EventAlarmSeparate.SEMICOLON, uEventIdsList));
            deviceEventAlarm.setAlarmTypes(String.join(CommonConstant.EventAlarmSeparate.COMMA, alarmTypesList));
            deviceEventAlarm.setAlarmTypesDes(String.join(CommonConstant.EventAlarmSeparate.SEMICOLON, alarmTypesDesList));
            deviceEventAlarm.setAlarmLevels(String.join(CommonConstant.EventAlarmSeparate.SEMICOLON, alarmLevelsList));
            deviceEventAlarm.setAlarmDates(String.join(CommonConstant.EventAlarmSeparate.SEMICOLON, alarmDatesList));
            deviceEventAlarm.setPromptTypes(String.join(CommonConstant.EventAlarmSeparate.COMMA, promptTypesList));

            //????????????
            Integer level = alarmTypeService.loadMinAlarmLevel(alarmTypesList);
            int alarmLevel = level == null ? AlarmLevelType.NORMAL.getType() : level;   //??????????????????
            deviceEventAlarm.setAlarmLevel(alarmLevel);
            deviceEventAlarm.setIsAlarm(StringUtils.isEmpty(deviceEventAlarm.getAlarmTypes()) ? 0 : 1);

            //????????????
            deviceEventAlarmMapper.update(deviceEventAlarm);
        }
    }

    /**
     * ??????????????????
     * @param deviceId
     * @param flag
     */
    public void enableDevicePort(List<String> deviceIds, String flag){
        if(deviceIds != null && deviceIds.size() > 0){
            new Thread(() -> {
                try {
                    Thread.sleep(300);  //???????????????????????????????????????????????????
                    for(String deviceId : deviceIds){
                        DeviceVo deviceVo = deviceMapper.findById(deviceId);
                        if(deviceVo != null){
                            //?????????????????????????????????
                            QueryDeviceDataDto queryDeviceDataDto = new QueryDeviceDataDto(deviceId, flag, deviceVo.getTenantCode(), deviceVo.getProjectCode());
                            mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.QUERY_DEVICE_DATA, JSON.toJSONString(queryDeviceDataDto), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    /**
     * ????????????
     * @param dto
     * @return
     */
    @Transactional
    public int updateBath(DeviceSettingsListDto dto){
      if (dto.getDeviceSettingsDtoList() == null || dto.getDeviceSettingsDtoList().size() == 0) {
            return 1;
        }

        // ??????????????????
        UserInfoDto userInfo = null;
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        if (info.getStatus() == 200){
            userInfo = info.getData();
        }
        DeviceSettingsDto deviceSettingsDto = dto.getDeviceSettingsDtoList().get(0);
        String type = dto.getType();
        String mode = deviceSettingsDto.getMode();
        String value = deviceSettingsDto.getValue();    //?????????????????????(?????????"??????1???????????????,??????1?????????;??????2???????????????,??????2?????????"????????????"50,0;-,-" ??? "50,0;60,0")
        if (DeviceSettingsType.DOOR_DEPLOY.getMode().equals(type)) {  //????????????
            deviceSettingsMapper.deleteBath(dto.getDeviceSettingsDtoList());
            // ????????????
            int result = this.insertBatch(dto.getDeviceSettingsDtoList());
            //??????MQ????????????
            Date date = new Date();
            for(DeviceSettingsDto deviceSettingsDto1 : dto.getDeviceSettingsDtoList()){
                DeviceVo deviceVo = deviceMapper.findById(deviceSettingsDto1.getDeviceId());
                if(deviceVo != null) {
                    com.aswl.iot.dto.Device deviceC = JSONObject.parseObject(JSON.toJSONString(deviceVo), com.aswl.iot.dto.Device.class);
                    DeviceCommand deviceCommand = new DeviceCommand();
                    deviceCommand.setDevice(deviceC);
                    deviceCommand.setOperationMoudle(OperationMoudle.DOOR_DEPLOY_SETTING);
                    if (DeviceSettingsType.DOOR_DEPLOY.getType().equals(deviceSettingsDto1.getMode())) {  //??????
                        deviceCommand.setOperationCodeA(DoorDeploySettingEnum.DOOR_DEPLOY_ENABLE.getSettingType());
                    } else if (DeviceSettingsType.DOOR_REVOKE.getType().equals(deviceSettingsDto1.getMode())) {    //??????
                        deviceCommand.setOperationCodeA(DoorDeploySettingEnum.DOOR_DEPLOY_DISABLE.getSettingType());
                    } else if (DeviceSettingsType.DOOR_TEMPORARY_REVOKE.getType().equals(deviceSettingsDto1.getMode())) {  //????????????
                        deviceCommand.setOperationCodeA(DoorDeploySettingEnum.DOOR_DEPLOY_TEMPORARY_DISABLE.getSettingType());
                        int minutes = (int) Math.ceil((Long.valueOf(deviceSettingsDto1.getValue()) - date.getTime()) / (1000 * 60.0d));
                        deviceCommand.setOperationCodeB(minutes);
                    }
                    mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.COMMAND_DEVICE_QUEUE, JSON.toJSONString(deviceCommand), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                }
            }
            return result;
        } else if (DeviceSettingsType.TEMPERATURE_AUTO.getMode().equals(type)) { //????????????
            List<DeviceSettings> list = deviceSettingsMapper.findDeviceSettingsByDeviceId(dto.getDeviceSettingsDtoList().stream().map(DeviceSettingsDto::getDeviceId).collect(Collectors.joining(",")), dto.getType());
            List<DeviceSettingsDto> settingsDtoList_insert = new ArrayList<>();
            List<String> values = Arrays.asList(value.split(";"));  //?????????????????????
            for (DeviceSettingsDto d : dto.getDeviceSettingsDtoList()) {
                List<DeviceSettings> streamDeviceSettingsList = list.stream().filter(s -> s.getDeviceId().equals(d.getDeviceId())).collect(Collectors.toList());
                DeviceSettings deviceSettings = streamDeviceSettingsList.size() > 0 ? streamDeviceSettingsList.get(0) : null;
                if (deviceSettings == null) {
                    settingsDtoList_insert.add(d);
                } else {
                    deviceSettings.setMode(mode);
                    List<String> values_db = new ArrayList<>(Arrays.asList(deviceSettings.getValue().split(";")));  //????????????????????????
                    for (int i = 0; i < values.size(); i++) {
                        String valueUnit = values.get(i);
                        if (!"-,-".equals(valueUnit)) {
                            if (values_db.size() >= (i + 1) && !"-,-".equals(values_db.get(i))) {
                                values_db.set(i, valueUnit);
                            }else if(values_db.size() < (i + 1)){
                                values_db.add(valueUnit);
                            }
                        }
                    }
                    deviceSettings.setValue(String.join(";", values_db));
                    deviceSettingsMapper.update(deviceSettings);
                    //??????????????????
                    DeviceOperationLog log = new DeviceOperationLog();
                    log.setId(IdGen.snowflakeId());
                    log.setDeviceId(d.getDeviceId());
                    log.setOperationCommand("??????");
                    //????????????
                    if (d.getMode().equals(DeviceSettingsType.TEMPERATURE_AUTO.getType())){
                        log.setOperationContent("??????????????????");
                        log.setFullContent(d.getFldName());
                      //????????????
                    }else{
                        log.setOperationContent("??????????????????");
                        log.setFullContent(d.getFldName());
                    }
                    log.setUserId(userInfo.getId());
                    log.setUserName(userInfo.getName());
                    log.setCreateDate(new Date());
                    log.setApplicationCode(SysUtil.getSysCode());
                    log.setTenantCode(SysUtil.getTenantCode());
                    deviceOperationLogMapper.insert(log);
                }
            }
            if(settingsDtoList_insert.size() > 0){
                this.insertBatch(settingsDtoList_insert);
            }
            return 1;
        }
        return 0; 
    }
    

    /**
     *  ????????????id?????????????????????
     * @return
     */
   public DeviceSettings findByDeviceIdAndMode(String deviceId,String mode){
        
        return deviceSettingsMapper.findByDeviceIdAndMode(deviceId,mode);
    }
}