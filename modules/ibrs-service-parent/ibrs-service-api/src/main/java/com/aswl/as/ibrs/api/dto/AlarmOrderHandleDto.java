package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 派单处理工单设置Dto
* @author hfx
* @date 2020-03-23 11:43
*/

@ApiModel(value = "AlarmOrderHandleDto",description = "派单处理工单设置Dto")
@Data
public class AlarmOrderHandleDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 唯一标识
    */
    @ApiModelProperty(value = "唯一标识",name="id")
    private String id;

    /**
     * 派单类型（1：自动派单，2：手动派单，3：不派单）
     */
    @ApiModelProperty(value = "派单类型（1：自动派单，2：手动派单，3：不派单）",name="sendOrderType")
    private Integer sendOrderType;

    /**
     * APP通知（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "APP通知（0：禁用，1：启用）",name="appNotice")
    private Integer appNotice;

    /**
     * 短信通知（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "短信通知（0：禁用，1：启用）",name="smsNotice")
    private Integer smsNotice;

    /**
     * 邮件通知（0：禁用，1：启用）
     */
    @ApiModelProperty(value = "邮件通知（0：禁用，1：启用）",name="emailNotice")
    private Integer emailNotice;

    /**
    * 自动处理已修复工单 0-自动 1-手动
    */
    @ApiModelProperty(value = "自动处理已修复工单 0-自动 1-手动",name="orderHandleType")
    private Integer orderHandleType;
    /**
    * 处理的管理人员，使用用户id
    */
    @ApiModelProperty(value = "处理的管理人员，使用用户id",name="handleUserId")
    private String handleUserId;
    /**
    * 修改者
    */
    @ApiModelProperty(value = "修改者",name="modifier")
    private String modifier;
    /**
    * 最后修改时间
    */
    @ApiModelProperty(value = "最后修改时间",name="modifyDate")
    private Date modifyDate;
    /**
    * 创建着
    */
    @ApiModelProperty(value = "创建着",name="creator")
    private String creator;
    /**
    * 添加时间
    */
    @ApiModelProperty(value = "添加时间",name="createDate")
    private Date createDate;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", name = "projectId")
    protected String projectId;
    /**
     * 系统编号
     */
    @ApiModelProperty(value = "系统编号", name = "applicationCode")
    protected String applicationCode;
    /**
    * SAAS租户编码
    */
    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;
}
