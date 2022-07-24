package com.aswl.as.metadata.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig /* implements AsyncConfigurer */ {

	@Bean
	public Executor getTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(100);	// 核心线程数量
		threadPoolTaskExecutor.setMaxPoolSize(1000);	// 最大线程数量
		threadPoolTaskExecutor.setQueueCapacity(100000);	// 队列中最大任务数
		threadPoolTaskExecutor.setThreadNamePrefix("ThreadPool-");	// 线程名称前缀
		threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());	// 当达到最大线程数时如何处理新任务
		threadPoolTaskExecutor.setKeepAliveSeconds(60);	// 线程空闲后最大存活时间
		threadPoolTaskExecutor.initialize();	// 初始化线程池
		return threadPoolTaskExecutor;
	}

}
