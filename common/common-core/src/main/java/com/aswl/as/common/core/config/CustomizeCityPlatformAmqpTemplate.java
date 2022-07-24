package com.aswl.as.common.core.config;

import com.aswl.as.common.core.constant.MqConstant;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

@Configuration
@Lazy
public class CustomizeCityPlatformAmqpTemplate {

//    @Value("${cityPlatform.ip}")
//    private String ip;
//
//    @Value("${cityPlatform.rabbitUserName}")
//    private String rabbitUserName;
//
//    @Value("${cityPlatform.rabbitPassword}")
//    private String rabbitPassword;
//
//    @Value("${cityPlatform.port}")
//    private String port;

    @Autowired
    private Environment environment;

    @Bean(value = "customizeAmqpTemplate")
    @Lazy
    public AmqpTemplate customizeAmqpTemplate(){
        CachingConnectionFactory customizeConnectionFactory = new CachingConnectionFactory();
        customizeConnectionFactory.setHost(environment.getProperty("cityPlatform.platform.ip"));
        customizeConnectionFactory.setUsername(environment.getProperty("cityPlatform.platform.rabbitUserName"));
        customizeConnectionFactory.setPassword(environment.getProperty("cityPlatform.platform.rabbitPassword"));
        customizeConnectionFactory.setPort(Integer.parseInt(environment.getProperty("cityPlatform.platform.rabbitPort")));
        RabbitTemplate customizeAmqpTemplate = new RabbitTemplate(customizeConnectionFactory);
        return customizeAmqpTemplate;
    }

//    @Bean(value = "customizeContainerFactory")
//    @Lazy
//    public SimpleRabbitListenerContainerFactory customizeContainerFactory(){
//        SimpleRabbitListenerContainerFactory customizeContainerFactory = new SimpleRabbitListenerContainerFactory();
//        CachingConnectionFactory customizeConnectionFactory = new CachingConnectionFactory();
//        Boolean isCityPlatform = environment.getProperty("platform.isCityPlatform",Boolean.class);
//        if(!isCityPlatform){
//            customizeConnectionFactory.setHost(environment.getProperty("cityPlatform.ip"));
//            customizeConnectionFactory.setUsername(environment.getProperty("cityPlatform.rabbitUserName"));
//            customizeConnectionFactory.setPassword(environment.getProperty("cityPlatform.rabbitPassword"));
//            customizeConnectionFactory.setPort(Integer.parseInt(environment.getProperty("cityPlatform.rabbitPort")));
//        }else {
//            customizeConnectionFactory.setHost("192.168.200.225");
//            customizeConnectionFactory.setUsername("aswl_test");
//            customizeConnectionFactory.setPassword("aswl_Test");
//            customizeConnectionFactory.setPort(5672);
//        }
//        customizeContainerFactory.setConnectionFactory(customizeConnectionFactory);
//        return customizeContainerFactory;
//    }

}
