package com.aswl.as.iface.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求体
 * @param <T>
 */
@Data
public class RequestBean<T> implements Serializable {

    private String eventNo;
    private T data;
}
