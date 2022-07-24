package com.aswl.as.ibrs.utils;

/**
 * @author jk
 * @version 1.0.0
 * @create 2019/12/13 11:38
 */
public enum ErrorCode {
    ClassNameError(400, "类名不存在"),
    CannotFinish(400, "故障未修复,工单状态不能为已处理");
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
