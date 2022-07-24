package com.aswl.as.user.controller;

import com.aswl.as.common.core.model.ResponseBean;
import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.common.security.constant.SecurityConstant;
import com.aswl.as.user.service.MobileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 手机管理Controller
 *
 * @author aswl.com
 * @date 2019/07/02 09:34
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/mobile", tags = "手机管理")
@RestController
@RequestMapping("/v1/mobile")
public class MobileController extends BaseController {

    private final MobileService mobileService;

    /**
     * 发送短信
     *
     * @param mobile     mobile
     * @param tenantCode tenantCode
     * @return ResponseBean
     * @author aswl.com
     * @date 2019/07/02 09:49:05
     */
    @GetMapping("sendSms/{mobile}")
    @ApiOperation(value = "发送短信", notes = "发送短信到指定的手机号")
    @ApiImplicitParam(name = "mobile", value = "mobile", required = true, dataType = "String", paramType = "path")
    public ResponseBean<Boolean> sendSms(@PathVariable String mobile, @RequestHeader(SecurityConstant.TENANT_CODE_HEADER) String tenantCode) {
        return mobileService.sendSms(mobile, tenantCode);
    }
}
