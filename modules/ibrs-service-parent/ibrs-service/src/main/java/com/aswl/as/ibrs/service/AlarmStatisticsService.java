package com.aswl.as.ibrs.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.ibrs.api.dto.DeviceDto;
import com.aswl.as.ibrs.api.dto.MaintenanceStatisticsDto;
import com.aswl.as.ibrs.api.module.QrcodeBatch;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.utils.DeviceUtil;
import com.aswl.as.ibrs.utils.MonthUtils;
import com.aswl.as.ibrs.utils.StringUtils;
import com.aswl.as.user.api.module.Config;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.DeviceKindType;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.core.vo.UserVo;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.AlarmStatistics;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.module.Region;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.mapper.*;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.aswl.as.user.api.module.Tenant;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
@Service
public class AlarmStatisticsService extends CrudService<AlarmStatisticsMapper, AlarmStatistics> {
    private final AlarmStatisticsMapper statisticsMapper;
    private final UserServiceClient userServiceClient;
    private final AsEventHisAlarmMapper asEventHisAlarmMapper;
    private final AlarmTypeUserFavoriteMapper favoriteMapper;
    private  final DeviceEventAlarmMapper eventAlarmMapper;
    private final DeviceMapper deviceMapper;
    private final AsEventNetworkMapper networkMapper;
    private final ProjectMapper projectMapper;
    private final OnlineStatisticsMapper onlineStatisticsMapper;
    private final FlowRunMapper flowRunMapper;
    private final RegionMapper regionMapper;
    private final ElectricStatisticsMapper electricStatisticsMapper;

//    /**
//     * windows上传路径
//     */
//    @Value(value = "${file.path.winUpload}")
//    private String winUploadPath;

//    /**
//     * linux上传路径
//     */
//    @Value(value = "${linuxfile.path.linuxUpload}")
//    private String linuxUploadPath;

    /**
     * 首页设备故障趋势统计
     *
     * @param num          时间区间
     * @param type         时间类型 week、month、year
     * @param deviceId    设备ID
     * @param deviceKindId 设备类型  1:光纤收发器 2:传输箱 3:摄像头
     * @param format       日期格式
     * @return
     */
    public List<Object> queryByDate(int num, String type, String deviceId, int deviceKindId, String format,String isAsOs) {

        String roles = RoleContextHolder.getRole();
        String regionCode = null;
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (CommonConstant.IS_ASOS_TRUE.equals(isAsOs)) {
            tenantCode = null;
            projectId = null;
        } else {
            if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
                //加载所有项目
                tenantCode = null;
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
                //加载用户所在租户的所有项目
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
                //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
            } else {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
//                    throw new CommonException("当前用户下暂无区域设备");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }

        //获取日期的区间字符串
        List<String> dateStr = DateUtils.getBeforeOrAfterDate(new Date(), num, type);
        List<AlarmDeviceVo> list = statisticsMapper.getAlarmCounts(deviceKindId, deviceId, regionCode, format,tenantCode,projectId);
        Map<String, Map> resultMap = new LinkedHashMap<>();
        for (int i = dateStr.size() - 1; i >= 0; i--){
            Map<String, Object> map = new HashMap<>();
            map.put("queryType", type);
            map.put("gatherTime", dateStr.get(i));
            map.put("counts",0);
            resultMap.put(dateStr.get(i),map);
        }
        if (list!=null && list.size()>0)
            for (AlarmDeviceVo vo : list) {
                Map map = resultMap.get(vo.getGatherTime());
                if (map!=null){
                    map.put("queryType",type);
                    map.put("counts",vo.getCounts());
                }
            }
        return new ArrayList<>(resultMap.values());
    }

    /**
     * 统计报表-->告警级别统计
     *
     * @param deviceAlarmDto
     * @return List
     */
    public List<Object> getAlarmLevelData(DeviceAlarmDto deviceAlarmDto) {

        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
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
        List<String> list = new ArrayList<>();
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
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        deviceAlarmDto.setTableNames(tables);
        List<DeviceEventHisAlarmVo> alarmLevelData = statisticsMapper.findAlarmlevelData(deviceAlarmDto);
        List<String> dateList = new ArrayList<>();
        Map<String, Map> resultMap = new LinkedHashMap<>();
        if("day".equals(deviceAlarmDto.getTimeUnit())){
            //每天数据
            dateList = DateUtils.getDayBetweenDates(formatter.format(startTime), formatter.format(endTime));
            for(String date : dateList){
                Map<String, Object> map = new HashMap<>();
                map.put("total", 0);
                map.put("level1", 0);
                map.put("level2", 0);
                map.put("date", date);
                resultMap.put(date +"日", map);
            }
            for(DeviceEventHisAlarmVo deviceEventHisAlarmVo : alarmLevelData){
                Map<String, Object> map = resultMap.get(deviceEventHisAlarmVo.getDate());
                if(map != null){
                    map.put("total",(Integer) map.get("total") + deviceEventHisAlarmVo.getTotal());
                    map.put("level1", (Integer) map.get("level1") + deviceEventHisAlarmVo.getLevel1());
                    map.put("level2", (Integer) map.get("level2") + deviceEventHisAlarmVo.getLevel2());
                }
            }
            
        }else {
            // 每月数据
            dateList = DateUtils.getMonthBetween(formatter.format(startTime), formatter.format(endTime));
            for (String date : dateList) {
                Map<String, Object> map = new HashMap<>();
                map.put("total", 0);
                map.put("level1", 0);
                map.put("level2", 0);
                map.put("date", date);
                resultMap.put(date + "月", map);
            }
            for (DeviceEventHisAlarmVo deviceEventHisAlarmVo : alarmLevelData) {
                Map<String, Object> map = resultMap.get(deviceEventHisAlarmVo.getDate());
                if (map != null) {
                    map.put("total", (Integer) map.get("total") + deviceEventHisAlarmVo.getTotal());
                    map.put("level1", (Integer) map.get("level1") + deviceEventHisAlarmVo.getLevel1());
                    map.put("level2", (Integer) map.get("level2") + deviceEventHisAlarmVo.getLevel2());
                }
            }
        }
        return new ArrayList<>(resultMap.values());
    }


    /**
     * 统计报表-->告警类型统计
     *
     * @param deviceAlarmDto
     * @return
     */
    public List<AlarmTypeStatisticsVo> getAlarmTypesData(DeviceAlarmDto deviceAlarmDto) {
        List<AlarmTypeStatisticsVo> alarmTypeStatisticsVoList=new ArrayList<>();
        String userId = "";
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        if (info.getCode() == 200) {
            userId = info.getData().getId();
        }
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        //设置projectId
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
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
        List<String> list = new ArrayList<>();
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
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        deviceAlarmDto.setTableNames(tables);
        // deviceAlarmDto.setRegionCode(regionCode);

        // 查询用户关注
        List<AlarmTypeUserFavoriteVo> userFavoriteVoList = favoriteMapper.findAlarmUserFavoriteTypeLists(userId);
        if (userFavoriteVoList != null && userFavoriteVoList.size() > 0) {
            for (AlarmTypeUserFavoriteVo vo : userFavoriteVoList) {
                deviceAlarmDto.setAlarmTypeName(vo.getAlarmType());
                AlarmTypeStatisticsVo alarmTypesData = statisticsMapper.findAlarmTypesData(deviceAlarmDto);
                if (alarmTypesData!=null) {
                    alarmTypeStatisticsVoList.add(alarmTypesData);
                }

            }
        }
        Collections.sort(alarmTypeStatisticsVoList, new Comparator<AlarmTypeStatisticsVo>() {
            @Override
            public int compare(AlarmTypeStatisticsVo o1, AlarmTypeStatisticsVo o2) {
                if (o1.getTotal() <o2.getTotal()){
                    return 1;
                }
                if (o1.getTotal() ==o2.getTotal()){
                    return 0;
                }
                return -1;

            }
        });
        return alarmTypeStatisticsVoList;
    }


    /**
     * 统计报表-->故障维护统计
     *
     * @param deviceAlarmDto
     * @return
     */
    public List<FlowRunStatisticsVo> getAlarmFaultData(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无区域设备");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId =deviceAlarmDto.getQueryProjectId();
        if (queryProjectId!= null){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        return statisticsMapper.getAlarmFaultData(deviceAlarmDto);
    }

    /**
     * 地图上设备统计
     *
     * @return Map
     */
    public Map<String, Integer> getMapDeviceData(String isAsOs) {
        //Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        DecimalFormat df=new DecimalFormat("0.00");
        Map<String, Integer> map = new HashMap<String, Integer>();
        String roles = RoleContextHolder.getRole();
        String regionCode = null;
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if(CommonConstant.IS_ASOS_TRUE.equals(isAsOs)){ //运营端
            tenantCode = null;
            projectId = null;
        } else {
            if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
                //加载所有项目
                tenantCode = null;
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
                //加载用户所在租户的所有项目
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
                //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
            } else {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        // 总设备数
//        Integer devicesCount = deviceMapper.getDevicesCount(regionCode,tenantCode,projectId);
        Integer devicesCount = deviceMapper.getDevicesBoxCount(DeviceKindType.BOX.getType(),regionCode,tenantCode,projectId);
        // 告警智能箱
//        int alarmDataCounts = eventAlarmMapper.getAlarmCounts(regionCode,tenantCode,projectId,"2");
        int alarmDataCounts = eventAlarmMapper.getAlarmOrWrongCount(DeviceKindType.BOX.getType(),regionCode,tenantCode,projectId,"2");
        //故障智能箱
        int wrongDataCounts = eventAlarmMapper.getAlarmOrWrongCount(DeviceKindType.BOX.getType(),regionCode,tenantCode,projectId,"1");

//        int newAlarm1DataCounts=eventAlarmMapper.getAlarmCounts(regionCode,tenantCode,projectId,"1");

        //离线告警设备数
        //int offAlarmCounts = eventAlarmMapper.getOffAlarmDataCounts(regionCode,tenantCode,projectId);

        //离线设备数
//        int offCount=networkMapper.findOffDevice(regionCode,tenantCode,projectId);

        //离线智能箱数
        int offCount=networkMapper.findOffDeviceBox(DeviceKindType.BOX.getType(),regionCode,tenantCode,projectId);

        //在线智能箱数
//        int onlineCount=networkMapper.findOnlineDevice(regionCode,tenantCode,projectId);
        int onlineCount=networkMapper.findOnlineDeviceBox(DeviceKindType.BOX.getType(),regionCode,tenantCode,projectId,null);

        // 调试的智能箱数

        int debugCount=networkMapper.findOnlineDeviceBox(DeviceKindType.BOX.getType(),regionCode,tenantCode,projectId,"debug");

        //故障率
        int faultRate;
        if (alarmDataCounts==0 &&devicesCount==0){
            faultRate=0;
        }else {
            faultRate = (int) (Double.parseDouble(df.format((float) alarmDataCounts / devicesCount)) * 100);
        }

        //待处理工单数(web)
        int untreatedCount=statisticsMapper.getUntreatedRunCount(regionCode,tenantCode,projectId,"web");

        int untreatedCountApp=statisticsMapper.getUntreatedRunCount(regionCode,tenantCode,projectId,"app");

        map.put("normalCount",onlineCount-alarmDataCounts);
        map.put("alarmDataCounts",alarmDataCounts);
        //这两个是新加的，用来分开等级1和等级2的错误
//        map.put("newAlarm1DataCounts",newAlarm1DataCounts);
        //eventAlarmMapper.getAlarmCounts flag为2的时候是包含1和2，flag为1的时候只含有1，所以算2的数量时只要减一下，如果后面getAlarmCounts改了，也要改这里
//        map.put("newAlarm2DataCounts",alarmDataCounts-newAlarm1DataCounts);
        map.put("offCount",offCount);
        map.put("faultRate",faultRate);
        map.put("untreatedCount",untreatedCount);
        map.put("untreatedCountApp",untreatedCountApp);
        map.put("wrongDataCounts",wrongDataCounts);
        map.put("debugCount",debugCount);
        return map;
    }

    /**
     * 查询报障箱摄像头在线数据
     * @param
     * @return
     */
    public Map getOnLineDeviceData(String type,String isAsOs) {

        String role = RoleContextHolder.getRole();
        String tenantCode=SysUtil.getTenantCode();
        String projectId=SysUtil.getProjectId();
        String regionCode = null;
        if(CommonConstant.IS_ASOS_TRUE.equals(isAsOs)){
            tenantCode = null;
            projectId = null;
        }else {
            if (SysUtil.isAdmin()){
                tenantCode = null;
            }else if(role != null && !"".equals(role) && role.contains(SecurityConstant.ROLE_ADMIN)){

            }else {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
               if(userRegionCode == null || "".equals(userRegionCode)){
                   return null;
               }
               regionCode = userRegionCode;
            }
        }
        return statisticsMapper.getOnLineDeviceCount(regionCode,type,tenantCode,projectId);

    }


    /**
     * 首页近30天告警数据TOP5
     *
     * @param deviceAlarmDto
     * @return
     */
    public List<AlarmTypeStatisticsVo> getHistoryTop5(DeviceAlarmDto deviceAlarmDto) {

        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        String tablePrefix = "as_device_event_his_alarm_";
        List<AlarmTypeStatisticsVo> historyTop5 = null;
        List<String> tableNames = new ArrayList<>();
        List<String> months = DateUtils.getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
        if(months != null && months.size() > 0){
            for (String month : months) {
                tableNames.add(tablePrefix+month.replaceAll("-",""));
            }
        List<String> tables = asEventHisAlarmMapper.findHisAlarmTab(tableNames);
            if(tables.size()>0){
                deviceAlarmDto.setTableNames(tables);
                historyTop5 = statisticsMapper.getHistoryTop5(deviceAlarmDto);
            }
        }
        return historyTop5;
    }



    /**
     * 在线率统计
     *
     * @param deviceAlarmDto
     * @return
     */
    public Map getOnlineRateData(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {

            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        Map map=new HashMap();
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getStartTime());
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
            // 查询设备总数
            int boxCounts = networkMapper.findBoxCounts(deviceAlarmDto.getRegionCode(), deviceAlarmDto.getKind(), tenantCode,deviceAlarmDto.getProjectId());
            // 在线设备数
            int onLineBoxCounts = networkMapper.findOnLineBoxCounts(deviceAlarmDto.getRegionCode(), deviceAlarmDto.getKind(), tenantCode, deviceAlarmDto.getProjectId());
            map.put("onlineCount",onLineBoxCounts);
            map.put("offlineCount",boxCounts-onLineBoxCounts);
            map.put("deviceCount",boxCounts);
        }else{
//            deviceAlarmDto.setTenantCode(tenantCode);
//            deviceAlarmDto.setProjectId(projectId);
            Map onlineStatistics = onlineStatisticsMapper.getOnlineStatistics(deviceAlarmDto);
            map.put("onlineCount",onlineStatistics.get("onlineNum"));
            map.put("offlineCount",onlineStatistics.get("offlineCount"));
            map.put("deviceCount",onlineStatistics.get("deviceNum"));
        }
        return map;
    }



    /**
     * 在线率统计列表
     *
     * @param deviceAlarmDto
     * @return
     */
    public List<DeviceAlarmVo> getOnlineList(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
//                deviceAlarmDto.setRegionCode(userRegionCode);
                regionCode = userRegionCode;
            }
        }

        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_event_his_network_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_event_network";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> EventHisTableVos = asEventHisAlarmMapper.findEventHisTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : EventHisTableVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (EventHisTableVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        List<DeviceAlarmVo> onlineList = statisticsMapper.getOnlineList(deviceAlarmDto);
        if(deviceAlarmDto.getNetworkStatus() == 0){ //离线列表,设置离线时长
            for (int i = 0; i < onlineList.size(); i++) {
                DeviceAlarmVo deviceAlarmVo = onlineList.get(i);
                deviceAlarmVo.setIntervalTime(formatDateTime(deviceAlarmVo.getIntervalTime()));
            }
        }
        return onlineList;
    }

