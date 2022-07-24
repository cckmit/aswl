package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 流程实例日志Dto
* @author dingfei
* @date 2019-10-28 15:34
*/

@ApiModel(value = "FlowRunLogDto",description = "流程实例日志Dto")
@Data
public class FlowRunLogDto implements Serializable {

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
    * 流程实例名称
    */
    @ApiModelProperty(value = "流程实例名称",name="runName")
    private String runName;
    /**
     * 流程实例状态
     */
    @ApiModelProperty(value = "流程实例状态",name="runStatus")
    private String runStatus;
    /**
    * 流程id
    */
    @ApiModelProperty(value = "流程id",name="flowId")
    private Integer flowId;
    /**
    * 流程实例步骤id（流程实例实际步骤号）
    */
    @ApiModelProperty(value = "流程实例步骤id（流程实例实际步骤号）",name="prcsId")
    private Integer prcsId;
    /**
    * 流程步骤id（设计流程步骤号）
    */
    @ApiModelProperty(value = "流程步骤id（设计流程步骤号）",name="flowPrcs")
    private Integer flowPrcs;
    /**
    * 操作人id
    */
    @ApiModelProperty(value = "操作人id",name="userId")
    private String userId;
    /**
    * 操作时间
    */
    @ApiModelProperty(value = "操作时间",name="logTime")
    private Integer logTime;
    /**
    * 操作人ip地址
    */
    @ApiModelProperty(value = "操作人ip地址",name="ip")
    private String ip;
    /**
    * 日志类型：
        1-新建、转交、回退、收回；
        2-委托；
        3-删除；
        4-销毁；
        5-还原被终止的流程；
        6-编辑数据；
        7-流转过程中修改经办权限；
    */
    @ApiModelProperty(value = "日志类型： 1-新建、转交、回退、收回； 2-委托； 3-删除； 4-销毁； 5-还原被终止的流程； 6-编辑数据； 7-流转过程中修改经办权限； ",name="logType")
    private Integer logType;
    /**
    * 日志信息
    */
    @ApiModelProperty(value = "日志信息",name="content")
    private String content;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="remark")
    private String remark;
}
