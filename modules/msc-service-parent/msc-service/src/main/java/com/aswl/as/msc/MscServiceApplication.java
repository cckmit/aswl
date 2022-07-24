package com.aswl.as.msc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
// 扫描api包里的FeignClient
@EnableFeignClients(basePackages = {"com.aswl.as"})
@ComponentScan(basePackages = {"com.aswl.as"})
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCircuitBreaker
public class MscServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MscServiceApplication.class, args);
    }

}
