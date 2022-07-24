package com.aswl.as.iface.model.consumer;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jk
 * @version 1.0.0
 * @create 2020/1/13 16:24
 */
@Data
public class ChangeStatusVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    private String code;

    private String codeCN;

    private Double value;

    private String unit;
}
