package com.aswl.as.common.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Dtu消息体Bean
 */
@Data
public class DtuBodyBean implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * 数据标识
     */
    private String flag;

    /**
     * msg内容
     */
    private String msg;

    /**
     * data内容
     */
    private Object data;

    /**
     * 手机号码（多个用,号分隔）
     */
    private String telephoneNo;

    /**
     * 设备IP
     */
    private String ip;

    /**
     * 设备项目编码
     */
    private String projectCode;
}
