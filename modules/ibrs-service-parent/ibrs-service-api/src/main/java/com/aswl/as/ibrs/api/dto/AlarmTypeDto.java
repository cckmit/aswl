package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 报警类型Dto
* @author dingfei
* @date 2019-10-22 11:48
*/

@ApiModel(value = "AlarmTypeDto",description = "报警类型Dto")
@Data
public class AlarmTypeDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 报警类型
    */
    @ApiModelProperty(value = "报警类型",name="alarmType")
    private String alarmType;
    /**
    * 报警类型名
    */
    @ApiModelProperty(value = "报警类型名",name="alarmTypeName")
    private String alarmTypeName;

    /**
     * 报警类型英文名
     */
    @ApiModelProperty(value = "报警类型英文名",name="alarmTypeNameEn")
    private String alarmTypeNameEn;
    /**
    * 优先权
    */
    @ApiModelProperty(value = "优先权",name="priority")
    private Integer priority;
    /**
    * 报警等级
    */
    @ApiModelProperty(value = "报警等级",name="alarmLevel")
    private Integer alarmLevel;
    /**
    * 状态名
    */
    @ApiModelProperty(value = "状态名",name="statusName")
    private String statusName;
    /**
    * 状态值
    */
    @ApiModelProperty(value = "状态值",name="statusValue")
    private Integer statusValue;
    /**
    * 关联状态
    */
    @ApiModelProperty(value = "关联状态",name="relevancyStatus")
    private String relevancyStatus;
    /**
    * 否决权
    */
    @ApiModelProperty(value = "否决权",name="veto")
    private Integer veto;
    /**
    * 报警类型种类
    */
    @ApiModelProperty(value = "报警类型种类",name="KindVo")
    private String kind;
    /**
    * 图标路径
    */
    @ApiModelProperty(value = "图标路径",name="iconPath")
    private String iconPath;
    /**
    * 事件元数据状态ID
    */
    @ApiModelProperty(value = "事件元数据状态ID",name="eventMetadataId")
    private String eventMetadataId;
    /**
    * 是否页面展示
    */
    @ApiModelProperty(value = "是否页面展示",name="pageShow")
    private Integer pageShow;
    /**
    * 编码
    */
    @ApiModelProperty(value = "编码",name="code")
    private String code;
    /**
    * code中文名
    */
    @ApiModelProperty(value = "code中文名",name="codeCn")
    private String codeCn;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    /**
     * 备注英文
     */
    @ApiModelProperty(value = "备注英文",name="remarkEn")
    private String remarkEn;
}
