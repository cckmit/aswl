package com.aswl.as.ibrs.api.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dingfei
 * @Description
 * @date 2019-10-26 11:28
 * @Version V1
 */
@Data
public class AlarmStatusVo implements java.io.Serializable {

    /**
     * 表字段名
     */
    @ApiModelProperty(value = "表字段名对应值",name = "fldValue")
    private String fldValue;
    /**
     * 状态组ID
     */
    @ApiModelProperty(value = "状态组ID",name = "groupId")
    private String groupId;

    /**
     * 矢量类型 0 模拟量 1开关量
     */
    @ApiModelProperty(value = "矢量类型 0 模拟量 1开关量",name = "vectorType")
    private Integer  vectorType;
}
