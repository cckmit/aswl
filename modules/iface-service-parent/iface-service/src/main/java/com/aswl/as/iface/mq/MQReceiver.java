package com.aswl.as.iface.mq;

import com.alibaba.fastjson.JSON;
import com.aswl.as.iface.model.consumer.ChangeDeviceVo;
import com.aswl.as.iface.model.consumer.IfaceCallbackDeviceStatusDto;
import com.aswl.as.iface.model.consumer.IfaceCallbackDeviceStatusVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/10 14:21
 */
@Component
@RabbitListener(queues = "ifaceCallbackQueue")
@Slf4j
public class MQReceiver {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    @Qualifier(value = "consumer")
    ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @RabbitHandler
    public void process(String msg) {

        log.info("receive --> {} " + msg);

        threadPoolTaskExecutor.execute(() -> {
            if (msg != null && !"".equals(msg)) {
                IfaceCallbackDeviceStatusVO vo = JSON.parseObject(msg, IfaceCallbackDeviceStatusVO.class);

                log.info("反序列化数据 --> {} " + vo);

                String callbackUrl = vo.getCallbackUrl();
                ChangeDeviceVo changeDevice = vo.getChangeDevice();
                IfaceCallbackDeviceStatusDto dto = new IfaceCallbackDeviceStatusDto();
                dto.setChangeDevice(changeDevice);

                log.info("请求url实体 --> {}" + dto);

                HttpHeaders headers = new HttpHeaders();

                //这里设置的是以payLoad方式提交数据，对于Payload方式，提交的内容一定要是String，且Header要设为“application/json”

                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

                ObjectMapper mapper = new ObjectMapper();

                String value = null;
                try {
                    value = mapper.writeValueAsString(dto);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
                // ResponseEntity<String> res = restTemplate.postForEntity(callbackUrl, requestEntity, String.class);
                // TODO 发送post请求
                restTemplate.postForEntity(callbackUrl, requestEntity, String.class);

            }
        });


    }
}
