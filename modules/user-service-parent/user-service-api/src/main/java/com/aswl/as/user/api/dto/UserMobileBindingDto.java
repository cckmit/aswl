package com.aswl.as.user.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 手机端登录授权Dto
 *
 * @author df
 * @date 2022/03/18 14:24
 */

@ApiModel(value = "UserMobileBindingDto", description = "手机端登录授权Dto")
@Data
public class UserMobileBindingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键", name = "id")
    private String id;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId")
    private String userId;
    /**
     * MEID唯一码
     */
    @ApiModelProperty(value = "MEID唯一码", name = "mobileMeid")
    private String mobileMeid;
    /**
     * 手机型号
     */
    @ApiModelProperty(value = "手机型号", name = "mobileModel")
    private String mobileModel;
    /**
     * 是否授权（0：否，1：是）
     */
    @ApiModelProperty(value = "是否授权（0：否，1：是）", name = "isAuth")
    private Integer isAuth;
    /**
     *
     */
    @ApiModelProperty(value = "", name = "createDate")
    private Date createDate;
    /**
     * 登录时间
     */
    @ApiModelProperty(value = "登录时间", name = "loginTime")
    private Date loginTime;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    private String applicationCode;
    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号", name = "tenantCode")
    private String tenantCode;
}
