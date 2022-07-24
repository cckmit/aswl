package com.aswl.as.iface.model.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 响应体
 * @param <T>
 */
@Data
public class ResponseBean<T> implements Serializable {

    private String eventNo;
    private int code;
    private String msg;
    private T data;
}
