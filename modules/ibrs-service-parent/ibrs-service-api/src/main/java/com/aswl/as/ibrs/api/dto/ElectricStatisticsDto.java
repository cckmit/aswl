package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 电量统计表Dto
 *
 * @author df
 * @date 2021/06/01 20:18
 */

@ApiModel(value = "ElectricStatisticsDto", description = "电量统计表Dto")
@Data
public class ElectricStatisticsDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID主键
     */
    @ApiModelProperty(value = "ID主键", name = "id")
    private String id;
    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID", name = "deviceId")
    private String deviceId;
    /**
     * 区域编码
     */
    @ApiModelProperty(value = "区域编码", name = "regionCode")
    private String regionCode;
    /**
     * 统计日期
     */
    @ApiModelProperty(value = "统计日期", name = "statisticsDate")
    private Date statisticsDate;
    /**
     * 总电量
     */
    @ApiModelProperty(value = "总电量", name = "electricTotal")
    private Double electricTotal;
    /**
     * 当天电量
     */
    @ApiModelProperty(value = "当天电量", name = "electricDay")
    private Double electricDay;
    /**
     * 设备型号ID
     */
    @ApiModelProperty(value = "设备型号ID", name = "deviceModelId")
    private String deviceModelId;
    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", name = "projectId")
    private String projectId;

    /**
     * 租户编码
     */
    @ApiModelProperty(value = "租户编码", name = "tenantCode")
    private String tenantCode;

    /**
     * 统计日期显示单位
     */
    @ApiModelProperty(value = "统计日期显示单位", name = "timeUnit")
    private String timeUnit;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", name = "startTime")
    private String startTime;
    
    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", name = "endTime")
    private String endTime;

    /**
     * 用电单位
     */
    @ApiModelProperty(value = "用电单位ID", name = "unitId")
    private String unitId;

    /**
     * 前一天总电量读数
     */
    @ApiModelProperty(value = "前一天总电量读数", name = "electricTotalLastDay")
    private Double electricTotalLastDay;

    /**
     * 查询年份
     */
    private String queryYear;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 强制查询某个项目
     */
    @ApiModelProperty(value = "强制查询某个项目", name = "queryProjectId")
    private String queryProjectId;
    
}
