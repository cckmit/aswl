package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.ibrs.api.dto.EventPatrolDto;
import com.aswl.as.ibrs.api.module.PatrolHisNoRecord;
import com.aswl.as.ibrs.api.vo.PatrolHisNoRecordVo;
import com.aswl.as.ibrs.filter.RegionCodeContextHolder;
import com.aswl.as.ibrs.filter.RoleContextHolder;
import com.aswl.as.ibrs.mapper.AsEventHisAlarmMapper;
import com.aswl.as.ibrs.mapper.PatrolHisNoRecordMapper;
import com.aswl.as.ibrs.utils.MonthUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 未巡更Service
 */
@Service
@Slf4j
@AllArgsConstructor
public class PatrolHisNoRecordService extends CrudService<PatrolHisNoRecordMapper,PatrolHisNoRecord>{
    private final PatrolHisNoRecordMapper patrolHisNoRecordMapper;
    private final AsEventHisAlarmMapper asEventHisAlarmMapper;
    /**
     * 根据钥匙ID 巡更开始时间 结束时间 历史表名 查询未巡更记录
     * @param patrolKeyId
     * @param periodBeginTime
     * @param periodEndTime
     * @param tables
     * @return
     */
    public List<PatrolHisNoRecord> findByPatrolKeyIdAndPeriod(String regionCode,String patrolKeyId, String periodBeginTime, String periodEndTime, List<String> tables) {
        return patrolHisNoRecordMapper.findByPatrolKeyIdAndPeriod(regionCode,patrolKeyId,periodBeginTime,periodEndTime,tables);
    }

    /**
     * 批量新增未巡更记录
     * @param patrolHisNoRecordList
     */
    public void batchInsert(List<PatrolHisNoRecord> patrolHisNoRecordList,String noRecordHisTable) {
        patrolHisNoRecordMapper.batchInsert(patrolHisNoRecordList,noRecordHisTable);
    }

    /**
     * 分页查询未巡更记录
     * @param pageNum
     * @param pageSize
     * @param eventPatrolDto
     * @return
     */
    public ResponseBean findByPage(String pageNum, String pageSize, EventPatrolDto eventPatrolDto) {
        
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
        } else {
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
        String queryProjectId = eventPatrolDto.getQueryProjectId();
        if (queryProjectId != null){
            eventPatrolDto.setProjectId(queryProjectId);
        }
        eventPatrolDto.setRegionCode(regionCode);
        String tablePrefix = "as_patrol_his_no_record_";
        String patrolStartTime = eventPatrolDto.getPatrolStartTime();
        String patrolEndTime = eventPatrolDto.getPatrolEndTime();
        List<String> tablesNames = new ArrayList<>();
            List<String> monthBetween = MonthUtils.getMonthBetween(patrolStartTime, patrolEndTime);
            for (String month : monthBetween) {
                tablesNames.add(tablePrefix+month.replaceAll("-",""));
            }
      List<String> tableNameByTime = asEventHisAlarmMapper.findHisPatrolTableNameByTime(tablesNames);
       if(tableNameByTime.size() == 0){
                return new ResponseBean(null,"时间段无数据"); 
       }
       eventPatrolDto.setTableNames(tableNameByTime);
        PageHelper.startPage(Integer.parseInt(pageNum),Integer.parseInt(pageSize));
        return new ResponseBean<>(new PageInfo<>(patrolHisNoRecordMapper.findByPage(eventPatrolDto)));
    }

    /**
     * 上个月有多少未巡更记录
     * @param tableName 历史表名
     * @param previousMonthStart 上个月时间
     * @param previousMonthEnd 上个月时间
     * @return
     */
    public List<PatrolHisNoRecordVo> getNoRecordExamineTime(String tableName, String previousMonthStart, String previousMonthEnd) {

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

        return patrolHisNoRecordMapper.getNoRecordExamineTime(tableName,previousMonthStart,previousMonthEnd,regionCode,tenantCode,projectId);
    }
}
