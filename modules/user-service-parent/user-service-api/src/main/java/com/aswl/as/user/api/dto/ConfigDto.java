package com.aswl.as.user.api.dto;

import java.io.Serializable;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统配置信息表Dto
 *
 * @author dingfei
 * @date 2019-12-18 10:57
 */

@ApiModel(value = "ConfigDto", description = "系统配置信息表Dto")
@Data
public class ConfigDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty(value = "", name = "id")
    private String id;
    /**
     * key
     */
    @ApiModelProperty(value = "key", name = "paramKey")
    private String paramKey;
    /**
     * value
     */
    @ApiModelProperty(value = "value", name = "paramValue")
    private String paramValue;
    /**
     * 状态   0：隐藏   1：显示
     */
    @ApiModelProperty(value = "状态   0：隐藏   1：显示", name = "status")
    private Integer status;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", name = "remark")
    private String remark;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    private String applicationCode;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码", name = "tenantCode")
    private String tenantCode;
}
