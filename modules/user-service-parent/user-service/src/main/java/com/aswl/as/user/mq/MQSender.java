package com.aswl.as.user.mq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mq
 *
 * @author aswl.com
 * @version 1.0
 * createTime 2019-10-22 14:37
 */
@Component
public class MQSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * 测试
     * 非枚点对点发布
     *
     * @param queues
     * @param content
     * @createTime 2019-9-3
     */
    public void send(String queues, String content) {
        Message message = MessageBuilder.withBody(content.getBytes())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8")
                .build();

        if ("".equals(queues) || queues == null) {
            return;
        }
        LOGGER.info("send -> {} -> {}", queues, message);
        this.rabbitTemplate.convertAndSend(queues, message);
    }

    /**
     * 测试
     * Topic广播发送
     *
     * @param queues
     * @param mqExchange
     * @param content
     */
    public void send(String mqExchange, String queues, String content) {
        {
            Message message = MessageBuilder.withBody(content.getBytes())
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setContentEncoding("utf-8")
                    .build();
            if ("".equals(mqExchange) || mqExchange == null) {
                return;
            }
            if ("".equals(queues) || queues == null) {
                return;
            }
            LOGGER.info("send -> {} -> {}", mqExchange, message);
            this.rabbitTemplate.convertAndSend(mqExchange, queues, message);
        }
    }

    /**
     * 测试
     * Topic广播发送
     *
     * @param queues
     * @param mqExchange
     * @param content
     */
    public void send(String mqExchange, String queues, String content, String contentType) {
        {
            Message message = MessageBuilder.withBody(content.getBytes())
                    .setContentType(contentType)
                    .setContentEncoding("utf-8")
                    .build();
            if ("".equals(mqExchange) || mqExchange == null) {
                return;
            }
            if ("".equals(queues) || queues == null) {
                return;
            }
            LOGGER.info("send -> {} -> {}", mqExchange, message);
            this.rabbitTemplate.convertAndSend(mqExchange, queues, message);
        }
    }

}
