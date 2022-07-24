package com.aswl.as.common.log;

import com.aswl.as.common.log.aspect.LogAspect;
import com.aswl.as.common.log.event.LogListener;
import com.aswl.as.user.api.feign.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author aswl.com
 * @date 2019/3/12 23:51
 */
@EnableAsync
@Configuration
@ConditionalOnWebApplication
public class LogAutoConfiguration {

    @Autowired
    private UserServiceClient userServiceClient;

    @Bean
    public LogListener sysLogListener() {
        return new LogListener(userServiceClient);
    }

    @Bean
    public LogAspect sysLogAspect() {
        return new LogAspect();
    }
}
