package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 报警方式Dto
* @author dingfei
* @date 2019-11-06 09:51
*/

@ApiModel(value = "AlarmWayDto",description = "报警方式Dto")
@Data
public class AlarmWayDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 报警方式
    */
    @ApiModelProperty(value = "报警方式",name="alarmMethod")
    private String alarmMethod;
    /**
    * 报警方式名
    */
    @ApiModelProperty(value = "报警方式名",name="alarmMethodName")
    private String alarmMethodName;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="isOn")
    private Integer isOn;
    /**
    * 优先权
    */
    @ApiModelProperty(value = "优先权",name="priority")
    private Integer priority;
    /**
    * 终端类型组
    */
    @ApiModelProperty(value = "终端类型组",name="terminalTypeGroup")
    private String terminalTypeGroup;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="createTerminal")
    private String createTerminal;
    /**
    * 修改终端
    */
    @ApiModelProperty(value = "修改终端",name="modifyTerminal")
    private String modifyTerminal;
    /**
    * 
    */
    @ApiModelProperty(value = "",name="lowestAlarmLevel")
    private Integer lowestAlarmLevel;
    /**
    * 创建者
    */
    @ApiModelProperty(value = "创建者",name="creator")
    private String creator;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",name="createDate")
    private Date createDate;
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
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
}
