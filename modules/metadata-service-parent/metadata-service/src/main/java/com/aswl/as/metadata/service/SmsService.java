package com.aswl.as.metadata.service;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.utils.JsonMapper;
import com.aswl.as.user.api.dto.SmsDto;
import com.aswl.as.user.api.module.SmsResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author aswl
 * @date 2021/11/15 14:51
 */
@Slf4j
@AllArgsConstructor
@Service
public class SmsService {
    private final String appKey = "LTAI4GFF3zxX48bR7X2YyL65";
    private final String appSecret = "78FpD1OueNZr3iV9RLmKS7TL9jusBB";
    private final String regionId = "default";
    private final String domain = "dysmsapi.aliyuncs.com";
    private final String signName = "安防通";
    private final String templateCode = "SMS_204945143";
    private final String version = "2017-05-25";
    private final String action = "SendSms";

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String ALARM_SMS_CODE = "SMS_213691411";

    private static final String ORDER_SMS_CODE = "SMS_227736238";
    

    /**
     * 发送短信
     *
     * @param smsDto smsDto
     * @return SmsResponse
     * @author aswl
     * @date 2021/11/15 14:56
     */
    public SmsResponse sendSms(SmsDto smsDto) {
        DefaultProfile profile = DefaultProfile.getProfile(regionId, appKey, appSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(domain);
        request.putQueryParameter("RegionId", regionId);
        request.putQueryParameter("PhoneNumbers", smsDto.getReceiver());
        Map m = new HashMap<>();
        String num = "";
        if (StringUtils.isNotEmpty(smsDto.getTemplateCode()) && ALARM_SMS_CODE.equals(smsDto.getTemplateCode())) {  //发告警短信
            String content = smsDto.getContent();
            m.put("content", content);
            request.putQueryParameter("SignName", smsDto.getSignName());
            request.putQueryParameter("TemplateCode", smsDto.getTemplateCode());
            request.putQueryParameter("TemplateParam", JSON.toJSONString(m));
        } else if(StringUtils.isNotEmpty(smsDto.getTemplateCode()) && ORDER_SMS_CODE.equals(smsDto.getTemplateCode())){
            String content = smsDto.getContent();
            m.put("content", content);
            request.putQueryParameter("SignName", smsDto.getSignName());
            request.putQueryParameter("TemplateCode", smsDto.getTemplateCode());
            request.putQueryParameter("TemplateParam", JSON.toJSONString(m));
        }
        request.setVersion(version);
        request.setAction(action);
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("发送结果：{}", response.getData());
            if (response.getHttpStatus() != 200)
                throw new CommonException(response.getData());
            SmsResponse smsResponse = JsonMapper.getInstance().fromJson(response.getData(), SmsResponse.class);
            if (smsResponse == null)
                throw new CommonException("解析短信返回结果失败");
            if (!"OK".equals(smsResponse.getCode()))
                throw new CommonException(smsResponse.getMessage());
            if (StringUtils.isEmpty(smsDto.getTemplateCode()) || templateCode.equals(smsDto.getTemplateCode())) {
                redisTemplate.opsForValue().set("sms:" + smsDto.getReceiver(), num, 300, TimeUnit.SECONDS);
            }
            return smsResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("发送短信失败：" + e.getMessage());
        }
    }
}
