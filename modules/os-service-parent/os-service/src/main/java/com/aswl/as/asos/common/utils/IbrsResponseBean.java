package com.aswl.as.asos.common.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * 本类用于使用OKHttpUtil的时候，用来传给前端使用的
 */
//实际上返回前端的数据格式跟ResponseBean<T>返回的字符串是一样的
@Data
public class IbrsResponseBean implements Serializable {

    /**
     * 成功
     */
    public static final int SUCCESS = 200;

    /**
     * 失败
     */
    public static final int FAIL = 500;

    private String msg = "success";

    private Integer code = 200;

    /**
     * http 状态码
     */
    private Integer status = 200;

    private Object data;

    public IbrsResponseBean(){}

    public IbrsResponseBean(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = FAIL;
    }

    public IbrsResponseBean(Integer code,Integer status,String msg,Object data)
    {
        this.code=code;
        this.status=status;
        this.msg=msg;
        this.data=data;
    }

}
