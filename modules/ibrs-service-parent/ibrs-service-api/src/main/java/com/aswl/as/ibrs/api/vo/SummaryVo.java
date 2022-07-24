package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *故障维护汇总统计Vo
 */
@Data
@Getter
@Setter
public class SummaryVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 智能箱型号
     */
    @ApiModelProperty(value = "智能箱型号",name = "models")
    private List<String> models;

    /**
     * 每个类型的数量和年度数量
     */
    @ApiModelProperty(value = "每个类型的数量和年度数量",name = "list")
    private List<AlarmTypeCountVo> list;

    /**
     * 年度故障预警趋势图
     */
    @ApiModelProperty(value = "年度故障预警趋势图",name = "trend")
    private List<Map> trend;

    /**
     * 在线率
     */
    @ApiModelProperty(value = "在线率",name = "onlineRate")
    private List<Map> onlineRate;

    /**
     * 平均修复时长
     */
    @ApiModelProperty(value = "平均修复时长",name = "repairDuration")
    private List<Map> repairDuration;

}
