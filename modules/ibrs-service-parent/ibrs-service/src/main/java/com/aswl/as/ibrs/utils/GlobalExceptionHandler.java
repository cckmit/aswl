package com.aswl.as.ibrs.utils;

import com.aswl.as.common.core.exceptions.CommonException;
import com.aswl.as.common.core.exceptions.ValidateCodeExpiredException;
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

    @ExceptionHandler(value = CommonException.class)// 拦截所有异常, 这里只是为了演示，一般情况下一个方法特定处理一种异常
    public Object exceptionHandler1(Exception e) {

        if (e instanceof CommonException) {
//            return getErrorObject(ErrorCode.ClassNameError.getCode(), ErrorCode.ClassNameError.getMessage());
            return getErrorObject(ErrorCode.ClassNameError.getCode(), e.getMessage());
        }
        return null;
    }

    @ExceptionHandler(value = ValidateCodeExpiredException.class)// 拦截所有异常, 这里只是为了演示，一般情况下一个方法特定处理一种异常
    public Object exceptionHandler2(Exception e) {

        if (e instanceof ValidateCodeExpiredException) {
            return getErrorObject(ErrorCode.CannotFinish.getCode(), ErrorCode.CannotFinish.getMessage());
        }
        return null;
    }

}
