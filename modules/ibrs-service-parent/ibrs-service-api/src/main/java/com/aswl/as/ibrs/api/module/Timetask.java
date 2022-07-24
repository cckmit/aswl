package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 定时任务Entity
* @author dingfei
* @date 2019-11-13 10:19
*/

@ApiModel(value = "Timetask",description = "定时任务Entity")
@Data
public class Timetask extends BaseEntity<Timetask> {


    /**
    * cron表达式
    */

    @ApiModelProperty(value = "cron表达式",name="cronExpression")
    private String cronExpression;
    /**
    * 是否生效 0/未生效,1/生效
    */

    @ApiModelProperty(value = "是否生效 0/未生效,1/生效",name="isEffect")
    private String isEffect;
    /**
    * 是否运行0停止,1运行
    */

    @ApiModelProperty(value = "是否运行0停止,1运行",name="isStart")
    private String isStart;
    /**
    * 任务描述
    */

    @ApiModelProperty(value = "任务描述",name="taskDescribe")
    private String taskDescribe;
    /**
    * 任务ID
    */

    @ApiModelProperty(value = "任务ID",name="taskId")
    private String taskId;
    /**
    * 任务类名
    */

    @ApiModelProperty(value = "任务类名",name="className")
    private String className;
    /**
    * 任务运行服务器IP
    */

    @ApiModelProperty(value = "任务运行服务器IP",name="runServerIp")
    private String runServerIp;
    /**
    * 远程主机（域名/IP+项目路径）
    */

    @ApiModelProperty(value = "远程主机（域名/IP+项目路径）",name="runServer")
    private String runServer;
    /**
    * 修改终端
    */

    @ApiModelProperty(value = "修改终端",name="modifyTerminal")
    private String modifyTerminal;
    /**
    * 创建着
    */

    /**
    * 创建终端
    */

    @ApiModelProperty(value = "创建终端",name="createTerminal")
    private String createTerminal;
    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
    /**
    * 系统编号
    */


}
