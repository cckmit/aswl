package com.aswl.as.ibrs.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.DeviceFaultDto;
import com.aswl.as.ibrs.api.module.*;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.enums.AlarmTypePrefix;
import com.aswl.as.ibrs.mapper.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jdk.nashorn.internal.runtime.GlobalConstants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceDetailsService extends CrudService<DeviceDetailsMapper, Device> {
    private final DeviceDetailsMapper deviceDetailsMapper;
    private  final AlarmTypeMapper alarmTypeMapper;
    private  final EventUcMetadataModelMapper ucMetadataModelMapper;
    private final EventUcMetadataMapper ucMetadataMapper;
    private final DeviceModelAlarmThresholdMapper thresholdMapper;
    private final EventUcStatusGroupMapper eventUcStatusGroupMapper;
    private final DeviceEventAlarmMapper eventAlarmMapper;
    private final  FlowRunMapper runMapper;
    private final DeviceService deviceService;
    private final AsEventHisAlarmMapper asEventHisAlarmMapper;
    private final FlowRunMapper flowRunMapper;
    private final EventSecCtlPowerMapper eventSecCtlPowerMapper;
    private EventSecCtlPowerOutputMapper eventSecCtlPowerOutputMapper;
    private  EventSecCtlPowerSetMapper eventSecCtlPowerSetMapper;
    private EventSfpInfoMapper eventSfpInfoMapper;
    private final EventDataExtService eventDataExtService;
    private final EventPostureMapper eventPostureMapper;
    private final EventInputMapper eventInputMapper;

    /**
     * 根据id查询设备
     *
     * @param id
     * @return DeviceVo
     */
    public DeviceVo findById(String id) {

        DeviceVo vo = deviceDetailsMapper.findById(id);
        //查询设备是否有视频
        int videoValue = deviceService.getIsVideoValue(vo.getId(), vo.getType(), vo.getNetworkState());
        vo.setIsVideo(videoValue);
        return vo;
    }

    /**
     * 查询上级设备
     *
     * @param parentId 父级ID
     * @return DeviceVo
     */
    public DeviceVo getSuperiorDevice(String parentId) {

        return deviceDetailsMapper.getSuperiorDevice(parentId);
    }

    /**
     * 查询下级设备
     *
     * @param id 设备id
     * @return DeviceVo
     */
    public List<DeviceVo> getSubordinateDevice(String id) {

        return deviceDetailsMapper.getSubordinateDevice(id);
    }

    /**
     * 设备监控------根据设备ID获取设备信息
     * @param id
     * @return DeviceDetailsVo
     */
    public DeviceDetailsVo getDeviceDetail(String id){
         DeviceDetailsVo deviceDetailsVo=new DeviceDetailsVo();
         DeviceVo prentDevice=null;
         List<DeviceVo> childDevice=new ArrayList<>();
        // 查询设备基础信息
        DeviceVo device = deviceDetailsMapper.findById(id);
        //查询设备是否有视频
        int videoValue = deviceService.getIsVideoValue(device.getId(), device.getType(), device.getNetworkState());
        device.setIsVideo(videoValue);
        if (device.getParentDeviceId()!=null){
            // 获取上级设备信息
            prentDevice=deviceDetailsMapper.getSuperiorDevice(device.getParentDeviceId());
        }
        // 下级设备信息
        childDevice=deviceDetailsMapper.getSubordinateDevice(device.getId());
        deviceDetailsVo.setDevice(device);
        deviceDetailsVo.setParentDevice(prentDevice);
        deviceDetailsVo.setChildrenDeviceList(childDevice);

        //TODO 获取图片 TODO

        return deviceDetailsVo;
    }
    /**
     * 设备状态
     * @param id 设备ID
     * @return DeviceStautsOverallVo
     */
    public DeviceStautsOverallVo getDeviceStatusData(String id){
        DeviceStautsOverallVo resultGroupVo=new DeviceStautsOverallVo();
        List<DeviceStatusGroupVo> statusGroupVoList = new ArrayList<>();
        List<DeviceStatusVo> noGroupsStatus=new ArrayList<>();
        List<String> eventMetadataIds=new ArrayList<>();
        List<StatusOperationVo> statusOperationList=null;
        DeviceStatusGroupVo statusGroupVo;
        AlarmStatusVo statusVo=null;
        Map<String, DeviceStatusGroupVo> deviceStatusGroupVoMap = new HashMap();
        //根据ID获取设备分控板-电源信息
        EventSecCtlPower eventSecCtlPower = eventSecCtlPowerMapper.findByDeviceId(id);
        //根据ID获取设备分控板-电源输出
        EventSecCtlPowerOutput eventSecCtlPowerOutput =eventSecCtlPowerOutputMapper.findByDeviceId(id);
        //根据ID获取设备分控板-电源设置
        EventSecCtlPowerSet eventSecCtlPowerSet = eventSecCtlPowerSetMapper.findByDeviceId(id);
        //根据ID获取SFP信息
        EventSfpInfo eventSfpInfo =eventSfpInfoMapper.findByDeviceId(id);
        // 根据ID获取设备事件-输入信息
        EventInput eventInput = eventInputMapper.findByDeviceId(id);
        //根据ID获取设备姿态信息
        EventPosture eventPosture = eventPostureMapper.findByDeviceId(id);
        
        // 根据设备ID获取设备信息
        DeviceVo deviceVo = deviceDetailsMapper.findById(id);
        if (null!=deviceVo){
            // 根据设备型号ID获取事件元数据-设备型号集合
            List<EventUcMetadataModel> eventUcMetadataModels = ucMetadataModelMapper.findByUcMetadataByModelId(deviceVo.getDeviceModelId());
            if ( null !=eventUcMetadataModels && eventUcMetadataModels.size()>0) {
                // 获取所有组名称
                EventUcStatusGroup eventUcStatusGroup=new EventUcStatusGroup();
                List<EventUcStatusGroup> groups = eventUcStatusGroupMapper.findList(eventUcStatusGroup);
                for (EventUcMetadataModel model : eventUcMetadataModels) {
                    eventMetadataIds.add(model.getEventMetadataId());
                }
                // 根据事件元数据设备型号ID集合获取事件元数据数据(组内数据)
                List<EventUcMetadata> eventUcMetadata = ucMetadataMapper.findEventUcMetadataByIds(eventMetadataIds);
                // 组集合
                List<EventUcMetadata> statusGroupData = eventUcMetadata.stream().filter(ucdata -> ucdata.getIsStatusGroup() == 1).collect(Collectors.toList());
                //加载事件扩展数据
                EventDataExt eventDataExt = eventDataExtService.findByDeviceId(deviceVo.getId());
                JSONObject eventDataExtJson = new JSONObject();
                if(eventDataExt != null && eventDataExt.getData() != null) {
                    eventDataExtJson = JSON.parseObject(eventDataExt.getData());
                }
                for (EventUcMetadata metadata: statusGroupData) {
                    // 根据动态表获取fldcode对应值
                    Map<String, Object> fldMap = deviceDetailsMapper.getDynamicTable(metadata.getFldCode(), metadata.getTabCode(), deviceVo.getId());
                    Map<String, Object> mapData = deviceDetailsMapper.getData(metadata.getFldCode(), metadata.getTabCode());
                    EventUcMetadataModel ucMetadataModel = ucMetadataModelMapper.findEventUcMetadataModel(deviceVo.getDeviceModelId(), metadata.getId());

                    DeviceStatusVo deviceStatusVo = new DeviceStatusVo();
                    deviceStatusVo.setFldCode(metadata.getFldCode());
                    deviceStatusVo.setFldName(metadata.getFldName());
                    deviceStatusVo.setFldNameEn(metadata.getFldNameEn());
                    if(metadata.getStatusName().startsWith(AlarmTypePrefix.DCPOWER_EXT.getPrefix())){
                        Integer port = Integer.parseInt(metadata.getStatusName().substring(10));
                        //根据设备Id和电源口槽位查询子设备
                        Device device = deviceService.findSubsetDevice(id,"parent_dcpower_no",port);
                        if(device != null){
                            deviceStatusVo.setSubsetDevice(device.getDeviceName());
                            deviceStatusVo.setSubsetIp(device.getIp());
                        }
                    }
                    if(metadata.getStatusName().startsWith(AlarmTypePrefix.RJ45_EXT.getPrefix())){
                        Integer port = Integer.parseInt(metadata.getStatusName().substring(7));
                        //根据设备Id和网络口槽位查询子设备
                        Device device = deviceService.findSubsetDevice(id,"parent_rj45_no",port);
                        if(device != null){
                            deviceStatusVo.setSubsetDevice(device.getDeviceName());
                            deviceStatusVo.setSubsetIp(device.getIp());
                        }
                    }
                    if(metadata.getStatusName().startsWith(AlarmTypePrefix.FIBRE_OPTICAL_EXT.getPrefix())){
                        Integer port = Integer.parseInt(metadata.getStatusName().substring(15));
                        //根据设备Id和光纤口槽位查询子设备
                        Device device = deviceService.findSubsetDevice(id,"parent_fibre_optical_no",port);
                        if(device != null){
                            deviceStatusVo.setSubsetDevice(device.getDeviceName());
                            deviceStatusVo.setSubsetIp(device.getIp());
                        }
                    }
                    deviceStatusVo.setFldUnit(metadata.getFldUnit());
                    deviceStatusVo.setIconPath(metadata.getIconPath());
                    statusVo = new AlarmStatusVo();
                    if (fldMap != null) {
                        statusVo.setFldValue(fldMap.get("fldValue").toString());
                        statusVo.setVectorType(Integer.parseInt(mapData.get("vectorType").toString()));
                        if (mapData.get("groupId").toString() != null) {
                            statusVo.setGroupId(mapData.get("groupId").toString());
                            // 获取组名称
                            for (EventUcStatusGroup group : groups) {
                                if (group.getId().equals(statusVo.getGroupId())) {
                                    if (!deviceStatusGroupVoMap.containsKey(group.getId())) {
                                        statusGroupVo = new DeviceStatusGroupVo();
                                        statusGroupVo.setName(group.getName());
                                        statusGroupVo.setNameEn(group.getNameEn());
                                        statusGroupVo.setStatusGroupName(group.getStatusGroupName());
                                        deviceStatusGroupVoMap.put(group.getId(), statusGroupVo);
                                    }
                                    break;
                                }
                            }
                        }

                        if (metadata.getIsAlarm() == 1) {   //告警的状态
                            if (statusVo.getVectorType() == 1) {    //开关量（比如箱门打开）
                                deviceStatusVo.setVectorType(1);
                                if (statusVo.getFldValue().equals("-1")) {
                                    deviceStatusVo.setValue(statusVo.getFldValue());
                                    deviceStatusVo.setAlarmType(metadata.getStatusName());  //临时标记用，不是AlarmType报警类型
                                    deviceStatusVo.setCode("enable");
                                    deviceStatusVo.setCodeCN("启用");
                                }else if(statusVo.getFldValue().equals("-9")){
                                    deviceStatusVo.setValue(statusVo.getFldValue());
                                    deviceStatusVo.setAlarmType(metadata.getStatusName());  //临时标记用，不是AlarmType报警类型
                                    deviceStatusVo.setCode("disable");
                                    deviceStatusVo.setCodeCN("禁用");
                                }
                                // 通过元数据ID和字段值获取报警类型
                                List<StatusGroupAlarmTypeVo> alarmTypes = alarmTypeMapper.findAlarmByMetadataId(Arrays.asList(metadata.getId()), statusVo.getFldValue());
                                StatusGroupAlarmTypeVo alarmTypeVo = (alarmTypes != null && alarmTypes.size() > 0) ? alarmTypes.get(0) : null;
                                if (alarmTypeVo != null) {
                                    deviceStatusVo.setAlarmTypeName(alarmTypeVo.getAlarmTypeName());
                                    deviceStatusVo.setAlarmType(alarmTypeVo.getAlarmType());
                                    deviceStatusVo.setCode(alarmTypeVo.getCode());
                                    deviceStatusVo.setCodeCN(alarmTypeVo.getCodeCN());
                                    deviceStatusVo.setIconPath(alarmTypeVo.getIconPath());
                                    deviceStatusVo.setAlarmLevel(alarmTypeVo.getAlarmLevel());
                                }

                                //加载事件扩展数据
                                if(eventDataExtJson.containsKey(metadata.getStatusName())){
                                    deviceStatusVo.setValue(eventDataExtJson.get(metadata.getStatusName()).toString());
                                }

                                // 通过设备型号ID获取设备可操作列表
                                List<EventUcStatusOperationVo> statusOperations = deviceDetailsMapper.getEventUcStatusOperation(deviceVo.getDeviceModelId());
                                statusOperationList = new ArrayList<>();
                                if (statusOperations != null && statusOperations.size() > 0) {
                                    for (EventUcStatusOperationVo vo : statusOperations) {
                                        if (vo.getMetadataId().equals(metadata.getId())) {
                                            StatusOperationVo entity = new StatusOperationVo();
                                            entity.setId(vo.getId());
                                            entity.setTitle(vo.getTitle());
                                            entity.setOperCode(vo.getOperCode());
                                            entity.setOperName(vo.getOperName());
                                            entity.setOperSort(vo.getOperSort());
                                            entity.setPortSerial(vo.getPortSerial());
                                            statusOperationList.add(entity);
                                        }
                                    }
                                    deviceStatusVo.setOperationList(statusOperationList);
                                }
                            } else {
                                {
                                    deviceStatusVo.setVectorType(0);
                                    // （比如温度、湿度）
                                    List<DeviceModelAlarmThreshold> alarmThresholds = thresholdMapper.findByMetadataModelId(Arrays.asList(ucMetadataModel.getId()), statusVo.getFldValue());
                                    DeviceModelAlarmThreshold alarmThreshold = (alarmThresholds != null && alarmThresholds.size() > 0) ? alarmThresholds.get(0) : null;
                                    EventUcMetadataModel model = new EventUcMetadataModel();
                                    if (alarmThreshold != null) {
                                        List<StatusGroupAlarmTypeVo> groupAlarmTypeVos = alarmTypeMapper.findAlarmByMetadataId(Arrays.asList(metadata.getId()), alarmThreshold.getStatusValue().toString());
                                        StatusGroupAlarmTypeVo alarmTypeVo = (groupAlarmTypeVos != null && groupAlarmTypeVos.size() > 0) ? groupAlarmTypeVos.get(0) : null;
                                        if (alarmTypeVo != null) {
                                            deviceStatusVo.setValue(statusVo.getFldValue());
                                            deviceStatusVo.setAlarmTypeName(alarmTypeVo.getAlarmTypeName());
                                            deviceStatusVo.setAlarmType(alarmTypeVo.getAlarmType());
                                            deviceStatusVo.setCode(alarmTypeVo.getCode());
                                            deviceStatusVo.setCodeCN(alarmTypeVo.getCodeCN());
                                            deviceStatusVo.setIconPath(alarmTypeVo.getIconPath());
                                            deviceStatusVo.setAlarmLevel(alarmTypeVo.getAlarmLevel());
                                        }
                                    }
                                }
                            }

                        } else {
                            if (statusVo.getVectorType() == 1) {
                                deviceStatusVo.setVectorType(1);
                                if (statusVo.getFldValue().equals("-1")) {
                                    deviceStatusVo.setValue(statusVo.getFldValue());
                                    deviceStatusVo.setAlarmType(metadata.getStatusName());  //临时标记用，不是AlarmType报警类型
                                    deviceStatusVo.setCode("enable");
                                    deviceStatusVo.setCodeCN("启用");
                                }else if(statusVo.getFldValue().equals("-9")){
                                    deviceStatusVo.setValue(statusVo.getFldValue());
                                    deviceStatusVo.setAlarmType(metadata.getStatusName());  //临时标记用，不是AlarmType报警类型
                                    deviceStatusVo.setCode("disable");
                                    deviceStatusVo.setCodeCN("禁用");
                                }
                            } else {
                                deviceStatusVo.setValue(statusVo.getFldValue());
                                deviceStatusVo.setVectorType(0);
                            }
                        }
                    }
                    statusGroupVo = deviceStatusGroupVoMap.get(statusVo.getGroupId());
                    if (statusGroupVo != null) {
                        statusGroupVo.getDeviceStatusVoList().add(deviceStatusVo);
                    }
                }
                //非组集合
                List<EventUcMetadata> statusNoGroupData = eventUcMetadata.stream().filter(ucdata -> ucdata.getIsStatusGroup() == 0).collect(Collectors.toList());
                for (EventUcMetadata metadata :statusNoGroupData) {
                    Map<String, Object> fldMap = deviceDetailsMapper.getDynamicTable(metadata.getFldCode(), metadata.getTabCode(), deviceVo.getId());
                    Map<String, Object> mapData = deviceDetailsMapper.getData(metadata.getFldCode(), metadata.getTabCode());
                    EventUcMetadataModel ucMetadataModel=ucMetadataModelMapper.findEventUcMetadataModel(deviceVo.getDeviceModelId(),metadata.getId());
                    if (fldMap == null) {
                        DeviceStatusVo deviceStatusVo = new DeviceStatusVo();
                        deviceStatusVo.setFldCode(metadata.getFldCode());
                        deviceStatusVo.setFldName(metadata.getFldName());
                        deviceStatusVo.setFldUnit(metadata.getFldUnit());
                    } else {
                    	statusVo = new AlarmStatusVo();
                        statusVo.setFldValue(fldMap.get("fldValue").toString());
                        statusVo.setVectorType(Integer.parseInt(mapData.get("vectorType").toString()));
                        List<EventUcStatusOperationVo> statusOperations = deviceDetailsMapper.getEventUcStatusOperation(deviceVo.getDeviceModelId());
                        List<StatusOperationVo> statusOperationVoList = new ArrayList<>();
                        for (EventUcStatusOperationVo vo:statusOperations) {
                            if (vo.getMetadataId().equals(metadata.getId())){
                                StatusOperationVo entity = new StatusOperationVo();
                                entity.setId(vo.getId());
                                entity.setTitle(vo.getTitle());
                                entity.setOperCode(vo.getOperCode());
                                entity.setOperName(vo.getOperName());
                                entity.setOperSort(vo.getOperSort());
                                entity.setPortSerial(vo.getPortSerial());
                                statusOperationVoList.add(entity);
                            }
                        }
                    }
                    if (metadata.getIsAlarm()==1) {
                        if( statusVo.getVectorType() == 1) {
                            List<StatusGroupAlarmTypeVo> alarmTypes = alarmTypeMapper.findAlarmByMetadataId(Arrays.asList(metadata.getId()), statusVo.getFldValue());
                            for (StatusGroupAlarmTypeVo alarmType : alarmTypes) {
                                DeviceStatusVo deviceStatusVo = new DeviceStatusVo();
                                deviceStatusVo.setAlarmTypeName(alarmType.getAlarmTypeName());
                                deviceStatusVo.setAlarmType(alarmType.getAlarmType());
                                deviceStatusVo.setCode(alarmType.getCode());
                                deviceStatusVo.setVectorType(1);
                                deviceStatusVo.setFldCode(alarmType.getFldCode());
                                deviceStatusVo.setFldName(alarmType.getFldName());
                                deviceStatusVo.setFldNameEn(metadata.getFldNameEn());
                                deviceStatusVo.setFldUnit(alarmType.getFldUnit());
                                deviceStatusVo.setCodeCN(alarmType.getCodeCN());
                                deviceStatusVo.setIconPath(alarmType.getIconPath());
                                deviceStatusVo.setOperationList(statusOperationList);
                                deviceStatusVo.setAlarmLevel(alarmType.getAlarmLevel());
                                //加载事件扩展数据
                                if(eventDataExtJson.containsKey(metadata.getStatusName())){
                                    deviceStatusVo.setValue(eventDataExtJson.get(metadata.getStatusName()).toString());
                                }
                                noGroupsStatus.add(deviceStatusVo);
                            }
                        }else{
                            List<DeviceModelAlarmThreshold> alarmThresholds = thresholdMapper.findByMetadataModelId(Arrays.asList(ucMetadataModel.getId()), statusVo.getFldValue());
                            EventUcMetadataModel model = null;
                            if (alarmThresholds != null && alarmThresholds.size() > 0) {
                                for (DeviceModelAlarmThreshold threshold : alarmThresholds) {
                                    model = new EventUcMetadataModel();
                                    model.setId(threshold.getEventMetadataModelId());
                                    EventUcMetadataModel eventUcMetadataModel = ucMetadataModelMapper.get(model);
                                    List<StatusGroupAlarmTypeVo> alarmTypes = alarmTypeMapper.findAlarmByMetadataId(Arrays.asList(eventUcMetadataModel.getEventMetadataId()), threshold.getStatusValue().toString());
                                    for (StatusGroupAlarmTypeVo alarmType : alarmTypes) {
                                        DeviceStatusVo deviceStatusVo = new DeviceStatusVo();
                                        deviceStatusVo.setAlarmTypeName(alarmType.getAlarmTypeName());
                                        deviceStatusVo.setValue(statusVo.getFldValue());
                                        deviceStatusVo.setAlarmType(alarmType.getAlarmType());
                                        deviceStatusVo.setCode(alarmType.getCode());
                                        deviceStatusVo.setVectorType(0);
                                        deviceStatusVo.setFldCode(alarmType.getFldCode());
                                        deviceStatusVo.setFldName(alarmType.getFldName());
                                        deviceStatusVo.setFldNameEn(metadata.getFldNameEn());
                                        deviceStatusVo.setFldUnit(alarmType.getFldUnit());
                                        deviceStatusVo.setCodeCN(alarmType.getCodeCN());
                                        deviceStatusVo.setIconPath(alarmType.getIconPath());
                                        deviceStatusVo.setOperationList(statusOperationList);
                                        deviceStatusVo.setAlarmLevel(alarmType.getAlarmLevel());
                                        noGroupsStatus.add(deviceStatusVo);
                                    }
                                }
                            }
                        }
                    }else {
                        if(statusVo.getVectorType()==1){
                            DeviceStatusVo deviceStatusVo = new DeviceStatusVo();
                            deviceStatusVo.setFldCode(metadata.getFldCode());
                            deviceStatusVo.setFldName(metadata.getFldName());
                            deviceStatusVo.setFldNameEn(metadata.getFldNameEn());
                            deviceStatusVo.setFldUnit(metadata.getFldUnit());
                            deviceStatusVo.setIconPath(metadata.getIconPath());
                            deviceStatusVo.setVectorType(1);
                            noGroupsStatus.add(deviceStatusVo);
                        }else {
                            DeviceStatusVo deviceStatusVo = new DeviceStatusVo();
                            deviceStatusVo.setFldCode(metadata.getFldCode());
                            deviceStatusVo.setFldName(metadata.getFldName());
                            deviceStatusVo.setFldNameEn(metadata.getFldNameEn());
                            deviceStatusVo.setFldUnit(metadata.getFldUnit());
                            deviceStatusVo.setValue(statusVo.getFldValue());
                            deviceStatusVo.setIconPath(metadata.getIconPath());
                            deviceStatusVo.setVectorType(0);
                            noGroupsStatus.add(deviceStatusVo);
                        }
                    }
                }
            }
            statusGroupVoList = new ArrayList<>(deviceStatusGroupVoMap.values());
            resultGroupVo.setDeviceStatusGroup(statusGroupVoList);
            resultGroupVo.setDeviceStatusNotGroup(noGroupsStatus);
            resultGroupVo.setEventSecCtlPower(eventSecCtlPower);
            resultGroupVo.setEventSecCtlPowerOutput(eventSecCtlPowerOutput);
            resultGroupVo.setEventSecCtlPowerSet(eventSecCtlPowerSet);
            resultGroupVo.setEventInput(eventInput);
            resultGroupVo.setEventSfpInfo(eventSfpInfo);
            resultGroupVo.setEventPosture(eventPosture);
        }
        return resultGroupVo;

    }

    /**
     *   设备详细信息(告警记录列表)
     * @param pageInfo 分页参数
     * @param deviceAlarmDto 查询参数
     * @return
     */
    public PageInfo<DeviceEventAlarmVo> getHistoryAlarm (PageInfo<DeviceAlarmVo> pageInfo, DeviceAlarmDto deviceAlarmDto){
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getStartTime());
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        List<String> list = new ArrayList();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            aa = tableName + "" + str;
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo vo : deviceEventHisAlarmVos) {
            tables.add(vo.getTableName());
        }
        deviceAlarmDto.setTableNames(tables);
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<DeviceEventAlarmVo> listVo = eventAlarmMapper.getHistoryAlarmData(deviceAlarmDto);
        String alarmInfo = "";
        DeviceFaultDto deviceFaultDto = new DeviceFaultDto();
        String[] statues = {"0", "1","2", "3", "4"};
        deviceFaultDto.setStatuses(statues);
        // TODO 判断故障类型
        // TODO 判断故障显示状态 优先级最高 : 已派单 > 已恢复&未恢复
        if (listVo != null && listVo.size() > 0) {
            for (DeviceEventAlarmVo vo : listVo) {
                if (vo.getAlarmTypesDes() != null) {
                    String[] alarmTypes = vo.getAlarmTypesDes().split(";"); //报警类型中文描述
                    String[] alarmType = vo.getAlarmTypes().split(","); //报警类型英文描述
                    if (alarmTypes.length == 1) {
                        // TODO 通过设备id和报警类型去查找工单来判断是否显示已派单
                        deviceFaultDto.setDeviceId(vo.getDeviceId());
                        deviceFaultDto.setAlarmType(alarmType[0]);
                        deviceFaultDto.setEventId(vo.getEventIds());
                        List<DeviceFaultVo> voList = flowRunMapper.findUndoneList(deviceFaultDto);
                        log.info("voList Size : {}",voList.size());
                        if (voList != null && voList.size() > 0) {
                            vo.setStatusDes("已派单");
                        }
                    }
                    if (alarmTypes.length > 1) {
                        for (int i = 0; i < alarmTypes.length; i++) {
                            String info = eventAlarmMapper.findAlarmInfo(vo.getDeviceId(), vo.getEventIds(), alarmTypes[i]);
                            // TODO 通过设备id和报警类型去查找工单来判断是否显示已派单
                            deviceFaultDto.setDeviceId(vo.getDeviceId());
                            deviceFaultDto.setAlarmType(alarmType[i]);
                            deviceFaultDto.setEventId(vo.getEventIds());
                            List<DeviceFaultVo> voList = flowRunMapper.findUndoneList(deviceFaultDto);
                            if (voList != null && voList.size() > 0) {
                                info = "已派单";
                            }
                            if (i < (alarmTypes.length - 1)) {
                                alarmInfo = alarmInfo + info + ";";
                            }
                            if (i == (alarmTypes.length - 1)) {
                                alarmInfo = alarmInfo + info;
                            }
                        }
                        vo.setStatusDes(alarmInfo);
                        // TODO 重置alarmInfo
                        alarmInfo = "";
                    }
                }
            }
        }
        return new PageInfo<>(listVo);
    }

    /**
     *   设备详细信息(告警记录列表统计)
     * @return
     */
    public List<Object> getHistoryAlarmStatistics ( DeviceAlarmDto deviceAlarmDto){
        // 动态构建日期
        List<String> dateList = DateUtils.getDayBetweenDates(deviceAlarmDto.getStartTime(),deviceAlarmDto.getEndTime());
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getStartTime());
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        List<String> list = new ArrayList();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            aa = tableName + "" + str;
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo vo : deviceEventHisAlarmVos) {
            tables.add(vo.getTableName());
        }
        deviceAlarmDto.setTableNames(tables);
        // 获取数据告警数据
        List<DeviceEventAlarmVo> historyAlarmData = eventAlarmMapper.getHistoryAlarmCounts(deviceAlarmDto);
        Map<String, Map> resultMap = new LinkedHashMap<>();
        for(String date : dateList){
            Map<String, Object> map = new HashMap<>();
            map.put("total", 0);
            map.put("alarmTime", date);
            resultMap.put(date, map);
        }
        for(DeviceEventAlarmVo eventAlarmVo : historyAlarmData){
            Map<String, Object> map = resultMap.get(eventAlarmVo.getAlarmTime());
            if(map != null){
                map.put("total",(Integer) map.get("total") + eventAlarmVo.getTotal());
            }
        }
        return new ArrayList<>(resultMap.values());
    }



    /**
     * 维护历史统计
     * @param deviceId 设备id
     * @param startTime 开始时间
     * @param endTime 结束时间  
     * @return  map
     */
    public Map getHistoryMaintainCounts(String deviceId,String startTime,String endTime){
        Map map=new HashMap();
        // 历史维修次数
        map.put("historyCounts",runMapper.getHistoryMaintainCounts(null,deviceId,startTime,endTime));
        // 自动
          map.put("automaticCounts",runMapper.getHistoryMaintainCounts("0",deviceId,startTime,endTime));
        // 手动
        map.put("manualCounts",runMapper.getHistoryMaintainCounts("1",deviceId,startTime,endTime));
        return map;

    }

    public Map getOnlineStatusByDeviceId(String id) {
        return deviceDetailsMapper.getOnlineStatusByDeviceId(id);
    }
}