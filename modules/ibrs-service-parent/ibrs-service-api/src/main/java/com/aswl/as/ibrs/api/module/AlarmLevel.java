package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 设备故障统计Entity
* @author dingfei
* @date 2019-10-23 16:21
*/

@ApiModel(value = "AlarmLevel",description = "设备故障统计Entity")
@Data
public class AlarmLevel extends BaseEntity<AlarmLevel> {

    /**
     * 级别
     */
    @ApiModelProperty(value = "级别",name = "alarmLevel")
    private Integer alarmLevel;

    /**
     * 级别名称
     */
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
}
