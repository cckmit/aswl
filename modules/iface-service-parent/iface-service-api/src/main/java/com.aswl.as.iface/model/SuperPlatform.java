package com.aswl.as.iface.model;

import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;

/**
 * 上级平台信息
 * @author aswl
 * @date 2019-07-11
 */
@Data
public class SuperPlatform extends BaseEntity<SuperPlatform> {

    /**服务号*/
    private String serviceId;

    /**平台名称*/
    private String platformName;

    /**密钥*/
    private String secret;

    /**接口WSDL路径*/
    private String wsdlUrl;

    /**接口namespace*/
    private String nameSpace;

    /**有效状态*/
    private Integer status;

    /**备注*/
    private String remark;
}
