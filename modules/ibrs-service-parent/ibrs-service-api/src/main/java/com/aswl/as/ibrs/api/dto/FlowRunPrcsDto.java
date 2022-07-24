package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 流程实例步骤信息Dto
* @author dingfei
* @date 2019-10-28 16:31
*/

@ApiModel(value = "FlowRunPrcsDto",description = "流程实例步骤信息Dto")
@Data
public class FlowRunPrcsDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键id
    */
    @ApiModelProperty(value = "主键id",name="id")
    private String id;
    /**
    * 流程实例id
    */
    @ApiModelProperty(value = "流程实例id",name="runId")
    private Integer runId;
    /**
    * 流程实例步骤id
    */
    @ApiModelProperty(value = "流程实例步骤id",name="prcsId")
    private Integer prcsId;
    /**
    * 用户id
    */
    @ApiModelProperty(value = "用户id",name="userId")
    private String userId;
    /**
    * 工作接收时间
    */
    @ApiModelProperty(value = "工作接收时间",name="prcsTime")
    private Integer prcsTime;
    /**
    * 工作转交/办结时间
    */
    @ApiModelProperty(value = "工作转交/办结时间",name="deliverTime")
    private Integer deliverTime;
    /**
    * 步骤状态：
0-未接收；
1-办理中；
2-转交下一步，下一步经办人无人接收；
4-已办结；
5-自由流程预设步骤；
6-已挂起；

    */
    @ApiModelProperty(value = "步骤状态： 0-未接收； 1-办理中； 2-转交下一步，下一步经办人无人接收； 4-已办结； 5-自由流程预设步骤； 6-已挂起； ",name="prcsFlag")
    private Integer prcsFlag;
    /**
    * 步骤id（设计流程中的步骤号）
    */
    @ApiModelProperty(value = "步骤id（设计流程中的步骤号）",name="flowPrcs")
    private Integer flowPrcs;
    /**
    * 是否主办（0-经办；1-主办）
    */
    @ApiModelProperty(value = "是否主办（0-经办；1-主办）",name="opFlag")
    private Integer opFlag;
    /**
    * 主办人选项：
0-指定主办人；
1-先接收者主办；
2-无主办人会签；

    */
    @ApiModelProperty(value = "主办人选项： 0-指定主办人； 1-先接收者主办； 2-无主办人会签； ",name="topFlag")
    private Integer topFlag;
    /**
    * 上一步骤id（哪个步骤转交过来的）
    */
    @ApiModelProperty(value = "上一步骤id（哪个步骤转交过来的）",name="parentId")
    private Integer parentId;
    /**
    * 子流程的流程实例id（run_id）
    */
    @ApiModelProperty(value = "子流程的流程实例id（run_id）",name="childRunId")
    private Integer childRunId;
    /**
    * 办理时限
    */
    @ApiModelProperty(value = "办理时限",name="timeOut")
    private String timeOut;
    /**
    * 工作委托用户id串（即工作委托给哪些用户，逗号分隔）
    */
    @ApiModelProperty(value = "工作委托用户id串（即工作委托给哪些用户，逗号分隔）",name="otherUser")
    private String otherUser;
    /**
    * 是否超时（1-超时；其他-不超时）
    */
    @ApiModelProperty(value = "是否超时（1-超时；其他-不超时）",name="timeOutFlag")
    private Integer timeOutFlag;
    /**
    * 工作创建时间
    */
    @ApiModelProperty(value = "工作创建时间",name="createTime")
    private Integer createTime;
    /**
    * 工作移交用户id（即工作是由哪个用户移交过来的）
    */
    @ApiModelProperty(value = "工作移交用户id（即工作是由哪个用户移交过来的）",name="fromUserId")
    private String fromUserId;
    /**
    * 取消挂起的时间（仅用于挂起的流程）
    */
    @ApiModelProperty(value = "取消挂起的时间（仅用于挂起的流程）",name="activeTime")
    private Integer activeTime;
    /**
    * 批注
    */
    @ApiModelProperty(value = "批注",name="comment")
    private String comment;
    /**
    * 超时统计查询增加部门
    */
    @ApiModelProperty(value = "超时统计查询增加部门",name="prcsOrg")
    private Integer prcsOrg;
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
