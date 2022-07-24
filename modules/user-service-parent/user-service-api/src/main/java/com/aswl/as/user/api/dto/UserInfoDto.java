package com.aswl.as.user.api.dto;

import com.aswl.as.common.core.enums.RoleEnum;
import com.aswl.as.user.api.module.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author aswl.com
 * @date 2018-09-13 17:18
 */
@Data
public class UserInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
     */
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
    private String identifier;

    /**
     * 密码
     */
    @JsonIgnore
    private String credential;

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 头像对应的附件id
     */
    private String avatarId;

    /**
     * 头像地址
     */
    private String avatarUrl;


    /**
     * 图片地址
     */
    private String headPicture;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 生日
     */
    private Date born;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门ID
     */
    private String deptId;


    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 所属区域ID
     */
    private String regionId;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态，0-启用，1-禁用
     */
    private Integer status;

    /**
     * 权限信息
     */
    private String[] permissions;

    /**
     * 角色Id集
     */
    private String[] roleIds;
    /**
     * 角色信息
     */
    private String[] roles;

    /**
     * 角色列表
     */
    private List<Role> roleList;

    /**
     * 角色名称
     */
    private String[] roleNames;

    /**
     * 系统编号
     */
    private String applicationCode;

    /**
     * 租户标识
     */
    private String tenantCode;

    /**
     * 引导注册人
     */
    private String parentUid;

    /**
     * 乡/镇
     */
    private String streetId;

    /**
     * 县
     */
    private String countyId;

    /**
     * 城市
     */
    private String cityId;

    /**
     * 省份
     */
    private String provinceId;

    /**
     * 最近登录时间
     */
    private Date loginTime;

    /**
     * 用户归档时间
     */
    private Date lockTime;

    /**
     * 微信
     */
    private String wechat;

    /**
     * 是否运营端传递过来的对象
     */
    private String isAsOs;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 租户名称
     */
    private String  tenantName;

    /**
     * 是否有设备设置权限
     */
    private boolean deviceSettingPermission;

    /**
     * 是否有审核权限
     */
    private boolean auditOrderPermission;

    /**
     * 在线时长
     */
    @ApiModelProperty(value = "在线时长",name="onlineDuration")
    private Integer onlineDuration;

    /**
     * 用户公司
     */
    @ApiModelProperty(value = "用户公司",name="userCompany")
    private String userCompany;

    /**
     * 区域负责人主键ID
     */
    private String maintainUserId;

    /********************************手写get方法***********************************/
    public boolean getDeviceSettingPermission(){
        if(!Optional.ofNullable(this.roles).isPresent()){
            deviceSettingPermission = false;
        }
        String role = Arrays.stream(roles).filter(s -> s.startsWith(RoleEnum.ROLE_ADMIN.getCode()) || s.startsWith(RoleEnum.ROLE_SYS_ADMIN.getCode()) ||
                s.startsWith(RoleEnum.ROLE_PROJECT_ADMIN.getCode())).findFirst().orElse(null);
        if(this.name.equalsIgnoreCase("admin") || role != null){
            deviceSettingPermission = true;
        }
        return deviceSettingPermission;
    }

    public boolean getAuditOrderPermission() {
        if(!Optional.ofNullable(this.roles).isPresent()){
            auditOrderPermission = false;
        }
        String role = Arrays.stream(roles).filter(s -> s.startsWith(RoleEnum.ROLE_SYS_ADMIN.getCode()) ||
                s.startsWith(RoleEnum.ROLE_PROJECT_ADMIN.getCode())).findFirst().orElse(null);
        if(role != null){
            auditOrderPermission = true;
        }
        return auditOrderPermission;
    }
}
