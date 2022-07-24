package com.aswl.as.ibrs.utils;

import com.aswl.as.ibrs.api.module.Timetask;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 * 
 *
 */
public class QuartzJobExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, Timetask sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
