package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
*
* 流程实例Entity
* @author dingfei
* @date 2019-10-25 18:16
*/

@ApiModel(value = "FlowRun",description = "流程实例Entity")
@Data
public class FlowRun extends BaseEntity<FlowRun> {
    /**
    * 流程编号
    */

    @ApiModelProperty(value = "流程编号",name="runNo")
    private String runNo;
    /**
    * 流程实例名称（工作名称/文号）
    */

    @ApiModelProperty(value = "流程实例名称（工作名称/文号）",name="runName")
    private String runName;
    /**
    * 流程实例状态 0 审批中（
待处理）1已审批（已完成） 2 未通过（未修复） 3 已撤销
    */

    @ApiModelProperty(value = "流程实例状态 0 审批中（ 待处理）1已审批（已完成） 2 未通过（未修复） 3 已撤销",name="runStatus")
    private Integer runStatus;
    /**
    * 流程id
    */

    @ApiModelProperty(value = "流程id",name="flowId")
    private Integer flowId;
    /**
    * 流程发起人id
    */

    @ApiModelProperty(value = "流程发起人id",name="beginUserId")
    private String beginUserId;
    /**
    * 流程发起人部门id
    */

    @ApiModelProperty(value = "流程发起人部门id",name="beginOrgId")
    private Integer beginOrgId;
    /**
    * 
    */

    @ApiModelProperty(value = "",name="beginDeviceId")
    private String beginDeviceId;
    /**
    * 
    */

    @ApiModelProperty(value = "",name="beginUEventId")
    private String beginUEventId;
    /**
    * 流程实例创建时间
    */

    @ApiModelProperty(value = "流程实例创建时间",name="beginTime")
    private Integer beginTime;
    /**
    * 流程实例结束时间
    */

    @ApiModelProperty(value = "流程实例结束时间",name="endTime")
    private Integer endTime;
    /**
    * 报警类型
    */

    @ApiModelProperty(value = "报警类型",name="alarmType")
    private String alarmType;

    @ApiModelProperty(value = "报警时间",name="alarmTime")
    private Date alarmTime;
    /**
     * 工单类型
     */
    @ApiModelProperty(value = "工单类型",name="type")
    private Integer type;

    /**
     * 工单优先级
     */
    @ApiModelProperty(value = "工单优先级",name="priority")
    private String priority;
    /**
    * 附件id串（逗号分隔）
    */

    @ApiModelProperty(value = "附件id串（逗号分隔）",name="attachmentIds")
    private String attachmentIds;
    /**
    * 附件名称串（逗号分隔）
    */

    @ApiModelProperty(value = "附件名称串（逗号分隔）",name="attachmentNames")
    private String attachmentNames;
    /**
    * 当前处理人员ID
    */

    @ApiModelProperty(value = "当前处理人员ID",name="curUserId")
    private String curUserId;
    /**
    * 维护人员id 多个的话用逗号隔开
    */

    @ApiModelProperty(value = "维护人员id 多个的话用逗号隔开",name="maintainUserId")
    private String maintainUserId;
    /**
    * 报警级别
    */
    @ApiModelProperty(value = "最报警等级",name="alarmLevel")
    private Integer alarmLevel;
    /**
    * 各步骤的用户ID串，逗号分隔
    */

    @ApiModelProperty(value = "各步骤的用户ID串，逗号分隔",name="prcsUserIds")
    private String prcsUserIds;
    /**
    * 关注该流程的用户（逗号分隔）
    */

    @ApiModelProperty(value = "关注该流程的用户（逗号分隔）",name="focusUserIds")
    private String focusUserIds;
    /**
    * 已审批的用户（逗号分隔）
    */

    @ApiModelProperty(value = "已审批的用户（逗号分隔）",name="chkedUserIds")
    private String chkedUserIds;
    /**
    * 父流程id
    */

    @ApiModelProperty(value = "父流程id",name="parentRun")
    private Integer parentRun;
    /**
    * 传阅人id串（逗号分隔）
工作办理结束时选择的传阅人

    */

    @ApiModelProperty(value = "传阅人id串（逗号分隔） 工作办理结束时选择的传阅人 ",name="viewUserIds")
    private String viewUserIds;
    /**
    * 是否归档（0-否；1-是）
    */

    @ApiModelProperty(value = "是否归档（0-否；1-是）",name="isArchive")
    private Integer isArchive;
    /**
    * 强制结束信息
    */

    @ApiModelProperty(value = "强制结束信息",name="forceOver")
    private String forceOver;

    /**
     * 手工派单备注
     */
    @ApiModelProperty(value = "手工派单备注",name="remark")
    private String remark;

    /**
     * 预估时间
     */
    @ApiModelProperty(value = "预估时间",name="estimatedTime")
    private Integer estimatedTime;

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
}
