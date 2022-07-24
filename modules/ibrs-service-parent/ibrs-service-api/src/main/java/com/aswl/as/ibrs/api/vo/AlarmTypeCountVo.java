package com.aswl.as.ibrs.api.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Getter
@Setter
public class AlarmTypeCountVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类型名
     */
    @ApiModelProperty(value = "类型名",name = "typeName")
    private String typeName;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量",name = "count")
    private Long count;

    /**
     * 年度数量
     */
    @ApiModelProperty(value = "年度数量",name = "yearCount")
    private Long yearCount;
}
