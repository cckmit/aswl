package com.aswl.as.asos.modules.asuser.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户授权表
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysUserAuths对象", description="用户授权表")
@TableName(value = "sys_user_auths")
public class AsUserSysUserAuths implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一ID")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "登录类型，手机号、邮箱、用户名或第三方应用名称（微信 微博等）")
    private Integer identityType;

    @ApiModelProperty(value = "标识，手机号、邮箱、用户名或第三方应用的唯一标识")
    private String identifier;

    @ApiModelProperty(value = "密码凭证，站内的保存密码，站外的不保存或保存token")
    private String credential;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createDate;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyDate;

    @ApiModelProperty(value = "删除标记 0:正常;1:删除")
    private Integer delFlag;

    @ApiModelProperty(value = "系统编号")
    private String applicationCode;

    @ApiModelProperty(value = "租户编号")
    private String tenantCode;


}
