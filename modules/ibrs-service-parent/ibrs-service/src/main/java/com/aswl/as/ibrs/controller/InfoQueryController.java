package com.aswl.as.ibrs.controller;

import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.DeviceAlarmConditionDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.DynamicMessage;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.service.*;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.github.pagehelper.PageInfo;
import com.google.common.net.HttpHeaders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 信息查询Controller
 *
 * @author jk
 * @version 1.0.0
 * @create 2019/10/21 16:21
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/infoQuery", tags = "信息查询")
@RestController
@RequestMapping("/v1/infoQuery")
public class InfoQueryController {
    private final AlarmTypeService alarmTypeService;
    private final RegionService regionService;
    private final AlarmLevelService alarmLevelService;
    private final EventUcMetadataService metadataService;
    private final UserServiceClient userServiceClient;
    private final DeviceService deviceService;
    @Autowired
    @Qualifier(value = "redisTemplateJson")
    private RedisTemplate redisTemplate;

    /**
     * 查询区域
     *
     * @param parentId
     * @param query
     * @return
     */
    @GetMapping(value = "regions")
    @ApiOperation(value = "查询区域")
    public ResponseBean<List<Map>> getRegions(@RequestParam(value = "parentId", defaultValue = "-1") String parentId,
                                              @RequestParam(value = "query", defaultValue = "") String query,
                                              @RequestParam(value = "regionCode") String regionCode
    ) {


        String tenantCode = null;
        String projectId = null;

        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();
            projectId = SysUtil.getProjectId();
        }

        if (query != null && query.length() > 0) {
            parentId = null;
        }
        if (regionCode != null && !"".equals(regionCode)) {
            parentId = regionService.findRegionId(regionCode, tenantCode, projectId);
        }
        ResponseBean<UserInfoDto> info = userServiceClient.info();

