package com.aswl.as.common.core.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class CityPlatformSender {


//    @Autowired
//    @Qualifier(value = "customizeAmqpTemplate")
//    private AmqpTemplate customizeAmqpTemplate;

    @Autowired
    private BeanFactory beanFactory;

    private static AmqpTemplate customizeAmqpTemplate;


    public void sender(String exchange,String routeKey,String content,String contentType){

            Message message = MessageBuilder.withBody(content.getBytes())
                    .setContentType(contentType)
                    .setContentEncoding("utf-8")
                    .build();
            if ("".equals(exchange) || exchange == null) {
                return;
            }
            if ("".equals(routeKey) || routeKey == null) {
                return;
            }
//        this.customizeAmqpTemplate.convertAndSend(exchange, routeKey, message);
//        AmqpTemplate customizeAmqpTemplate = beanFactory.getBean("customizeAmqpTemplate", AmqpTemplate.class);
            if(customizeAmqpTemplate == null){
                customizeAmqpTemplate = beanFactory.getBean("customizeAmqpTemplate", AmqpTemplate.class);
            }
            customizeAmqpTemplate.convertAndSend(exchange, routeKey, message);
    }

    public void sender(String exchange,String routeKey,byte[] bytes){
        if ("".equals(exchange) || exchange == null) {
            return;
        }
        if ("".equals(routeKey) || routeKey == null) {
            return;
        }
        if(customizeAmqpTemplate == null){
            customizeAmqpTemplate = beanFactory.getBean("customizeAmqpTemplate", AmqpTemplate.class);
        }
        customizeAmqpTemplate.convertAndSend(exchange, routeKey, bytes);
    }
}
