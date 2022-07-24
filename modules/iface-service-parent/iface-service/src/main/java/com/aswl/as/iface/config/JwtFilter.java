package com.aswl.as.iface.config;


import com.alibaba.fastjson.JSONObject;
import com.aswl.as.iface.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonParseException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.System.out;

/**
 * JWT权限拦截器
 */
@Component
public class JwtFilter extends HandlerInterceptorAdapter {


    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 执行进行Controller的方法之前执行
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;

        String auth = request.getHeader("Auth");
        if (auth != null) {
            if (auth.startsWith("Bearer")) {
                String token = auth.substring(7);

                 Claims body = null;
                try {
                    body = jwtUtil.parseJWT(token);
                    if (body != null) {
                        return true;
                    }
                } catch (Exception e) {
                    //放行
                    JSONObject res = new JSONObject();
                    res.put("code", "400");
                    res.put("msg", "token有误");
                    out = response.getWriter();
                    out.append(res.toString());
                    out.close();
                    return false;
                }

            }
        }

        //放行
        JSONObject res = new JSONObject();
        res.put("code", "401");
        res.put("msg", "无权限访问");
        out = response.getWriter();
        out.append(res.toString());
        out.close();
        return false;
    }
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("123456");
        out.println(encode);
    }
}
