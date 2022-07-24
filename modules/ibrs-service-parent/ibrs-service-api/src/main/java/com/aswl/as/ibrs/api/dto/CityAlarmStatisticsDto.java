package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 故障统计表Dto
 *
 * @author df
 * @date 2021/01/15 11:21
 */

@ApiModel(value = "CityAlarmStatisticsDto", description = "故障统计表Dto")
@Data
public class CityAlarmStatisticsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    @ApiModelProperty(value = "ID主键", name = "id")
    private String id;
    /**
     * 统计日期
     */
    @ApiModelProperty(value = "统计日期", name = "statisticsDate")
    private Date statisticsDate;
    /**
     * 城市编码
     */
    @ApiModelProperty(value = "城市编码", name = "cityCode")
    private String cityCode;
    /**
     * 设备种类
     */
    @ApiModelProperty(value = "设备种类", name = "deviceKind")
    private String deviceKind;
    /**
     * 设备型号ID
     */
    @ApiModelProperty(value = "设备型号ID", name = "deviceModelId")
    private String deviceModelId;

    /**
     * 告警类型
     */
    @ApiModelProperty(value = "告警类型", name = "alarmType")
    private String alarmType;

    /**
     * 告警数量
     */
    @ApiModelProperty(value = "告警数量", name = "alarmNum")
    private Integer alarmNum;

    /**
     * 故障数量
     */
    @ApiModelProperty(value = "故障数量", name = "faultNum")
    private Integer faultNum;

    /**
     * 派单数
     */
    @ApiModelProperty(value = "派单数", name = "runNum")
    private Integer runNum;

    /**
     * 修复数
     */
    @ApiModelProperty(value = "修复数", name = "repairNum")
    private Integer repairNum;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", name = "projectId")
    private String projectId;


    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", name = "startTime")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", name = "endTime")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private String endTime;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称", name = "cityName")
    private String cityName;

    /**
     * 日期
     */
    @ApiModelProperty(value = "日期", name = "date")
    private String date;

    /**
     * 工单平均修复时长
     */
    @ApiModelProperty(value = "工单平均修复时长",name = "avgrepairTime")
    private Integer avgRepairTime;

    /**
     * 时间单位
     */
    @ApiModelProperty(value = "时间单位",name = "avgrepairTime")
    private String timeUnit;

    /**
     * 上级区域id
     */
    @ApiModelProperty(value = "上级区域id",name = "parentId")
    private String parentId;

}
