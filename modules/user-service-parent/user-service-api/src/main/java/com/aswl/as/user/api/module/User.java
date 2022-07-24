package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.Date;
import java.util.List;

/**
 * 用户基本信息
 *
 * @author aswl.com
 * @date 2018-08-25 15:30
 */
@Data
public class User extends BaseEntity<User> {

    /**
     * 姓名
     */
    private String name;

    /**
     * 电话
     */
    @Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
    private String phone;


    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 头像id
     */
    private String avatarId;

    /**
     * 头像路径
     */

    private String headPicture;

    /**
     * 出生日期
     */
    private Date born;

    /**
     * 描述
     */
    private String userDesc;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 职位id
     */

    private String positionId;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 区域ID
     */
    private String regionId;

    /**
     * 区域编码
     */
    private String regionCode;

    /**
     * 自定义固定角色
     */
    @ApiModelProperty(value = "自定义固定角色")
    private String sysRole;

    /**
     * 角色列表
     */
    private List<Role> roleList;

    /**
     * 角色
     */
    private List<String> role;

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
     * 家庭角色，参考UserStudentConstant
     */
    private Integer familyRole;

    /**
     * 多条件查询参数
     */
    private String query;

    /**
     * 是否系统角色
     */
    private String isSysAdminRole;

    /**
     * APP通知（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "APP通知（0：禁用，1：启用）",name="appNotice")
    private  Integer appNotice;

    /**
     * 短信通知（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "短信通知（0：禁用，1：启用）",name="smsNotice")
    private Integer smsNotice;

    /**
     * 在线时长
     */
    @ApiModelProperty(value = "在线时长",name="onlineDuration")
    private Integer onlineDuration;

    /**
     * 在线起始时间
     */
    @ApiModelProperty(value = "在线起始时间",name="onlineDurationBegin")
    private Integer onlineDurationBegin;


    /**
     * 用户公司
     */
    @ApiModelProperty(value = "用户公司",name="userCompany")
    private String userCompany;

}
