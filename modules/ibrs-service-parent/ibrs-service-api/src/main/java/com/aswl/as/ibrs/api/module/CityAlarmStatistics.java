package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 故障统计表Entity
 *
 * @author df
 * @date 2021/01/15 11:21
 */

@ApiModel(value = "CityAlarmStatistics", description = "故障统计表Entity")
@Data
public class CityAlarmStatistics extends BaseEntity<CityAlarmStatistics> {
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
     * 工单平均修复时长
     */
    @ApiModelProperty(value = "工单平均修复时长",name = "avgrepairTime")
    private Integer avgRepairTime;
    
}
