package com.aswl.as.asos.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
*
* 设备故障统计Entity
* @author dingfei
* @date 2019-10-23 16:21
*/

@ApiModel(value = "AlarmLevel",description = "设备故障统计Entity")
@Data
public class Test extends BaseEntity<Test> {

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
    /**
    * 备注
    */

    @ApiModelProperty(value = "备注",name="remark")
    private String remark;
}
