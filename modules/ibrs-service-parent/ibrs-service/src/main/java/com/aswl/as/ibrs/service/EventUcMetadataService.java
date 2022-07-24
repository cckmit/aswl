package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.common.core.enums.FlowRunStatus;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.ibrs.api.dto.*;
import com.aswl.as.ibrs.api.module.EventUcMetadata;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.mapper.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public class EventUcMetadataService extends CrudService<EventUcMetadataMapper, EventUcMetadata> {
    private final EventUcMetadataMapper metadataMapper;
    private final AsEventHisAlarmMapper asEventHisAlarmMapper;
    private final FlowRunMapper flowRunMapper;
    private final RegionMapper regionMapper;
    private  final DeviceEventAlarmMapper eventAlarmMapper;


    /**
     * 分页查询事件元数据列表
     * @param page
     * @param eventUcMetadata
     * @return  PageInfo
     */

    public PageInfo<EventUcMetadataVo> findPageInfo(PageInfo<EventUcMetadata> page, EventUcMetadata eventUcMetadata) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(metadataMapper.findPageInfo(eventUcMetadata));
    }
    

    /**
     * 新增事件元数据
     *
     * @param eventUcMetadata
     * @return int
     */
    @Transactional
    @Override
    public int insert(EventUcMetadata eventUcMetadata) {
        return super.insert(eventUcMetadata);
    }

    /**
     * 删除事件元数据
     *
     * @param eventUcMetadata
     * @return int
     */
    @Transactional
    @Override
    public int delete(EventUcMetadata eventUcMetadata) {
        return super.delete(eventUcMetadata);
    }

    /**
     * 分页条件查询设备报警列表
     *
     * @param pageInfo
     * @param deviceAlarmDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> findPage(PageInfo<DeviceAlarmVo> pageInfo, DeviceAlarmDto deviceAlarmDto) {

        //这里添加租户和项目id
        String tenantCode = null;
        String projectId = null;
        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();// admin和运营端应该不需要设置该tenantCode和projectId
            projectId = SysUtil.getProjectId();
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);


        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        PageInfo<DeviceAlarmVo> info = new PageInfo<>(metadataMapper.findDeviceAlarmInfo(deviceAlarmDto));
        List<DeviceAlarmVo> list = new ArrayList<>();
        DeviceFaultDto deviceFaultDto = new DeviceFaultDto();

        deviceFaultDto.setTenantCode(tenantCode);
        deviceFaultDto.setProjectId(projectId);

        String[] statues = {"0", "2", "3", "4"};
        deviceFaultDto.setStatuses(statues);
        if (info.getList() != null && info.getList().size() > 0) {

            for (DeviceAlarmVo deviceAlarmVo : info.getList()) {
                deviceAlarmVo.setIntervalTime(formatDateTime(deviceAlarmVo.getIntervalTime()));
                if (deviceAlarmDto.getStatus() != null && "1".equals(deviceAlarmDto.getStatus())) {
                    String alarmType = deviceAlarmVo.getAlarmTypes();
                    String[] alarmTypes = alarmType.split(",");

                    //通过设备id和报警类型去查找工单来判断是否手动派单
                    deviceFaultDto.setDeviceId(deviceAlarmVo.getId());
                    deviceFaultDto.setStatuses(statues);
                    if (alarmTypes != null && alarmTypes.length > 0) {
                        for (String type : alarmTypes) {
                            deviceFaultDto.setAlarmType(type);
                            List<DeviceFaultVo> voList = flowRunMapper.findUndoneList(deviceFaultDto);
                            if (voList == null || voList.size() == 0) {
                                //查询设备的报警类型没有派单就设置为手动派单
                                deviceAlarmVo.setIsManual(1);
                                break;
                            }
                        }
                    } else {
                        deviceAlarmVo.setIsManual(0);
                    }
                }

                list.add(deviceAlarmVo);
            }
        }

        info.setList(list);
        return info;
    }

    public PageInfo<DeviceAlarmVo> findByPage(PageInfo<DeviceAlarmVo> pageInfo, DeviceAlarmConditionDto deviceAlarmConditionDto) {
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        PageInfo<DeviceAlarmVo> info = new PageInfo<>(metadataMapper.findDeviceAlarmInfoByCondition(deviceAlarmConditionDto));
        List<DeviceAlarmVo> list = new ArrayList<>();
        if (info.getList() != null && info.getList().size() > 0) {
            for (DeviceAlarmVo deviceAlarmVo : info.getList()) {
                deviceAlarmVo.setIntervalTime(formatDateTime(deviceAlarmVo.getIntervalTime()));

                if (deviceAlarmVo.getType() == 2 && deviceAlarmVo.getIsOnline() == 1) {
                    List<RegionDeviceVo> video = regionMapper.findVideo(deviceAlarmVo.getId());
                    if (video == null || video.size() == 0) {
                        deviceAlarmVo.setIsVideo(0);
                    }
                    for (RegionDeviceVo vo : video) {
                        if (vo.getType() == 3) {
                            deviceAlarmVo.setIsVideo(1);
                            break;
                        }
                    }

                } else if (deviceAlarmVo.getType() == 3 && deviceAlarmVo.getIsOnline() == 1) {
                    deviceAlarmVo.setIsVideo(1);
                } else {
                    deviceAlarmVo.setIsVideo(0);
                }

                list.add(deviceAlarmVo);
            }
        }
        info.setList(list);
        return info;
    }

    /**
     * 查询设备报警列表
     *
     * @param deviceAlarmDto
     * @return
     */
    public List<DeviceAlarmVo> findDeviceAlarmInfo(DeviceAlarmDto deviceAlarmDto) {
        List<DeviceAlarmVo> info = metadataMapper.findDeviceAlarmInfo(deviceAlarmDto);
        List<DeviceAlarmVo> list = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            info.get(i).setNum(i + 1);
            info.get(i).setIntervalTime(formatDateTime(info.get(i).getIntervalTime()));
            list.add(info.get(i));
        }
        return list;
    }

    /**
     * 获取设备告警明细记录
     *
     * @param id
     * @return
     */
    public List<DeviceAlarmDetailsVo> findDeviceInfo(String id) {

        String tenantCode = null;
        String projectId = null;
        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();// admin和运营端应该不需要设置该tenantCode和projectId
            projectId = SysUtil.getProjectId();
        }

        List<DeviceAlarmDetailsVo> deviceInfo = metadataMapper.findAlarmInfo(id, tenantCode, projectId);
        List<DeviceAlarmDetailsVo> promptInfo = metadataMapper.findPromptInfo(id,tenantCode,projectId);
        deviceInfo.addAll(promptInfo);
        return deviceInfo;
    }


    /**
     * 获取历史告警列表
     *
     * @param deviceAlarmDto
     * @return
     */
    public List<DeviceAlarmVo> findHistory(DeviceAlarmDto deviceAlarmDto) {
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
        List<DeviceAlarmVo> info = metadataMapper.findDeviceHistoryStatusAlarmInfo(deviceAlarmDto);
        List<DeviceAlarmVo> list1 = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            info.get(i).setNum(i + 1);
            info.get(i).setIntervalTime(formatDateTime(info.get(i).getIntervalTime()));
            list1.add(info.get(i));
        }
        return list1;
    }

    /**
     * 获取历史状态列表
     *
     * @param pageInfo
     * @param deviceAlarmDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> findHistoryStatusPage(PageInfo<DeviceAlarmVo> pageInfo, DeviceAlarmDto deviceAlarmDto) {
//        Date startTime = null;
//        Date endTime = null;
//        try {
//            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getStartTime());
//            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getEndTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Calendar c = Calendar.getInstance();
//        c.setTime(startTime);
//        List<String> list = new ArrayList();
//        String tableName = "as_device_event_his_alarm_";
//        String aa = "";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
//            String str = sdf.format(c.getTime());
//            aa = tableName + "" + str;
//            list.add(aa);
//            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
//        }
//        //查询表是否存在
//        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
//        List<String> tables = new ArrayList<>();
//        for (DeviceEventHisAlarmVo vo : deviceEventHisAlarmVos) {
//            tables.add(vo.getTableName());
//        }
        List<String> months = null;
        List<String> tableNames = new ArrayList<>();
        String tablePrefix = "as_device_event_his_alarm_";
        try {
            months = getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (String month : months) {
            tableNames.add(tablePrefix+month.replaceAll("-",""));
        }
        List<String> tables = asEventHisAlarmMapper.findHisAlarmTab(tableNames);
        if(tables.size() <= 0){
            return null;
        }
        deviceAlarmDto.setTableNames(tables);
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        PageInfo<DeviceAlarmVo> info = new PageInfo<>(metadataMapper.findDeviceHistoryStatusAlarmInfo(deviceAlarmDto));
        List<DeviceAlarmVo> list1 = new ArrayList<>();
        for (DeviceAlarmVo deviceAlarmVo : info.getList()) {
            deviceAlarmVo.setIntervalTime(formatDateTime(deviceAlarmVo.getIntervalTime()));
            list1.add(deviceAlarmVo);
        }
        info.setList(list1);
        return info;
    }

    //排序
    public String[] reverseArray(String[] Array) {
        String[] new_array = new String[Array.length];
        for (int i = 0; i < Array.length; i++) {
            // 反转后数组的第一个元素等于源数组的最后一个元素：
            new_array[i] = Array[Array.length - i - 1];
        }
        return new_array;
    }

    //时长转换
    public String formatDateTime(String time) {
        if (time == null || "".equals(time)) {
            return "";
        }
        long ss = Long.valueOf(time);
        String intervalTime = null;
        long days = ss / (60 * 60 * 24);
        long hours = (ss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (ss % (60 * 60)) / 60;
        long seconds = ss % 60;
        if (days > 0) {
            intervalTime = days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours > 0) {
            intervalTime = hours + "小时" + minutes + "分钟";
        } else if (minutes > 0) {
            intervalTime = minutes + "分钟";
        } else {
            intervalTime = "1分钟内";
        }
        return intervalTime;
    }

    /**
     * 根据元数据ID查询已设置操作事件列表
     *
     * @param id
     * @return List
     */
    public List<EventUcStatusOperationVo> findSeletedOperationByMetadataId(String id) {
        return metadataMapper.findSeletedOperationByMetadataId(id);
    }

    /**
     * 根据设备型号ID获取元数据
     *
     * @return List<EventUcMetadata>
     */
    public List<EventUcMetadataVo> findEventUcMetadataByDeviceModelId(String deviceModelId) {

        return metadataMapper.findEventUcMetadataByDeviceModelId(deviceModelId);
    }

    /**
     * 查询设备历史报警信息列表
     *
     * @param pageInfo
     * @param deviceAlarmConditionDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> findHistoryByPage(PageInfo<DeviceAlarmVo> pageInfo, DeviceAlarmConditionDto deviceAlarmConditionDto) {

        // 这里历史报警信息列表需要添加项目过滤
        String tenantCode = null;
        String projectId = null;
        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();
            projectId = SysUtil.getProjectId();
        }

        deviceAlarmConditionDto.setTenantCode(tenantCode);
        deviceAlarmConditionDto.setProjectId(projectId);

        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmConditionDto.getStartTime());
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmConditionDto.getEndTime());
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
        deviceAlarmConditionDto.setTableNames(tables);
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        PageInfo<DeviceAlarmVo> info = new PageInfo<>(metadataMapper.findHistoryByPage(deviceAlarmConditionDto));
        List<DeviceAlarmVo> list1 = new ArrayList<>();
        for (DeviceAlarmVo deviceAlarmVo : info.getList()) {
            deviceAlarmVo.setIntervalTime(formatDateTime(deviceAlarmVo.getIntervalTime()));
            list1.add(deviceAlarmVo);

            // 遍历一下有没有视频
            // deviceAlarmVo.setIsVideo(deviceService.getIsVideoValue(deviceAlarmVo.getId(), deviceAlarmVo.getType(), String.valueOf(deviceAlarmVo.getIsOnline())));

        }
        info.setList(list1);


        return info;
    }

    public PageInfo<DeviceAlarmVo> findHistoryMsg(PageInfo<DeviceAlarmVo> pageInfo, DeviceAlarmDto deviceAlarmDto) {

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
        if(deviceAlarmDto.getAlarmTypeNameGroup() != null && !"".equals(deviceAlarmDto.getAlarmTypeNameGroup())){
            deviceAlarmDto.setAlarmTypeNameGroups(Arrays.asList(deviceAlarmDto.getAlarmTypeNameGroup().split(";")));
        }
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<DeviceAlarmVo> msgs = metadataMapper.findHistoryMsg(deviceAlarmDto);
        List<DeviceAlarmVo> voList = new ArrayList<>();
        if (msgs != null) {
            for (DeviceAlarmVo msg : msgs) {
                //根据evevtId查询工单id及工单状态
                List<DeviceFaultVo> list1 = flowRunMapper.findWorkorderByEventId(msg.getEventId());
                if (list1 != null && list1.size() > 0) {
                    msg.setRunId(list1.get(0).getId());
                    msg.setStatus(list1.get(0).getStatus());
                }
                voList.add(msg);
            }
        }
        PageInfo<DeviceAlarmVo> page = new PageInfo<>(msgs);
        page.setList(voList);
        return page;
    }

    public List<DeviceAlarmDetailsVo> findDeviceWorkOrderInfo(String id) {

        String tenantCode = null;
        String projectId = null;
        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();// admin和运营端应该不需要设置该tenantCode和projectId
            projectId = SysUtil.getProjectId();
        }


        List<DeviceAlarmDetailsVo> info = metadataMapper.findDeviceInfo(id, tenantCode, projectId);
        List<DeviceAlarmDetailsVo> list = new ArrayList<>();
        DeviceFaultDto deviceFaultDto = new DeviceFaultDto();

        deviceFaultDto.setTenantCode(tenantCode);
        deviceFaultDto.setProjectId(projectId);

        String[] statues = {FlowRunStatus.INIT.getValue().toString(), FlowRunStatus.CONFIRMED.getValue().toString(), FlowRunStatus.MAINTENANCE.getValue().toString(),
                FlowRunStatus.MAINTENANCE_UNDONE.getValue().toString(),FlowRunStatus.REVIEW_PENDING.getValue().toString(),FlowRunStatus.REVIEW_NOT_PASS.getValue().toString()};
        deviceFaultDto.setStatuses(statues);
        for (DeviceAlarmDetailsVo vo : info) {
            deviceFaultDto.setDeviceId(vo.getId());
            deviceFaultDto.setAlarmType(vo.getAlarmType());
            List<DeviceFaultVo> voList = flowRunMapper.findUndoneList(deviceFaultDto);
            if (voList == null || voList.size() == 0) {
                vo.setIsSendOrder(0);
            } else {
                vo.setIsSendOrder(1);
                vo.setRunId(voList.get(0).getId());
            }
            list.add(vo);
        }
        return list;
    }

    public Map getCount(DeviceAlarmConditionDto deviceAlarmConditionDto) {
        List<DeviceAlarmVo> list = metadataMapper.findDeviceAlarmInfoByCondition(deviceAlarmConditionDto);
        Map map = new LinkedHashMap();
        //获得设备总数
        long count1 = list.stream().count();
        map.put("total", count1);
        //获取设备在线数
        long count2 = list.stream().filter(d -> d.getIsOnline() == 1 && d.getType() == 2).count();
        map.put("online", count2);
        // //获取设备故障数
        // long count3 = list.stream().filter(d ->d.getAlarmLevel() != null && d.getAlarmLevel().equals("故障") && d.getType()!=null && d.getType() == 2).count();
        int count3 = eventAlarmMapper.getAlarmOrWrongCount(DeviceKindType.BOX.getType(), deviceAlarmConditionDto.getRegionCode(), deviceAlarmConditionDto.getTenantCode(), deviceAlarmConditionDto.getProjectId(), "1");
        map.put("warning", count3);
        //获取设备预警数
        long count4 = list.stream().filter(d -> d.getAlarmLevel() != null && d.getAlarmLevel().equals("预警") && d.getType() != null && d.getType() == 2).count();
        map.put("pre-warning", count4);
        //获取设备正常数
        long count8 = list.stream().filter(d -> d.getAlarmLevel() != null && d.getAlarmLevel().equals("正常") && d.getType() != null && d.getType() == 2).count();
        map.put("normal", count8);
        //获取设备离线数
        long count5 = list.stream().filter(d -> d.getIsOnline() != null && d.getIsOnline() != 1 && d.getType() != null && d.getType() == 2).count();
        map.put("offline", count5);
        //获取摄像头在线数
        long count6 = list.stream().filter(d -> d.getType() != null && d.getType() == 3 && d.getIsOnline() != null && d.getIsOnline() == 1).count();
        map.put("camOnline", count6);
        //获取摄像头离线数
        long count7 = list.stream().filter(d -> d.getType() != null && d.getType() == 3 && d.getIsOnline() != null && d.getIsOnline() != 1).count();
        map.put("camOffline", count7);
        return map;
    }

    /**
     * 监控模块设备状态数量统计
     * @param deviceAlarmConditionDto
     * @return
     */
    public Map getDeviceCounts(DeviceAlarmConditionDto deviceAlarmConditionDto) {
        List<DeviceAlarmVo> list = metadataMapper.findDeviceAlarmInfoByCondition(deviceAlarmConditionDto);
        Map map = new LinkedHashMap();
        //获得设备总数
        long count1 = list.stream().count();
        map.put("total", count1);
        //获取设备故障数
        long count3 = list.stream().filter(d -> d.getAlarmLevel() != null && d.getAlarmLevel().equals("故障") && d.getType()!=null && d.getType() == Integer.parseInt(deviceAlarmConditionDto.getKind())).count();
        map.put("warning", count3);
        //获取设备预警数
        long count4 = list.stream().filter(d -> d.getAlarmLevel() != null && d.getAlarmLevel().equals("预警") && d.getType()!=null && d.getType() == Integer.parseInt(deviceAlarmConditionDto.getKind())).count();
        map.put("pre-warning", count4);
        //获取设备正常数
        long count5 = list.stream().filter(d -> d.getAlarmLevel() != null && d.getAlarmLevel().equals("正常") && d.getType()!=null && d.getType() == Integer.parseInt(deviceAlarmConditionDto.getKind())).count();
        map.put("normal", count5);
        //获取设备离线数
        long count6 = list.stream().filter(d -> d.getIsOnline() != null && d.getIsOnline() != 1 &&  d.getType()!= null && d.getType() ==  Integer.valueOf(deviceAlarmConditionDto.getKind())).count();
        map.put("offline", count6);
        return map;
    }

    public PageInfo<DeviceAlarmVo> findConditionByPage(PageInfo<DeviceAlarmVo> pageInfo, DeviceAlarmConditionDto deviceAlarmConditionDto) {
        //初始化
        if ((deviceAlarmConditionDto.getQuery() == null || "".equals(deviceAlarmConditionDto.getQuery()))
                && (deviceAlarmConditionDto.getAlarmLevel() == null || "".equals(deviceAlarmConditionDto.getAlarmLevel()))
                && (deviceAlarmConditionDto.getAlarmType() == null || "".equals(deviceAlarmConditionDto.getAlarmType()))) {
            PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
            //初始化找出所有的顶级设备建树
            deviceAlarmConditionDto.setFlag(0); //flag查询所有区域设备以及离线的标记
            List<DeviceAlarmVo> init = metadataMapper.findDeviceAlarmInfoByCondition(deviceAlarmConditionDto);
            PageInfo<DeviceAlarmVo> info = new PageInfo<>(init);

            if (init != null && init.size() > 0) {
                //找出所有设备集合
                DeviceAlarmConditionDto dto = new DeviceAlarmConditionDto();
                List<DeviceAlarmVo> all = metadataMapper.findDeviceAlarmInfoByCondition(dto);
                List<DeviceAlarmVo> list = dataClean(all);
                // 2、递归获取子节点
                for (DeviceAlarmVo parent : init) {
                    parent = recursiveTree(parent, all);
                }
                return info;
            }
        }
        //过滤报警级别,离线,关键字搜索

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        List<DeviceAlarmVo> offline = metadataMapper.findDeviceAlarmInfoByCondition(deviceAlarmConditionDto);
        PageInfo<DeviceAlarmVo> info = new PageInfo<>(offline);
        List<DeviceAlarmVo> list = dataClean(offline);
        info.setList(list);
        return info;
    }


    public static DeviceAlarmVo recursiveTree (DeviceAlarmVo parent, List < DeviceAlarmVo > list){
        for (DeviceAlarmVo child : list) {
            if (parent.getId().equals(child.getParentId())) {
                child = recursiveTree(child, list);
                parent.getChildren().add(child);
            }
        }

        return parent;
    }

    public  List<DeviceAlarmVo> dataClean(List<DeviceAlarmVo> init) {
        List<DeviceAlarmVo> list = new ArrayList<>();
        if (init != null && init.size() > 0) {
            for (DeviceAlarmVo deviceAlarmVo : init) {
                deviceAlarmVo.setIntervalTime(formatDateTime(deviceAlarmVo.getIntervalTime()));

                if (deviceAlarmVo.getType() == 2 && deviceAlarmVo.getIsOnline() == 1) {
                    List<RegionDeviceVo> video = regionMapper.findVideo(deviceAlarmVo.getId());
                    if (video == null || video.size() == 0) {
                        deviceAlarmVo.setIsVideo(0);
                    }
                    for (RegionDeviceVo vo : video) {
                        if (vo.getType() == 3) {
                            deviceAlarmVo.setIsVideo(1);
                            break;
                        }
                    }

                } else if (deviceAlarmVo.getType() == 3 && deviceAlarmVo.getIsOnline() == 1) {
                    deviceAlarmVo.setIsVideo(1);
                } else {
                    deviceAlarmVo.setIsVideo(0);
                }
                list.add(deviceAlarmVo);
            }
        }
        return list;
    }

    private static List<String> getMonthBetween(String minDate, String maxDate) throws ParseException {
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        while (min.before(max)) {
            result.add(sdf.format(min.getTime()));
            min.add(Calendar.MONTH, 1);
        }
        return result;
    }
}