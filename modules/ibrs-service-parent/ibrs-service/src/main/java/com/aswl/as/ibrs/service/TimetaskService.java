package com.aswl.as.ibrs.service;

import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.service.CrudService;
import com.aswl.as.ibrs.api.module.Timetask;
import com.aswl.as.ibrs.mapper.TimetaskMapper;
import com.aswl.as.ibrs.utils.JobInvokeUtil;
import com.aswl.as.ibrs.utils.ScheduleUtils;
import com.aswl.as.ibrs.utils.SpringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class TimetaskService extends CrudService<TimetaskMapper, Timetask> {
    private final TimetaskMapper timetaskMapper;
    private final Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器
     * 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException {
        Timetask timetask = new Timetask();
        timetask.setIsStart("1");
        timetask.setIsEffect("1");
        List<Timetask> jobList = timetaskMapper.findList(timetask);
        for (Timetask job : jobList) {
            updateSchedulerJob(job);
        }
    }

    /**
     * 新增定时任务
     *
     * @param timetask
     * @return int
     */
    @Transactional
    @Override
    public int insert(Timetask timetask) {
        timetask.setIsEffect("0");
        timetask.setIsStart("0");
        if (timetask.getCronExpression() == null || "".equals(timetask.getCronExpression())) {
            timetask.setCronExpression("* * * * * ?");
        }
        int rows = super.insert(timetask);
//        if (rows > 0) {
//            try {
//                ScheduleUtils.createScheduleJob(scheduler, timetask);
//                return rows;
//            } catch (SchedulerException e) {
//                e.printStackTrace();
//            }
//            return 0;
//        }
        return rows;
    }

    /**
     * 修改定时任务
     * @param timetask
     * @return
     * @throws SchedulerException
     */
    @Transactional
    public int updateScheduler(Timetask timetask) throws SchedulerException {
//        String jobId = timetask.getId();
//        // 判断是否存在
//        JobKey jobKey = ScheduleUtils.getJobKey(jobId);
//        if (scheduler.checkExists(jobKey)) {
//            // 防止创建时存在数据问题 先移除，然后在执行创建操作
//            scheduler.deleteJob(jobKey);
//        }
//        ScheduleUtils.createScheduleJob(scheduler, timetask);

//            String id = timetask.getId();
//            Timetask job = timetaskMapper.get(timetask);
//
//            JobDataMap dataMap = new JobDataMap();
//            dataMap.put("job", job);
//        if (SpringUtils.containsBean(JobInvokeUtil.getBeanName(job.getClassName()))) {
//            scheduler.resumeJob(ScheduleUtils.getJobKey(id));//.triggerJob(ScheduleUtils.getJobKey(id), dataMap);
//        }else {
//            throw  new CommonException("任务类名参数有误");
//        }
        int rows = timetaskMapper.updateScheduler(timetask);
        String id = timetask.getId();
        Timetask task = timetaskMapper.findById(id);
        if("1".equals(task.getIsEffect()) && "1".equals(task.getIsStart())){  //修改了正在运行的任务,移除之前的任务,然后执行创建操作
            JobKey jobKey = ScheduleUtils.getJobKey(id);
            if(scheduler.checkExists(jobKey)){
                scheduler.deleteJob(jobKey);
            }
            ScheduleUtils.createScheduleJob(scheduler,task);
        }
            return rows;
    }

    /**
     * 删除定时任务
     * @param timetask
     * @return
     * @throws SchedulerException
     */
    @Transactional
    public int deleteScheduler(Timetask timetask) throws SchedulerException {
        String id = timetask.getId();
        int rows = timetaskMapper.deleteScheduler(timetask);
        JobKey jobKey = ScheduleUtils.getJobKey(id);
        if (rows > 0 && scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
            return rows;
        }
        return 0;
    }

    /**
     * 启动定时任务
     * @param timetask
     * @throws SchedulerException
     */
    public void run(Timetask timetask) throws SchedulerException {
        String id = timetask.getId();
        Timetask job = timetaskMapper.get(timetask);
        job.setIsStart("1");
        int rows = timetaskMapper.updateScheduler(job);
        if (rows > 0) {
            updateSchedulerJob(job);
        }
        //参数
       /* JobDataMap dataMap = new JobDataMap();
        dataMap.put("job",job);
        scheduler.triggerJob(ScheduleUtils.getJobKey(id),dataMap);*/
    }

    /**
     * 停止定时任务
     * @param job
     * @return
     * @throws SchedulerException
     */
    @Transactional
    public int stop(Timetask job) throws SchedulerException {
        String id = job.getId();
        Timetask timetask = timetaskMapper.get(job);
        timetask.setIsStart("0");
        int rows = timetaskMapper.updateScheduler(timetask);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(id));
        }
        return rows;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    @Transactional
    public int resumeJob(Timetask job) throws SchedulerException {
        String id = job.getId();
        job.setIsStart("1");
        int rows = timetaskMapper.updateScheduler(job);
        if (rows > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(id));
        }
        return rows;
    }

    /**
     * 修改定时任务信息
     * @param job
     * @throws SchedulerException
     */
    public void updateSchedulerJob(Timetask job) throws SchedulerException {
        String jobId = job.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
}