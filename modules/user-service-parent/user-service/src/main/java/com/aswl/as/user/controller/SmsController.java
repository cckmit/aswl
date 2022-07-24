package com.aswl.as.user.controller;

import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.utils.SysUtil;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.user.api.dto.SmsDto;
import com.aswl.as.user.api.module.SmsResponse;
import com.aswl.as.user.api.module.Tenant;
import com.aswl.as.user.api.module.UserAuths;
import com.aswl.as.user.service.SmsService;
import com.aswl.as.user.service.TenantService;
import com.aswl.as.user.service.UserAuthsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 发送短信接口
 *
 * @author df
 * @date 2020/10/15 15:05
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/sms", tags = "发送短信")
@RestController
@RequestMapping(value = "/v1/sms")
public class SmsController extends BaseController {
    private final SmsService smsService;

    /**
     * 发送短信
     *
     * @param smsDto smsDto
     * @return ResponseBean
     * @author df
     * @date 2020/10/15 15:06
     */
    @PostMapping("sendSms")
    @ApiOperation(value = "发送短信", notes = "根据手机号发送短信")
    public ResponseBean<SmsResponse> sendSms(@RequestBody SmsDto smsDto) {
        log.info("发送短信给{}，发送内容：{}", smsDto.getReceiver(), smsDto.getContent());
        SmsResponse smsResponse = smsService.sendSms(smsDto);
        log.info("发送短信成功，返回内容：{}", smsResponse);
        return new ResponseBean<>(smsResponse);
    }
}
