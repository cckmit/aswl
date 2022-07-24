package com.aswl.as.ibrs.api.dto;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
*
* 上级平台信息Dto
* @author dingfei
* @date 2019-11-11 16:03
*/

@ApiModel(value = "SuperPlatformDto",description = "上级平台信息Dto")
@Data
public class SuperPlatformDto extends BaseEntity<SuperPlatformDto> {

private static final long serialVersionUID = 1L;

    /**
    * 主键ID
    */
    @ApiModelProperty(value = "主键ID",name="id")
    private String id;
    /**
    * 客户端ID
    */
    @ApiModelProperty(value = "客户端ID",name="clientId")
    private String clientId;
    /**
    * 客户端密钥
    */
    @ApiModelProperty(value = "客户端密钥",name="clientSecret")
    private String clientSecret;
    /**
    * 组织名称(英文简称)
    */
    @ApiModelProperty(value = "组织名称(英文简称)",name="orgName")
    private String orgName;
    /**
    * 应用名称(英文简称)
    */
    @ApiModelProperty(value = "应用名称(英文简称)",name="appName")
    private String appName;
    /**
    * 接口URL路径
    */
    @ApiModelProperty(value = "接口URL路径",name="apiUrl")
    private String apiUrl;
    /**
    * 接口namespace
    */
    @ApiModelProperty(value = "回调URL",name="callbackUrl")
    private String callbackUrl;
    /**
    * 有效状态
    */
    @ApiModelProperty(value = "有效状态",name="status")
    private Integer status;
    /**
    * 创建人名称
    */
    @ApiModelProperty(value = "创建人名称",name="createName")
    private String createName;
    /**
    * 更新人名称
    */
    @ApiModelProperty(value = "更新人名称",name="updateName")
    private String updateName;
    /**
    * 所属机构
    */
    @ApiModelProperty(value = "所属机构",name="sysOrgCode")
    private String sysOrgCode;
    /**
    * 所属区域
    */
    @ApiModelProperty(value = "所属区域",name="regionCode")
    private String regionCode;
    /**
    * 删除日期
    */
    @ApiModelProperty(value = "删除日期",name="delDate")
    private Date delDate;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id",name="userId")
    private String userId;
}
