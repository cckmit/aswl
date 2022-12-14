package com.aswl.as.common.core.exceptions;

/**
 * 验证码错误异常
 *
 * @author aswl.com
 * @date 2019/3/18 16:46
 */
public class InvalidValidateCodeException extends CommonException {

    private static final long serialVersionUID = -7285211528095468156L;

    public InvalidValidateCodeException() {
    }

    public InvalidValidateCodeException(String msg) {
        super(msg);
    }
}
