package com.aswl.as.iface;

import com.aswl.as.iface.utils.JwtUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableDiscoveryClient
// 扫描api包里的FeignClient
@EnableFeignClients(basePackages = {"com.aswl.as"})
@ComponentScan(basePackages = {"com.aswl.as"})
// @EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableCircuitBreaker
@EnableAsync
public class IfaceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IfaceServiceApplication.class, args);
    }
    /*
    这里是写死的,先注释掉,改成了动态从token中获取serviceId
    @Value(value = "${wsBaseParam.serviceId}")
    private String serviceId;

    @Value(value = "${wsBaseParam.srcret}")
    private String srcret;

    @Bean
    public WsBaseParam wsBaseParam(){
        WsBaseParam wsBaseParam = new WsBaseParam();
        wsBaseParam.setServiceId(serviceId);
        wsBaseParam.setKey(CustomMd5.MD5(serviceId+"_"+srcret+"_"+new SimpleDateFormat("yyyyMMddHHmm").format(new Date())));
        return wsBaseParam;
    }
    */
    // @Bean
    // public IBzptService getBzptServiceImplPort(){
    //     return new BzptServiceImpl().getBzptServiceImplPort();
    // }
    /**
     * JWT签发token
     */
   /* @Bean
    public JwtBuilder jwts(){
        return Jwts.builder();
    }*/
    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "consumer")
    public ThreadPoolTaskExecutor taskExecutor() {

        ThreadPoolTaskExecutor executor =new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("Pool-A");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        return executor;
    }
}


