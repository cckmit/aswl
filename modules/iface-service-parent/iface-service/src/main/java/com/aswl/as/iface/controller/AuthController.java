package com.aswl.as.iface.controller;

import com.aswl.as.common.core.web.BaseController;
import com.aswl.as.iface.model.SuperPlatform;
import com.aswl.as.iface.service.SuperPlatformService;
import com.aswl.as.iface.utils.Base64;
import com.aswl.as.iface.utils.JwtUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author aswl
 * @date 2019-09-11
 */
@Slf4j
@AllArgsConstructor
@Api(value = "/api/v1/auth", tags = "用户授权")
@RestController
@RequestMapping(value = "/v1/auth")
public class AuthController extends BaseController {

    @Autowired
    private SuperPlatformService superPlatformService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/token")
    @ApiOperation(value = "登录认证", notes = "用户密码登录认证")
    public Map login(@RequestParam("appKey") String appKey, @RequestParam("authStr") String authStr) {

        if (authStr != null && !"".equals(authStr)) {
            String secret = Base64.decodeBase64(authStr);
            if (secret.indexOf(":") == -1) {
                Map map = new LinkedHashMap();
                map.put("code", 400);
                map.put("msg", "密码错误");
                return map;
            }
            String[] split = secret.split(":");
            if (!split[0].equals(appKey) && split.length == 2) {
                Map map = new LinkedHashMap();
                map.put("code", 400);
                map.put("msg", "appKey错误");
                return map;
            }
            SuperPlatform platform = superPlatformService.findSuperPlatform(appKey);
            if (platform == null) {
                Map map = new LinkedHashMap();
                map.put("code", 400);
                map.put("msg", "appKey错误");
                return map;
            }
            if (!split[1].equals(platform.getSecret())) {
                Map map = new LinkedHashMap();
                map.put("code", 400);
                map.put("msg", "密码错误");
                return map;
            }
        }
        String token = jwtUtil.createJWT(appKey, authStr);
        Map map = new LinkedHashMap();
        map.put("access_token", token);
        map.put("expires_in", jwtUtil.getTtl());
        map.put("code", 200);
        map.put("status", 0);
        map.put("msg", "success");
        return map;
    }

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.signWith(SignatureAlgorithm.HS256, "fffff");
        //签发token
        jwtBuilder.setIssuer("gzas_Ltd"); // 发行人
        jwtBuilder.setIssuedAt(new Date()); // 签发时间
        jwtBuilder.setNotBefore(new Date()); // 生效时间
        jwtBuilder.setExpiration(new Date(l + 5555555)); // 失效时间
        // 用户自定义字段
        jwtBuilder.claim("serviceId", "123456");
        System.out.println(jwtBuilder.compact());

    }
}
