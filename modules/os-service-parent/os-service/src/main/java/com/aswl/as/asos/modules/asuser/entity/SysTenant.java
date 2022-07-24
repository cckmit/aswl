package com.aswl.as.asos.modules.asuser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * SAAS租户信息表
 * </p>
 *
 * @author hfx
 * @since 2019-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="SysTenant对象", description="SAAS租户信息表")
@TableName(value = "sys_tenant")
public class SysTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一ID")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "租户标识,不用传")
    private String tenantCode;

    @ApiModelProperty(value = "租户名称，需要传,必填")
    private String tenantName;

    @ApiModelProperty(value = "租户描述信息，需要传")
    private String tenantDesc;

    @ApiModelProperty(value = "状态，0-待审核，1-正常，2-审核不通过") //TODO 现在状态改为3种，0是锁定，1是激活，2是过期
    private String status;

    private String creator;

    private String createDate;

    private String modifier;

    private String modifyDate;

    @ApiModelProperty(value = "删除标记，0为未删除，1为已删除",example="0")
    private String delFlag;

    @ApiModelProperty(value = "帐户全称,需要传")
    @NotBlank(message="帐户全称不能为空")
    private String fullName;

    @ApiModelProperty(value = "详细地址，需要传")
    private String address;

    @ApiModelProperty(value = "联系电话，需要传")
    private String telephone;

    @ApiModelProperty(value = "通知电子邮件，需要传",example = "example@qq.com")
    private String notifyEmail;

    @ApiModelProperty(value = "租户角色ID")
    private String tenantRoleId;

    @ApiModelProperty(value = "租户角色名称")
    private String tenantRoleName;

    @ApiModelProperty(value = "试用手机号码(多个,分割)")
    private String trialMobile;

    @ApiModelProperty(value = "负责人，需要传")
    private String ownerName;

    @ApiModelProperty(value = "负责人职务，需要传")
    private String ownerPosition;

    @ApiModelProperty(value = "负责人手机，需要传，必填")
    private String ownerMobile;

    @ApiModelProperty(value = "超限时间，不用传")
    private Integer expTime;

    @ApiModelProperty(value = "营业执照证书编码，需要传")
    private String licenseCode;

    @ApiModelProperty(value = "营业执照证书扫描件，需要传，需要上传图片附件")
    private String licenseImage;

    @ApiModelProperty(value = "租户LOGO，需要传")
    private String tenantLogo;

    @ApiModelProperty(value = "备注，需要传")
    private String remark;

    // 添加字段，用来显示激活还是未激活
    @ApiModelProperty(value = "续费有效数量")
    private Integer validCount;

    @ApiModelProperty(value = "续费有效期单位")
    private String validUnit;

    @ApiModelProperty(value = "生效开始时间")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime effectiveStartTime;

    @ApiModelProperty(value = "生效结束时间")
//    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime effectiveEndTime;

    @ApiModelProperty(value = "设备最大数量")
    private Integer maxDeviceCount;

    @ApiModelProperty(value = "最后的有效使用时长，如果一直没断，就所有时间加起来，如果中途已过期，就按过期后的第一次续费开始算时间，以年月分隔开，每次启用和续费都要算一次")
    private String validTime;

}
