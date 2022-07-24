package com.aswl.as.ibrs.service;
import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.EventPatrolDto;
import com.aswl.as.ibrs.api.module.EventPatrol;
import com.aswl.as.ibrs.api.vo.EventPatrolVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.AsEventHisAlarmMapper;
import com.aswl.as.ibrs.mapper.EventPatrolMapper;
import com.aswl.as.user.api.feign.UserServiceClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
@Service
@Slf4j
public class EventPatrolService extends CrudService<EventPatrolMapper, EventPatrol> {

    private final EventPatrolMapper eventPatrolMapper;

    public PageInfo<EventPatrolVo> findByPage(String pageNum, String pageSize, EventPatrolDto eventPatrolDto) {
        //默认最近7天
        if(eventPatrolDto.getPatrolStartTime() == null || eventPatrolDto.getPatrolEndTime() == null){
            Date date = new Date();
            Calendar instance = Calendar.getInstance();
            instance.setTime(date);
            eventPatrolDto.setPatrolStartTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd"));
            instance.add(Calendar.DAY_OF_MONTH,-6);
            eventPatrolDto.setPatrolEndTime(DateUtils.formatDate(instance.getTime(),"yyyy-MM-dd"));
        }
        String roles = RoleContextHolder.getRole();
        String regionCode = eventPatrolDto.getRegionCode();
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
        }else {
            if(regionCode == null || "".equals(regionCode)){
                String userRegionCode = RegionCodeContextHolder.getRegionCode();
                if(userRegionCode == null || "".equals(userRegionCode)){
                    return null;
                }
                regionCode = userRegionCode;
            }
        }
        eventPatrolDto.setTenantCode(tenantCode);
        eventPatrolDto.setProjectId(projectId);
        String queryProjectId =eventPatrolDto.getQueryProjectId();
        if (queryProjectId!= null){
            eventPatrolDto.setProjectId(queryProjectId);
        }
        eventPatrolDto.setRegionCode(regionCode);
        //判断查询的时间是当前还是历史
        List<String> tables = new ArrayList<>();
        String currentTable = "as_event_bt_patrol";
        String histTablePrefix = "as_event_his_bt_patrol_";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if (eventPatrolDto.getPatrolStartTime().substring(0, 10).equals(format.format(date)) &&
                eventPatrolDto.getPatrolEndTime().substring(0, 10).equals(format.format(date))) {
            //查询当前的巡更
            tables.add(currentTable);
            eventPatrolDto.setTableNames(tables);
        } else {
            //获取两个日期之间所有的月份
            List<String> monthList = DateUtils.getMonthBetween(eventPatrolDto.getPatrolStartTime(), eventPatrolDto.getPatrolEndTime());
            for (String month : monthList) {
                tables.add(histTablePrefix + month.replaceAll("-", ""));
            }
            eventPatrolDto.setTableNames(tables);
        }
        PageHelper.startPage(Integer.parseInt(pageNum), Integer.parseInt(pageSize));
        List<EventPatrolVo> patrolVoList = eventPatrolMapper.findByPage(eventPatrolDto);
        return new PageInfo<>(patrolVoList);
    }
    /**
     * 按照区域负责人的巡更周期查询当前巡更记录
     * @param patrolKeyId 巡更钥匙Id
     * @param periodBeginTime 巡更开始时间
     * @param periodEndTime 巡更结束时间
     * @return 当前已巡更设备的id集合
     */
    public List<String> findByPeriod(String patrolKeyId,String periodBeginTime,String periodEndTime) {
        return eventPatrolMapper.findByPeriod(patrolKeyId,periodBeginTime,periodEndTime);
    }

    public void deleteByDeviceIds(String[] idsArr) {
        eventPatrolMapper.deleteByDeviceIds(Arrays.toString(idsArr));
    }
}
