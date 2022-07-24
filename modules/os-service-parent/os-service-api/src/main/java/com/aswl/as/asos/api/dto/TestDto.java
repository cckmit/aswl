package com.aswl.as.asos.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
*
* 设备故障统计Dto
* @author dingfei
* @date 2019-10-23 16:21
*/

@ApiModel(value = "AlarmLevelDto",description = "设备故障统计Dto")
@Data
public class TestDto implements Serializable {

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
    /**
    * 备注
    */
    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
}
