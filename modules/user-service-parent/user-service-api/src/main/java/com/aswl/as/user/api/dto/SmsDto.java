package com.aswl.as.user.api.dto;

import lombok.Data;

/**
 * @author df
 * @date 2020/10/15 14:54
 */
@Data
public class SmsDto {
    private static final long serialVersionUID = 1L;

    /**
     * 接收人
     */
    private String receiver;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 短信模板code
     */
    private String templateCode;

    /**
     * 短信签名
     */
    private String signName;


}
