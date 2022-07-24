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
 * 用户信息表
 * </p>
 *
 * @author hfx
 * @since 2019-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysUser对象", description="用户信息表")
@TableName(value = "sys_user")
public class AsUserSysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "头像ID")
    private String avatarId;

    @ApiModelProperty(value = "头像路径")
    private String head_picture;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "出生日期",dataType = "Date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date born;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "性别")
    private Integer sex;

    @ApiModelProperty(value = "部门id")
    private String deptId;

    @ApiModelProperty(value = "部门编码")
    private String deptCode;

    @ApiModelProperty(value = "职位ID")
    private String positionId;

    @ApiModelProperty(value = "职位名称")
    private String positionName;

    @ApiModelProperty(value = "区域ID")
    private String regionId;

    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    @ApiModelProperty(value = "详细描述")
    private String userDesc;

    @ApiModelProperty(value = "上司ID")
    private String parentUid;

    @ApiModelProperty(value = "所在街道")
    private String streetId;

    @ApiModelProperty(value = "所在区县")
    private String countyId;

    @ApiModelProperty(value = "所在城市")
    private String cityId;

    @ApiModelProperty(value = "所在省")
    private String provinceId;

    @ApiModelProperty(value = "登录时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date loginTime;

    @ApiModelProperty(value = "锁屏时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lockTime;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "角色分工")
    private Integer familyRole;

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

    @ApiModelProperty(value = "租户编码")
    private String tenantCode;

    @ApiModelProperty(value = "自定义固定角色")
    private String sysRole;


}
