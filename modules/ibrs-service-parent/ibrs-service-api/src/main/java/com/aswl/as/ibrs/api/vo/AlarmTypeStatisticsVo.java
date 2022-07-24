package com.aswl.as.ibrs.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dingfei
 * @version 1.0.0
 * @create 2019/10/21 16:29
 */
@Data
public class AlarmTypeStatisticsVo implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 总数
     */
    @ApiModelProperty(value = "总数",name = "total")
    private Integer total;
    /**
     * 统计时间
     */
    @ApiModelProperty(value = "统计时间",name = "storeTime")
    private String storeTime;
    /**
     * 报警类型
     */
    @ApiModelProperty(value = "报警类型",name = "alarmType")
    private String alarmType;
    /**
     * 报警类型名称
     */
    @ApiModelProperty(value = "报警类型名称",name = "alarmTypeName")
    private String alarmTypeName;

    /**
     * 报警类型名称英文
     */
    @ApiModelProperty(value = "报警类型名称英文",name = "alarmTypeNameEn")
    private String alarmTypeNameEn;
    /**
     * 事件状态组组名英文
     */
    @ApiModelProperty(value = "事件状态组组名英文",name = "groupName")
    private String groupName;

    /**
     * 事件状态组组名中文
     */
    @ApiModelProperty(value = "事件状态组组名中文",name = "name")
    private String name;

    /**
     * 事件状态组组名英文
     */
    @ApiModelProperty(value = "事件状态组组名英文",name = "nameEn")
    private String nameEn;

    /**
     * 是否状态组(1、是、2、否)
     */
    @ApiModelProperty(value = "是否状态组(1、是、2、否)",name = "isGroup")
    private Boolean isGroup;

    /**
     * 开关量/模拟量 (1、开关量、2、模拟量)
     */
    @ApiModelProperty(value = "开关量/模拟量 (1、开关量、2、模拟量)",name = "vectorType")
    private Integer vectorType;

    /**
     * 报警类型CODECN中文值
     */
    @ApiModelProperty(value = "报警类型CODECN中文值",name = "codeCn")
    private String codeCn;

    /**
     * 报警类型CODECN英文
     */
    @ApiModelProperty(value = "报警类型CODECN英文",name = "code")
    private String code;
}
