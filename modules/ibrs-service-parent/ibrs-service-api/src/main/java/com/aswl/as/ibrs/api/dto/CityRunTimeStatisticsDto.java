package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 市级平台各时段工单趋势统计Dto
 *
 * @author hzj
 * @date 2021/01/22 15:57
 */

@ApiModel(value = "CityRunTimeStatisticsDto", description = "市级平台各时段工单趋势统计Dto")
@Data
public class CityRunTimeStatisticsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    @ApiModelProperty(value = "ID主键", name = "id")
    private String id;
    /**
     * 城市编码
     */
    @ApiModelProperty(value = "城市编码", name = "cityCode")
    private String cityCode;
    /**
     * 统计日期
     */
    @ApiModelProperty(value = "统计日期", name = "statisticsDate")
    private Date statisticsDate;
    /**
     * 派单数(时段0-2)
     */
    @ApiModelProperty(value = "派单数(时段0-2)", name = "runNumTime1")
    private Integer runNumTime1;
    /**
     * 派单数(时段2-4)
     */
    @ApiModelProperty(value = "派单数(时段2-4)", name = "runNumTime2")
    private Integer runNumTime2;
    /**
     * 派单数(时段4-6)
     */
    @ApiModelProperty(value = "派单数(时段4-6)", name = "runNumTime3")
    private Integer runNumTime3;
    /**
     * 派单数(时段6-8)
     */
    @ApiModelProperty(value = "派单数(时段6-8)", name = "runNumTime4")
    private Integer runNumTime4;
    /**
     * 派单数(时段8-10)
     */
    @ApiModelProperty(value = "派单数(时段8-10)", name = "runNumTime5")
    private Integer runNumTime5;
    /**
     * 派单数(时段10-12)
     */
    @ApiModelProperty(value = "派单数(时段10-12)", name = "runNumTime6")
    private Integer runNumTime6;
    /**
     * 派单数(时段12-14)
     */
    @ApiModelProperty(value = "派单数(时段12-14)", name = "runNumTime7")
    private Integer runNumTime7;
    /**
     * 派单数(时段14-16)
     */
    @ApiModelProperty(value = "派单数(时段14-16)", name = "runNumTime8")
    private Integer runNumTime8;
    /**
     * 派单数(时段16-18)
     */
    @ApiModelProperty(value = "派单数(时段16-18)", name = "runNumTime9")
    private Integer runNumTime9;
    /**
     * 派单数(时段18-20)
     */
    @ApiModelProperty(value = "派单数(时段18-20)", name = "runNumTime10")
    private Integer runNumTime10;
    /**
     * 派单数(时段20-22)
     */
    @ApiModelProperty(value = "派单数(时段20-22)", name = "runNumTime11")
    private Integer runNumTime11;
    /**
     * 派单数(时段22-24)
     */
    @ApiModelProperty(value = "派单数(时段22-24)", name = "runNumTime12")
    private Integer runNumTime12;
    /**
     * 修复数(时段0-2)
     */
    @ApiModelProperty(value = "修复数(时段0-2)", name = "repairNumTime1")
    private Integer repairNumTime1;
    /**
     * 修复数(时段2-4)
     */
    @ApiModelProperty(value = "修复数(时段2-4)", name = "repairNumTime2")
    private Integer repairNumTime2;
    /**
     * 修复数(时段4-6)
     */
    @ApiModelProperty(value = "修复数(时段4-6)", name = "repairNumTime3")
    private Integer repairNumTime3;
    /**
     * 修复数(时段6-8)
     */
    @ApiModelProperty(value = "修复数(时段6-8)", name = "repairNumTime4")
    private Integer repairNumTime4;
    /**
     * 修复数(时段8-10)
     */
    @ApiModelProperty(value = "修复数(时段8-10)", name = "repairNumTime5")
    private Integer repairNumTime5;
    /**
     * 修复数(时段10-12)
     */
    @ApiModelProperty(value = "修复数(时段10-12)", name = "repairNumTime6")
    private Integer repairNumTime6;
    /**
     * 修复数(时段12-14)
     */
    @ApiModelProperty(value = "修复数(时段12-14)", name = "repairNumTime7")
    private Integer repairNumTime7;
    /**
     * 修复数(时段14-16)
     */
    @ApiModelProperty(value = "修复数(时段14-16)", name = "repairNumTime8")
    private Integer repairNumTime8;
    /**
     * 修复数(时段16-18)
     */
    @ApiModelProperty(value = "修复数(时段16-18)", name = "repairNumTime9")
    private Integer repairNumTime9;
    /**
     * 修复数(时段18-20)
     */
    @ApiModelProperty(value = "修复数(时段18-20)", name = "repairNumTime10")
    private Integer repairNumTime10;
    /**
     * 修复数(时段20-22)
     */
    @ApiModelProperty(value = "修复数(时段20-22)", name = "repairNumTime11")
    private Integer repairNumTime11;
    /**
     * 修复数(时段22-24)
     */
    @ApiModelProperty(value = "修复数(时段22-24)", name = "repairNumTime12")
    private Integer repairNumTime12;
    /**
     * 告警数(时段0-2)
     */

    @ApiModelProperty(value = "告警数(时段0-2)", name = "alarmNumTime1")
    private Integer alarmNumTime1;
    /**
     * 告警数(时段2-4)
     */

    @ApiModelProperty(value = "告警数(时段2-4)", name = "alarmNumTime2")
    private Integer alarmNumTime2;
    /**
     * 告警数(时段4-6)
     */

    @ApiModelProperty(value = "告警数(时段4-6)", name = "alarmNumTime3")
    private Integer alarmNumTime3;
    /**
     * 告警数(时段6-8)
     */

    @ApiModelProperty(value = "告警数(时段6-8)", name = "alarmNumTime4")
    private Integer alarmNumTime4;
    /**
     * 告警数(时段8-10)
     */

    @ApiModelProperty(value = "告警数(时段8-10)", name = "alarmNumTime5")
    private Integer alarmNumTime5;
    /**
     * 告警数(时段10-12)
     */

    @ApiModelProperty(value = "告警数(时段10-12)", name = "alarmNumTime6")
    private Integer alarmNumTime6;
    /**
     * 告警数(时段12-14)
     */

    @ApiModelProperty(value = "告警数(时段12-14)", name = "alarmNumTime7")
    private Integer alarmNumTime7;
    /**
     * 告警数(时段14-16)
     */

    @ApiModelProperty(value = "告警数(时段14-16)", name = "alarmNumTime8")
    private Integer alarmNumTime8;
    /**
     * 告警数(时段16-18)
     */

    @ApiModelProperty(value = "告警数(时段16-18)", name = "alarmNumTime9")
    private Integer alarmNumTime9;
    /**
     * 告警数(时段18-20)
     */

    @ApiModelProperty(value = "告警数(时段18-20)", name = "alarmNumTime10")
    private Integer alarmNumTime10;
    /**
     * 告警数(时段20-22)
     */

    @ApiModelProperty(value = "告警数(时段20-22)", name = "alarmNumTime11")
    private Integer alarmNumTime11;
    /**
     * 告警数(时段22-24)
     */

    @ApiModelProperty(value = "告警数(时段22-24)", name = "alarmNumTime12")
    private Integer alarmNumTime12;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", name = "projectId")
    private String projectId;
    /**
     * 系统编码
     */
    @ApiModelProperty(value = "系统编码", name = "applicationCode")
    private String applicationCode;
    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码", name = "tenantCode")
    private String tenantCode;

    /**
     * 设备种类
     */
    @ApiModelProperty(value = "设备种类", name = "deviceKind")
    private String deviceKind;
}
