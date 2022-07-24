package com.aswl.as.user.api.dto;


import com.aswl.as.user.api.module.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * userDto
 *
 * @author aswl.com
 * @date 2018/8/26 14:36
 */
@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private String id;

    /**
     * 授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ
     */
    @ApiModelProperty(value = "授权类型，1：用户名密码，2：手机号，3：邮箱，4：微信，5：QQ", dataType = "Integer", example = "1")
    private Integer identityType;

    /**
     * 唯一标识，如用户名、手机号
     */
    @ApiModelProperty(value = "账号唯一标识，如用户名、手机号", example = "git")
    private String identifier;

    /**
     * 密码凭证，跟授权类型有关，如密码、第三方系统的token等
     */
    @ApiModelProperty(value = "密码，需要使用AES加密", example = "lBTqrKS0kZixOFXeZ0HRng==")
    private String credential;

    /**
     * 头像id
     */
    @ApiModelProperty(value = "头像id")
    private String avatarId;

    /**
     * 头像路径
     */
    @ApiModelProperty(value = "头像路径")
    private String headPicture;

    /**
     * 头像路径
     */
    @ApiModelProperty(value = "头像路径")
    private String avatarUrl;


    /**
     * 角色
     */
    @ApiModelProperty(value = "角色", hidden = true)
    private List<String> role;

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    private String deptId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称", hidden = true)
    private String deptName;

    /**
     * 部门编码
     */
    @ApiModelProperty(value = "部门编码")
    private String deptCode;

    /**
     * 职位id
     */
    @ApiModelProperty(value = "职位id")
    private String positionId;
    /**
     * 职位名称
     */
    @ApiModelProperty(value = "职位名称")
    private String positionName;


    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID")
    private String projectId;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String projectName;


    /**
     * 区域ID
     */
    @ApiModelProperty(value = "区域ID")
    private String regionId;

    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码")
    private String regionCode;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称")
    private String regionName;

    /**
     * 自定义固定角色
     */
    @ApiModelProperty(value = "自定义固定角色")
    private String sysRole;

    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码", hidden = true)
    private String oldPassword;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码", hidden = true)
    private String newPassword;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", example = "git")
    private String name;

    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", example = "15521089184")
    // @Pattern(regexp = "^\\d{11}$", message = "请输入11位手机号")
    private String phone;


    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱", example = "1633736729@qq.com")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 性别
     */
    @ApiModelProperty(value = "性别，0：男，1：女", dataType = "Integer", example = "0")
    private Integer sex;

    /**
     * 出生日期
     */
    @ApiModelProperty(value = "出生日期", dataType = "Date")
    private Date born;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态，0：启用，1：禁用", example = "0")
    private Integer status;

    /**
     * 角色列表
     */
    @ApiModelProperty(value = "角色列表", hidden = true)
    private List<Role> roleList;

    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", example = "IC")
    private String applicationCode;

    /**
     * 租户标识
     */
    @ApiModelProperty(value = "租户标识", example = "aswl")
    private String tenantCode;

    /**
     * 引导注册人
     */
    @ApiModelProperty(value = "引导注册人")
    private String parentUid;

    /**
     * 乡/镇
     */
    @ApiModelProperty(value = "乡/镇")
    private String streetId;

    /**
     * 县
     */
    @ApiModelProperty(value = "县")
    private String countyId;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市")
    private String cityId;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份")
    private String provinceId;

    /**
     * 最近登录时间
     */
    @ApiModelProperty(value = "最近登录时间", hidden = true)
    private Date loginTime;

    /**
     * 用户归档时间
     */
    @ApiModelProperty(value = "用户归档时间", hidden = true)
    private Date lockTime;

    /**
     * 微信
     */
    @ApiModelProperty(value = "微信")
    private String wechat;

    /**
     * 家庭角色
     */
    @ApiModelProperty(value = "家庭角色，0：爸爸，1：妈妈，2：爷爷，3：奶奶，5：外公，6：外婆", example = "0")
    private Integer familyRole;
    
    /** --------------------区域负责人信息Dto对象-----------------------------------*/

    /**
     * 用户ID，可空
     */
    @ApiModelProperty(value = "用户ID，可空",name="userId")
    private String userId;
    
    /**
     * 负责人姓名
     */
    @ApiModelProperty(value = "负责人姓名",name="userName")
    private String userName;
    /**
     * 负责人手机
     */
    @ApiModelProperty(value = "负责人手机",name="userMobile")
    private String userMobile;
    /**
     * 是否接收报警消息
     */
    @ApiModelProperty(value = "是否接收报警消息",name="isReceiveAlarm")
    private Integer isReceiveAlarm;
    /**
     * 接收消息开始时间
     */
    @ApiModelProperty(value = "接收消息开始时间",name="receiveStartAt")
    private String receiveStartAt;
    /**
     * 接收消息结束时间
     */
    @ApiModelProperty(value = "接收消息结束时间",name="receiveEndAt")
    private String receiveEndAt;

    /**
     * 是否巡更
     */
    @ApiModelProperty(value = "是否巡更",name = "isPatrol")
    private Integer isPatrol;


    /**
     * 巡更钥匙ID
     */
    @ApiModelProperty(value = "巡更钥匙ID",name = "patrolKeyId")
    private String patrolKeyId;

    /**
     * 巡更周期数量
     */
    @ApiModelProperty(value = "巡更周期数量",name = "patrolPeriodNum")
    private Integer patrolPeriodNum;

    /**
     * 巡更周期单位
     */
    @ApiModelProperty(value = "巡更周期单位",name = "patrolPeriodUnit")
    private String patrolPeriodUnit;

    /**
     * 巡更周期（时间戳，单位为秒）
     */
    @ApiModelProperty(value = "巡更周期（时间戳，单位为秒）",name = "patrolPeriod")
    private Integer patrolPeriod;

    /**
     * 巡更周期开始时间
     */
    @ApiModelProperty(value = "巡更周期开始时间",name = "patrolPeriodBeginTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date patrolPeriodBeginTime;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    /**
     * 授权管理员功能(0,否,1,是)
     */
    @ApiModelProperty(value = "授权管理员功能(0,否,1,是)",name="isCheck")
    private String isCheck;

    /**
     * 角色编码
     */
    @ApiModelProperty(value = "角色编码",name="roleCode")
    private String roleCode;

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
