package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 流程信息Dto
* @author dingfei
* @date 2019-10-25 16:49
*/

@ApiModel(value = "FlowInfoDto",description = "流程信息Dto")
@Data
public class FlowInfoDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 流程ID
    */
    @ApiModelProperty(value = "流程ID",name="id")
    private String id;
    /**
    * 流程名称
    */
    @ApiModelProperty(value = "流程名称",name="flowName")
    private String flowName;
    /**
    * 表单ID
    */
    @ApiModelProperty(value = "表单ID",name="formId")
    private Integer formId;
    /**
    * 是否允许附件（0-否；1-是）
    */
    @ApiModelProperty(value = "是否允许附件（0-否；1-是）",name="flowDoc")
    private Integer flowDoc;
    /**
    * 流程类型（1-自由流程，2-固定流程）
    */
    @ApiModelProperty(value = "流程类型（1-自由流程，2-固定流程）",name="flowType")
    private Integer flowType;
    /**
    * 状态 0 停用 1 启用
    */
    @ApiModelProperty(value = "状态 0 停用 1 启用",name="flowStatus")
    private Integer flowStatus;
    /**
    * 流程图标
    */
    @ApiModelProperty(value = "流程图标",name="flowIcon")
    private String flowIcon;
    /**
    * 流程编号
    */
    @ApiModelProperty(value = "流程编号",name="flowNo")
    private String flowNo;
    /**
    * 流程分类id
    */
    @ApiModelProperty(value = "流程分类id",name="flowSort")
    private Integer flowSort;
    /**
    * 自动文号表达式
    */
    @ApiModelProperty(value = "自动文号表达式",name="autoName")
    private String autoName;
    /**
    * 自动编号计数器
    */
    @ApiModelProperty(value = "自动编号计数器",name="autoNum")
    private Integer autoNum;
    /**
    * 自动编号显示长度
    */
    @ApiModelProperty(value = "自动编号显示长度",name="autoLen")
    private Integer autoLen;
    /**
    * 新建工作时是否允许手工修改文号： 0-不允许手工修改文号；1-允许手工修改文号；2-允许在自动文号前输入前缀；3-允许在自动文号后输入后缀；4-允许在自动文号前后输入前缀和后缀；
    */
    @ApiModelProperty(value = "新建工作时是否允许手工修改文号： 0-不允许手工修改文号；1-允许手工修改文号；2-允许在自动文号前输入前缀；3-允许在自动文号后输入后缀；4-允许在自动文号前后输入前缀和后缀；",name="autoEdit")
    private Integer autoEdit;
    /**
    * 流程说明
    */
    @ApiModelProperty(value = "流程说明",name="flowDesc")
    private String flowDesc;
    /**
    * 自由流程新建权限：分为按用户、按部门、按角色三种授权方式；形成“用户ID串|部门ID串|角色ID串”格式的字符串；其中用户ID串、部门ID串和角色ID串均是逗号分隔的字符串；
    */
    @ApiModelProperty(value = "自由流程新建权限：分为按用户、按部门、按角色三种授权方式；形成“用户ID串|部门ID串|角色ID串”格式的字符串；其中用户ID串、部门ID串和角色ID串均是逗号分隔的字符串；",name="newUser")
    private String newUser;
    /**
    * 所属部门ID
    */
    @ApiModelProperty(value = "所属部门ID",name="orgId")
    private Integer orgId;
    /**
    * 是否允许预设步骤（0-否；1-是）
    */
    @ApiModelProperty(value = "是否允许预设步骤（0-否；1-是）",name="freePreset")
    private Integer freePreset;
    /**
    * 委托类型0-禁止委托；1-仅允许委托当前步骤经办人（本步骤实际的经办人，该步骤可能定义了五个人，但是转交时选择了三个）；2-自由委托；3-按步骤设置的经办权限委托（跟1的区别是按照定义的经办人来委托）
    */
    @ApiModelProperty(value = "委托类型0-禁止委托；1-仅允许委托当前步骤经办人（本步骤实际的经办人，该步骤可能定义了五个人，但是转交时选择了三个）；2-自由委托；3-按步骤设置的经办权限委托（跟1的区别是按照定义的经办人来委托）",name="freeOther")
    private Integer freeOther;
    /**
    * 列表扩展字段串（逗号分隔）查询页面仅查询该流程时生效
    */
    @ApiModelProperty(value = "列表扩展字段串（逗号分隔）查询页面仅查询该流程时生效",name="listFldsStr")
    private String listFldsStr;
    /**
    * 是否强制修改文号
    */
    @ApiModelProperty(value = "是否强制修改文号",name="procePreSet")
    private Integer procePreSet;
    /**
    * 模型ID
    */
    @ApiModelProperty(value = "模型ID",name="modelId")
    private String modelId;
    /**
    * 模型名称
    */
    @ApiModelProperty(value = "模型名称",name="modelName")
    private String modelName;
    /**
    * 说明文档附件ID串（逗号分隔）
    */
    @ApiModelProperty(value = "说明文档附件ID串（逗号分隔）",name="attachmentId")
    private String attachmentId;
    /**
    * 说明文档附件名称串（*分隔）
    */
    @ApiModelProperty(value = "说明文档附件名称串（*分隔）",name="attachmentName")
    private String attachmentName;
    /**
    * 传阅人ID串（逗号分隔）
    */
    @ApiModelProperty(value = "传阅人ID串（逗号分隔）",name="viewUserIds")
    private String viewUserIds;
    /**
    * 允许传阅（0-不允许；1-允许）
    */
    @ApiModelProperty(value = "允许传阅（0-不允许；1-允许）",name="viewPriv")
    private Integer viewPriv;
    /**
    * 是否启用版本控制（0-否；1-是）
    */
    @ApiModelProperty(value = "是否启用版本控制（0-否；1-是）",name="isVersion")
    private Integer isVersion;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="updateTime")
    private Integer updateTime;
    /**
    * 系统编号
    */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
    * SAAS租户编号
    */
    @ApiModelProperty(value = "SAAS租户编号",name="tenantCode")
    private String tenantCode;
}
