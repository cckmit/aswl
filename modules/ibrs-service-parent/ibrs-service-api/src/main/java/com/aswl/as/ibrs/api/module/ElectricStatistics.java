package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 电量统计表Entity
 *
 * @author df
 * @date 2021/06/01 20:18
 */

@ApiModel(value = "ElectricStatistics", description = "电量统计表Entity")
@Data
public class ElectricStatistics extends BaseEntity<ElectricStatistics> {
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
     * 前一天总电量读数
     */
    @ApiModelProperty(value = "前一天总电量读数", name = "electricTotalLastDay")
    private Double electricTotalLastDay;

}
