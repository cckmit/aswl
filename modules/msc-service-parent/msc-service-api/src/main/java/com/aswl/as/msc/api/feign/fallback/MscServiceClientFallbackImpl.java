package com.aswl.as.msc.api.feign.fallback;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.msc.api.dto.SmsDto;
import com.aswl.as.msc.api.feign.MscServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 消息中心服务断路器
 *
 * @author aswl.com
 * @date 2019/07/02 16:09
 */
@Slf4j
@Component
public class MscServiceClientFallbackImpl implements MscServiceClient {

    private Throwable throwable;

    @Override
    public ResponseBean<?> sendSms(SmsDto smsDto) {
        log.error("feign 发送短信失败:{}, {}, {}", smsDto.getReceiver(), smsDto.getContent(), throwable);
        return null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