    /**
     * 总体健康率统计
     *
     * @param deviceAlarmDto
     * @return
     */
    public Map getHealthyRateData(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        deviceAlarmDto.setProjectId(projectId);
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);

        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                String str = sdf.format(c.getTime());
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }

        return statisticsMapper.getHealthyRateData(deviceAlarmDto);
    }




    /**
     * 统计报表--->健康指数统计 --->项目健康指数排名
     *
     * @param deviceAlarmDto
     * @return
     */
    public List<Map> getHealthyTop(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String startTime = deviceAlarmDto.getStartTime().substring(0,10);
        String endTime = deviceAlarmDto.getEndTime().substring(0,10);
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setStartTime(startTime);
        deviceAlarmDto.setEndTime(endTime);
        // 当前
        if (endTime.equals(startTime)) {
            return deviceMapper.getCurrentHealthyCount(deviceAlarmDto);
        }else{
            // 本月/本年度
            return onlineStatisticsMapper.getHisHealthyCount(deviceAlarmDto);
             
        }
    }



    /**
     * 总体健康率统计列表
     *
     * @param deviceAlarmDto
     * @return
     */
    public PageInfo<DeviceAlarmVo> getHealthyListPage(PageInfo<DeviceDto> page, DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);

        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd").parse(deviceAlarmDto.getStartTime());
            endTime = new SimpleDateFormat("yyyy-MM-dd").parse(deviceAlarmDto.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        return new PageInfo<>(statisticsMapper.getHealthyList(deviceAlarmDto));
    }


    /**
     * 故障维护统计报表导出
     *
     * @param deviceAlarmDto
     * @return Boolean
     */
    public ResponseEntity<byte[]> export(HttpServletResponse response, DeviceAlarmDto deviceAlarmDto) throws Exception {
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("multipart/form-data");
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=file01.xlsx");

        return exportReportExcel(response,deviceAlarmDto);
        //return  new ResponseBean<>(true,"导出成功");
    }

    public ResponseEntity<byte[]> exportReportExcel(HttpServletResponse response,DeviceAlarmDto deviceAlarmDto) throws Exception {

//        response.addHeader("ContentType", "application/octet-stream");

        Resource resource = new ClassPathResource("temp/reporttemplate.xlsx");
        boolean isFile = resource.isFile();
//        if(!isFile){     //如果不存在返回
//            return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
//        }
//        String path = resource.getFile().getPath();     //获取文件路径
        String path = "C:\\asms\\reporttemplate.xlsx";     //获取文件路径

        /* 数据写入模板文件中 */
        //更改文件名编码
        String fileName = "故障维护统计表.xlsx";
        String gFileName = URLEncoder.encode(fileName, "UTF-8");
        InputStream in = null;
        Workbook exl = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        OutputStream out = response.getOutputStream();
        try {
            in = new FileInputStream(path);
            exl = WorkbookFactory.create(in);

            exportSheet1(exl, deviceAlarmDto);

            exportSheet2(exl,deviceAlarmDto);

//            //激活浏览器弹出窗口
//            response.setCharacterEncoding("utf-8");
//            response.setContentType("application/x-msdownload");
//            //浏览器弹出窗口显示的文件名
//            response.addHeader("Content-Disposition", "attachment;filename=" +gFileName);
            exl.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=\"" + gFileName + "\"");
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload")).body(out.toByteArray());
//            response.getOutputStream().write(out.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
//                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ResponseEntity<byte[]>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 导出到第一个sheet
     */
    public void exportSheet1(Workbook exl, DeviceAlarmDto deviceAlarmDto) throws IOException {
        Sheet sheet1 = exl.getSheetAt(0);
        List<DeviceFaultVo> list = flowRunMapper.getFlowRunData(deviceAlarmDto);
        for (int i = 0, y = list.size(); i < y; ++i) {
            Row row = sheet1.createRow(i + 2);
            row.createCell(0).setCellValue(i + 1);  //序号
            row.createCell(1).setCellValue(list.get(i).getRegionName().trim());     //区域
            row.createCell(2).setCellValue(list.get(i).getDeviceName().trim());      //设备名称
            row.createCell(3).setCellValue(list.get(i).getIp().trim()); //ip
            row.createCell(4).setCellValue(list.get(i).getAlarmTypeName());  //故障类型
            row.createCell(5).setCellValue(list.get(i).getAlarmTime());                //故障时间
            row.createCell(6).setCellValue(list.get(i).getRepairTime());                //修复时间
            row.createCell(7).setCellValue(SecurityConstant.DEFAULT_TENANT_CODE);                //维护公司
            row.createCell(8).setCellValue(list.get(i).getRepairer());                //维护人
        }
    }


    // 第二个sheet导出
    private void exportSheet2(Workbook exl, DeviceAlarmDto deviceAlarmDto) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
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
        List<String> list = new ArrayList<>();
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
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        deviceAlarmDto.setTableNames(tables);

        DecimalFormat df=new DecimalFormat("0.0");
        Double boxOnlineRate=0.0d;
        Double cameraOnlineRate=0.0d;
        Double boxAlarmRate=0.0d;
        Double cameraAlarmRate=0.0d;
        List<Project> allList;
        // 获取项目信息
        if(deviceAlarmDto.getRegionCode() == null || "".equals(deviceAlarmDto.getRegionCode())){
            Project entity = new Project();
            entity.setProjectId(deviceAlarmDto.getProjectId());
            entity.setTenantCode(deviceAlarmDto.getTenantCode());
            allList  = projectMapper.findListProject(entity);
        }else {
            allList = projectMapper.findProjectByRegionCode(deviceAlarmDto);
        }
        //获取租户信息
        ResponseBean<Tenant> tenantData = userServiceClient.findTenantByTenantCode(deviceAlarmDto.getTenantCode());
        // 获取报障箱数据
        Map boxMap = onlineStatisticsMapper.getDevivceCount(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), DeviceKindType.BOX.getType(),deviceAlarmDto.getRegionCode(),deviceAlarmDto.getTenantCode(),deviceAlarmDto.getProjectId());
        // 获取摄像机数据
        Map cameraMap = onlineStatisticsMapper.getDevivceCount(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), DeviceKindType.CAMERA.getType(),deviceAlarmDto.getRegionCode(),deviceAlarmDto.getTenantCode(),deviceAlarmDto.getProjectId());
        // 获取报障箱告警数量
        deviceAlarmDto.setKind("2");
        Map boxAlarmMap=statisticsMapper.findAlarmlCount(deviceAlarmDto);
        deviceAlarmDto.setKind("3");
        Map cameraAlarmMap=statisticsMapper.findAlarmlCount(deviceAlarmDto);
        // 在线率计算(报障箱)
        if (Integer.parseInt(boxMap.get("onlineCount").toString())>0){
            boxOnlineRate = (Double.parseDouble(df.format((float)Integer.parseInt(boxMap.get("onlineCount").toString())/Integer.parseInt(boxMap.get("deviceCount").toString())))*100);
        }
        // 在线率计算(摄像机)
        if (Integer.parseInt(cameraMap.get("onlineCount").toString())>0){
            cameraOnlineRate =(Double.parseDouble(df.format((float)Integer.parseInt(cameraMap.get("onlineCount").toString())/Integer.parseInt(cameraMap.get("deviceCount").toString())))*100);
        }
        // 故障率计算(报障箱)
        if (Integer.parseInt(boxAlarmMap.get("alarmCount").toString())>0){
            System.out.println( Integer.parseInt(boxAlarmMap.get("alarmCount").toString())/Integer.parseInt(boxMap.get("deviceCount").toString()));
            boxAlarmRate = (Double.parseDouble(df.format((float)Integer.parseInt(boxAlarmMap.get("alarmCount").toString())/Integer.parseInt(boxMap.get("deviceCount").toString())))*100);
        }
        // 故障率计算(摄像机)
        if (Integer.parseInt(cameraAlarmMap.get("alarmCount").toString())>0){
            cameraAlarmRate = (Double.parseDouble(df.format((float)Integer.parseInt(cameraAlarmMap.get("alarmCount").toString())/Integer.parseInt(cameraMap.get("deviceCount").toString())))*100);
        }
        //设备箱故障平均修复时长
        Map<String,Number> boxRepairTime = flowRunMapper.getRepairTime(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), DeviceKindType.BOX.getType(),deviceAlarmDto.getRegionCode(),deviceAlarmDto.getTenantCode(),deviceAlarmDto.getProjectId());
        // 图像故障平均修复时长
        Map<String,Number> cameraRepairTime= flowRunMapper.getRepairTime(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), DeviceKindType.CAMERA.getType(),deviceAlarmDto.getRegionCode(),deviceAlarmDto.getTenantCode(),deviceAlarmDto.getProjectId());
        Sheet sheet2 = exl.getSheetAt(1);
        Row row1 = sheet2.getRow(1);
        Row row2 = sheet2.getRow(2);
        Row row3 = sheet2.getRow(3);
        Row row4 = sheet2.getRow(4);
        Row row5 = sheet2.getRow(5);
        Row row6 = sheet2.getRow(6);
        Row row7 = sheet2.getRow(7);
        Row row8 = sheet2.getRow(8);
        // 项目名称
        row1.getCell(1).setCellValue(allList.get(0).getProjectName());
        //使用单位
        row2.getCell(1).setCellValue(allList.get(0).getProjectOwner());
        //联系人
        row2.getCell(3).setCellValue(allList.get(0).getContacts());
        //电话
        row2.getCell(5).setCellValue(allList.get(0).getTelephone());
        //邮箱
        row2.getCell(7).setCellValue(allList.get(0).getEmail());
        //维护单位
        row3.getCell(1).setCellValue(tenantData.getData().getTenantName());
        // 联系人
        row3.getCell(3).setCellValue(tenantData.getData().getOwnerName());
        //电话
        row3.getCell(5).setCellValue(tenantData.getData().getTelephone());
        // 邮箱
        row3.getCell(7).setCellValue(tenantData.getData().getNotifyEmail());
        //统计时间
        row4.getCell(1).setCellValue(deviceAlarmDto.getStartTime().substring(0,10)+"到"+deviceAlarmDto.getEndTime().substring(0,10));
        //设备箱总数量
        row5.getCell(1).setCellValue(boxMap.get("deviceCount").toString());
        //摄像机总数量
        row5.getCell(4).setCellValue(cameraMap.get("deviceCount").toString());
        //设备箱在线率
        row6.getCell(1).setCellValue(df.format(boxOnlineRate)+"%");
        //图像在线率
        row6.getCell(4).setCellValue(df.format(cameraOnlineRate)+"%");
        // 设备箱故障率
        row7.getCell(1).setCellValue(df.format(boxAlarmRate)+"%");
        //图像故障率
        row7.getCell(4).setCellValue(df.format(cameraAlarmRate)+"%");
        // 设备箱故障平均修复时长
        row8.getCell(1).setCellValue(boxRepairTime == null ? "" : formatSeconds(boxRepairTime.get("ofTime").longValue()));
        //图像故障平均修复时长
        row8.getCell(4).setCellValue(cameraRepairTime == null ? "" : formatSeconds(cameraRepairTime.get("ofTime").longValue()));
    }


    /**
     * 故障或告警设备统计,区域和型号占比及排名
     */
    public AlarmStatisticsVo getRegionModelPercentTop(DeviceAlarmDto deviceAlarmDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        //所有区域的历史故障数或告警数    故障:alarmLevels = 1 告警:alarmLevels = 1,2
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        Integer alarmCount = statisticsMapper.findAlarmCountByTime(deviceAlarmDto);
        //每个区域的历史故障数或告警数    故障:alarmLevels = 1 告警:alarmLevels = 1,2
        List<RegionAlarmCountVo> alarmCountByTimeAndRegion = statisticsMapper.findAlarmCountByTimeAndRegion(deviceAlarmDto);
        //有设备的区域
        List<String> haveDeviceRegions = regionMapper.getHaveDeviceRegion(deviceAlarmDto);
        List<String> haveAlarmRegion = alarmCountByTimeAndRegion.stream().map(RegionAlarmCountVo::getRegionName).collect(Collectors.toList());
        List<String> collect = haveDeviceRegions.stream().filter(item -> !haveAlarmRegion.contains(item)).collect(Collectors.toList());
        if (collect.size() > 0) {
            for (String regionName : collect) {
                RegionAlarmCountVo alarmCountVo = new RegionAlarmCountVo();
                alarmCountVo.setRegionName(regionName);
                alarmCountVo.setRegionAlarmCount(0);
                alarmCountByTimeAndRegion.add(alarmCountVo);
            }
        }
        //每个型号的历史故障数或告警数    故障:alarmLevels = 1 告警:alarmLevels = 1,2
        List<DeviceModelAlarmCountVo> modelAlarmCountVoList = statisticsMapper.findAlarmCountByTimeAndModel(deviceAlarmDto);
        return new AlarmStatisticsVo(alarmCount,alarmCountByTimeAndRegion,alarmCount,modelAlarmCountVoList);
    }

    /**
     * 故障设备列表
     */
    public List<DeviceAlarmVo> getWrongList(DeviceAlarmDto deviceAlarmDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无区域");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        List<DeviceAlarmVo> wrongList = statisticsMapper.getWrongList(deviceAlarmDto);
        for (int i = 0; i < wrongList.size(); i++) {
            wrongList.get(i).setIntervalTime(formatDateTime(wrongList.get(i).getIntervalTime()));
        }
        return wrongList;
    }

    /**
     * 故障趋势分析
     * @return
     */
    public List<DeviceEventHisWrongVo> queryWrongTrend(DeviceAlarmDto deviceAlarmDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
//                            throw new CommonException("当前用户暂无区域");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        return statisticsMapper.queryWrongTrend(deviceAlarmDto);
    }

    /**
     * 区域在线占比
     */
    public List<Map> regionOnlineRatio(DeviceAlarmDto deviceAlarmDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无区域设备");
                    return null;
                }
              regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId =deviceAlarmDto.getQueryProjectId();
        if (queryProjectId != null){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = format.format(new Date());
        deviceAlarmDto.setCurrentDate(currentDate);
        return onlineStatisticsMapper.regionOnlineRatio(deviceAlarmDto);
    }

    /**
     * 统计报表--> 维护趋势分析
     */
    public Object maintenanceTrend(DeviceAlarmDto deviceAlarmDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无区域设备");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getStartTime());
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //动态构建日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = DateUtils.getDayBetweenDates(formatter.format(startTime), formatter.format(endTime));
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        Map<String,Integer> resultMap = new HashMap<>();
        //查询告警总数
        Long alarmCount  = statisticsMapper.findAlarmTotal(deviceAlarmDto);
        resultMap.put("alarmCount",alarmCount.intValue());
        //故障派单数和完成数
        Map<String,Number> dispatchCount  = flowRunMapper.findFaultDispatchCount(deviceAlarmDto);
        // 已完成数
        resultMap.put("finishCount",dispatchCount.get("finishCount") == null ? 0:dispatchCount.get("finishCount").intValue());
        //修复中数
        resultMap.put("repairCount",dispatchCount.get("repairCount") == null ? 0:dispatchCount.get("repairCount").intValue());
        //未派单数
        resultMap.put("noDispatchCount",resultMap.get("alarmCount").intValue() - (resultMap.get("finishCount").intValue()+resultMap.get("repairCount").intValue()));
        //趋势图  告警数分组
        List<Map> alarmTrend = statisticsMapper.findAlarmTrend(deviceAlarmDto);
        //趋势图  派单数分组
        List<Map<String,Long>> dispatchTrend = flowRunMapper.dispatchTrend(deviceAlarmDto);
        //趋势图  完成数分组
        List<Map<String,Long>> doneTrent = flowRunMapper.doneTrent(deviceAlarmDto);
        //趋势图  修复中分组
        List<Map<String,Long>> repairTrent = flowRunMapper.repairTrent(deviceAlarmDto);
        //趋势图  未派单中分组
        List<Map<String,BigDecimal>> noDispatchTrent = flowRunMapper.noDispatchTrent(deviceAlarmDto);
        
        if(deviceAlarmDto.getTimeUnit().equals("day")){
            //告警趋势图Map
            Map<String,Long> alarmTrendMapDay = new LinkedHashMap<>();
            for (int i = 0; i < dateList.size(); i++) {
                alarmTrendMapDay.put(dateList.get(i)+"日",0L);
            }
            for (int i = 0; i < alarmTrend.size(); i++) {
                alarmTrendMapDay.put(alarmTrend.get(i).get("date")+"",(Long)alarmTrend.get(i).get("alarmCount"));
            }
            //派单趋势图Map
            Map<String,Long> dispatchTrendMapDay = new LinkedHashMap<>();
            for (int i = 0; i < dateList.size(); i++) {
                dispatchTrendMapDay.put(dateList.get(i)+"日",0L);
            }
            for (int i = 0; i < dispatchTrend.size(); i++) {
                dispatchTrendMapDay.put(dispatchTrend.get(i).get("date")+"", dispatchTrend.get(i).get("dispatchCount"));
            }

            //完成趋势图Map
            Map<String,Long> doneTrendMapDay = new LinkedHashMap<>();
            for (int i = 0; i < dateList.size(); i++) {
                doneTrendMapDay.put(dateList.get(i)+"日",0L);
            }
            for (int i = 0; i < doneTrent.size(); i++) {
                doneTrendMapDay.put(doneTrent.get(i).get("date")+"", doneTrent.get(i).get("doneCount"));
            }
            //修复趋势图Map
            Map<String,Long> repairTrendMapDay = new LinkedHashMap<>();
            for (int i = 0; i < dateList.size(); i++) {
                repairTrendMapDay.put(dateList.get(i)+"日",0L);
            }
            for (int i = 0; i < repairTrent.size(); i++) {
                repairTrendMapDay.put(repairTrent.get(i).get("date")+"", repairTrent.get(i).get("repairCount"));
            }

            //未派单趋势图Map
            Map<String,Long> noDispatchTrendMapDay = new LinkedHashMap<>();
            for (int i = 0; i < dateList.size(); i++) {
                noDispatchTrendMapDay.put(dateList.get(i)+"日",0L);
            }
            for (int i = 0; i < noDispatchTrent.size(); i++) {
                noDispatchTrendMapDay.put(noDispatchTrent.get(i).get("date")+"",noDispatchTrent.get(i).get("noDispatch").longValue());
            }
            return new FlowMaintenanceTrendVo(resultMap,alarmTrendMapDay,dispatchTrendMapDay,doneTrendMapDay,repairTrendMapDay,noDispatchTrendMapDay);
        }
        //返回日期之间所有的月份
            List<String> monthList  = DateUtils.getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
        //告警趋势图Map
        Map<String, Long> alarmTrendMapMonth = new LinkedHashMap<>();
        for (int i = 0; i < monthList.size(); i++) {
            alarmTrendMapMonth.put(monthList.get(i) + "月", 0L);
        }
        for (int i = 0; i < alarmTrend.size(); i++) {
            alarmTrendMapMonth.put(alarmTrend.get(i).get("date") + "", (Long)alarmTrend.get(i).get("alarmCount"));
        }

        //派单趋势图Map
        Map<String, Long> dispatchTrendMapMonth = new LinkedHashMap<>();
        for (int i = 0; i < monthList.size(); i++) {
            dispatchTrendMapMonth.put(monthList.get(i) + "月", 0L);
        }
        for (int i = 0; i < dispatchTrend.size(); i++) {
            dispatchTrendMapMonth.put(dispatchTrend.get(i).get("date") + "", dispatchTrend.get(i).get("dispatchCount").longValue());
        }

        //完成趋势图Map
        Map<String, Long> doneTrendMapMonth = new LinkedHashMap<>();
        for (int i = 0; i < monthList.size(); i++) {
            doneTrendMapMonth.put(monthList.get(i) + "月", 0L);
        }
        for (int i = 0; i < doneTrent.size(); i++) {
            doneTrendMapMonth.put(doneTrent.get(i).get("date") + "", doneTrent.get(i).get("doneCount"));
        }
        //修复趋势图Map
        Map<String, Long> repairTrendMapMonth = new LinkedHashMap<>();
        for (int i = 0; i < monthList.size(); i++) {
            repairTrendMapMonth.put(monthList.get(i) + "月", 0L);
        }
        for (int i = 0; i < repairTrent.size(); i++) {
            repairTrendMapMonth.put(repairTrent.get(i).get("date") + "",repairTrent.get(i).get("repairCount"));
        }

        //未派单趋势图Map
        Map<String, Long> noDispatchTrendMapMonth = new LinkedHashMap<>();
        for (int i = 0; i < monthList.size(); i++) {
            noDispatchTrendMapMonth.put(monthList.get(i) + "月", 0L);
        }
        for (int i = 0; i < noDispatchTrent.size(); i++) {
            noDispatchTrendMapMonth.put(noDispatchTrent.get(i).get("date") + "", noDispatchTrent.get(i).get("noDispatch").longValue());
        }
        return new FlowMaintenanceTrendVo(resultMap,alarmTrendMapMonth,dispatchTrendMapMonth,doneTrendMapMonth,repairTrendMapMonth,noDispatchTrendMapMonth);
    }

    /**
     *维护类型分析
     */
    public List<Object> maintainTypeTrend(DeviceAlarmDto deviceAlarmDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无区域设备");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getStartTime());
            endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deviceAlarmDto.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //动态构建日期
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = DateUtils.getDayBetweenDates(formatter.format(startTime), formatter.format(endTime));
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        //每个告警类型有哪些告警数
        List<Map<String,Long>> countType = statisticsMapper.findAlarmCountByType(deviceAlarmDto);
        //每个告警类型的派单数和完成数
        //List<Map<String,Long>> dispatchAndDone = flowRunMapper.dispatchAndDoneByType(deviceAlarmDto);
        ArrayList<Object> result = new ArrayList<>();
        result.add(countType);
      //  result.add(dispatchAndDone);
        return result;
    }


    /**
     * 维护率统计
     * @param deviceAlarmDto
     * @return
     */
    public MaintenanceRateStatisticsVo maintenanceRateStatistics(DeviceAlarmDto deviceAlarmDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无数据");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }

        MaintenanceRateStatisticsVo maintenanceRateStatisticsVo = new MaintenanceRateStatisticsVo();
        //告警总数
        Long alarmTotal = statisticsMapper.findAlarmTotal(deviceAlarmDto);
        //维修完成总数
        Long finishedCount = flowRunMapper.finishedCount(deviceAlarmDto);
        //故障派单数
        Long faultDispatchCount = flowRunMapper.faultDispatchCount(deviceAlarmDto);
        //设置告警总数
        maintenanceRateStatisticsVo.setAlarmCount(alarmTotal);
        //设置维修完成数
        maintenanceRateStatisticsVo.setFinishedCount(finishedCount);
        //设置故障派单数
        maintenanceRateStatisticsVo.setDispatchCount(faultDispatchCount);
        //月度告警明细
        List<Map> monthlyAlarmDetail = statisticsMapper.monthlyAlarmDetail(deviceAlarmDto);
        //月度派单明细
        List<Map> monthlyDispatchDetail = flowRunMapper.monthlyDispatchDetail(deviceAlarmDto);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        String year = String.valueOf(instance.get(Calendar.YEAR));
        List<String> monthList;
        if(Integer.parseInt(deviceAlarmDto.getYear()) < Integer.parseInt(year)){
            monthList = MonthUtils.getMonthBetween(deviceAlarmDto.getStartTime(),deviceAlarmDto.getEndTime());
        }else {
            monthList = MonthUtils.getMonthBetween(deviceAlarmDto.getStartTime(),format.format(new Date()));
        }
        List<Map> monthlyRepairDetail = flowRunMapper.monthlyRepairDetail(deviceAlarmDto);
        maintenanceRateStatisticsVo.setMonthlyDispatchDetail(getMonthlyDispatchResult(monthList,monthlyAlarmDetail,monthlyDispatchDetail));
        //月度修复明细
        maintenanceRateStatisticsVo.setMonthlyRepairDetail(getMonthlyRepairResult(monthList,monthlyDispatchDetail,monthlyRepairDetail));
        //区域或项目派单率排名
        List<Map> regionDispatchRateTop = flowRunMapper.regionDispatchRateTop(deviceAlarmDto);
        maintenanceRateStatisticsVo.setRegionDispatchRateTop(regionDispatchRateTop);
        //区域或项目修复率
        List<Map> regionRepairRate = flowRunMapper.regionRepairRate(deviceAlarmDto);
        maintenanceRateStatisticsVo.setRegionRepairRateTop(regionRepairRate);
        return maintenanceRateStatisticsVo;
    }

    //时长转换
    private String formatDateTime(String time) {
        if (time == null) {
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
     * 维护统计总表
     * @param deviceAlarmDto
     * @return
     */
    public List<MaintenanceStatisticsVo> maintenanceStatisticsSummary(DeviceAlarmDto deviceAlarmDto) {
        List<MaintenanceStatisticsVo> allProject = new ArrayList<>();
        List<MaintenanceStatisticsVo> singleProject = new ArrayList<>();
        List<MaintenanceStatisticsVo> result = new ArrayList<>();
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无数据");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            projectId = queryProjectId;
        }
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
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
        List<String> list = new ArrayList<>();
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
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        deviceAlarmDto.setTableNames(tables);
        List<Project> allList;
        // 获取项目信息
        if(deviceAlarmDto.getRegionCode() == null || "".equals(deviceAlarmDto.getRegionCode())){
            Project entity = new Project();
            entity.setProjectId(projectId);
            entity.setTenantCode(tenantCode);
            allList  = projectMapper.findListProject(entity);
        }else {
            allList = projectMapper.findProjectByRegionCode(deviceAlarmDto);
            if(allList.size() == 0){
                return null;
            }
        }
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df=new DecimalFormat("0.0000");
        Double boxOnlineRate=0.00d;
        Double cameraOnlineRate=0.00d;
        Double boxAlarmRate=0.00d;
        Double cameraAlarmRate=0.00d;
        Double boxRepairRate=0.00d;
        Double camRepairRate=0.00d;
        // 报障箱在线数量
        int boxOnLineBoxCounts = 0 ,camOnlineCounts = 0 ,wrongBoxCount = 0,wrongCamCount = 0,boxCount = 0,camCount =0;
        //设备箱平均修复时长
        String boxRepairDuration = "";
        //摄像机平均修复时长
        String camRepairDuration = "";
        if(deviceAlarmDto.getEndTime().substring(0,10).equals(deviceAlarmDto.getStartTime().substring(0,10))){//当前的
            //设备箱总数
            boxCount = deviceMapper.getDeviceCount(DeviceKindType.BOX.getType(),regionCode,deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId(),null).intValue();
            //摄像机总数
            camCount = deviceMapper.getDeviceCount(DeviceKindType.CAMERA.getType(),regionCode,deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId(),null).intValue();
            //当前的在线设备箱数量
            boxOnLineBoxCounts = networkMapper.findOnLineBoxCounts(regionCode, DeviceKindType.BOX.getType(), deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            if(boxOnLineBoxCounts > 0 && boxCount > 0){
                //设备箱在线率
                boxOnlineRate = (Double.parseDouble(df.format((float)boxOnLineBoxCounts/boxCount))*100);
            }else{
                boxOnlineRate= 0d;
            }
            //当前的在线摄像头数量
            camOnlineCounts = networkMapper.findOnLineBoxCounts(regionCode, DeviceKindType.CAMERA.getType(), deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            if(camOnlineCounts > 0 && camCount > 0){
                //摄像机在线率
                cameraOnlineRate = (Double.parseDouble(df.format((float)camOnlineCounts/camCount))*100);
            }else{
                cameraOnlineRate = 0d ;
            }
            //当前的故障的报障箱
            wrongBoxCount = eventAlarmMapper.currentWrongBoxCount(regionCode, DeviceKindType.BOX.getType(), deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            if(wrongBoxCount > 0 && boxCount > 0){
                boxAlarmRate = (Double.parseDouble(df.format((float)wrongBoxCount/boxCount))*100);
            }else{
                boxAlarmRate = 0d;
            }
            //当前故障的摄像机设备数
            wrongCamCount = eventAlarmMapper.currentWrongBoxCount(regionCode, DeviceKindType.CAMERA.getType(), deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            if(wrongCamCount > 0 && camCount > 0){
                cameraAlarmRate = (Double.parseDouble(df.format((float)wrongCamCount/camCount))*100);
            }else{
                cameraAlarmRate = 0d;
            }
            //设备箱修复率
            boxRepairRate = flowRunMapper.getRepairRate(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            //图像修复率
            camRepairRate = flowRunMapper.getRepairRate(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.CAMERA.getType(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            //当前的设备箱平均修复时长
            Number boxRepairDuration1 = flowRunMapper.boxRepairDuration(regionCode,DeviceKindType.BOX.getType(),deviceAlarmDto.getStartTime(),deviceAlarmDto.getEndTime(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            boxRepairDuration = boxRepairDuration1 == null ? "" : formatSeconds(boxRepairDuration1.longValue());
            //当前的摄像机平均修复时长
            Number camRepairDuration1 = flowRunMapper.boxRepairDuration(regionCode,DeviceKindType.CAMERA.getType(),deviceAlarmDto.getStartTime(),deviceAlarmDto.getEndTime(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            camRepairDuration = camRepairDuration1 == null ? "" : formatSeconds(camRepairDuration1.longValue());
        }else { //历史的
            Map deviceCountMap = onlineStatisticsMapper.getCount(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            //设备箱总数
            if (deviceCountMap != null) {
                boxCount = Integer.parseInt(deviceCountMap.get("count").toString());
                //设备箱在线数
                boxOnLineBoxCounts = Integer.parseInt(deviceCountMap.get("onlineNum").toString());
            }
            Map camCountMap = onlineStatisticsMapper.getCount(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.CAMERA.getType(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            //摄像机总数
            if (camCountMap!=null) {
                camCount = Integer.parseInt(camCountMap.get("count").toString());
                //摄像机在线数
                camOnlineCounts = Integer.parseInt(camCountMap.get("onlineNum").toString());
            }
            //设备箱故障数
            wrongBoxCount = (int) statisticsMapper.getWrongCount(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            //摄像机故障数
            wrongCamCount = camCount-camOnlineCounts;
            // 在线率计算(报障箱)
            if(boxCount > 0 && boxOnLineBoxCounts > 0){
                boxOnlineRate = (Double.parseDouble(df.format((float)boxOnLineBoxCounts/boxCount))*100);
            }else{
                boxOnlineRate = 0d;
            }
            //设备箱故障率
            if(boxCount > 0 && wrongBoxCount > 0){
                boxAlarmRate = (Double.parseDouble(df.format((float)wrongBoxCount/boxCount))*100);
            }else{
                boxAlarmRate = 0d;
            }
            //摄像机在线率
            if(camCount > 0 && camOnlineCounts > 0){
                cameraOnlineRate = (Double.parseDouble(df.format((float)camOnlineCounts/camCount))*100);
            }else{
                cameraOnlineRate = 0d;
            }
            //摄像机故障率
            if(camCount > 0 && wrongCamCount > 0){
                cameraAlarmRate = (Double.parseDouble(df.format((float)wrongCamCount/camCount))*100);
            }else{
                cameraAlarmRate = 0d;
            }
            //设备箱修复率
            boxRepairRate = flowRunMapper.getRepairRate(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            //图像修复率
            camRepairRate = flowRunMapper.getRepairRate(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.CAMERA.getType(),deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            //设备箱故障平均修复时长
            Map<String,Number> boxRepairTime = flowRunMapper.getRepairTime(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), DeviceKindType.BOX.getType(),regionCode,deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            // 图像故障平均修复时长
            Map<String,Number> cameraRepairTime= flowRunMapper.getRepairTime(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), DeviceKindType.CAMERA.getType(),regionCode,deviceAlarmDto.getTenantCode(), deviceAlarmDto.getProjectId());
            if(boxRepairTime != null){
                boxRepairDuration = formatSeconds(boxRepairTime.get("ofTime").longValue());
            }
            if(cameraRepairTime != null){
                camRepairDuration = formatSeconds(cameraRepairTime.get("ofTime").longValue());
            }
        }
        //用电量统计
        UnitElectricStatisticsVo  unitElectricStatisticsVo = electricStatisticsMapper.getElectricByTime(regionCode, projectId, tenantCode, deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
        //故障数统计
        Long alarmCount  = statisticsMapper.findAlarmTotal(deviceAlarmDto);
        //故障类型统计
        List<AlarmTypeStatisticsVo> faultType = statisticsMapper.getFaultTypeTop(deviceAlarmDto);
        MaintenanceStatisticsVo maintenanceStatisticsVo = new MaintenanceStatisticsVo();
        maintenanceStatisticsVo.setProjectName("全部项目");/*
        ResponseBean<Tenant> tenantData = userServiceClient.findTenantByTenantCode(tenantCode == null ? "aswl" : tenantCode);
        Tenant tenant = tenantData.getData();
        maintenanceStatisticsVo.setMaintenanceCompany(tenant.getTenantName());
        maintenanceStatisticsVo.setMaintenanceCompanyLinkMan(tenant.getOwnerName());
        maintenanceStatisticsVo.setMaintenanceCompanyPhoneNo(tenant.getOwnerMobile());
        maintenanceStatisticsVo.setMaintenanceCompanyEmail(tenant.getNotifyEmail());*/
        maintenanceStatisticsVo.setStatisticsTime(deviceAlarmDto.getStartTime().substring(0,10)+"到"+deviceAlarmDto.getEndTime().substring(0,10));
        maintenanceStatisticsVo.setDeviceBoxCount((long) boxCount);
        maintenanceStatisticsVo.setCamCount((long) camCount);
        maintenanceStatisticsVo.setDeviceBoxOnlineRate(boxOnlineRate);
        maintenanceStatisticsVo.setCamCountOnlineRate(cameraOnlineRate);
        maintenanceStatisticsVo.setDeviceBoxWrongRate(boxAlarmRate);
        maintenanceStatisticsVo.setCamCountWrongRate(cameraAlarmRate);
        maintenanceStatisticsVo.setDeviceBoxRepairDuration(boxRepairDuration);
        maintenanceStatisticsVo.setCamRepairDuration(camRepairDuration);
        maintenanceStatisticsVo.setBoxRepairRate(boxRepairRate);
        maintenanceStatisticsVo.setCamRepairRate(camRepairRate);
        if (unitElectricStatisticsVo != null) {
            maintenanceStatisticsVo.setElectricNum(unitElectricStatisticsVo.getElectricNum());
            maintenanceStatisticsVo.setElectricFee(String.format("%.2f",unitElectricStatisticsVo.getElectricNum()*unitElectricStatisticsVo.getElectricPrice()));
        }
        maintenanceStatisticsVo.setAlarmCount(alarmCount);
        maintenanceStatisticsVo.setAlarmType(faultType);
        //全部项目
        allProject.add(maintenanceStatisticsVo);
        //单个项目
        int singleBoxCount =0; //设备箱数量
        int singleCamCount = 0; //摄像机数量
        int singleBoxOnLineCounts = 0 ;//在线设备箱数量
        int singleCamOnlineCounts = 0; //在线摄像机数量
        int singleWrongBoxCount = 0; //故障设备箱数量
        int singleWrongCamCount = 0; // 故障摄像机数量
        Double singleBoxOnlineRate =0d;// 设备箱在线率
        Double singleCameraOnlineRate = 0d; //摄像机在线率
        Double singleBoxAlarmRate=0d;//设备箱故障率
        Double  singleCameraAlarmRate=0d ; //摄像机故障率
        Double singleBoxRepairRate=0d;//设备箱修复率
        Double  singleCamRepairRate=0d ; //摄像机修复率
        String singleBoxRepairDuration = null; // 设备箱故障平均修复时长
        String  singCamRepairDuration = null ;//  	图像故障平均修复时长
        for (int i = 0; i <allList.size() ; i++) {
            String singleProjectId = allList.get(i).getProjectId();
            //设备箱总数
            if(sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))){//当前的
                //设备箱总数
                singleBoxCount = deviceMapper.getDeviceCount(DeviceKindType.BOX.getType(),regionCode,deviceAlarmDto.getTenantCode(), singleProjectId,null).intValue();
                //摄像机总数
                 singleCamCount = deviceMapper.getDeviceCount(DeviceKindType.CAMERA.getType(),regionCode,deviceAlarmDto.getTenantCode(), singleProjectId,null).intValue();
                //当前的在线设备箱数量
                 singleBoxOnLineCounts = networkMapper.findOnLineBoxCounts(regionCode, DeviceKindType.BOX.getType(), deviceAlarmDto.getTenantCode(), singleProjectId);
                if(singleBoxOnLineCounts > 0 && singleBoxCount > 0){
                    //设备箱在线率
                    singleBoxOnlineRate = (Double.parseDouble(df.format((float)singleBoxOnLineCounts/singleBoxCount))*100);
                }else{
                    singleBoxOnlineRate = 0d;
                }
                //当前的在线摄像头数量
                 singleCamOnlineCounts = networkMapper.findOnLineBoxCounts(regionCode, DeviceKindType.CAMERA.getType(), deviceAlarmDto.getTenantCode(), singleProjectId);
                if(singleCamOnlineCounts > 0 && singleCamCount > 0){
                    //摄像机在线率
                     singleCameraOnlineRate = (Double.parseDouble(df.format((float)singleCamOnlineCounts/singleCamCount))*100);
                }else{
                    singleCameraOnlineRate = 0d;
                }
                //当前的故障的报障箱
                  singleWrongBoxCount = eventAlarmMapper.currentWrongBoxCount(regionCode, DeviceKindType.BOX.getType(), deviceAlarmDto.getTenantCode(), singleProjectId);
                if(singleWrongBoxCount > 0 && singleBoxCount > 0){
                    singleBoxAlarmRate = (Double.parseDouble(df.format((float)singleWrongBoxCount/singleBoxCount))*100);
                }else{
                    singleBoxAlarmRate = 0d;
                }
                //当前故障的摄像机设备数
                 singleWrongCamCount = eventAlarmMapper.currentWrongBoxCount(regionCode, DeviceKindType.CAMERA.getType(), deviceAlarmDto.getTenantCode(), singleProjectId);
                if(singleWrongCamCount > 0 && singleCamCount > 0){ 
                    singleCameraAlarmRate = (Double.parseDouble(df.format((float)singleWrongCamCount/singleCamCount))*100);
                }else{
                    singleCameraAlarmRate = 0d;
                }
                //设备箱修复率
                singleBoxRepairRate = flowRunMapper.getRepairRate(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(), singleProjectId);
                //图像修复率
                singleCamRepairRate = flowRunMapper.getRepairRate(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.CAMERA.getType(),deviceAlarmDto.getTenantCode(), singleProjectId);
                //当前的设备箱平均修复时长
                Number boxRepairDuration1 = flowRunMapper.boxRepairDuration(regionCode,DeviceKindType.BOX.getType(),deviceAlarmDto.getStartTime(),deviceAlarmDto.getEndTime(),deviceAlarmDto.getTenantCode(), singleProjectId);
                singleBoxRepairDuration = boxRepairDuration1 == null ? "" : formatSeconds(boxRepairDuration1.longValue());
                //当前的摄像机平均修复时长
                Number camRepairDuration1 = flowRunMapper.boxRepairDuration(regionCode,DeviceKindType.CAMERA.getType(),deviceAlarmDto.getStartTime(),deviceAlarmDto.getEndTime(),deviceAlarmDto.getTenantCode(), singleProjectId);
                singCamRepairDuration = camRepairDuration1 == null ? "" : formatSeconds(camRepairDuration1.longValue());
            }else { //历史的
                Map deviceCountMap = onlineStatisticsMapper.getCount(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(), singleProjectId);
                if (deviceCountMap != null) {
                    //设备箱总数
                    singleBoxCount = Integer.parseInt(deviceCountMap.get("count").toString());
                    //设备箱在线数
                    singleBoxOnLineCounts = Integer.parseInt(deviceCountMap.get("onlineNum").toString());
                }
                Map camCountMap = onlineStatisticsMapper.getCount(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.CAMERA.getType(),deviceAlarmDto.getTenantCode(), singleProjectId);
                if (camCountMap!=null) {
                    //摄像机总数
                    singleCamCount = Integer.parseInt(camCountMap.get("count").toString());
                    //摄像机在线数
                    singleCamOnlineCounts = Integer.parseInt(camCountMap.get("onlineNum").toString());
                }
                //设备箱故障数
                 singleWrongBoxCount = (int) statisticsMapper.getWrongCount(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(), singleProjectId);
                //摄像机故障数
                singleWrongCamCount = singleCamCount-singleCamOnlineCounts;
                
                // 在线率计算(报障箱)
                if(singleBoxCount > 0 && singleBoxOnLineCounts > 0){
                    singleBoxOnlineRate = (Double.parseDouble(df.format((float)singleBoxOnLineCounts/singleBoxCount))*100);
                }else{
                    singleBoxOnlineRate= 0d;
                }
                //设备箱故障率
                if(singleBoxCount > 0 && singleWrongBoxCount > 0){
                    singleBoxAlarmRate = (Double.parseDouble(df.format((float)singleWrongCamCount/singleBoxCount))*100);
                }else{
                    singleBoxAlarmRate = 0d;
                }
                //摄像机在线率
                if(singleCamCount > 0 && singleCamOnlineCounts > 0){
                    singleCameraOnlineRate = (Double.parseDouble(df.format((float)singleCamOnlineCounts/singleCamCount))*100);
                }else{
                    singleCameraOnlineRate = 0d;
                }
                //摄像机故障率
                if(singleCamCount > 0 && singleWrongCamCount > 0){
                    singleCameraAlarmRate = (Double.parseDouble(df.format((float)singleWrongCamCount/singleCamCount))*100);
                }else{
                    singleCameraAlarmRate = 0d;
                }
                //设备箱修复率
                singleBoxRepairRate = flowRunMapper.getRepairRate(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(), singleProjectId);
                //图像修复率
                singleCamRepairRate = flowRunMapper.getRepairRate(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), regionCode, DeviceKindType.CAMERA.getType(),deviceAlarmDto.getTenantCode(), singleProjectId);
                //设备箱故障平均修复时长
                Map<String,Number> boxRepairTime = flowRunMapper.getRepairTime(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), DeviceKindType.BOX.getType(),regionCode,deviceAlarmDto.getTenantCode(), singleProjectId);
                // 图像故障平均修复时长
                Map<String,Number> cameraRepairTime= flowRunMapper.getRepairTime(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime(), DeviceKindType.CAMERA.getType(),regionCode,deviceAlarmDto.getTenantCode(), singleProjectId);
                singleBoxRepairDuration = boxRepairTime == null ? "" : formatSeconds(boxRepairTime.get("ofTime").longValue());
                singCamRepairDuration = cameraRepairTime == null ? "" : formatSeconds(boxRepairTime.get("ofTime").longValue());
            }
            //用电量统计
            UnitElectricStatisticsVo  unitElectricStatisticsVo1 = electricStatisticsMapper.getElectricByTime(regionCode, singleProjectId, tenantCode, deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
            //故障数统计
            deviceAlarmDto.setProjectId(singleProjectId);
            Long alarmCount1  = statisticsMapper.findAlarmTotal(deviceAlarmDto);
            //故障类型统计
            List<AlarmTypeStatisticsVo> faultType1= statisticsMapper.getFaultTypeTop(deviceAlarmDto);
            MaintenanceStatisticsVo maintenanceStatisticsVo1 = new MaintenanceStatisticsVo();
            Project project = allList.get(i);
            maintenanceStatisticsVo1.setProjectName(project.getProjectName());
            maintenanceStatisticsVo1.setUseCompany(project.getProjectOwner());
            maintenanceStatisticsVo1.setUseCompanyLinkMan(project.getContacts());
            maintenanceStatisticsVo1.setUseCompanyPhoneNo(project.getTelephone());
            maintenanceStatisticsVo1.setUseCompanyEmail(project.getEmail());
           /* ResponseBean<Tenant> tenantData1 = userServiceClient.findTenantByTenantCode(tenantCode == null ? "aswl" : tenantCode);
            Tenant tenant1 = tenantData1.getData();*/
            maintenanceStatisticsVo1.setMaintenanceCompany(project.getMaintainDeptName());
            maintenanceStatisticsVo1.setMaintenanceCompanyLinkMan(project.getMaintainDeptLeader());
            maintenanceStatisticsVo1.setMaintenanceCompanyPhoneNo(project.getMaintainDeptPhone());
           // maintenanceStatisticsVo1.setMaintenanceCompanyEmail(tenant1.getNotifyEmail());
            maintenanceStatisticsVo1.setStatisticsTime(deviceAlarmDto.getStartTime().substring(0,10)+"到"+deviceAlarmDto.getEndTime().substring(0,10));
            maintenanceStatisticsVo1.setDeviceBoxCount((long) singleBoxCount);
            maintenanceStatisticsVo1.setCamCount((long) singleCamCount);
            maintenanceStatisticsVo1.setDeviceBoxOnlineRate(singleBoxOnlineRate);
            maintenanceStatisticsVo1.setCamCountOnlineRate(singleCameraOnlineRate);
            maintenanceStatisticsVo1.setDeviceBoxWrongRate(singleBoxAlarmRate);
            maintenanceStatisticsVo1.setCamCountWrongRate(singleCameraAlarmRate);
            maintenanceStatisticsVo1.setBoxRepairRate(singleBoxRepairRate);
            maintenanceStatisticsVo.setCamRepairRate(singleCamRepairRate);
            maintenanceStatisticsVo1.setDeviceBoxRepairDuration(singleBoxRepairDuration);
            maintenanceStatisticsVo1.setCamRepairDuration(singCamRepairDuration);
            if (unitElectricStatisticsVo1 != null) {
                maintenanceStatisticsVo1.setElectricNum(unitElectricStatisticsVo1.getElectricNum());
                maintenanceStatisticsVo1.setElectricFee(String.format("%.2f",unitElectricStatisticsVo1.getElectricNum()*unitElectricStatisticsVo1.getElectricPrice()));
            }
            maintenanceStatisticsVo1.setAlarmCount(alarmCount1);
            maintenanceStatisticsVo1.setAlarmType(faultType1);
            singleProject.add(maintenanceStatisticsVo1);
   }
        if (StringUtils.isEmpty(queryProjectId)) {
            result.addAll(allProject);
        }
        result.addAll(singleProject);
        return result;
    }

    /**
     * 导出统计总表
     * @return
     */
    public ResponseEntity<byte[]> exportExcel(HttpServletResponse response,MaintenanceStatisticsDto maintenanceStatisticsDto) throws Exception {

        String[] exportData= getExportData();
        String report=exportData[0];
        String reportCopy=exportData[1];
        String printType=exportData[2]; //默认打印第一种

        //获取文件路径
        String path;
        if("1".equals(printType))
        {
            //第一种表格
            path = "reporttemplate1.xlsx";
        }
        else
        {
            //第二种表格
            path = "reporttemplate2.xlsx";
        }

        //更改文件名编码
        String fileName = "故障维护统计总表.xlsx";
        String gFileName = URLEncoder.encode(fileName, "UTF-8");
        InputStream in = null;
        Workbook exl;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            in =getClass().getClassLoader().getResourceAsStream("temp/"+path);

            exl = WorkbookFactory.create(in);

            if("1".equals(printType))
            {
                exportExcelSheet1(exl, maintenanceStatisticsDto);
                exportExcelSheet2(exl,maintenanceStatisticsDto);
            }
            else
            {
                exportExcelSheet3(exl,maintenanceStatisticsDto,report,reportCopy);
            }

            exl.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=\"" + gFileName + "\"");
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload")).body(out.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
//                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 导出第一张表
     * @param exl
     * @param maintenanceStatisticsDto
     */
    private void exportExcelSheet1(Workbook exl, MaintenanceStatisticsDto maintenanceStatisticsDto) {
        //获取第一个sheet
        Sheet sheet1 = exl.getSheetAt(0);
        DeviceAlarmDto deviceAlarmDto = new DeviceAlarmDto();
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无数据");
                    return;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setStartTime(maintenanceStatisticsDto.getStartTime());
        deviceAlarmDto.setEndTime(maintenanceStatisticsDto.getEndTime());
        List<DeviceFaultVo> list = flowRunMapper.getFlowRunData(deviceAlarmDto);
        for (int i = 0, y = list.size(); i < y; ++i) {
            Row row = sheet1.createRow(i + 2);
            row.createCell(0).setCellValue(i + 1);  //序号
            row.createCell(1).setCellValue(list.get(i).getRegionName().trim());     //区域
            row.createCell(2).setCellValue(list.get(i).getDeviceName().trim());      //设备名称
            row.createCell(3).setCellValue(list.get(i).getIp().trim()); //ip
            row.createCell(4).setCellValue(list.get(i).getAlarmTypeName());  //故障类型
            row.createCell(5).setCellValue(list.get(i).getAlarmTime());                //故障时间
            row.createCell(6).setCellValue(list.get(i).getRepairTime());                //修复时间
            row.createCell(7).setCellValue(SecurityConstant.DEFAULT_TENANT_CODE);                //维护公司
            row.createCell(8).setCellValue(list.get(i).getRepairer());                //维护人
        }
    }

    /**
     * 导出第二张表
     * @param exl
     * @param maintenanceStatisticsDto
     */
    private void exportExcelSheet2(Workbook exl, MaintenanceStatisticsDto maintenanceStatisticsDto) {
        DecimalFormat df=new DecimalFormat("0.0");
        Sheet sheet2 = exl.getSheetAt(1);
        Row row1 = sheet2.getRow(0);
        Row row2 = sheet2.getRow(1);
        Row row3 = sheet2.getRow(2);
        Row row4 = sheet2.getRow(3);
        Row row5 = sheet2.getRow(4);
        Row row6 = sheet2.getRow(5);
        Row row7 = sheet2.getRow(6);
        Row row8 = sheet2.getRow(7);
        Row row9 = sheet2.getRow(8);
        Row row10 = sheet2.getRow(9);
        Row row11 = sheet2.getRow(10);
        Row row12 = sheet2.getRow(11);
        // 项目名称
        row2.getCell(1).setCellValue(maintenanceStatisticsDto.getProjectName());
        //使用单位
        row3.getCell(1).setCellValue(maintenanceStatisticsDto.getUseCompany());
        //联系人
        row3.getCell(3).setCellValue(maintenanceStatisticsDto.getUseCompanyLinkMan());
        //电话
        row3.getCell(5).setCellValue(maintenanceStatisticsDto.getUseCompanyPhoneNo());
        //邮箱
        row3.getCell(7).setCellValue(maintenanceStatisticsDto.getUseCompanyEmail());
        //维护单位
        row4.getCell(1).setCellValue(maintenanceStatisticsDto.getMaintenanceCompany());
        // 联系人
        row4.getCell(3).setCellValue(maintenanceStatisticsDto.getMaintenanceCompanyLinkMan());
        //电话
        row4.getCell(5).setCellValue(maintenanceStatisticsDto.getMaintenanceCompanyPhoneNo());
        // 邮箱
        row4.getCell(7).setCellValue(maintenanceStatisticsDto.getMaintenanceCompanyEmail());
        //统计时间
        row5.getCell(1).setCellValue(maintenanceStatisticsDto.getStatisticsTime());
        //设备箱总数量
        row6.getCell(1).setCellValue(maintenanceStatisticsDto.getDeviceBoxCount());
        //摄像机总数量
        row6.getCell(4).setCellValue(maintenanceStatisticsDto.getCamCount());
        //设备箱在线率
        row7.getCell(1).setCellValue(df.format(maintenanceStatisticsDto.getDeviceBoxOnlineRate())+"%");
        //图像在线率
        row7.getCell(4).setCellValue(df.format(maintenanceStatisticsDto.getCamCountOnlineRate())+"%");
        // 设备箱故障率
        row8.getCell(1).setCellValue(df.format(maintenanceStatisticsDto.getDeviceBoxWrongRate())+"%");
        //图像故障率
        row8.getCell(4).setCellValue(df.format(maintenanceStatisticsDto.getCamCountWrongRate())+"%");
        //设备箱修复率
        row10.getCell(1).setCellValue(df.format(maintenanceStatisticsDto.getBoxRepairRate())+"%");
        //摄像机修复率
        row10.getCell(4).setCellValue(df.format(maintenanceStatisticsDto.getCamRepairRate())+"%");
        // 设备箱故障平均修复时长
        row9.getCell(1).setCellValue(maintenanceStatisticsDto.getDeviceBoxRepairDuration());
        //图像故障平均修复时长
        row9.getCell(4).setCellValue(maintenanceStatisticsDto.getCamRepairDuration());
        //电量
        row11.getCell(1).setCellValue((maintenanceStatisticsDto.getElectricNum() == null ?0 :maintenanceStatisticsDto.getElectricNum()) + "Kw·h");
        //电费
        row11.getCell(4).setCellValue((maintenanceStatisticsDto.getElectricFee()== null ? 0: maintenanceStatisticsDto.getElectricFee()) + "元");
        //故障数
        row12.getCell(1).setCellValue(maintenanceStatisticsDto.getAlarmCount());
        //故障类型前5
        row12.getCell(4).setCellValue(maintenanceStatisticsDto.getAlarmType());
    }

    /**
     * 导出第三张表
     * @param exl
     * @param maintenanceStatisticsDto
     */
    private void exportExcelSheet3(Workbook exl, MaintenanceStatisticsDto maintenanceStatisticsDto,String report,String reportCopy) {
        DecimalFormat df=new DecimalFormat("0.0");
        Sheet sheet3 = exl.getSheetAt(0);
        Row row1 = sheet3.getRow(1);

        row1.getCell(0).setCellValue("统计时间:" + maintenanceStatisticsDto.getStatisticsTime()  +
                "项目:" +maintenanceStatisticsDto.getProjectName()  +
                "下载时间:" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

        //报送
        setCellValue(sheet3,2,1,report);
        //签收
//        setCellValue(sheet3,7,6,"已签收");
        //抄送
        setCellValue(sheet3,3,1,reportCopy);
        //签收
//        setCellValue(sheet3,9,6,"已签收");
        //设备箱总数量
        setCellValue(sheet3,4,1,maintenanceStatisticsDto.getDeviceBoxCount());
        //摄像机总数量
        setCellValue(sheet3,4,3,maintenanceStatisticsDto.getCamCount());
        //设备箱在线率
        setCellValue(sheet3,5,1,df.format(maintenanceStatisticsDto.getDeviceBoxOnlineRate())+"%");
        //图像在线率
        setCellValue(sheet3,5,3,df.format(maintenanceStatisticsDto.getCamCountOnlineRate())+"%");
        //设备箱故障率
        setCellValue(sheet3,6,1,df.format(maintenanceStatisticsDto.getDeviceBoxWrongRate())+"%");
        //图像故障率
        setCellValue(sheet3,6,3,df.format(maintenanceStatisticsDto.getCamCountWrongRate())+"%");
        //设备箱修复时长
        setCellValue(sheet3,7,1,maintenanceStatisticsDto.getDeviceBoxRepairDuration());
        //图像修复时长
        setCellValue(sheet3,7,3,maintenanceStatisticsDto.getCamRepairDuration());
    }

    private void setCellValue(Sheet sheet,int rowNum,int cellNum,Object value)
    {
        // 判断对应的类型，然后设置对应的值，重复获取row完全没问题，因为看源码，row的对象是放在treeMap里面的，重复获取可以忽略该时间
        Cell c=sheet.getRow(rowNum).getCell(cellNum);
        // 判断对应数据框的类型，然后设置对应的值，只要设置几个常用的类型就可以了
        if(c==null || value==null) //这里可以写成多加一个判断，如果为空是否需要创建一个Cell
        {
            return;
        }
        if(value instanceof String)
        {
            c.setCellValue((String)value);
        }
        else if(value instanceof Integer)
        {
            c.setCellValue((Integer)value);
        }
        else if(value instanceof Double)
        {
            c.setCellValue((Double)value);
        }
        else if(value instanceof Long)
        {
            c.setCellValue((Long)value);
        }
        else if(value instanceof Short)
        {
            c.setCellValue((Short)value);
        }
        else if(value instanceof Number)
        {
            c.setCellValue(((Number)value).doubleValue());
        }
        else if(value instanceof Date)
        {
            c.setCellValue((Date)value);
        }
        else if(value instanceof Calendar)
        {
            c.setCellValue((Calendar)value);
        }
        else if(value instanceof RichTextString)
        {
            c.setCellValue((RichTextString)value);
        }
        else if(value instanceof Boolean)
        {
            c.setCellValue((Boolean)value);
        }
        else
        {
            c.setCellValue(value.toString());
        }
    }

    /**
     * 转换月度派单率结果集
     */
    private List<Map> getMonthlyDispatchResult(List<String> monthList,List<Map> monthlyAlarmDetail,List<Map> monthlyDispatchDetail){
        List<Map> monthlyDispatchResult = new ArrayList<>();
        for (String s : monthList) {
            Map map = new HashMap();
            map.put("date",s+"月");
            monthlyDispatchResult.add(map);
        }
        for (Map map : monthlyDispatchResult) {
            String date = (String) map.get("date");
            for (Map map1 : monthlyAlarmDetail) {
                String date1 = (String) map1.get("date");
                if(date.equals(date1)){
                    map.put("alarmCount",map1.get("alarmCount"));
                    break;
                }else {
                    map.put("alarmCount",0);
                }
            }
            for (Map map1 : monthlyDispatchDetail) {
                String date1 = (String) map1.get("date");
                if(date.equals(date1)){
                    map.put("dispatchCount",map1.get("dispatchCount"));
                    break;
                }else {
                    map.put("dispatchCount",0);
                }
            }
        }
        return monthlyDispatchResult;
    }


    /**
     * 转换月度修复率结果集
     */
    private List<Map> getMonthlyRepairResult(List<String> monthList,List<Map> monthlyDispatchDetail,List<Map> monthlyRepairDetail){
        List<Map> monthlyRepairResult = new ArrayList<>();
        for (String s : monthList) {
            Map map = new HashMap();
            map.put("date",s+"月");
            monthlyRepairResult.add(map);
        }
        for (Map map : monthlyRepairResult) {
            String date = (String) map.get("date");
            for (Map map1 : monthlyDispatchDetail) {
                String date1 = (String) map1.get("date");
                if(date.equals(date1)){
                    map.put("dispatchCount",map1.get("dispatchCount"));
                    break;
                }else {
                    map.put("dispatchCount",0);
                }
            }
            for (Map map1 : monthlyRepairDetail) {
                String date1 = (String) map1.get("date");
                if(date.equals(date1)){
                    map.put("repairCount",map1.get("repairCount"));
                    break;
                }else {
                    map.put("repairCount",0);
                }
            }
        }
        return monthlyRepairResult;
    }


    /**
     * 总体健康率统计列表 // TODO 添加健康率统计的Excel导出 TODO getHealthyList
     *
     * @param deviceAlarmDto
     * @return
     */
    public ResponseEntity getHealthyListExport(HttpServletResponse response,DeviceAlarmDto deviceAlarmDto) throws Exception {

        // 添加导出Excel的代码
        DeviceAlarmDto old= JSON.parseObject(JSON.toJSON(deviceAlarmDto).toString(),DeviceAlarmDto.class);
        PageInfo<DeviceAlarmVo> healthyListPage = this.getHealthyListPage(PageUtil.pageInfo("1", "100", "", ""), deviceAlarmDto);
        List<DeviceAlarmVo> list =healthyListPage.getList();
        //
        // List<DeviceAlarmVo> list=this.getHealthyList(deviceAlarmDto);

//        String path = "";

//        String p="\\src\\main\\resources\\temp";

        //更改文件名编码
        String fileName = "故障维护统计总表.xlsx";
        String gFileName = URLEncoder.encode(fileName, "UTF-8");
        InputStream in = null;
        Workbook exl;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            in =getClass().getClassLoader().getResourceAsStream("temp/healthRate2.xlsx");

            exl = WorkbookFactory.create(in);

            //写入Excel数据
//                exportExcelSheet3(exl,maintenanceStatisticsDto,report,reportCopy);

            exportExcel(list,old,exl);

            exl.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=\"" + gFileName + "\"");
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload")).body(out.toByteArray());
//            response.getOutputStream().write(out.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
//                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    //添加具体的逻辑
    private void exportExcel(List<DeviceAlarmVo> list,DeviceAlarmDto deviceAlarmDto,Workbook exl)
    {
        Sheet s = exl.getSheetAt(0);
//        Row row1 = sheet3.getRow(1);

        String listName="健康";
        if("1".equals(deviceAlarmDto.getAlarmLevel()))
        {
            listName="故障";
        }

//        row1.getCell(0).setCellValue("统计时间:" + maintenanceStatisticsDto.getStatisticsTime()  +
//                "项目:" +maintenanceStatisticsDto.getProjectName()  +
//                "下载时间:" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));

        //报送
//        setCellValue(sheet3,2,1,report);
//        //签收
////        setCellValue(sheet3,7,6,"已签收");
//        //抄送
//        setCellValue(sheet3,3,1,reportCopy);

        //s.getLastRowNum()
        int newRowCount=9;
        if(list.size()>15)
        {
            newRowCount=list.size();
        }
        int cellCount=9;

        CellStyle[] arr=new CellStyle[11];
        for(int j=cellCount;j>=0;j--)
        {
            arr[j]=s.getRow(8).getCell(j).getCellStyle();
        }

        int start = 9;
        s.shiftRows(start, s.getLastRowNum(), newRowCount,true,false);
        Row r;
        Cell c;
        CellRangeAddress a;
        for(int i=start;i<start+newRowCount;i++)
        {
            r=s.createRow(i);

            // 合并单元格
//            a = new CellRangeAddress(i, i, (short) 1, (short) 2); //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
            a = new CellRangeAddress(i, i, (short) 1, (short) 2);
            s.addMergedRegion(a);
            a = new CellRangeAddress(i, i, (short) 3, (short) 4);
            s.addMergedRegion(a);
            a = new CellRangeAddress(i, i, (short) 6, (short) 9);
            s.addMergedRegion(a);

            for(int j=cellCount;j>=0;j--)
            {
                c=r.createCell(j);
                c.setCellStyle(arr[j]);
            }
        }

        String[] exportData=getExportData();


        // 2 0      统计时间：2020年2月   项目：北京房山区   下载时间：2020/02/26  15:30:23
        String projectName="全部项目";
        String projectId=SysUtil.getProjectId();
        if(!StringUtils.isEmpty(projectId) && projectId.indexOf(",")==-1)
        {
            //查询projectId的东西
            Project p=new Project();
            p.setProjectId(projectId);
            List<Project>pList=projectMapper.findList(p);
            if(pList!=null && pList.size()>0)
            {
                projectName=pList.get(0).getProjectName();
            }
        }

        setCellValue(s,2,0,"统计时间："+deviceAlarmDto.getStartTime().split(" ")[0]+"至"+deviceAlarmDto.getEndTime().split(" ")[0]+"  项目："+projectName+"   下载时间："+new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date())+"\t\t\t\t\t\t\t");

        // 5 0     健康设备列表（数量）                                 概况分数：96
        // 需要获取分数
        deviceAlarmDto.setAlarmLevel(null);
        Map<String,Integer> map= getHealthyRateData(deviceAlarmDto);

        int alarmCount=((Number)map.get("alarmCount")).intValue();
        int deviceCount=((Number)map.get("deviceCount")).intValue();
        int v=100;

        if(deviceCount!=0)
        {
            v=(deviceCount-alarmCount)*100/deviceCount;
            if(v*deviceCount-alarmCount<deviceCount)
            {
                //有小数就+1
                v++;
            }
        }

        setCellValue(s,5,0,"  "+listName+"设备列表("+list.size()+")                           概况分数："+v+"      ");

        // 3 2     报送后台设置生成
        setCellValue(s,3,2,exportData[0]);

        // 4 2     报送后台设置生成
        setCellValue(s,4,2,exportData[1]);

        //TODO 设置列表
        DeviceAlarmVo vo;
        int rowNum=0;
        for(int i=0;i<list.size();i++)
        {
            vo=list.get(i);
            rowNum=start+i-1;
            setCellValue(s,rowNum,0,i+1);
            setCellValue(s,rowNum,1,vo.getDeviceName());
            setCellValue(s,rowNum,3,vo.getIp());
            setCellValue(s,rowNum,5,vo.getRegionName());
            setCellValue(s,rowNum,6,vo.getDeviceModelKind());
        }

        //如果小于rowCount ，就加上对应的数字
        for(int i=list.size();i<=newRowCount;i++)
        {
            rowNum=start+i-1;
            setCellValue(s,rowNum,0,i+1);
        }

//        boderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //报送人
        setCellValue(s,start+newRowCount+5,8,exportData[0]);

        setCellValue(s,start+newRowCount+6,0,"下载人："+SysUtil.getUser()+"  制表：广州傲视物联网信息技术有限公司\t\t\t\t\t\t\t");

    }

    /**
     * 返回一个数组
     * [0] 是报送人
     * [1] 是抄送人
     * [2] 是导出报表的类型
     * @return
     */
    public String[] getExportData()
    {
        String report="";
        String reportCopy="";
        String printType="1"; //默认打印第一种
        Config config=new Config();
//        config.setDelFlag(CommonConstant.DEL_FLAG_NORMAL);
//        config.setTenantCode(SysUtil.getTenantCode());
        ResponseBean<List<Config>> r=userServiceClient.findConfigList(config);
        if(r!=null && ResponseBean.SUCCESS==r.getCode())
        {
            for(Config c:r.getData())
            {
                if(CommonConstant.CONFIG_PARAM_KEY_REPORT_PERSON.equals(c.getParamKey()))
                {
                    report=c.getParamValue();
                }
                if(CommonConstant.CONFIG_PARAM_KEY_REPORT_COPY_PERSON.equals(c.getParamKey()))
                {
                    reportCopy=c.getParamValue();
                }
                if(CommonConstant.CONFIG_PARAM_KEY_REPORT_TOTAL_PRINT_TYPE.equals(c.getParamKey()))
                {
                    printType=c.getParamValue();
                }
            }
        }
        return new String[]{report,reportCopy,printType};
    }

    /**
     * 统计报表------>维护率统计---->派单率和修复率
     * @param deviceAlarmDto
     * @return
     */
    public Map dispatchRepairRate(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无数据");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        Map resultMap = new HashMap();
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        //告警总数
        Long alarmTotal = statisticsMapper.findAlarmTotal(deviceAlarmDto);
        //维修完成总数
        Long finishedCount = flowRunMapper.finishedCount(deviceAlarmDto);
        //故障派单数
        Long faultDispatchCount = flowRunMapper.faultDispatchCount(deviceAlarmDto);
        resultMap.put("alarmTotal",alarmTotal);
        resultMap.put("finishedCount",finishedCount);
        resultMap.put("faultDispatchCount",faultDispatchCount);
        return resultMap;
    }

    /**
     * 统计报表------>维护率统计---->月度派单率和修复率
     * @param deviceAlarmDto
     * @return
     */
    public Map monthlyDispatchRepairRate(DeviceAlarmDto deviceAlarmDto) {

        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    throw new CommonException("当前用户暂无数据");
                }
                regionCode = userRegionCode;
            //  deviceAlarmDto.setRegionCode(userRegionCode);
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        Map resultMap = new HashMap();
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        //月度告警明细
        List<Map> monthlyAlarmDetail = statisticsMapper.monthlyAlarmDetail(deviceAlarmDto);
        //月度派单明细
        List<Map> monthlyDispatchDetail = flowRunMapper.monthlyDispatchDetail(deviceAlarmDto);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        String year = String.valueOf(instance.get(Calendar.YEAR));
        List<String> monthList;
        if(Integer.parseInt(deviceAlarmDto.getYear()) < Integer.parseInt(year)){
            monthList = MonthUtils.getMonthBetween(deviceAlarmDto.getStartTime(),deviceAlarmDto.getEndTime());
        }else {
            monthList = MonthUtils.getMonthBetween(deviceAlarmDto.getStartTime(),format.format(new Date()));
        }
        List<Map> monthlyRepairDetail = flowRunMapper.monthlyRepairDetail(deviceAlarmDto);
        //月度派单率
        resultMap.put("monthlyDispatch",getMonthlyDispatchResult(monthList,monthlyAlarmDetail,monthlyDispatchDetail));
        //月度修复明细
        resultMap.put("monthlyRepair",getMonthlyRepairResult(monthList,monthlyDispatchDetail,monthlyRepairDetail));
        return resultMap;
    }


    /**
     * 统计报表------>维护率统计---->区域派单率和修复率
     * @param deviceAlarmDto
     * @return
     */
    public Map regionDispatchRepairRate(DeviceAlarmDto deviceAlarmDto) {

        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    //throw new CommonException("当前用户暂无数据");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        Map resultMap = new HashMap();
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        //区域派单率排名
        List<Map> regionDispatchRateTop = flowRunMapper.regionDispatchRateTop(deviceAlarmDto);
        resultMap.put("regionDispatchRate",regionDispatchRateTop);
        //区域修复率
        List<Map> regionRepairRate = flowRunMapper.regionRepairRate(deviceAlarmDto);
        resultMap.put("regionRepairRate",regionRepairRate);
        return resultMap;
    }

    public List<StatisticsVo> getProjectMaintainStatistics(DeviceAlarmDto deviceAlarmDto) {

        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
//                    throw new CommonException("当前用户暂无数据");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        if (deviceAlarmDto.getQueryProjectId() != null){
            deviceAlarmDto.setProjectId(deviceAlarmDto.getQueryProjectId());
        }
        deviceAlarmDto.setRegionCode(regionCode);

        // TODO 按月统计项目设备的平均在线率
        List<StatisticsVo> onlineStatistics = statisticsMapper.queryPerMonthProjectDeviceOnline(deviceAlarmDto);

        // TODO 按月统计项目设备的平均修复时长

        List<StatisticsVo> repairStatistic = statisticsMapper.queryPerMonthProjectDeviceRepairTime(deviceAlarmDto);

        // TODO 构造出所有的月份
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        String year = String.valueOf(instance.get(Calendar.YEAR));
        List<String> monthList = new ArrayList<>();
        if (year != null && deviceAlarmDto.getYear() != null) {
            if (Integer.parseInt(deviceAlarmDto.getYear()) < Integer.parseInt(year)) {
                monthList = MonthUtils.getMonthBetween1(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
            } else {
                monthList = MonthUtils.getMonthBetween1(deviceAlarmDto.getStartTime(), format.format(new Date()));
            }
        }
        log.info("月份集合-->{}",monthList);
        // TODO 构造结果集
        List<StatisticsVo> result = getMonthly(monthList, onlineStatistics, repairStatistic);
        return result;
    }
    private List<StatisticsVo> getMonthly(List<String> monthList,List<StatisticsVo> online,List<StatisticsVo> repair){
        List<StatisticsVo> monthlyDispatchResult = new ArrayList<>();
        if (monthList != null && monthList.size() > 0) {
            for (String s : monthList) {
                StatisticsVo vo = new StatisticsVo();
                vo.setDateTime(s);
                monthlyDispatchResult.add(vo);
            }
        }
        for (StatisticsVo vo : monthlyDispatchResult) {
            String date = vo.getDateTime();
            if (online != null && online.size() > 0) {
                for (StatisticsVo vo1 : online) {
                    String date1 = vo1.getDateTime();
                    if(date.equals(date1)){
                        vo.setRat(vo1.getRat());
                        break;
                    }else {
                        vo.setRat(0.00);
                    }
                }
            }
            if (repair != null && repair.size() > 0) {
                for (StatisticsVo vo1 : repair) {
                    String date1 = vo1.getDateTime();
                    if(date.equals(date1)){
                        vo.setRepairTime(vo1.getRepairTime());
                        break;
                    }else {
                        vo.setRepairTime("");
                    }
                }
            }
        }
        return monthlyDispatchResult;
    }
    private String formatSeconds(long seconds) {
        String timeStr = seconds + "秒";
        if (seconds > 60) {
            long second = seconds % 60;
            long min = seconds / 60;
            timeStr = min + "分" + second + "秒";
            if (min > 60) {
                min = (seconds / 60) % 60;
                long hour = (seconds / 60) / 60;
                timeStr = hour + "小时" + min + "分" + second + "秒";
                if (hour > 24) {
                    hour = ((seconds / 60) / 60) % 24;
                    long day = (((seconds / 60) / 60) / 24);
                    timeStr = day + "天" + hour + "小时" + min + "分" + second + "秒";
                }
            }
        }
        return timeStr;
    }

    /**
     * 故障维护统计总表
     * @param deviceAlarmDto
     * @return
     */
    public ResponseBean<SummaryVo> summary(DeviceAlarmDto deviceAlarmDto){

        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
//                    throw new CommonException("当前用户暂无数据");
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);

        String year = deviceAlarmDto.getYear();
        SummaryVo summaryVo = new SummaryVo();
        List<String> models = deviceMapper.getDeviceModelByProjectId(projectId);
        summaryVo.setModels(models);
        String startTime = deviceAlarmDto.getStartTime();
        String endTime = deviceAlarmDto.getEndTime();
        List<String> months = MonthUtils.getMonthBetween(year+"-01", year+"-12");
        String tableNamePrefix = "as_device_event_his_alarm_";
        List<String> tables = new ArrayList<>();
        for (String month : months) {
            tables.add(tableNamePrefix + month.replaceAll("-",""));
        }
        List<String> tablesNames = asEventHisAlarmMapper.findTableNames(tables);
        List<AlarmTypeCountVo> alarmCount = statisticsMapper.findAlarmCount(tablesNames, startTime, endTime, projectId, DeviceKindType.BOX.getType(),deviceAlarmDto.getTenantCode(),deviceAlarmDto.getRegionCode());
        summaryVo.setList(alarmCount);
        //年度故障趋势
        List<Map> trend = statisticsMapper.annualTrend(tablesNames,DeviceKindType.BOX.getType(),projectId,deviceAlarmDto.getRegionCode(),deviceAlarmDto.getTenantCode());
        //每月在线率最低的一天
        List<Map> online = onlineStatisticsMapper.lowestRate(year,projectId,DeviceKindType.BOX.getType(),deviceAlarmDto.getRegionCode(),deviceAlarmDto.getTenantCode());
        //每个月的平均修复时长
        List<Map> repairDuration = flowRunMapper.repairDuration(year,projectId,DeviceKindType.BOX.getType(),deviceAlarmDto.getRegionCode(),deviceAlarmDto.getTenantCode());
        List<Map> trendList = new ArrayList<>();
        List<Map> onlineList = new ArrayList<>();
        List<Map> repairDurationList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Map trendMap = new HashMap();
            trendMap.put("date",year+"-"+(i < 10 ? "0"+i : i));
            trendMap.put("count",0);
            Map onlineMap = new HashMap();
            onlineMap.put("date",year+"-"+(i < 10 ? "0"+i : i));
            onlineMap.put("lowestRate",0);
            Map repairDurationMap = new HashMap();
            repairDurationMap.put("date",year+"-"+(i < 10 ? "0"+i : i));
            repairDurationMap.put("repairDuration","0");
            boolean trendFlag = false;
            boolean onlineFlag = false;
            boolean repairDurationFlag = false;
            for (Map map : trend) {
                for (Object o : map.values()) {
                    if(o.equals(year+"-"+(i < 10 ? "0"+i : i))){
                        trendMap.put("count",map.get("count"));
                        trendFlag = true;
                        break;
                    }
                    if(trendFlag){
                        break;
                    }
                }
            }
            for (Map map : online) {
                for (Object o : map.values()) {
                    if(o.equals(year+"-"+(i < 10 ? "0"+i : i))){
                        onlineMap.put("lowestRate",map.get("lowestRate"));
                        onlineFlag = true;
                        break;
                    }
                    if(onlineFlag){
                        break;
                    }
                }
            }
            for (Map map : repairDuration) {
                for (Object o : map.values()) {
                    if(o.equals(year+"-"+(i < 10 ? "0"+i : i))){
                        repairDurationMap.put("repairDuration",map.get("repairDuration"));
                        repairDurationFlag = true;
                        break;
                    }
                    if(repairDurationFlag){
                        break;
                    }
                }
            }
            trendList.add(trendMap);
            onlineList.add(onlineMap);
            repairDurationList.add(repairDurationMap);
        }
        summaryVo.setTrend(trendList);
        summaryVo.setOnlineRate(onlineList);
        for (Map map : repairDurationList) {
            String repairDuration1 = map.get("repairDuration").toString();
            if(repairDuration1.equals("0")){
                map.put("repairDuration","--");
            }else {
                map.put("repairDuration",DateUtils.formatDateTime(repairDuration1));
            }
        }
        summaryVo.setRepairDuration(repairDurationList);
        return new ResponseBean<>(summaryVo);
    }

    /**
     * 首页设备在线数
     * @param type
     * @param queryProjectId
     * @param isAsOs
     * @return map
     */
    public Map indexOnlineCount(String type, String queryProjectId, String queryRegionCode, String isAsOs) {
        String roles = RoleContextHolder.getRole();
        String regionCode = queryRegionCode;
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
        if (CommonConstant.IS_ASOS_TRUE.equals(isAsOs)) {
            tenantCode = null;
            projectId = null;
        } else {
            if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
                //加载所有项目
                tenantCode = null;
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
                //加载用户所在租户的所有项目
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
                //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
            } else {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        Map<String, Integer> map = new HashMap<>();
        // 总设备数
        Integer devicesCount = deviceMapper.getDevicesBoxCount(type,regionCode,tenantCode,projectId);
        //  设置的设备数
        Map mapNum = regionMapper.findByProjectId(Arrays.asList(projectId.split(",")));
        //在线数
        Integer onlineCount = networkMapper.findOnlineDeviceBox(type,regionCode,tenantCode,projectId,null);
        map.put("boxCount",mapNum.get("boxNum")!= null ? Integer.parseInt( mapNum.get("boxNum").toString()) : 0);
        map.put("cameraNum",mapNum.get("cameraNum")!= null ? Integer.parseInt(mapNum.get("cameraNum").toString()) : 0);
        map.put("online",onlineCount);
        map.put("total",devicesCount);
        return map;
    }

    /**
     * 告警类型分析
     * @param deviceAlarmDto
     * @return
     */
    public List<AlarmTypeStatisticsVo> alarmTypeAnalys(DeviceAlarmDto deviceAlarmDto) {
        String startTime = deviceAlarmDto.getStartTime().substring(0, 10);
        String endTime = deviceAlarmDto.getEndTime().substring(0, 10);
        List<AlarmTypeStatisticsVo> result = null;
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }

        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if (queryProjectId != null && !"".equals(queryProjectId)) {
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        //当前告警类型
        if (endTime.equals(startTime)) {
            result = statisticsMapper.getCurrentAlarmTypeList(deviceAlarmDto);
        } else {
            List<String> monthBetween = DateUtils.getMonthBetween(startTime, endTime);
            List<String> tables = new ArrayList<>();
            for (String month : monthBetween) {
                tables.add(month.replaceAll("-", ""));
            }
            //查询表是否存在
            tables = asEventHisAlarmMapper.finHisTable(tables);
            if (tables.size() > 0) {
                deviceAlarmDto.setTableNames(tables);
                result = statisticsMapper.alarmTypeAnalys(deviceAlarmDto);
            }
        }
        return result;
    }

    /**
     * 故障类型排名
     * @param deviceAlarmDto
     * @return
     */
    public List<AlarmTypeStatisticsVo> getFaultTypeTop(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        return statisticsMapper.getFaultTypeTop(deviceAlarmDto);
    }



    /**
     * 故障类型设备数统计
     * @param deviceAlarmDto
     * @return
     */
    public List<AlarmTypeStatisticsVo> getFaultTypeDeviceCount(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        return statisticsMapper.getFaultTypeDeviceCount(deviceAlarmDto);
    }



    /**
     * 首页健康指数
     * @param deviceAlarmDto
     * @return
     */
    public ResponseBean<Map> indexHealth(DeviceAlarmDto deviceAlarmDto) {
        Map result = new HashMap();
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        String startTime = deviceAlarmDto.getStartTime();
        String endTime = deviceAlarmDto.getEndTime();
        if(startTime.substring(0,10).equals(endTime.substring(0,10))){  //当前
            Long deviceCount = deviceMapper.getDeviceCount(null, regionCode, tenantCode, deviceAlarmDto.getProjectId(),deviceAlarmDto.getModel());
            result.put("deviceCount",deviceCount);
            Map map = eventAlarmMapper.getFaultAlarmCount(regionCode,tenantCode,deviceAlarmDto.getProjectId(),deviceAlarmDto.getModel());
            result.put("faultCount",map.get("faultCount"));
            result.put("alarmCount",map.get("alarmCount"));
            result.put("offLineCount",map.get("offLineCount"));
        }else {
            List<String> monthBetween = DateUtils.getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
            List<String> tables = new ArrayList<>();
            for (String month : monthBetween) {
                tables.add(month.replaceAll("-", ""));
            }
            //查询表是否存在
            tables = asEventHisAlarmMapper.finHisTable(tables);
            if (tables.size() > 0) {
                deviceAlarmDto.setTableNames(tables);
                result = eventAlarmMapper.getHisFaultAlarmCount(deviceAlarmDto);
            }
        }
        return new ResponseBean<>(result);
    }


    /**
     * 项目区域告警排名
     */
    public Map alarmTop(DeviceAlarmDto deviceAlarmDto){
        Map map = new HashMap();
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        String[] alarmLevels = deviceAlarmDto.getAlarmLevels();
        if(alarmLevels == null || alarmLevels.length < 1){
            alarmLevels = new String[]{"1"};
            deviceAlarmDto.setAlarmLevels(alarmLevels);
        }
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_device_event_his_alarm_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_device_event_alarm";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (deviceEventHisAlarmVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        List<Map> alarmMap = eventAlarmMapper.alarmTop(deviceAlarmDto);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            Region region = new Region();
            region.setTenantCode(tenantCode);
            region.setProjectId(queryProjectId);
            region.setRegionCode(regionCode);
            if (deviceAlarmDto.getRegionId() == null){
                region.setParentId("-1");
            }else{
                region.setParentId(deviceAlarmDto.getRegionId());
            }
            //findRegionListByParentId
            List<Region> regionList = regionMapper.findListAll(region);
            if(regionList.size()>0){
                    map = alarmMap.stream().collect(Collectors.toMap(s -> s.get("regionName"), s -> s.get("alarmCount")));
                    /*for (Region region1 : regionList) {
                        String regionName = region1.getRegionName();
                        if(!map.containsKey(regionName)){
                            map.put(regionName,0);
                        }
                    }*/
            }
        }else {
            Project project = new Project();
            project.setTenantCode(tenantCode);
            project.setRegionCode(regionCode);
            List<Project> projectList = projectMapper.findProject(project);
            if(projectList.size()>0){
                map = alarmMap.stream().collect(Collectors.toMap(s -> s.get("projectName"), s -> s.get("alarmCount")));
                for (Project project1 : projectList) {
                    String projectName = project1.getProjectName();
                    if(!map.containsKey(projectName)){
                        map.put(projectName,0);
                    }
                }
            }
        }
        return map;
    }
    /**
     * 统计报表->健康指数统计->健康指数趋势分析
     * @param deviceAlarmDto
     * @return list
     */
    public  List<Object> healthyTrend(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId =deviceAlarmDto.getQueryProjectId();
        if (queryProjectId!= null){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = new SimpleDateFormat("yyyy-MM-dd").parse(deviceAlarmDto.getStartTime());
            endTime = new SimpleDateFormat("yyyy-MM-dd").parse(deviceAlarmDto.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(startTime);
        List<String> list = new ArrayList<>();
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
        for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        deviceAlarmDto.setTableNames(tables);
        List<Map> mapList =statisticsMapper.getHealthyTrend(deviceAlarmDto);
        Map<String,Object> resultData = new LinkedHashMap<>();
        if("day".equals(deviceAlarmDto.getTimeUnit())) {
            //返回日期之间所有的日期
            List<String> dayList = DateUtils.getDayBetweenDates(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
            for (int i = 0; i < dayList.size(); i++) {
                Map map = new HashMap();
                map.put("faultCount", 0L);
                map.put("alarmCount", 0L);
                map.put("offLineCount", 0L);
                map.put("deviceCount", 0L);
                map.put("date",dayList.get(i));
                resultData.put(dayList.get(i) + "日",map);
            }
          for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> map = (Map<String, Object>) resultData.get(mapList.get(i).get("date"));
                map.put("faultCount", mapList.get(i).get("faultCount"));
                map.put("alarmCount", mapList.get(i).get("alarmCount"));
                map.put("offLineCount", mapList.get(i).get("offLineCount"));
                map.put("deviceCount", mapList.get(i).get("deviceCount"));
            }
        }
        else{
            //返回日期之间所有的月份
            List<String> monthList = DateUtils.getMonthBetween(deviceAlarmDto.getStartTime(), deviceAlarmDto.getEndTime());
            for (int i = 0; i < monthList.size(); i++) {
                Map map =new HashMap();
                map.put("faultCount",0L);
                map.put("alarmCount",0L);
                map.put("offLineCount",0L);
                map.put("deviceCount",0L);
                map.put("date",monthList.get(i));
                resultData.put(monthList.get(i) + "月", map);
            }
            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> map =(Map<String, Object>) resultData.get(mapList.get(i).get("date"));
                map.put("faultCount",mapList.get(i).get("faultCount"));
                map.put("alarmCount",mapList.get(i).get("alarmCount"));
                map.put("offLineCount",mapList.get(i).get("offLineCount"));
                map.put("deviceCount",mapList.get(i).get("deviceCount"));
            }
        }
       return new ArrayList<>(resultData.values());
    }


    /**
     * 健康监测---> 在线率完好率
     * @param  deviceAlarmDto
     * @return list
     */
    public List<RegionDeviceListVo> getRegionOnlineIntactStatistics(DeviceAlarmDto deviceAlarmDto) {
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String startTime=deviceAlarmDto.getStartTime();
        String endTime =deviceAlarmDto.getEndTime();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        if (startTime == null || endTime.equals(startTime)) {
            // 当前
            return regionMapper.getRegionCurrentOnlineIntactList(deviceAlarmDto);
        }else{
            // 本月/本年度
            return regionMapper.getRegionHistoryOnlineIntactList(deviceAlarmDto);

        }
    }

    /**
     * 统计报表->故障维护统计->历史平均修复时长
     * @param deviceAlarmDto
     * @return map
     */
    public Map getOrderAvgRepairDuration(DeviceAlarmDto deviceAlarmDto){
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if (regionCode == null || "".equals(regionCode)) {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if (StringUtils.isNotEmpty(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        return flowRunMapper.getOrderAvgRepairDuration(deviceAlarmDto);
    }


    /**
     * 故障告警统计--->故障设备列表
     *
     * @param deviceAlarmDto 查询参数
     * @param request   请求对象
     * @param response  响应对象
     * @return ResponseBean
     */
    public ResponseBean<Boolean> faultAlarmExport(@RequestBody DeviceAlarmDto deviceAlarmDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            String roles = RoleContextHolder.getRole();
            String regionCode = deviceAlarmDto.getRegionCode();
            String tenantCode = SysUtil.getTenantCode();
            String projectId = SysUtil.getProjectId();
            if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
                //加载所有项目
                tenantCode = null;
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
                //加载用户所在租户的所有项目
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
                //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
            } else {
                if(regionCode == null || "".equals(regionCode)){
                    String userRegionCode = RegionCodeContextHolder.getRegionCode();
                    if(userRegionCode == null || "".equals(userRegionCode)){
                        return null;
                    }
                    regionCode = userRegionCode;
                }
            }
            String queryProjectId = deviceAlarmDto.getQueryProjectId();
            if (com.aswl.as.ibrs.utils.StringUtils.isNotEmpty(queryProjectId)){
                projectId = queryProjectId;
            }
            deviceAlarmDto.setTenantCode(tenantCode);
            deviceAlarmDto.setProjectId(projectId);
            deviceAlarmDto.setRegionCode(regionCode);

            SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd").parse(deviceAlarmDto.getStartTime());
                endTime = new SimpleDateFormat("yyyy-MM-dd").parse(deviceAlarmDto.getEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            c.setTime(startTime);
            List<String> list = new ArrayList<>();
            String tableName = "as_device_event_his_alarm_";
            String aa = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
                String str = sdf.format(c.getTime());
                if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                    aa = "as_device_event_alarm";
                    deviceAlarmDto.setStartTime(null);
                    deviceAlarmDto.setEndTime(null);
                } else {
                    aa = tableName + "" + str;
                }
                list.add(aa);
                c.add(Calendar.MONTH, 1);//进行当前日期月份加1
            }
            //查询表是否存在
            List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
            List<String> tables = new ArrayList<>();
            for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
                tables.add(eventHisAlarmVo.getTableName());
            }
            if (deviceEventHisAlarmVos.size() == 0) {
                deviceAlarmDto.setTableNames(list);
            } else {
                deviceAlarmDto.setTableNames(tables);
            }
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "故障设备列表" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<DeviceAlarmVo> deviceAlarmVos = statisticsMapper.getHealthyList(deviceAlarmDto);
            if (CollectionUtils.isEmpty(deviceAlarmVos)) {
                throw new CommonException("无故障设备列表数据.");
            }
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("projectName", "项目名称");
            map.put("regionName", "区域");
            map.put("deviceName", "位置");
            map.put("deviceKindName", "种类");
            map.put("deviceModelName", "型号");
            map.put("ip", "设备IP");
            map.put("deviceCode", "编码");
            map.put("deviceModelName", "告警类型");
            map.put("alarmTypeName", "告警时间");
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(deviceAlarmVos), map);
            return new ResponseBean<>(true, "导出成功");
        } catch (Exception e) {
            log.error("导出故障设备列表数据失败！", e);
            throw new CommonException("导出故障设备列表数据失败！");
        }
    }


    /**
     * 故障告警统计--->故障设备列表
     *
     * @param deviceAlarmDto 查询参数
     * @param request   请求对象
     * @param response  响应对象
     * @return ResponseBean
     */
    public ResponseBean<Boolean> faultDeviceExport(@RequestBody DeviceAlarmDto deviceAlarmDto, HttpServletRequest request, HttpServletResponse response) {
        try {
            String roles = RoleContextHolder.getRole();
            String regionCode = deviceAlarmDto.getRegionCode();
            String tenantCode = SysUtil.getTenantCode();
            String projectId = SysUtil.getProjectId();
            if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
                //加载所有项目
                tenantCode = null;
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
                //加载用户所在租户的所有项目
            } else if (roles != null && !"".equals(roles) &&
                    (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
                //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
            } else {
                if(regionCode == null || "".equals(regionCode)){
                    String userRegionCode = RegionCodeContextHolder.getRegionCode();
                    if(userRegionCode == null || "".equals(userRegionCode)){
                        return null;
                    }
                    regionCode = userRegionCode;
                }
            }
            String queryProjectId = deviceAlarmDto.getQueryProjectId();
            if (com.aswl.as.ibrs.utils.StringUtils.isNotEmpty(queryProjectId)){
                projectId = queryProjectId;
            }
            deviceAlarmDto.setTenantCode(tenantCode);
            deviceAlarmDto.setProjectId(projectId);
            deviceAlarmDto.setRegionCode(regionCode);

            SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startTime = null;
            Date endTime = null;
            try {
                startTime = new SimpleDateFormat("yyyy-MM-dd").parse(deviceAlarmDto.getStartTime());
                endTime = new SimpleDateFormat("yyyy-MM-dd").parse(deviceAlarmDto.getEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            c.setTime(startTime);
            List<String> list = new ArrayList<>();
            String tableName = "as_device_event_his_alarm_";
            String aa = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
                String str = sdf.format(c.getTime());
                if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                    aa = "as_device_event_alarm";
                    deviceAlarmDto.setStartTime(null);
                    deviceAlarmDto.setEndTime(null);
                } else {
                    aa = tableName + "" + str;
                }
                list.add(aa);
                c.add(Calendar.MONTH, 1);//进行当前日期月份加1
            }
            //查询表是否存在
            List<DeviceEventHisAlarmVo> deviceEventHisAlarmVos = asEventHisAlarmMapper.finByinfoTable(list);
            List<String> tables = new ArrayList<>();
            for (DeviceEventHisAlarmVo eventHisAlarmVo : deviceEventHisAlarmVos) {
                tables.add(eventHisAlarmVo.getTableName());
            }
            if (deviceEventHisAlarmVos.size() == 0) {
                deviceAlarmDto.setTableNames(list);
            } else {
                deviceAlarmDto.setTableNames(tables);
            }
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "故障设备列表" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            List<DeviceAlarmVo> deviceAlarmVos = statisticsMapper.getHealthyList(deviceAlarmDto);
            if (CollectionUtils.isEmpty(deviceAlarmVos)) {
                throw new CommonException("无故障设备列表数据.");
            }
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("projectName", "项目名称");
            map.put("regionName", "区域");
            map.put("deviceName", "位置");
            map.put("deviceKindName", "种类");
            map.put("deviceModelName", "型号");
            map.put("ip", "设备IP");
            map.put("deviceCode", "编码");
            map.put("alarmlCount","故障数");
            map.put("alarmTypesDes", "告警类型");
            map.put("alarmTime", "告警时间");
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(deviceAlarmVos), map);
            return new ResponseBean<>(true, "导出成功");
        } catch (Exception e) {
            log.error("导出故障设备列表数据失败！", e);
            throw new CommonException("导出故障设备列表数据失败！");
        }
    }


    /**
     * 导出统计总表
     * @return
     */
    public ResponseEntity<byte[]> onlineOfflineDeviceExport(DeviceAlarmDto deviceAlarmDto,HttpServletResponse response) throws Exception {
        //获取文件路径
        String  path = "onlineRateStatistics.xlsx";
        //更改文件名编码
        String fileName = "在线率统计设备列表.xlsx";
        String gFileName = URLEncoder.encode(fileName, "UTF-8");
        InputStream in = null;
        Workbook exl;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {

            in =getClass().getClassLoader().getResourceAsStream("temp/"+path);

            exl = WorkbookFactory.create(in);
            execelSheet1(exl, deviceAlarmDto);
            execelSheet2(exl,deviceAlarmDto);
     
            exl.write(out);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=\"" + gFileName + "\"");
            return ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/x-msdownload")).body(out.toByteArray());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
//                    out.flush();
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    /**
     * 导出第一个sheet数据
     * @param exl
     * @param deviceAlarmDto
     */
    private void execelSheet1(Workbook exl, DeviceAlarmDto deviceAlarmDto) {
        Sheet sheet1 = exl.getSheetAt(0);
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return ;
                }
                regionCode = userRegionCode;
            }
        }

        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setNetworkStatus(1);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_event_his_network_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_event_network";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> EventHisTableVos = asEventHisAlarmMapper.findEventHisTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : EventHisTableVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (EventHisTableVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        List<DeviceAlarmVo> onlineList = statisticsMapper.getOnlineList(deviceAlarmDto);
        for (int i = 0, y = onlineList.size(); i < y; ++i) {
            Row row = sheet1.createRow(i + 2);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(onlineList.get(i).getProjectName().trim());
            row.createCell(2).setCellValue(onlineList.get(i).getRegionName().trim());
            row.createCell(3).setCellValue(onlineList.get(i).getDeviceName().trim());
            row.createCell(4).setCellValue(onlineList.get(i).getDeviceKindName());
            row.createCell(5).setCellValue(onlineList.get(i).getDeviceModelName());
            row.createCell(6).setCellValue(onlineList.get(i).getIp());
            row.createCell(7).setCellValue(onlineList.get(i).getDeviceCode());
        }
    }

    /**
     * 导出第二个sheet数据
     * @param exl
     * @param deviceAlarmDto
     */
    private void execelSheet2(Workbook exl,DeviceAlarmDto deviceAlarmDto) {

        Sheet sheet2 = exl.getSheetAt(1);
        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        if (SysUtil.isAdmin() || roles.contains(SecurityConstant.ROLE_ADMIN)) { //超级管理员
            //加载所有项目
            tenantCode = null;
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_SYS_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_SYS_WATCHER.getCode()))) {  //租户系统管理员或租户系统值班员
            //加载用户所在租户的所有项目
        } else if (roles != null && !"".equals(roles) &&
                (roles.contains(RoleEnum.ROLE_PROJECT_ADMIN.getCode()) || roles.contains(RoleEnum.ROLE_PROJECT_WATCHER.getCode()))) {   //项目管理员或项目值班员
            //只加载用户关联的项目（已在SysUtil.getProjectId()获取）
        } else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return ;
                }
                regionCode = userRegionCode;
            }
        }

        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setNetworkStatus(0);
        deviceAlarmDto.setProjectId(projectId);
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(queryProjectId != null && !"".equals(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
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
        List<String> list = new ArrayList<>();
        String tableName = "as_event_his_network_";
        String aa = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        while (Integer.parseInt(sdf.format(c.getTime())) <= Integer.parseInt(sdf.format(endTime))) {//判断是否到结束日期
            String str = sdf.format(c.getTime());
            if (sdfFormat.format(startTime).equals(sdfFormat.format(new Date()))) {
                aa = "as_event_network";
                deviceAlarmDto.setStartTime(null);
                deviceAlarmDto.setEndTime(null);
            } else {
                aa = tableName + "" + str;
            }
            list.add(aa);
            c.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        //查询表是否存在
        List<DeviceEventHisAlarmVo> EventHisTableVos = asEventHisAlarmMapper.findEventHisTable(list);
        List<String> tables = new ArrayList<>();
        for (DeviceEventHisAlarmVo eventHisAlarmVo : EventHisTableVos) {
            tables.add(eventHisAlarmVo.getTableName());
        }
        if (EventHisTableVos.size() == 0) {
            deviceAlarmDto.setTableNames(list);
        } else {
            deviceAlarmDto.setTableNames(tables);
        }
        List<DeviceAlarmVo> onlineList = statisticsMapper.getOnlineList(deviceAlarmDto);
            for (int i = 0; i < onlineList.size(); i++) {
                DeviceAlarmVo deviceAlarmVo = onlineList.get(i);
                deviceAlarmVo.setIntervalTime(formatDateTime(deviceAlarmVo.getIntervalTime()));
        }
        for (int i = 0, y = onlineList.size(); i < y; ++i) {
            Row row = sheet2.createRow(i + 2);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(onlineList.get(i).getProjectName().trim());
            row.createCell(2).setCellValue(onlineList.get(i).getRegionName().trim());
            row.createCell(3).setCellValue(onlineList.get(i).getDeviceName().trim());
            row.createCell(4).setCellValue(onlineList.get(i).getDeviceKindName());
            row.createCell(5).setCellValue(onlineList.get(i).getDeviceModelName());
            row.createCell(6).setCellValue(onlineList.get(i).getIp());
            row.createCell(7).setCellValue(onlineList.get(i).getDeviceCode());
            row.createCell(8).setCellValue(onlineList.get(i).getIntervalTime());
        }
    }
    
}




