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
     * 新增设备设置
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
     * 删除设备设置
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
     * 批量新增设备设置
     *
     * @param list 集合
     * @return int 数量
     */
    @Transactional
    public int insertBatch(List<DeviceSettingsDto> list) {
        // 获取用户信息
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
                //记录操作日志
                DeviceOperationLog log = new DeviceOperationLog();
                log.setId(IdGen.snowflakeId());
                log.setDeviceId(dto.getDeviceId());
                log.setOperationCommand("设置");
                // 设置机箱
                if (dto.getSetType().equals(DeviceSettingsType.DOOR_DEPLOY.getMode())){
                    if (dto.getMode().equals(DeviceSettingsType.DOOR_DEPLOY.getType())){
                        log.setOperationContent("设置前端机箱布防");
                        log.setFullContent("设置前端机箱状态为" + DeviceSettingsType.DOOR_DEPLOY.getDescription());
                    }else{
                        log.setOperationContent("设置前端机箱布防");
                        log.setFullContent("设置前端机箱状态为" + DeviceSettingsType.DOOR_REVOKE.getDescription());
                    }
                 //设置温度
                }else{
                    log.setOperationContent("设置温度");
                    //自动控制
                    if (dto.getMode().equals(DeviceSettingsType.TEMPERATURE_AUTO.getType())){
                        log.setOperationContent(dto.getFldName());
                        //手动控制
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
     *  根据设置类型删除数据
     * @param setType
     * @return int
     */
   public  int deleteByMode(String setType){
        return deviceSettingsMapper.deleteByMode(setType);
    }


    /**
     *  根据设备ID删除数据
     * @param list
     * @return int
     */
    public  int deleteBath(List list){
        return deviceSettingsMapper.deleteBath(list);
    }

    /**
     *  根据设备id和设置类型查询
     * @return
     */
    public DeviceSettings findDeviceSettings(String deviceId,String setType){

        return deviceSettingsMapper.findDeviceSettings(deviceId,setType);
    }

    /**
     * 设备网络口、光纤口端口设置
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
            //光纤口
            if ("1".equals(flag)) {
                // 启用
                if ("0".equals(status)) {
                    eventSfpMapper.updateBydeviceIdAndFld(fld, PortSettingEnum.Enable.getValue(),deviceId);
//                    enableDevicePort(deviceId, EventTableEnum.SFP.getTableName());
                    enableSfpDeviceIds.add(deviceId);
                    //记录操作日志
                    insertLog(flag,status,deviceId,fldName);
                } else {
                     eventSfpMapper.updateBydeviceIdAndFld(fld, PortSettingEnum.Disable.getValue(),deviceId);
                    forbiddenDevicePort(deviceId, EventTableEnum.SFP.getTableName(), fld);
                    //记录操作日志
                    insertLog(flag,status,deviceId,fldName);
                }

            } else {
                // 启用
                if ("0".equals(status)) {
                    update = eventEoutletMapper.updateBydeviceIdAndFld(fld, PortSettingEnum.Enable.getValue(),deviceId);
//                    enableDevicePort(deviceId, EventTableEnum.OUTLET.getTableName());
                    enableOutletDeviceIds.add(deviceId);
                    //记录操作日志
                    insertLog(flag,status,deviceId,fldName);
                } else {
                    update = eventEoutletMapper.updateBydeviceIdAndFld(fld, PortSettingEnum.Disable.getValue(),deviceId);
                    forbiddenDevicePort(deviceId, EventTableEnum.OUTLET.getTableName(), fld);
                    //记录操作日志
                    insertLog(flag,status,deviceId,fldName);
                }
            }
            update ++ ;
        }

        if(enableSfpDeviceIds != null && enableSfpDeviceIds.size() > 0){
            enableDevicePort(enableSfpDeviceIds, EventTableEnum.SFP.getTableName());    //启用设备光口
        }
        if(enableOutletDeviceIds != null && enableOutletDeviceIds.size() > 0){
            enableDevicePort(enableOutletDeviceIds, EventTableEnum.OUTLET.getTableName());  //启用设备电口
        }
        return update;
    }

    /**
     * 增加操作日志
     * @param flag 标记(1、光纤口,2、网络口)
     * @param status 状态(0、启用,1、禁用)
     * @param deviceId 设备ID
     * @param fldName 操作内容
     */
    public void insertLog(String flag,String status,String deviceId,String fldName){
        // 获取用户信息
        UserInfoDto userInfo = null;
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        if (info.getStatus() == 200){
            userInfo = info.getData();
        }
        DeviceOperationLog log = new DeviceOperationLog();
        log.setId(IdGen.snowflakeId());
        log.setDeviceId(deviceId);
        log.setOperationCommand("0".equals(status) ? "启用":"禁用");
        log.setOperationContent("1".equals(flag) ? "光纤口":"网络口");
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
     * 禁用设备端口
     * @param deviceId  设备ID
     * @param eventTabCode 事件表名
     * @param fld   事件表字段
     */
    public void forbiddenDevicePort(String deviceId, String eventTabCode, String fld){
        //获取端口相应的AlarmType
        List<AlarmType> alarmTypeList = eventUcMetadataMapper.findAlarmTypesByTabFld(eventTabCode, fld);

        //更新当前状态汇总表DeviceEventAlarm
        DeviceEventAlarm deviceEventAlarm = deviceEventAlarmMapper.findByDeviceId(deviceId);
        if(deviceEventAlarm != null){
            //当前告警集合与当前正常集合
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

            //移除禁用的告警
            for(AlarmType alarmType : alarmTypeList){
                if(AlarmTypeKindEnum.ALARM.getKind().equals(alarmType.getKind())){  //告警类
                    for (int i = 0; i < alarmTypesList.size(); i++) {
                        String alarm = alarmTypesList.get(i);
                        if(alarm.equals(alarmType.getAlarmType())){ //若与禁用的告警一致
                            alarmTypesList.remove(i);   //删除已禁用的告警

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
                }else{  //正常类
                    for (int i = 0; i < promptTypesList.size(); i++) {
                        String prompt = promptTypesList.get(i);
                        if(prompt.equals(alarmType.getAlarmType())){
                            promptTypesList.remove(i);
                            break;
                        }
                    }
                }
            }

            //更新告警信息
            deviceEventAlarm.setUEventId(String.join(CommonConstant.EventAlarmSeparate.SEMICOLON, uEventIdsList));
            deviceEventAlarm.setAlarmTypes(String.join(CommonConstant.EventAlarmSeparate.COMMA, alarmTypesList));
            deviceEventAlarm.setAlarmTypesDes(String.join(CommonConstant.EventAlarmSeparate.SEMICOLON, alarmTypesDesList));
            deviceEventAlarm.setAlarmLevels(String.join(CommonConstant.EventAlarmSeparate.SEMICOLON, alarmLevelsList));
            deviceEventAlarm.setAlarmDates(String.join(CommonConstant.EventAlarmSeparate.SEMICOLON, alarmDatesList));
            deviceEventAlarm.setPromptTypes(String.join(CommonConstant.EventAlarmSeparate.COMMA, promptTypesList));

            //更新级别
            Integer level = alarmTypeService.loadMinAlarmLevel(alarmTypesList);
            int alarmLevel = level == null ? AlarmLevelType.NORMAL.getType() : level;   //更新后的级别
            deviceEventAlarm.setAlarmLevel(alarmLevel);
            deviceEventAlarm.setIsAlarm(StringUtils.isEmpty(deviceEventAlarm.getAlarmTypes()) ? 0 : 1);

            //保存更新
            deviceEventAlarmMapper.update(deviceEventAlarm);
        }
    }

    /**
     * 启用设备端口
     * @param deviceId
     * @param flag
     */
    public void enableDevicePort(List<String> deviceIds, String flag){
        if(deviceIds != null && deviceIds.size() > 0){
            new Thread(() -> {
                try {
                    Thread.sleep(300);  //延时发送指令，避免在事务完成前处理
                    for(String deviceId : deviceIds){
                        DeviceVo deviceVo = deviceMapper.findById(deviceId);
                        if(deviceVo != null){
                            //发送查询状态的队列消息
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
     * 设置信息
     * @param dto
     * @return
     */
    @Transactional
    public int updateBath(DeviceSettingsListDto dto){
      if (dto.getDeviceSettingsDtoList() == null || dto.getDeviceSettingsDtoList().size() == 0) {
            return 1;
        }

        // 获取用户信息
        UserInfoDto userInfo = null;
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        if (info.getStatus() == 200){
            userInfo = info.getData();
        }
        DeviceSettingsDto deviceSettingsDto = dto.getDeviceSettingsDtoList().get(0);
        String type = dto.getType();
        String mode = deviceSettingsDto.getMode();
        String value = deviceSettingsDto.getValue();    //新设置的温度值(格式："风扇1自动温度值,风扇1手动值;风扇2自动温度值,风扇2手动值"；例如："50,0;-,-" 或 "50,0;60,0")
        if (DeviceSettingsType.DOOR_DEPLOY.getMode().equals(type)) {  //箱门设置
            deviceSettingsMapper.deleteBath(dto.getDeviceSettingsDtoList());
            // 新增数据
            int result = this.insertBatch(dto.getDeviceSettingsDtoList());
            //发送MQ设置设备
            Date date = new Date();
            for(DeviceSettingsDto deviceSettingsDto1 : dto.getDeviceSettingsDtoList()){
                DeviceVo deviceVo = deviceMapper.findById(deviceSettingsDto1.getDeviceId());
                if(deviceVo != null) {
                    com.aswl.iot.dto.Device deviceC = JSONObject.parseObject(JSON.toJSONString(deviceVo), com.aswl.iot.dto.Device.class);
                    DeviceCommand deviceCommand = new DeviceCommand();
                    deviceCommand.setDevice(deviceC);
                    deviceCommand.setOperationMoudle(OperationMoudle.DOOR_DEPLOY_SETTING);
                    if (DeviceSettingsType.DOOR_DEPLOY.getType().equals(deviceSettingsDto1.getMode())) {  //布防
                        deviceCommand.setOperationCodeA(DoorDeploySettingEnum.DOOR_DEPLOY_ENABLE.getSettingType());
                    } else if (DeviceSettingsType.DOOR_REVOKE.getType().equals(deviceSettingsDto1.getMode())) {    //撤防
                        deviceCommand.setOperationCodeA(DoorDeploySettingEnum.DOOR_DEPLOY_DISABLE.getSettingType());
                    } else if (DeviceSettingsType.DOOR_TEMPORARY_REVOKE.getType().equals(deviceSettingsDto1.getMode())) {  //临时撤防
                        deviceCommand.setOperationCodeA(DoorDeploySettingEnum.DOOR_DEPLOY_TEMPORARY_DISABLE.getSettingType());
                        int minutes = (int) Math.ceil((Long.valueOf(deviceSettingsDto1.getValue()) - date.getTime()) / (1000 * 60.0d));
                        deviceCommand.setOperationCodeB(minutes);
                    }
                    mqSender.send(MQExchange.DEVICE_CONTROL.getMqFanoutExchange(), MQConstants.COMMAND_DEVICE_QUEUE, JSON.toJSONString(deviceCommand), MessageProperties.CONTENT_TYPE_TEXT_PLAIN);
                }
            }
            return result;
        } else if (DeviceSettingsType.TEMPERATURE_AUTO.getMode().equals(type)) { //温度设置
            List<DeviceSettings> list = deviceSettingsMapper.findDeviceSettingsByDeviceId(dto.getDeviceSettingsDtoList().stream().map(DeviceSettingsDto::getDeviceId).collect(Collectors.joining(",")), dto.getType());
            List<DeviceSettingsDto> settingsDtoList_insert = new ArrayList<>();
            List<String> values = Arrays.asList(value.split(";"));  //新设置的温度值
            for (DeviceSettingsDto d : dto.getDeviceSettingsDtoList()) {
                List<DeviceSettings> streamDeviceSettingsList = list.stream().filter(s -> s.getDeviceId().equals(d.getDeviceId())).collect(Collectors.toList());
                DeviceSettings deviceSettings = streamDeviceSettingsList.size() > 0 ? streamDeviceSettingsList.get(0) : null;
                if (deviceSettings == null) {
                    settingsDtoList_insert.add(d);
                } else {
                    deviceSettings.setMode(mode);
                    List<String> values_db = new ArrayList<>(Arrays.asList(deviceSettings.getValue().split(";")));  //原来设置的温度值
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
                    //记录操作日志
                    DeviceOperationLog log = new DeviceOperationLog();
                    log.setId(IdGen.snowflakeId());
                    log.setDeviceId(d.getDeviceId());
                    log.setOperationCommand("设置");
                    //自动控制
                    if (d.getMode().equals(DeviceSettingsType.TEMPERATURE_AUTO.getType())){
                        log.setOperationContent("设置风扇温度");
                        log.setFullContent(d.getFldName());
                      //手动控制
                    }else{
                        log.setOperationContent("设置风扇开关");
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
     *  根据设备id和设置模式查询
     * @return
     */
   public DeviceSettings findByDeviceIdAndMode(String deviceId,String mode){
        
        return deviceSettingsMapper.findByDeviceIdAndMode(deviceId,mode);
    }
}