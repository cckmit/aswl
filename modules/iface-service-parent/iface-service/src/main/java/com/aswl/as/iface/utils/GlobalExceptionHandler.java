package com.aswl.as.iface.utils;

import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private Map<String, Object> getErrorObject(int code, String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", code);
        error.put("message", message);
        return error;
    }

    @ExceptionHandler(value = ExpiredJwtException.class)// 拦截所有异常, 这里只是为了演示，一般情况下一个方法特定处理一种异常
    public Object exceptionHandler1(Exception e) {

        if (e instanceof ExpiredJwtException) {
//            return getErrorObject(ErrorCode.ClassNameError.getCode(), ErrorCode.ClassNameError.getMessage());
            return getErrorObject(400, "token已过期,请重新登录");
        }
        return null;
    }

}
