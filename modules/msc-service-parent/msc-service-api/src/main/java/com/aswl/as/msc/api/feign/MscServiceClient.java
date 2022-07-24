package com.aswl.as.msc.api.feign;

import com.aswl.as.common.core.constant.ServiceConstant;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.feign.config.CustomFeignConfig;
import com.aswl.as.msc.api.dto.SmsDto;
import com.aswl.as.msc.api.feign.factory.MscServiceClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 消息中心服务
 *
 * @author aswl.com
 * @date 2019/07/02 16:04
 */
@FeignClient(value = ServiceConstant.MSC_SERVICE, configuration = CustomFeignConfig.class, fallbackFactory = MscServiceClientFallbackFactory.class)
public interface MscServiceClient {

    /**
     * 发送短信
     *
     * @param smsDto smsDto
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/07/02 16:07:27
     */
    @PostMapping("/v1/sms/sendSms")
    ResponseBean<?> sendSms(@RequestBody SmsDto smsDto);
}
