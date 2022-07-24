package com.aswl.as.common.core.config;

import com.aswl.as.common.core.constant.MqConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class CityPlatformMqConfig {


    @Bean
//    @Lazy
    public DirectExchange cityPlatformExchange(){
        return new DirectExchange(MqConstant.CITY_PLATFORM_EXCHANGE,true,false);
    }

    @Bean
//    @Lazy
    public Queue cityPlatformQueue(){
        return new Queue(MqConstant.CITY_PLATFORM_QUEUE);
    }

    @Bean
//    @Lazy
    public Binding bindingcityPlatformQueue(DirectExchange cityPlatformExchange,Queue cityPlatformQueue){
        return BindingBuilder.bind(cityPlatformQueue).to(cityPlatformExchange).with(MqConstant.CITY_PLATFORM_QUEUE);
    }

    @Bean
//    @Lazy
    public Queue cityPlatformDeviceEvent(){
        return new Queue(MqConstant.CITY_PLATFORM_DEVICEEVENT_QUEUE);
    }

    @Bean
//    @Lazy
    public Binding bindingCityDeviceEvent(DirectExchange cityPlatformExchange,Queue cityPlatformDeviceEvent){
        return BindingBuilder.bind(cityPlatformDeviceEvent).to(cityPlatformExchange).with(MqConstant.CITY_PLATFORM_DEVICEEVENT_QUEUE);
    }

    @Bean()
//    @Lazy
    public Queue cityPlatAlarmCount(){
        return new Queue(MqConstant.CITY_PLATFORM_ALARM_COUNT_QUEUE);
    }

    @Bean
//    @Lazy
    public Binding bindingAlarmCount(DirectExchange cityPlatformExchange,Queue cityPlatAlarmCount){
        return BindingBuilder.bind(cityPlatAlarmCount).to(cityPlatformExchange).with(MqConstant.CITY_PLATFORM_ALARM_COUNT_QUEUE);
    }

    @Bean
    public Queue cityPlatOnlineStatist(){
        return new Queue(MqConstant.CITY_PLATFORM_ONLINESTATICS_QUEUE);
    }

    @Bean
//    @Lazy
    public Binding bindingOnlineStatist(DirectExchange cityPlatformExchange,Queue cityPlatOnlineStatist){
        return BindingBuilder.bind(cityPlatOnlineStatist).to(cityPlatformExchange).with(MqConstant.CITY_PLATFORM_ONLINESTATICS_QUEUE);
    }


    @Bean
    public Queue cityPlatAlarmStatistics(){
        return new Queue(MqConstant.CITY_PLATFORM_ALARM_STATISTICS_QUEUE);
    }

    @Bean
//    @Lazy
    public Binding bindingAlarmStatistics(DirectExchange cityPlatformExchange,Queue cityPlatAlarmStatistics){
        return BindingBuilder.bind(cityPlatAlarmStatistics).to(cityPlatformExchange).with(MqConstant.CITY_PLATFORM_ALARM_STATISTICS_QUEUE);
    }

    @Bean
    public Queue cityPlatOnline(){
        return new Queue(MqConstant.CITY_PLATFORM_ONLINE_QUEUE);
    }

    @Bean
//    @Lazy
    public Binding bindingOnline(DirectExchange cityPlatformExchange,Queue cityPlatOnline){
        return BindingBuilder.bind(cityPlatOnline).to(cityPlatformExchange).with(MqConstant.CITY_PLATFORM_ONLINE_QUEUE);
    }

    @Bean
    public Queue cityPlatNetWork(){
        return new Queue(MqConstant.CITY_PLATFORM_NETWORK_QUEUE);
    }

    @Bean
//    @Lazy
    public Binding bindingNetWork(DirectExchange cityPlatformExchange,Queue cityPlatNetWork){
        return BindingBuilder.bind(cityPlatNetWork).to(cityPlatformExchange).with(MqConstant.CITY_PLATFORM_NETWORK_QUEUE);
    }

    @Bean
    public Queue cityPlatRunPeriodStatistics(){
        return new Queue(MqConstant.CITY_PLATFORM_RUN_PERIOD_QUEUE);
    }

    @Bean
//    @Lazy
    public Binding bindingRunPeriodStatistics(DirectExchange cityPlatformExchange,Queue cityPlatRunPeriodStatistics){
        return BindingBuilder.bind(cityPlatRunPeriodStatistics).to(cityPlatformExchange).with(MqConstant.CITY_PLATFORM_RUN_PERIOD_QUEUE);
    }
}
