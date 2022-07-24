package com.aswl.as.user.service;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.utils.DateUtils;
import com.aswl.as.common.core.utils.JsonMapper;
import com.aswl.as.user.api.dto.SmsDto;
import com.aswl.as.user.api.module.SmsResponse;
import com.aswl.as.user.properties.SmsProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author df
 * @date 2020/10/15 14:51
 */
@Slf4j
@AllArgsConstructor
@Service
public class SmsService {
    private final SmsProperties smsProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String ALARM_SMS_CODE = "SMS_213691411";

    private static final String ORDER_SMS_CODE = "SMS_227736238";
    

    /**
     * 发送短信
     *
     * @param smsDto smsDto
     * @return SmsResponse
     * @author df
     * @date 2020/10/15 14:56
     */
    public SmsResponse sendSms(SmsDto smsDto) {
        DefaultProfile profile = DefaultProfile.getProfile(smsProperties.getRegionId(), smsProperties.getAppKey(), smsProperties.getAppSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(smsProperties.getDomain());
        request.putQueryParameter("RegionId", smsProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", smsDto.getReceiver());
        Map m = new HashMap<>();
        String num = "";
        if (StringUtils.isNotEmpty(smsDto.getTemplateCode()) && ALARM_SMS_CODE.equals(smsDto.getTemplateCode())) {  //发告警短信
            String content = smsDto.getContent();
            m.put("content", content);
            String[] split = content.split(",");
            if (split.length >= 2) {
                m.put("device", split[0]);
                m.put("type", split[1]);
                m.put("time", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
            }
            request.putQueryParameter("TemplateCode", smsDto.getTemplateCode());
            request.putQueryParameter("TemplateParam", JSON.toJSONString(m));
            request.putQueryParameter("SignName", smsDto.getSignName());
        } else if(StringUtils.isNotEmpty(smsDto.getTemplateCode()) && ORDER_SMS_CODE.equals(smsDto.getTemplateCode())){
            String content = smsDto.getContent();
            m.put("content", content);
            request.putQueryParameter("TemplateCode", smsDto.getTemplateCode());
            request.putQueryParameter("TemplateParam", JSON.toJSONString(m));
            request.putQueryParameter("SignName", smsDto.getSignName());
        }else {  //验证码短信
            //生成随机的数字
            num = RandomStringUtils.randomNumeric(6);
            m.put("code", num);
            request.putQueryParameter("TemplateCode", smsProperties.getTemplateCode());
            request.putQueryParameter("TemplateParam", JSON.toJSONString(m));
            request.putQueryParameter("SignName", smsProperties.getSignName());
        }
        request.setVersion(smsProperties.getVersion());
        request.setAction(smsProperties.getAction());
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
            if (StringUtils.isEmpty(smsDto.getTemplateCode()) || smsProperties.getTemplateCode().equals(smsDto.getTemplateCode())) {
                redisTemplate.opsForValue().set("sms:" + smsDto.getReceiver(), num, 300, TimeUnit.SECONDS);
            }
            return smsResponse;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new CommonException("发送短信失败：" + e.getMessage());
        }
    }
}
