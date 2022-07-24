package com.aswl.as.ibrs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/11/13 9:53
 */
@Configuration
public class ScheduleConfig {

    /**
     * attention:
     * Details：定义quartz调度工厂
     */
    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setApplicationContextSchedulerContextKey("applicationContextKey");
        bean.setSchedulerName("asScheduler");
        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        bean.setOverwriteExistingJobs(true);
        // 延时启动，应用启动1秒后
        bean.setStartupDelay(1);
        return bean;
    }
}
