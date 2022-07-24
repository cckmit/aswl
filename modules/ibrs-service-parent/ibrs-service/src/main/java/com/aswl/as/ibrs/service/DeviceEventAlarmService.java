package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.constant.CommonConstant;
import com.aswl.as.common.core.enums.AlarmLevelType;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.CityAlarmStatisticsDto;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.CityAlarmStatistics;
import com.aswl.as.ibrs.api.module.CityRunTimeStatistics;
import com.aswl.as.ibrs.api.module.DeviceEventAlarm;
import com.aswl.as.ibrs.api.vo.AlarmTypeUserFavoriteVo;
import com.aswl.as.ibrs.api.vo.DeviceStatusVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.AlarmStatisticsMapper;
import com.aswl.as.ibrs.mapper.AlarmTypeUserFavoriteMapper;
import com.aswl.as.ibrs.mapper.DeviceEventAlarmMapper;
import com.aswl.as.ibrs.mapper.DeviceMapper;
import com.aswl.as.ibrs.utils.StringUtils;
import com.aswl.as.user.api.dto.UserInfoDto;
import com.aswl.as.user.api.feign.UserServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@AllArgsConstructor
@Slf4j
@Service
public class DeviceEventAlarmService extends CrudService<DeviceEventAlarmMapper, DeviceEventAlarm> {
    private final DeviceEventAlarmMapper deviceEventAlarmMapper;
    private final UserServiceClient userServiceClient;
    private final AlarmTypeUserFavoriteMapper favoriteMapper;
    private final DeviceMapper deviceMapper;
    private final AlarmStatisticsMapper statisticsMapper;

    /**
     * 根据区域查询告警级别统计
     * @return list
     */
    public Map getAlarmLevelCounts(String isAsOs){
        Map map=new LinkedHashMap();

        String regionCode="";
        String tenantCode=SysUtil.getTenantCode();
        String projectId=SysUtil.getProjectId();
        String role = RoleContextHolder.getRole();
        if (CommonConstant.IS_ASOS_TRUE.equals(isAsOs)){
            tenantCode = null;
            projectId = null;
        }else {
            if(SysUtil.isAdmin()){
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
        // 一级故障数量
        Map oneAlarmCounts = deviceEventAlarmMapper.getAlarmLevelCounts(regionCode, AlarmLevelType.FAULT.getType(),tenantCode,projectId);
        //二级预警数量
        Map twoAlarmCounts = deviceEventAlarmMapper.getAlarmLevelCounts(regionCode, AlarmLevelType.WARNING.getType(),tenantCode,projectId);
        //三级正常数量
        Map threeAlarmCounts = deviceEventAlarmMapper.getAlarmLevelCounts(regionCode, AlarmLevelType.NORMAL.getType(),tenantCode,projectId);
        map.put("故障",oneAlarmCounts.get("counts"));
        map.put("预警",twoAlarmCounts.get("counts"));
        map.put("正常",threeAlarmCounts.get("counts"));
        return map;

    }

    /**
     * 根据区域查询告警级别统计
     * @return list
     */
    public List<Map> getAlarmTypesCount(String isAsOs){
        List<Map> maps= new ArrayList<Map>();
        String userId="";
        String regionCode="";
        String tenantCode=SysUtil.getTenantCode();
        String projectId=SysUtil.getProjectId();
        String role = RoleContextHolder.getRole();
        ResponseBean<UserInfoDto> info = userServiceClient.info();
        if (info.getCode() == 200) {
            userId = info.getData().getId();
        }
        if (CommonConstant.IS_ASOS_TRUE.equals(isAsOs)){
            tenantCode = null;
            projectId = null;
        }else {
            if(SysUtil.isAdmin()){
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

        List<AlarmTypeUserFavoriteVo> list = favoriteMapper.findAlarmUserFavoriteTypeLists(userId);
        if (list!=null && list.size()>0){
            for (AlarmTypeUserFavoriteVo vo:list) {
                Map map = deviceEventAlarmMapper.getAlarmTypeCounts(regionCode,vo.getAlarmType(),tenantCode,projectId);
                map.put("alarmType",vo.getAlarmType());
                map.put("alarmTypeName",vo.getAlarmTypeName());
                map.put("counts",map.get("counts"));
                maps.add(map);
            }
        }
        return maps;
    }

    public Map getHealthyData(String isAsOs,String type,String queryProjectId, String queryRegionCode) {
        String roles = RoleContextHolder.getRole();
        String regionCode = queryRegionCode;
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
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        DeviceAlarmDto deviceAlarmDto = new DeviceAlarmDto();
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        if (StringUtils.isNotEmpty(queryProjectId)){
            deviceAlarmDto.setProjectId(queryProjectId);
        }
        deviceAlarmDto.setRegionCode(regionCode);
        deviceAlarmDto.setKind(type);
        List<String> table = new ArrayList<>();
        table.add("as_device_event_alarm");
        deviceAlarmDto.setTableNames(table);
        return statisticsMapper.getHealthyRateData(deviceAlarmDto);

    }

    public int getHealthyDataOs(String isAsOs) {
        DecimalFormat df = new DecimalFormat("0.0000");
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
                if (userRegionCode == null || "".equals(userRegionCode)) {
                    return 0;
                }
                regionCode = userRegionCode;
            }
        }
        DeviceAlarmDto deviceAlarmDto = new DeviceAlarmDto();
        deviceAlarmDto.setTenantCode(tenantCode);
        deviceAlarmDto.setProjectId(projectId);
        deviceAlarmDto.setRegionCode(regionCode);
        List<String> table = new ArrayList<>();
        table.add("as_device_event_alarm");
        deviceAlarmDto.setTableNames(table);
        Map map = statisticsMapper.getHealthyRateData(deviceAlarmDto);
        int deviceCount = ((Long) map.get("deviceCount")).intValue();
        int alarmCount = ((BigDecimal) map.get("alarmCount")).intValue();
        if (deviceCount == 0) {
            return 0;
        }
        String format = df.format((float) (deviceCount - alarmCount) / deviceCount);
        return (int) (Double.parseDouble(format) * 100);
    }

    /**
     * 当天每个告警类型的gaojing数
     * @param month
     * @param today
     * @param type
     */
    public List<CityAlarmStatistics> findTodayAlarmCount(String month, String today, String type) {
        return deviceEventAlarmMapper.findTodayAlarmCount(month,today,type);
    }

    /**
     * 每个时间段的告警数
     * @param yearMonth
     * @param today
     * @param type
     * @return
     */
    public CityRunTimeStatistics findPeriodAlarmNum(String yearMonth, String today, String type) {
        return deviceEventAlarmMapper.findPeriodAlarmNum(yearMonth,today,type);
    }

    /**
     * 根据设备ID获取当前报警数据
     * @param deviceId
     * @return
     */
    public DeviceEventAlarm findByDeviceId(String deviceId){
        return deviceEventAlarmMapper.findByDeviceId(deviceId);
    }

    public void deleteByDeviceIds(String[] idsArr) {
        deviceEventAlarmMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }

    /**
     * 市级平台设备详情-->设备状态
     *
     * @param id 设备ID
     * @return ResponseBean
     */
    public List<DeviceStatusVo> getDeviceStatusData(String id) {
        return deviceEventAlarmMapper.getDeviceStatusData(id);
    }
}