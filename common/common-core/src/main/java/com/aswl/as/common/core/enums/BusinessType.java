package com.aswl.as.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  BusinessType {
    /**
     * 日志类型
     */
    // public static final Short Log_Type_LOGIN = 1; //登陆
    // public static final Short Log_Type_EXIT = 2;  //退出
    // public static final Short Log_Type_INSERT = 3; //插入
    // public static final Short Log_Type_DEL = 4; //删除
    // public static final Short Log_Type_UPDATE = 5; //更新
    // public static final Short Log_Type_UPLOAD = 6; //上传
    // public static final Short Log_Type_OTHER = 7; //其他

    /**
     * 导出
     */
    EXPORT(0),
    LOGIN(1),
    EXIT(2),
    /**
     * 新增
     */
    INSERT(3),
    /**
     * 删除
     */
    DELETE(4),
    /**
     * 修改
     */
    UPDATE(5),
    UPLOAD(6),

    /**
     * 其它
     */
    OTHER(7);

    private int type;
}
