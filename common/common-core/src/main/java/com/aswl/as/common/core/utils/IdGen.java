package com.aswl.as.common.core.utils;

import java.util.UUID;

/**
 * id生成工具类
 *
 * @author aswl.com
 * @date 2018-08-23 12:03
 */
public class IdGen {

    /**
     * 封装JDK自带的UUID, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * 生成流程工单编号
     */
    public static String getFlowRunNum(String alarmType) {
        return alarmType.substring(0, 1)+DateUtils.getDate("yyyyMMddhhmmssSSS")+(10+(int)(Math.random()*90));
    }

    /**
     * 基于snowflake算法生成ID
     *
     * @return String
     * @author aswl.com
     * @date 2019/04/26 11:24
     */
    public static String snowflakeId() {
        return Long.toString(SpringContextHolder.getApplicationContext().getBean(SnowflakeIdWorker.class).nextId());
    }
}