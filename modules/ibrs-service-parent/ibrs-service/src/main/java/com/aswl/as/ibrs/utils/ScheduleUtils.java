package com.aswl.as.ibrs.utils;


import com.aswl.as.ibrs.api.module.Timetask;
import org.quartz.*;

/**
 * 定时任务工具类
 * 
 *
 */
public class ScheduleUtils {

    /**
     * 得到quartz任务类
     *
     * @param sysJob 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(Timetask sysJob)
    {
        boolean isConcurrent = "0".equals(sysJob.getIsStart());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(String jobId) {
        return TriggerKey.triggerKey(jobId);
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(String jobId) {
        return JobKey.jobKey(jobId);
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, Timetask job) {
        try {
            //构建job
            //Class<? extends Job> jobClass = getQuartzJobClass(job);
            Class<? extends Job> clazz = (Class<? extends Job>) Class.forName(job.getClassName());
            // 构建job信息
            String jobId = job.getId();
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(getJobKey(jobId)).build();

            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId))
                    .withSchedule(cronScheduleBuilder).build();

            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put("job", job);

            // 判断是否存在
            if (scheduler.checkExists(getJobKey(jobId))) {
                // 防止创建时存在数据问题 先移除，然后在执行创建操作
                scheduler.deleteJob(getJobKey(jobId));
            }

            scheduler.scheduleJob(jobDetail, trigger);

            // 暂停任务
            if (job.getIsStart().equals("0")&&job.getIsEffect().equals("0")) {
                scheduler.pauseJob(ScheduleUtils.getJobKey(jobId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}