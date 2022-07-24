package com.aswl.as.asos;

import java.io.Serializable;

public class TempR<T> implements Serializable {

    public static final long serialVersionUID = 42L;

    public static final int NO_LOGIN = -1;

    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 失败
     */
    public static final int FAIL = 500;

    /**
     * 验证码错误
     */
    public static final int INVALID_VALIDATE_CODE_ERROR = 478;

    /**
     * 验证码过期错误
     */
    public static final int VALIDATE_CODE_EXPIRED_ERROR = 479;

    /**
     * 用户名不存在或密码错误
     */
    public static final int USERNAME_NOT_FOUND_OR_PASSWORD_ERROR = 400;

    /**
     * 当前操作没有权限
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * 当前操作没有权限
     */
    public static final int NO_PERMISSION = 403;

    private String msg = "success";

    private int code = SUCCESS;

    /**
     * http 状态码
     */
    private int status = 200;

    private T data;

    public TempR() {
        super();
    }

    public TempR(T data) {
        super();
        this.data = data;
    }

    public TempR(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }

    public TempR(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = FAIL;
    }

    public TempR(Throwable e, int code) {
        super();
        this.msg = e.getMessage();
        this.code = code;
    }
}
