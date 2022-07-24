package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备故障统计Dto
* @author dingfei
* @date 2019-10-23 16:21
*/

@ApiModel(value = "AlarmLevelDto",description = "设备故障统计Dto")
@Data
public class AlarmLevelDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 级别
    */

    @ApiModelProperty(value = "级别",name = "alarmLevel")
    private Integer alarmLevel;

    @ApiModelProperty(value = "级别名称",name="alarmLevelName")
    private String alarmLevelName;

    @ApiModelProperty(value = "报警级别英文名",name="alarmLevelNameEn")
    private String alarmLevelNameEn;
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;

    /**
     * 英文备注
     */
    @ApiModelProperty(value = "英文备注",name="remarkEn")
    private String remarkEn;

    /**
     * 派单类型
     */
    @ApiModelProperty(value = "派单类型",name="orderGenType")
    private Integer orderGenType;

    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;

}
