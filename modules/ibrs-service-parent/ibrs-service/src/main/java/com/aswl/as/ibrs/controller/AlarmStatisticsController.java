package com.aswl.as.ibrs.controller;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.*;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.dto.MaintenanceStatisticsDto;
import com.aswl.as.ibrs.api.vo.*;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.service.AlarmStatisticsService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author dingfei
 * @Description  统计报表
 * @date 2019-10-31 15:55
 * @Version V1
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/alarmStatistics", tags = "统计报表")
@RestController
@RequestMapping("/v1/alarmStatistics")
public class AlarmStatisticsController extends BaseController {

    private final AlarmStatisticsService alarmStatisticsService;


    /**
     * 统计报表--->告警级别统计
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "告警级别统计")
    @GetMapping(value = "queryAlarmLevel")
    public ResponseBean<Object> getAlarmlevelData(DeviceAlarmDto deviceAlarmDto){
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime()==null) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay()-1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null ||  "".equals(deviceAlarmDto.getStartTime())) {

            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }/* else {
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
        }*/
        return new ResponseBean<>(alarmStatisticsService.getAlarmLevelData(deviceAlarmDto));
    }

    /**
     * 统计报表--->告警类型统计
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "告警类型统计")
    @GetMapping(value = "queryAlarmTypes")
    public ResponseBean<List<AlarmTypeStatisticsVo>> getAlarmTypesData(DeviceAlarmDto deviceAlarmDto){
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime()==null || "".equals(deviceAlarmDto.getStartTime())) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay()-1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null ||  "".equals(deviceAlarmDto.getStartTime())) {
            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } /*else {
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
        }*/
        return new ResponseBean<>(alarmStatisticsService.getAlarmTypesData(deviceAlarmDto));
    }


    /**
     * 统计报表--->故障维护统计
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "故障维护统计")
    @GetMapping(value = "queryAlarmFault")
    public ResponseBean<List<FlowRunStatisticsVo>> getAlarmFaultData(DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(alarmStatisticsService.getAlarmFaultData(deviceAlarmDto));
    }


    /**
     * 统计报表--->在线率统计
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "在线率统计")
    @GetMapping(value = "onlineRate")
    public ResponseBean<Map> getOnlineRateData(DeviceAlarmDto deviceAlarmDto){

        return new ResponseBean<>(alarmStatisticsService.getOnlineRateData(deviceAlarmDto));
    }


    /**
     * 统计报表--->在线率统计
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "在线率统计列表")
    @GetMapping(value = "onlineList")
    public ResponseBean<List<DeviceAlarmVo>> getOnlineList(DeviceAlarmDto deviceAlarmDto){

        return new ResponseBean<>(alarmStatisticsService.getOnlineList(deviceAlarmDto));
    }


    /**
     * 统计报表--->总体健康率统计
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "总体健康率统计")
    @GetMapping(value = "queryHealthyRate")
    public ResponseBean<Map> getHealthyRateData(DeviceAlarmDto deviceAlarmDto){

        return new ResponseBean<>(alarmStatisticsService.getHealthyRateData(deviceAlarmDto));
    }


    /**
     * 统计报表--->健康指数统计 --->项目健康指数排名
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "项目健康指数排名")
    @GetMapping(value = "healthyTop")
    public ResponseBean<List<Map>> getHealthyTop(DeviceAlarmDto deviceAlarmDto){

        return new ResponseBean<>(alarmStatisticsService.getHealthyTop(deviceAlarmDto));
    }


    /**
     * 统计报表--->总体健康率列表
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "总体健康率统计列表")
    @GetMapping(value = "healthyList")
    public ResponseBean<PageInfo<DeviceAlarmVo>> getHealthyList(@RequestParam(value = CommonConstant.PAGE_NUM, required = false, defaultValue = CommonConstant.PAGE_NUM_DEFAULT) String pageNum,
                                                                @RequestParam(value = CommonConstant.PAGE_SIZE, required = false, defaultValue = CommonConstant.PAGE_SIZE_DEFAULT) String pageSize,
                                                                DeviceAlarmDto deviceAlarmDto){

        return new ResponseBean<>(alarmStatisticsService.getHealthyListPage(PageUtil.pageInfo(pageNum,pageSize,"",""),deviceAlarmDto));
    }

    /**
     * 故障维护统计报表导出
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "故障维护统计报表导出")
    @PostMapping(value = "export")
    public ResponseEntity<byte[]> export(HttpServletResponse response, @RequestBody DeviceAlarmDto deviceAlarmDto) throws Exception {
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
                    throw new CommonException("当前用户暂无数据");
                }
                regionCode = userRegionCode;
            }
        }
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);

        log.info("export...............");
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime() == null) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay() - 1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null || "".equals(deviceAlarmDto.getStartTime())) {

            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
         return alarmStatisticsService.export(response,deviceAlarmDto);
    }

    /**
     * 故障维护统计报表导出
     * @param deviceAlarmDto
     * @return
     */
    @ApiOperation(value = "故障维护统计报表导出")
    @PostMapping(value = "getHealthyListExport")
    public ResponseEntity<byte[]> getHealthyListExport(HttpServletResponse response, @RequestBody DeviceAlarmDto deviceAlarmDto) throws Exception {

        log.info("getHealthyListExport...............");
        //历史记录就是以当前时间往后推7天
        return alarmStatisticsService.getHealthyListExport(response,deviceAlarmDto);
    }

    /**
     * 故障设备统计,区域和型号占比及排名
     */
    @ApiOperation(value = "故障设备统计,区域和型号占比及排名")
    @GetMapping(value = "regionModelPercentTop")
    public ResponseBean<AlarmStatisticsVo> getRegionModelPercentTop(DeviceAlarmDto deviceAlarmDto){
        String[] alarmLevels = deviceAlarmDto.getAlarmLevels();
        if(alarmLevels == null || alarmLevels.length < 1){
            String[] queryAlarmLevels = {"1"};
            deviceAlarmDto.setAlarmLevels(queryAlarmLevels);
        }
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime()==null || "".equals(deviceAlarmDto.getStartTime())) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay()-1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null ||  "".equals(deviceAlarmDto.getStartTime())) {
            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } /*else {
            String startTime = deviceAlarmDto.getStartTime();
            String endTime = deviceAlarmDto.getEndTime();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //LocalDate转DatealarmTypeAnalys
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
        }*/
        return new ResponseBean<>(alarmStatisticsService.getRegionModelPercentTop(deviceAlarmDto));

    }

    /**
     * 故障设备列表
     */
    @ApiOperation(value = "故障列表")
    @GetMapping(value = "wrongList")
    public ResponseBean<List<DeviceAlarmVo>> getWrongList(DeviceAlarmDto deviceAlarmDto){
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime()==null || "".equals(deviceAlarmDto.getStartTime())) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay()-1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null ||  "".equals(deviceAlarmDto.getStartTime())) {
            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } /*else {
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
        }*/
        return new ResponseBean<>(alarmStatisticsService.getWrongList(deviceAlarmDto));
    }

    /**
     *统计报表---->故障趋势分析
     */
    @ApiOperation(value = "故障趋势分析")
    @GetMapping(value = "queryWrongTrend")
    public ResponseBean<Object> queryWrongTrend(DeviceAlarmDto deviceAlarmDto){
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime()==null) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay()-1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null ||  "".equals(deviceAlarmDto.getStartTime())) {

            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } /*else {
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
        }*/
        return new ResponseBean<>(alarmStatisticsService.queryWrongTrend(deviceAlarmDto));
    }

    /**
     * 统计报表---->区域在线占比
     */
    @ApiOperation(value = "区域在线占比")
    @GetMapping(value = "regionOnlineRatio")
    public ResponseBean<List<Map>> regionOnlineRatio(DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(alarmStatisticsService.regionOnlineRatio(deviceAlarmDto));
    }

    /**
     * 统计报表----->派单统计:维护趋势分析
     */
    @ApiOperation(value = "维护趋势分析")
    @GetMapping(value = "maintenanceTrend")
    public ResponseBean<Object> maintenanceTrend(DeviceAlarmDto deviceAlarmDto){
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime()==null) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay()-1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null ||  "".equals(deviceAlarmDto.getStartTime())) {

            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } /*else {
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
        }*/
        return new ResponseBean<>(alarmStatisticsService.maintenanceTrend(deviceAlarmDto));
    }

    /**
     * 维护类型分析
     */
    @ApiOperation(value = "维护类型分析")
    @GetMapping(value = "maintainTypeTrend")
    public ResponseBean<Object> MaintainTypeTrend(DeviceAlarmDto deviceAlarmDto) {
        //历史记录就是以当前时间往后推7天
        Date date = new Date();//取时间
        Calendar calendar  =   Calendar.getInstance();
        calendar.setTime(date); //需要将date数据转移到Calender对象中操作
        if (deviceAlarmDto.getStartTime()==null) {
            calendar.add(calendar.DATE, -(deviceAlarmDto.getDay()-1));//把日期往后增加n天.正数往后推,负数往前移动
        }
        date=calendar.getTime();   //这个时间就是日期往后推一天的结果
        if (deviceAlarmDto.getStartTime() == null ||  "".equals(deviceAlarmDto.getStartTime())) {

            deviceAlarmDto.setStartTime(DateUtils.asLocalDateTime(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            deviceAlarmDto.setEndTime(DateUtils.asLocalDateTime(new Date()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } /*else {
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
        }*/
        return new ResponseBean<>(alarmStatisticsService.maintainTypeTrend(deviceAlarmDto));
    }

    /**
     * 统计报表----->维护率统计
     */
    @ApiOperation(value = "维护率统计")
    @GetMapping(value = "maintenanceRateStatistics")
    public ResponseBean<MaintenanceRateStatisticsVo> maintenanceRateStatistics(DeviceAlarmDto deviceAlarmDto){
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String year = deviceAlarmDto.getYear();
        if(year != null && !"".equals(year)){
            deviceAlarmDto.setStartTime(year+"-01-01 00:00:00");
            deviceAlarmDto.setEndTime(year+"-12-31 23:59:59");
        }
        return new ResponseBean<>(alarmStatisticsService.maintenanceRateStatistics(deviceAlarmDto));
    }

    /**
     * 统计报表------>维护率统计---->派单率和修复率
     */
    @ApiOperation(value = "派单率和修复率")
    @GetMapping(value = "dispatchRepairRate")
    public ResponseBean<Map> dispatchRepairRate(DeviceAlarmDto deviceAlarmDto){
       /* String year = deviceAlarmDto.getYear();
        if(year != null && !"".equals(year)){
            deviceAlarmDto.setStartTime(year+"-01-01 00:00:00");
            deviceAlarmDto.setEndTime(year+"-12-31 23:59:59");
        }*/
        return new ResponseBean<>(alarmStatisticsService.dispatchRepairRate(deviceAlarmDto));
    }

    /**
     * 统计报表------>维护率统计---->月度派单率和修复率
     */
    @ApiOperation(value = "月度派单率和修复率")
    @GetMapping(value = "monthlyDispatchRepairRate")
    public ResponseBean<Map> monthlyDispatchRepairRate(DeviceAlarmDto deviceAlarmDto){
        String tenantCode = SysUtil.getTenantCode();
        String projectId = SysUtil.getProjectId();
        String year = deviceAlarmDto.getYear();
        if(year != null && !"".equals(year)){
            deviceAlarmDto.setStartTime(year+"-01-01 00:00:00");
            deviceAlarmDto.setEndTime(year+"-12-31 23:59:59");
        }
        return new ResponseBean<>(alarmStatisticsService.monthlyDispatchRepairRate(deviceAlarmDto));
    }

    /**
     * 统计报表------>维护率统计---->区域派单率和修复率
     */
    @ApiOperation(value = "区域派单率和修复率")
    @GetMapping(value = "regionDispatchRepairRate")
    public ResponseBean<Map> regionDispatchRepairRate(DeviceAlarmDto deviceAlarmDto){
        String year = deviceAlarmDto.getYear();
        if(year != null && !"".equals(year)){
            deviceAlarmDto.setStartTime(year+"-01-01 00:00:00");
            deviceAlarmDto.setEndTime(year+"-12-31 23:59:59");
        }
        return new ResponseBean<>(alarmStatisticsService.regionDispatchRepairRate(deviceAlarmDto));
    }



    /**
     * 统计报表------>维护统计总表
     */
    @ApiModelProperty(value = "维护统计总表")
    @GetMapping(value = "MaintenanceStatisticsSummary")
    public ResponseBean<List<MaintenanceStatisticsVo>> maintenanceStatisticsSummary(DeviceAlarmDto deviceAlarmDto){
       /* MaintenanceStatisticsVo maintenanceStatisticsVo = alarmStatisticsService.maintenanceStatisticsSummary(deviceAlarmDto);
        if(maintenanceStatisticsVo == null){
            return new ResponseBean<>(null,"该区域下暂无项目");
        }*/
        return new ResponseBean<>(alarmStatisticsService.maintenanceStatisticsSummary(deviceAlarmDto));
    }

    /**
     * 维护统计导出
     */
    @ApiModelProperty(value = "维护统计总表导出")
    @PostMapping("exportExcel")
    public ResponseEntity<byte[]> exportExcel(HttpServletResponse response,@RequestBody MaintenanceStatisticsDto maintenanceStatisticsDto) throws Exception {

        return alarmStatisticsService.exportExcel(response,maintenanceStatisticsDto);
    }

    /**
     * 故障维护统计总表
     */
    @ApiModelProperty(value = "故障维护统计总表")
    @GetMapping("summary")
    public ResponseBean<SummaryVo> summary(DeviceAlarmDto deviceAlarmDto){
        return alarmStatisticsService.summary(deviceAlarmDto);
    }

    /**
     *告警类型分析
     */
    @ApiOperation("告警类型分析")
    @GetMapping("alarmTypeAnalys")
    public ResponseBean<List<AlarmTypeStatisticsVo>> alarmTypeAnalys(DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(alarmStatisticsService.alarmTypeAnalys(deviceAlarmDto));
    }


    /**
     * 故障类型排名
     */
    @ApiOperation("故障类型排名")
    @GetMapping("faultTypeTop")
    public ResponseBean<List<AlarmTypeStatisticsVo>> getFaultTypeTop(DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(alarmStatisticsService.getFaultTypeTop(deviceAlarmDto));
    }


    /**
     * 故障类型设备数统计
     */
    @ApiOperation("故障类型设备数统计")
    @GetMapping("faultTypeDeviceCount")
    public ResponseBean<List<AlarmTypeStatisticsVo>> getFaultTypeDeviceCount(DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(alarmStatisticsService.getFaultTypeDeviceCount(deviceAlarmDto));
    }

    /**
     * 项目区域告警排名
     */
    @ApiOperation("项目区域告警排名")
    @GetMapping("alarmTop")
    public ResponseBean<Map> getAlarmTop(DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(alarmStatisticsService.alarmTop(deviceAlarmDto));
    }


    /**
     * 统计报表->健康指数统计->健康指数趋势分析
     */
    @ApiOperation(value = "统计报表->健康指数统计->健康指数趋势分析")
    @GetMapping(value = "healthyTrend")
    public ResponseBean<Object> getHealthyTrend(DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(alarmStatisticsService.healthyTrend(deviceAlarmDto));
    }


    /**
     * 统计报表->故障维护统计->历史平均修复时长
     */
    @ApiOperation(value = "统计报表->故障维护统计->历史平均修复时长")
    @GetMapping(value = "avgRepairDuration")
    public ResponseBean<Map> getOrderAvgRepairDuration(DeviceAlarmDto deviceAlarmDto){
        return new ResponseBean<>(alarmStatisticsService.getOrderAvgRepairDuration(deviceAlarmDto));
    }
}
