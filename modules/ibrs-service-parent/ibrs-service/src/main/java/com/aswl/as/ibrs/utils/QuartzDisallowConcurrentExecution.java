package com.aswl.as.ibrs.utils;

import com.aswl.as.ibrs.api.module.Timetask;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 * 
 *
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob
{
    @Override
    protected void doExecute(JobExecutionContext context, Timetask sysJob) throws Exception
    {
        JobInvokeUtil.invokeMethod(sysJob);
    }
}
