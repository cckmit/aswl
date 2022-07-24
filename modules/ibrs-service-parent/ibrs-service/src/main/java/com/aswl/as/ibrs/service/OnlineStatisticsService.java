package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.DeviceAlarmDto;
import com.aswl.as.ibrs.api.module.OnlineStatistics;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.AsEventNetworkMapper;
import com.aswl.as.ibrs.mapper.OnlineStatisticsMapper;
import com.aswl.as.ibrs.mapper.RegionMapper;
import com.aswl.as.ibrs.utils.StringUtils;
import com.aswl.as.user.api.feign.UserServiceClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
@AllArgsConstructor
@Slf4j
@Service
public class OnlineStatisticsService extends CrudService<OnlineStatisticsMapper, OnlineStatistics> {
    private final OnlineStatisticsMapper onlineStatisticsMapper;
    private final RegionMapper regionMapper;
    private final AsEventNetworkMapper networkMapper;

    /**
     * 在线率统计
     * @param kind  设备种类（1：光纤收发器，2：传输箱，3：摄像机）
     * @return map
     */
    public Map onlineRateStatistics(String kind) {
        Map<String,Object> map=new HashMap<>();
        String roles = RoleContextHolder.getRole();
        String regionCode = null;
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
            String userRegionCode = RegionCodeContextHolder.getRegionCode();
            if(userRegionCode == null || "".equals(userRegionCode)){
                return null;
            }
            regionCode = userRegionCode;
        }
        Double onlineRate=0.0d;
        Double monthRate=0.0d;
        Double yearRate=0.0d;
        Double setBoxOnlineRate =0.0d;
        Double setCameraOnlineRate =0.0d;
        // 查询设备总数
        int boxCounts = networkMapper.findBoxCounts(regionCode,kind,tenantCode,projectId);
        //设置的设备数量
        Map mapNum = regionMapper.findByProjectId(Arrays.asList(projectId.split(",")));
        Integer boxNum = mapNum.get("boxNum")!= null ? Integer.parseInt( mapNum.get("boxNum").toString()) : 0;
        Integer cameraNum = mapNum.get("cameraNum")!= null ? Integer.parseInt(mapNum.get("cameraNum").toString()) : 0;
        // 在线设备数
        int onLineBoxCounts = networkMapper.findOnLineBoxCounts(regionCode,kind,tenantCode,projectId);
        //当前在线率
        onlineRate = onLineBoxCounts > 0 ? new BigDecimal((double)onLineBoxCounts/boxCounts*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() : onlineRate;
        setBoxOnlineRate = boxNum > 0 ? new BigDecimal((double)onLineBoxCounts/boxNum*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() : setBoxOnlineRate;
        setCameraOnlineRate = cameraNum > 0 ? new BigDecimal((double)onLineBoxCounts/cameraNum*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue() : setCameraOnlineRate;
        // 本月在线率
        Map monthMap = onlineStatisticsMapper.getOnlineStatisticsCounts("month", regionCode,kind,tenantCode,projectId);
        if (Integer.parseInt(monthMap.get("onlineNum").toString())==0){
            if (onLineBoxCounts > 0) {
                monthRate = new BigDecimal((double) onLineBoxCounts / boxCounts * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }else {
               monthRate =new BigDecimal((double)Integer.parseInt(monthMap.get("onlineNum").toString())/Integer.parseInt(monthMap.get("deviceNum").toString())*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        Map yearMap = onlineStatisticsMapper.getOnlineStatisticsCounts("year", regionCode,kind,tenantCode,projectId);
        if (Integer.parseInt(yearMap.get("onlineNum").toString())==0) {
            if (Integer.parseInt(monthMap.get("onlineNum").toString()) > 0) {
                yearRate = new BigDecimal((double) Integer.parseInt(monthMap.get("onlineNum").toString()) / Integer.parseInt(monthMap.get("deviceNum").toString()) * 100).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }else{
            yearRate = new BigDecimal((double)Integer.parseInt(yearMap.get("onlineNum").toString())/Integer.parseInt(yearMap.get("deviceNum").toString())*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        map.put("now",onlineRate);
        if ("2".equals(kind)) {
            map.put("setBoxOnlineRate", setBoxOnlineRate);
        }
        if ("3".equals(kind)) {
            map.put("setCameraOnlineRate", setCameraOnlineRate);
        }
        map.put("month",monthRate);
        map.put("year",yearRate);
        return map;
    }

    /**
     * 批量新增
     */
    @Transactional
    public void bathInsert(List<OnlineStatistics> list){
        onlineStatisticsMapper.bathInsert(list);
    }

    /**
     * 根据区域和设备种类查询设备总数和在线数量
     */
    public List<OnlineStatistics> getByRegionCodeAndDeviceKind(String regionCode, String deviceKind, String day, String hisTable){
        return onlineStatisticsMapper.getByRegionCodeAndDeviceKind(regionCode, deviceKind, day, hisTable);
    }

    /**
     * 设备在线趋势分析
     */
    public List<Map> queryOnlineTrend(DeviceAlarmDto deviceAlarmDto){
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

        return onlineStatisticsMapper.queryOnlineTrend(deviceAlarmDto);
    }

    public static void main(String[] args) {
        BigDecimal b = new BigDecimal(((float) 2/13)*100);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue(); //ROUND_HALF_UP表明四舍五入，
        System.out.println(f1);
    }

    public OnlineStatistics getByDeviceKind(String kind) {
        return onlineStatisticsMapper.getByDeviceKind(kind);
    }

    public OnlineStatistics getByCondition(Date statisticsDate, String deviceKind, String projectId) {
        return onlineStatisticsMapper.getByCondition(statisticsDate,deviceKind,projectId);
    }

    /**
     * 删除指定日期的统计数据
     * @param createDate
     * @return
     */
    public int deleteByCreateDate(String createDate){
        if(StringUtils.isEmpty(createDate)){
            return 0;
        }
        return onlineStatisticsMapper.deleteByCreateDate(createDate);
    }
    
}