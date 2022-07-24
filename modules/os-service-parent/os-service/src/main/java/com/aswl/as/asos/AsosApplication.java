/**
 * Copyright (c) 2019 aswl.com All rights reserved.
 *
 * https://www.gzaswl.net
 *
 * 2019.11
 */

package com.aswl.as.asos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableDiscoveryClient
// 扫描api包里的FeignClient
@EnableFeignClients(basePackages = {"com.aswl.as"})
@ComponentScan(basePackages = {"com.aswl.as"})
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCircuitBreaker
@EnableAsync
@EnableScheduling
// 扫描api包里的FeignClient
public class AsosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsosApplication.class, args);
	}

}