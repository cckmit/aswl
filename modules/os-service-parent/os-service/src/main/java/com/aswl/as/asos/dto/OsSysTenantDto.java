package com.aswl.as.asos.dto;

import com.aswl.as.asos.modules.asuser.entity.SysTenant;
import com.aswl.as.common.core.persistence.AsOsTreeEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OsSysTenantDto extends AsOsTreeEntity<OsSysTenantDto> {

    @ApiModelProperty(value = "唯一ID")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "租户标识,不用传")
    private String tenantCode;

    @ApiModelProperty(value = "租户名称，需要传,必填")
    private String tenantName;

    @ApiModelProperty(value = "租户描述信息，需要传")
    private String tenantDesc;

    @ApiModelProperty(value = "状态，0-待审核，1-正常，2-审核不通过")
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

    @ApiModelProperty(value = "节点类型，1为租户，2为项目，3为区域")
    @TableField(exist=false)
    private String type;

    public OsSysTenantDto(SysTenant t)
    {
        id=t.getId();
        tenantCode=t.getTenantCode();
        tenantName=t.getTenantName();
        tenantDesc=t.getTenantDesc();
        status=t.getStatus();
        creator=t.getCreator();
        createDate=t.getCreateDate();
        modifier=t.getModifier();
        modifyDate=t.getModifyDate();
        delFlag=t.getDelFlag();
        fullName=t.getFullName();
        address=t.getAddress();
        telephone=t.getTelephone();
        notifyEmail=t.getNotifyEmail();
        ownerName=t.getOwnerName();
        ownerPosition=t.getOwnerPosition();
        ownerMobile=t.getOwnerMobile();
        expTime=t.getExpTime();
        licenseCode=t.getLicenseCode();
        licenseImage=t.getLicenseImage();
        tenantLogo=t.getTenantLogo();
        remark=t.getRemark();

        code=t.getId();
        parent=null;//后面会设置
        parentId=null;//后面会设置
        sort=0;

        this.type="1";

    }

}