        if (info.getCode() == 200) {
            regionCode = info.getData().getRegionCode();
        }
        return new ResponseBean<>(regionService.getRegions(parentId, query, regionCode));
    }

    @GetMapping(value = "regionTree")
    @ApiOperation(value = "查询区域树")
    public ResponseBean<List<RegionVo>> findRegions() {

        String tenantCode = null;
        String projectId = null;

        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();
            projectId = SysUtil.getProjectId();
        }

        ResponseBean<UserInfoDto> info = userServiceClient.info();
        String regionCode = "";

        if (info.getCode() == 200) {

            regionCode = info.getData().getRegionCode();
        }
        List<RegionVo> regionTree = regionService.findRegionTree(regionCode, tenantCode, projectId);
        if (regionTree != null) {
            return new ResponseBean<>(TreeUtil.buildTree(regionTree, "-1"));
        } else {
            return new ResponseBean<>(new ArrayList<>());
        }
    }

    /**
     * 获取报警级别 TODO 这个没有用到，到时候可能需要删了，
     *
     * @return
     */
    @GetMapping(value = "findAlarmLevel")
    @ApiOperation(value = "获取报警级别")
    public ResponseBean<List<Map>> findAlarmLevel() {
        return new ResponseBean<>(alarmLevelService.findAlarmLevel());
    }

    /**
     * 获取设备种类,类型,型号
     *
     * @return
     */
    @GetMapping(value = "findDeviceModelKind")
    @ApiOperation(value = "获取设备型号,类型,种类")
    public ResponseBean<List<KindVo>> findDeviceModelKind() {
        return new ResponseBean<>(regionService.findDeviceModelKind());
    }

    /**
     * 获取离线报警类型
     *
     * @return
     */
    @GetMapping(value = "findAlarmType")
    @ApiOperation(value = "查询设备报警类型")
    public ResponseBean<List<Map>> findAlarmType(@RequestParam(value = "alarmLevel", required = false) String alarmLevel) {
        String[] alarmLevels = null;
        if (alarmLevel != null && !"".equals(alarmLevel)) {
            alarmLevels = alarmLevel.split(",");
        }
        List<AlarmTypeVo> alarmTypeList = alarmTypeService.findAlarmType(alarmLevels);
        List<Map> listVo = new ArrayList<>();
        //抽取组名相同的元素
        Map<String, List<AlarmTypeVo>> map = new LinkedHashMap<>();
        for (AlarmTypeVo alarmTypeVo : alarmTypeList) {
            List<AlarmTypeVo> list = new ArrayList<>();
            if (map.containsKey(alarmTypeVo.getGroupName())) {
                map.get(alarmTypeVo.getGroupName()).add(alarmTypeVo);

            } else {
                list.add(alarmTypeVo);
                map.put(alarmTypeVo.getGroupName(), list);
            }
        }
        listVo.add(map);
        return new ResponseBean<>(listVo);
    }

    /**
     * 获取当前告警列表
     *
     * @return
     */
    @GetMapping(value = "findAlarmList")
    @ApiOperation(value = "分页条件查询设备报警信息")
    public ResponseBean<PageInfo<DeviceAlarmVo>> findDeviceAlarmInfo(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                     @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                     DeviceAlarmDto deviceAlarmDto) {
        String tenantCode = null;
        String projectId = null;

        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();// admin和运营端应该不需要设置该tenantCode和projectId
            projectId = SysUtil.getProjectId();
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);

        //报警级别
        if (deviceAlarmDto.getAlarmLevel() != null) {
            String[] AlarmLevels = deviceAlarmDto.getAlarmLevel().split(",");
            deviceAlarmDto.setAlarmLevels(AlarmLevels);
        }
        //报警类型
        if (deviceAlarmDto.getAlarmTypeName() != null) {
            String[] types = deviceAlarmDto.getAlarmTypeName().split(",");
            List<String> alarm = new ArrayList<>();
            List<String> prompt = new ArrayList<>();
            for (int i = 0; i < types.length; i++) {
                if (types[i].indexOf("Alarm") != -1) {
                    alarm.add(types[i]);

                } else {
                    prompt.add(types[i]);
                }
            }
            deviceAlarmDto.setAlarmTypeNames(alarm);
            deviceAlarmDto.setPromptTypeNames(prompt);
        }

        return new ResponseBean<>(metadataService.findPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmDto));
    }

    /**
     * 获取设备告警明细记录
     *
     * @return
     */
    @GetMapping(value = "findDeviceAlarmDetails/{id}")
    @ApiOperation(value = "获取设备告警明细记录")
    public ResponseBean<List<DeviceAlarmDetailsVo>> findDeviceInfo(@PathVariable("id") String id) {
        return new ResponseBean<>(metadataService.findDeviceInfo(id));
    }

    /**
     * 获取设备告警工单的明细记录
     *
     * @param id
     * @return
     */
    @GetMapping(value = "findDeviceWorkOrderInfo/{id}")
    @ApiOperation(value = "获取设备告警工单的明细记录")
    public ResponseBean<List<DeviceAlarmDetailsVo>> findDeviceWorkOrderInfo(@PathVariable("id") String id) {
        return new ResponseBean<>(metadataService.findDeviceWorkOrderInfo(id));
    }


    /**
     * 获取历史状态列表
     *
     * @return
     */
    @GetMapping(value = "findHistoryStatusList")
    @ApiOperation(value = "获取历史状态列表")
    public ResponseBean<PageInfo<DeviceAlarmVo>> findHistoryStatusList(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                       @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                       DeviceAlarmDto deviceAlarmDto) {

        String tenantCode = null;
        String projectId = null;

        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();// admin和运营端应该不需要设置该tenantCode和projectId
            projectId = SysUtil.getProjectId();
        }

        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);

        //报警级别
        if (deviceAlarmDto.getAlarmLevel() != null) {
            String[] AlarmLevels = deviceAlarmDto.getAlarmLevel().split(",");
            deviceAlarmDto.setAlarmLevels(AlarmLevels);
        }
        //报警类型
        if (deviceAlarmDto.getAlarmTypeName() != null) {
            String[] types = deviceAlarmDto.getAlarmTypeName().split(",");
            if (types != null) {
                deviceAlarmDto.setAlarmNames(types);
            }
        }
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        calendar.add(calendar.DATE, -6);//把日期往后增加n天.正数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null || "".equals(deviceAlarmDto.getStartTime())) {

            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            String startTime = deviceAlarmDto.getStartTime();
            String endTime = deviceAlarmDto.getEndTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //LocalDate转Date
            Date startDate = DateUtils.asDate(LocalDate.parse(startTime, dtf));
            Date endDate = DateUtils.asDate(LocalDate.parse(endTime, dtf));
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            int start = c.get(Calendar.MONTH);
            c.setTime(endDate);
            int end = c.get(Calendar.MONTH);
            if ((end - start) > 6) {
                return new ResponseBean<>(null, "时间差不能超过6个月");
            }
            return new ResponseBean<>(metadataService.findHistoryStatusPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmDto));
        }

        return new ResponseBean<>(metadataService.findHistoryStatusPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmDto));
    }

    /**
     * 当前告警列表的导出
     *
     * @param deviceAlarmDto
     * @param request
     * @param response
     */
    @PostMapping(value = "export")
    @ApiOperation(value = "当前告警列表的导出")
    public void exportAlarmInfo(@RequestBody DeviceAlarmDto deviceAlarmDto, HttpServletRequest request, HttpServletResponse response) {

        String roles = RoleContextHolder.getRole();
        String regionCode = deviceAlarmDto.getRegionCode();
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String queryProjectId = deviceAlarmDto.getQueryProjectId();
        if(StringUtils.isNotEmpty(queryProjectId)){
            projectId = queryProjectId;
        }
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
                    throw new CommonException("导出设备报警信息数据失败[用户无数据权限]！");
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);

        //报警级别
        if (deviceAlarmDto.getAlarmLevel() != null) {
            String[] AlarmLevels = deviceAlarmDto.getAlarmLevel().split(",");
            deviceAlarmDto.setAlarmLevels(AlarmLevels);
        }

        List<DeviceAlarmVo> deviceAlarmInfo = null;
        try {
            // 配置response
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, Servlets.getDownName(request, "设备报警信息" + DateUtils.localDateMillisToString(LocalDateTime.now()) + ".xlsx"));
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            map.put("num", "序号");
            map.put("projectName","项目");
            map.put("regionName", "区域");
            map.put("deviceName", "位置");
            map.put("deviceKindName","种类");
            map.put("deviceModelName","型号");
            map.put("ip","IP");
            map.put("deviceCode", "编码");
            map.put("alarmTime", "告警时间");
            map.put("intervalTime", "告警时长");
            map.put("alarmLevelName", "级别");
            map.put("alarmTypeName", "状态信息");

            if (deviceAlarmDto.getFlag().equals("1")) { //当前故障
                deviceAlarmInfo = deviceService.getCurrentAlarmExport(deviceAlarmDto);
                map.put("alarmTypeName", "故障信息");
            }else if(deviceAlarmDto.getFlag().equals("2")){ //当前预警
                deviceAlarmInfo = deviceService.getCurrentAlarmExport(deviceAlarmDto);
                map.put("alarmTypeName", "预警信息");
            }else if(deviceAlarmDto.getFlag().equals("3")){ //当前状态
                deviceAlarmInfo = deviceService.getCurrentStatusExport(deviceAlarmDto);
                map.put("alarmTime", "时间");
                map.remove("intervalTime");
                map.put("alarmTypeName", "状态信息");
            }else if(deviceAlarmDto.getFlag().equals("4")){ //历史故障
                deviceAlarmInfo = deviceService.getHistoryAlarmExport(deviceAlarmDto);
                map.put("alarmTypeName", "故障信息");
            }else if(deviceAlarmDto.getFlag().equals("5")){ //历史预警
                deviceAlarmInfo = deviceService.getHistoryAlarmExport(deviceAlarmDto);
                map.put("alarmTypeName", "预警信息");
            }else if(deviceAlarmDto.getFlag().equals("6")){ //历史状态
                deviceAlarmInfo = deviceService.getHistoryStatusExport(deviceAlarmDto);
                map.put("alarmTime", "时间");
                map.remove("intervalTime");
                map.put("alarmTypeName", "状态信息");
            }else if(deviceAlarmDto.getFlag().equals("7")){ //当前告警（故障和预警）
                deviceAlarmInfo = deviceService.getCurrentAlarmExport(deviceAlarmDto);
                map.put("alarmTypeName", "告警信息");
            }else if(deviceAlarmDto.getFlag().equals("8")){ //历史告警（故障和预警）
                deviceAlarmInfo = deviceService.getHistoryAlarmExport(deviceAlarmDto);
                map.put("alarmTypeName", "告警信息");
            }
            ExcelToolUtil.exportExcel(request.getInputStream(), response.getOutputStream(), MapUtil.java2Map(deviceAlarmInfo), map);
        } catch (Exception e) {
            log.error("导出设备报警信息数据失败！", e);
            throw new CommonException("导出设备报警信息数据失败！");
        }
    }

    /**
     * 分页条件查询当前设备监控报警信息列表
     *
     * @param pageNum
     * @param pageSize
     * @param deviceAlarmConditionDto
     * @return
     */
    @GetMapping(value = "condition")
    @ApiOperation(value = "分页条件查询设备监控报警信息列表")
    public ResponseBean<PageInfo<DeviceAlarmVo>> findDeviceAlarmInfoByCondition(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                                @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                                DeviceAlarmConditionDto deviceAlarmConditionDto) {

        String tenantCode = null;
        String projectId = null;
        if (!SysUtil.isAdmin()) {
            tenantCode = SysUtil.getTenantCode();// admin和运营端应该不需要设置该tenantCode和projectId
            projectId = SysUtil.getProjectId();
        }

        ResponseBean<UserInfoDto> info = userServiceClient.info();
        String regionCode = "";
        String[] alarmLevels = null;
        String[] queries = null;

        deviceAlarmConditionDto.setTenantCode(tenantCode);
        deviceAlarmConditionDto.setProjectId(projectId);

        if (info.getCode() == 200) {
            regionCode = info.getData().getRegionCode();
            deviceAlarmConditionDto.setRegionCode(regionCode);
        }
        //报警级别
        if (deviceAlarmConditionDto.getAlarmLevel() != null && !"".equals(deviceAlarmConditionDto.getAlarmLevel())) {
            alarmLevels = deviceAlarmConditionDto.getAlarmLevel().split(",");
            deviceAlarmConditionDto.setAlarmLevels(alarmLevels);
        }
        //关键字搜索
        if (deviceAlarmConditionDto.getQuery() != null && !"".equals(deviceAlarmConditionDto.getQuery())) {
            queries = deviceAlarmConditionDto.getQuery().split(",");
            deviceAlarmConditionDto.setQueries(queries);
        }

        return new ResponseBean<>(metadataService.findByPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmConditionDto));
    }


    /**
     * 分页条件查询设备历史报警信息列表
     *
     * @param pageNum
     * @param pageSize
     * @param deviceAlarmConditionDto
     * @return
     */
    @GetMapping(value = "historyByCondition")
    @ApiOperation(value = "分页条件查询设备历史报警信息列表")
    public ResponseBean<PageInfo<DeviceAlarmVo>> findDeviceAlarmInfoHistoryByCondition(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                                       @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                                       DeviceAlarmConditionDto deviceAlarmConditionDto) {
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        String regionCode = "";
        String[] alarmLevels = null;
        String[] queries = null;

        //通过区域编码来鉴权
        if (info.getCode() == 200) {
            regionCode = info.getData().getRegionCode();
            deviceAlarmConditionDto.setRegionCode(regionCode);
        }

        //报警级别
        if (deviceAlarmConditionDto.getAlarmLevel() != null && !"".equals(deviceAlarmConditionDto.getAlarmLevel())) {
            alarmLevels = deviceAlarmConditionDto.getAlarmLevel().split(",");
            deviceAlarmConditionDto.setAlarmLevels(alarmLevels);
        }
        //关键字搜索
        if (deviceAlarmConditionDto.getQuery() != null && !"".equals(deviceAlarmConditionDto.getQuery())) {
            queries = deviceAlarmConditionDto.getQuery().split(",");
            deviceAlarmConditionDto.setQueries(queries);
        }
        //默认查询时间取当天的时间
        Date date = new Date();//取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        calendar.add(calendar.DATE, -1);//把日期往后增加n天.正数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmConditionDto.getStartTime() == null || "".equals(deviceAlarmConditionDto.getStartTime())) {
            deviceAlarmConditionDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
            deviceAlarmConditionDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return new ResponseBean<>(metadataService.findHistoryByPage(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmConditionDto));
    }

    @GetMapping(value = "findHistoryMsg")
    @ApiOperation(value = "手机端消息")
    public ResponseBean<PageInfo<DeviceAlarmVo>> findHistoryMsg(@RequestParam(value = "pageNum", required = false) String pageNum,
                                                                @RequestParam(value = "pageSize", required = false) String pageSize,
                                                                DeviceAlarmDto deviceAlarmDto) {
        // ResponseBean<UserInfoDto> info = userServiceClient.info();
        // String regionCode = "";
        // if (info.getCode() == 200) {
        //
        //     regionCode = info.getData().getRegionCode();
        //     deviceAlarmDto.setRegionCode(regionCode);
        // }
        //报警级别
        if (deviceAlarmDto.getAlarmLevel() != null) {
            String[] AlarmLevels = deviceAlarmDto.getAlarmLevel().split(",");
            deviceAlarmDto.setAlarmLevels(AlarmLevels);
        }
        //报警类型
        if (deviceAlarmDto.getAlarmTypeName() != null) {
            String[] types = deviceAlarmDto.getAlarmTypeName().split(",");
            if (types != null) {
                deviceAlarmDto.setAlarmNames(types);
            }
        }
        // 获取项目
        deviceAlarmDto.setProjectId(SysUtil.getProjectId());
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        calendar.add(calendar.DATE, -6);//把日期往后增加n天.正数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null || "".equals(deviceAlarmDto.getStartTime())) {

            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            String startTime = deviceAlarmDto.getStartTime();
            String endTime = deviceAlarmDto.getEndTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //LocalDate转Date
            Date startDate = DateUtils.asDate(LocalDate.parse(startTime, dtf));
            Date endDate = DateUtils.asDate(LocalDate.parse(endTime, dtf));
            Calendar c = Calendar.getInstance();
            c.setTime(startDate);
            int start = c.get(Calendar.MONTH);
            c.setTime(endDate);
            int end = c.get(Calendar.MONTH);
            if ((end - start) > 6) {
                return new ResponseBean<>(null, "时间差不能超过6个月");
            }
            return new ResponseBean<>(metadataService.findHistoryMsg(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmDto));
        }

        return new ResponseBean<>(metadataService.findHistoryMsg(PageUtil.pageInfo(pageNum, pageSize, "", ""), deviceAlarmDto));
    }

    @GetMapping(value = "getDeviceDynamic")
    @ApiOperation(value = "分页条件查询设备历史报警信息列表")
    public ResponseBean<List<DynamicMessage>> getDeviceDynamic(String alarmLevel,String query) {
        if(query == null){
            query = "";
        }
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        List<String> projectIds = Arrays.asList(projectId.split(","));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<DynamicMessage> result;
        if(alarmLevel.equals("1")){
            result = redisTemplate.opsForList().range("fault", 0, -1);

        }
        else if(alarmLevel.equals("2")){
            result = redisTemplate.opsForList().range("warn",0,-1);
        }
        else {
            result = redisTemplate.opsForList().range("normal",0,-1);
        }
        if(result != null && result.size() > 0){
            String finalQuery = query;
            if(SysUtil.isAdmin()){
                result = result.stream().filter(m -> (m.getDeviceName().contains(finalQuery) || m.getAlarmType().contains(finalQuery)) && tenantCode.equals(m.getTenantCode()) && projectIds.contains(m.getProjectId())).sorted(Comparator.comparing(DynamicMessage::getAlarmTime).reversed()).collect(Collectors.toList());
            }else {
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null){
                    return new ResponseBean<>(new ArrayList<>());
                }
                result = result.stream().filter(m -> (m.getDeviceName().contains(finalQuery) || m.getAlarmType().contains(finalQuery)) && tenantCode.equals(m.getTenantCode()) && projectIds.contains(m.getProjectId()) && m.getRegionCode().startsWith(userRegionCode)).sorted(Comparator.comparing(DynamicMessage::getAlarmTime).reversed()).collect(Collectors.toList());
            }
        }
        if (result.size() > 0) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                for (DynamicMessage dynamicMessage : result) {
                    long alarmTime = format.parse(dynamicMessage.getAlarmTime()).getTime();
                    long intervalTime = (currentTimeMillis - alarmTime) / 1000;
                    dynamicMessage.setIntervalTime(formatDateTime(intervalTime));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new ResponseBean<>(result);
    }

    //时长转换
    public  String formatDateTime(long time) {
        long ss = time;
        String intervalTime = null;
        long days = ss / (60 * 60 * 24);
        long hours = (ss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (ss % (60 * 60)) / 60;
        long seconds = ss % 60;
        if (days > 0) {
            intervalTime = days + "天" + hours + "小时" + minutes + "分钟";
        } else if (hours >0) {
            intervalTime = hours + "小时" + minutes + "分钟";
        } else if (minutes > 0) {
            intervalTime = minutes + "分钟";
        } else {
            intervalTime = "1分钟内";
        }
        return intervalTime;
    }

    /**
     * 当前告警列表分页查询
     */
    @GetMapping("getCurrentAlarm")
    @ApiOperation("当前告警列表分页查询")
    public ResponseBean<PageInfo<DeviceAlarmVo>> getCurrentAlarm(@RequestParam(value = "pageNum") String pageNum, @RequestParam(value = "pageSize") String pageSize, DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(deviceService.getCurrentAlarm(PageUtil.pageInfo(pageNum,pageSize,"",""),deviceAlarmDto));
    }


    /**
     * 当前状态列表分页查询
     */
    @GetMapping("getCurrentStatus")
    @ApiOperation("当前状态列表")
    public ResponseBean<PageInfo<DeviceAlarmVo>> getCurrentStatus(@RequestParam(value = "pageNum") String pageNum, @RequestParam(value = "pageSize") String pageSize, DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(deviceService.getCurrentStatus(PageUtil.pageInfo(pageNum,pageSize,"",""),deviceAlarmDto));
    }

    /**
     *历史告警列表
     */
    @GetMapping("getHistoryAlarm")
    @ApiOperation("历史告警列表")
    public ResponseBean<PageInfo<DeviceAlarmVo>> getHistoryAlarm(@RequestParam(value = "pageNum") String pageNum,@RequestParam(value = "pageSize") String pageSize,DeviceAlarmDto deviceAlarmDto){
            return new ResponseBean<>(deviceService.getHistoryAlarm(PageUtil.pageInfo(pageNum,pageSize,"",""),deviceAlarmDto));
    }

    /**
     * 历史状态分页
     */
    @GetMapping("getHistoryStatus")
    @ApiOperation("历史状态分页")
    public ResponseBean<PageInfo<DeviceAlarmVo>> getHistoryStatus(@RequestParam(value = "pageNum") String pageNum,@RequestParam(value = "pageSize") String pageSize,DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(deviceService.getHistoryStatus(PageUtil.pageInfo(pageNum,pageSize,"",""),deviceAlarmDto));
    }
}
