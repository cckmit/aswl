package com.aswl.as.user.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 租户
 *
 * @author aswl.com
 * @date 2019/5/22 22:44
 */
@Data
public class Tenant extends BaseEntity<Tenant> {

//    /**
//     * 租户标识
//     */
//    @NotBlank(message = "租户标识不能为空")
//    private String tenantCode;

    /**
     * 租户名称
     */
    @NotBlank(message = "租户名称不能为空")
    private String tenantName;

    /**
     * 租户描述信息
     */
    private String tenantDesc;

    /**
     * 状态，0-锁定，1-激活，2-已过期
     */
    private String status;

    /**
     * 帐户全称
     */
//    @NotBlank(message="帐户全称不能为空")
    private String fullName;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 通知电子邮件
     */
    private String notifyEmail;

    /**
     * 负责人
     */
    private String ownerName;

    /**
     * 负责人职务
     */
    private String ownerPosition;

    /**
     * 负责人手机，必填
     */
    private String ownerMobile;

    /**
     * 超限时间，不用传
     */
    private Integer expTime;

    /**
     * 营业执照证书编码
     */
    private String licenseCode;

    /**
     * 营业执照证书扫描件，需要上传图片附件
     */
    private String licenseImage;

    /**
     * 租户LOGO
     */
    private String tenantLogo;

    /**
     * 备注
     */
    private String remark;

    /**
     * 节点类型，1为租户，2为项目，3为区域
     */
    private String type;

    /**
     * 续费有效数量
     */
    private Integer validCount;

    /**
     * 续费有效期单位
     */
    private String validUnit;

    /**
     * 生效开始时间
     */
    private Date effectiveStartTime;

    /**
     * 生效结束时间
     */
    private Date effectiveEndTime;

    /**
     * 最大设备数量
     */
    private Integer maxDeviceCount;

    /**
     * 最后的有效使用时长，如果一直没断，就所有时间加起来，如果中途已过期，就按过期后的第一次续费开始算时间，以年月分隔开，每次启用和续费都要算一次
     */
    private String validTime;

    /**
     * 租户角色ID
     */
    private String tenantRoleId;

    /**
     * 租户角色名称
     */
    private String tenantRoleName;

    /**
     * 试用手机号码(多个,分割)
     */
    private String trialMobile;

}
