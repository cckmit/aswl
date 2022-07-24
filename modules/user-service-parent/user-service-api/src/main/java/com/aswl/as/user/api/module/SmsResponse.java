package com.aswl.as.user.api.module;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 封装短信服务返回的结果
 *
 * @author df
 * @date 2020/10/15 16:18
 */
@Data
public class SmsResponse {

    @JsonProperty("Message")
    private String message;

    @JsonProperty("RequestId")
    private String requestId;

    @JsonProperty("Code")
    private String code;

    @JsonProperty("BizId")
    private String bizId;
}
