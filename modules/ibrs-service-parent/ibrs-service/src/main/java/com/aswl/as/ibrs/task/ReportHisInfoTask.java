package com.aswl.as.ibrs.task;
import com.aswl.as.common.core.utils.IdGen;
import com.aswl.as.ibrs.api.module.Project;
import com.aswl.as.ibrs.api.module.ReportHisInfo;
import com.aswl.as.ibrs.service.ProjectService;
import com.aswl.as.ibrs.service.ReportHisInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 统计报表定时任务
 * @author df
 * @date 2021/07/20 18:55
 */
@Component
public class ReportHisInfoTask {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ReportHisInfoService reportHisInfoService;
    
    
    @Async
   // @Scheduled(cron = "0 30 10 ? * *" )
   @Scheduled(cron = "0 0 0 1 * ?")
    public void insertReportHisInfo() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取所有项目
        List<Project> projectList =  projectService.findList(new Project());
        String [] date = getUpMonthStartEndDate().split(":");
        for (Project p : projectList) {
            // 故障告警统计
            ReportHisInfo reportHisInfo = new ReportHisInfo();
            reportHisInfo.setId(IdGen.snowflakeId());
            reportHisInfo.setProjectId(p.getProjectId());
            reportHisInfo.setStatisType("故障告警统计");
            reportHisInfo.setStatisStartTime(simpleDateFormat.parse(date[0]));
            reportHisInfo.setStatisEndTime(simpleDateFormat.parse(date[1]));
            reportHisInfo.setDocFormat("pdf");
            reportHisInfo.setCreateDate( new Date());
            reportHisInfo.setApplicationCode(p.getApplicationCode());
            reportHisInfo.setTenantCode(p.getTenantCode());
            reportHisInfoService.insert(reportHisInfo);
            
            // 故障设备统计
            ReportHisInfo reportHisInfo1 = new ReportHisInfo();
            reportHisInfo1.setId(IdGen.snowflakeId());
            reportHisInfo1.setProjectId(p.getProjectId());
            reportHisInfo1.setStatisType("故障设备统计");
            reportHisInfo1.setStatisStartTime(simpleDateFormat.parse(date[0]));
            reportHisInfo1.setStatisEndTime(simpleDateFormat.parse(date[1]));
            reportHisInfo1.setDocFormat("pdf");
            reportHisInfo1.setCreateDate( new Date());
            reportHisInfo1.setApplicationCode(p.getApplicationCode());
            reportHisInfo1.setTenantCode(p.getTenantCode());
            reportHisInfoService.insert(reportHisInfo1);
            
            //故障维护统计
            ReportHisInfo reportHisInfo2 = new ReportHisInfo();
            reportHisInfo2.setId(IdGen.snowflakeId());
            reportHisInfo2.setProjectId(p.getProjectId());
            reportHisInfo2.setStatisType("故障维护统计");
            reportHisInfo2.setStatisStartTime(simpleDateFormat.parse(date[0]));
            reportHisInfo2.setStatisEndTime(simpleDateFormat.parse(date[1]));
            reportHisInfo2.setDocFormat("pdf");
            reportHisInfo2.setCreateDate( new Date());
            reportHisInfo2.setApplicationCode(p.getApplicationCode());
            reportHisInfo2.setTenantCode(p.getTenantCode());
            reportHisInfoService.insert(reportHisInfo2);
            
            // 在线率统计
            ReportHisInfo reportHisInfo3 = new ReportHisInfo();
            reportHisInfo3.setId(IdGen.snowflakeId());
            reportHisInfo3.setProjectId(p.getProjectId());
            reportHisInfo3.setStatisType("在线率统计");
            reportHisInfo3.setStatisStartTime(simpleDateFormat.parse(date[0]));
            reportHisInfo3.setStatisEndTime(simpleDateFormat.parse(date[1]));
            reportHisInfo3.setDocFormat("pdf");
            reportHisInfo3.setCreateDate( new Date());
            reportHisInfo3.setApplicationCode(p.getApplicationCode());
            reportHisInfo3.setTenantCode(p.getTenantCode());
            reportHisInfoService.insert(reportHisInfo3);
            
            // 健康指数统计
            ReportHisInfo reportHisInfo4 = new ReportHisInfo();
            reportHisInfo4.setId(IdGen.snowflakeId());
            reportHisInfo4.setProjectId(p.getProjectId());
            reportHisInfo4.setStatisType("健康指数统计");
            reportHisInfo4.setStatisStartTime(simpleDateFormat.parse(date[0]));
            reportHisInfo4.setStatisEndTime(simpleDateFormat.parse(date[1]));
            reportHisInfo4.setDocFormat("pdf");
            reportHisInfo4.setCreateDate( new Date());
            reportHisInfo4.setApplicationCode(p.getApplicationCode());
            reportHisInfo4.setTenantCode(p.getTenantCode());
            reportHisInfoService.insert(reportHisInfo4);

            // 用电量统计
            ReportHisInfo reportHisInfo5 = new ReportHisInfo();
            reportHisInfo5.setId(IdGen.snowflakeId());
            reportHisInfo5.setProjectId(p.getProjectId());
            reportHisInfo5.setStatisType("用电量统计");
            reportHisInfo5.setStatisStartTime(simpleDateFormat.parse(date[0]));
            reportHisInfo5.setStatisEndTime(simpleDateFormat.parse(date[1]));
            reportHisInfo5.setDocFormat("pdf");
            reportHisInfo5.setCreateDate( new Date());
            reportHisInfo5.setApplicationCode(p.getApplicationCode());
            reportHisInfo5.setTenantCode(p.getTenantCode());
            reportHisInfoService.insert(reportHisInfo5);

            // 运维考核统计
            ReportHisInfo reportHisInfo6 = new ReportHisInfo();
            reportHisInfo6.setId(IdGen.snowflakeId());
            reportHisInfo6.setProjectId(p.getProjectId());
            reportHisInfo6.setStatisType("运维考核统计");
            reportHisInfo6.setStatisStartTime(simpleDateFormat.parse(date[0]));
            reportHisInfo6.setStatisEndTime(simpleDateFormat.parse(date[1]));
            reportHisInfo6.setDocFormat("pdf");
            reportHisInfo6.setCreateDate( new Date());
            reportHisInfo6.setApplicationCode(p.getApplicationCode());
            reportHisInfo6.setTenantCode(p.getTenantCode());
            reportHisInfoService.insert(reportHisInfo6);

            // 运维汇总统计
            ReportHisInfo reportHisInfo7 = new ReportHisInfo();
            reportHisInfo7.setId(IdGen.snowflakeId());
            reportHisInfo7.setProjectId(p.getProjectId());
            reportHisInfo7.setStatisType("运维汇总统计");
            reportHisInfo7.setStatisStartTime(simpleDateFormat.parse(date[0]));
            reportHisInfo7.setStatisEndTime(simpleDateFormat.parse(date[1]));
            reportHisInfo7.setDocFormat("pdf");
            reportHisInfo7.setCreateDate( new Date());
            reportHisInfo7.setApplicationCode(p.getApplicationCode());
            reportHisInfo7.setTenantCode(p.getTenantCode());
            reportHisInfoService.insert(reportHisInfo7);
        }
        
    }

    /**
     * 获取上月的开始日期和结束日期
     * @return
     */
    public String getUpMonthStartEndDate(){
        Calendar c=Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String gtimelast = sdf.format(c.getTime()); //上月
        System.out.println(gtimelast);
        int lastMonthMaxDay=c.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(lastMonthMaxDay);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), lastMonthMaxDay, 23, 59, 59);
        //按格式输出
        String gtime = sdf.format(c.getTime()); //上月最后一天
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-01");
        String gtime2 = sdf2.format(c.getTime()); //上月第一天
        return gtime2 + ":" +gtime ;
    }
}
