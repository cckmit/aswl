package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 图形色标设置表Entity
 *
 * @author df
 * @date 2021/07/12 16:54
 */

@ApiModel(value = "ReportStatisticsColorSetting", description = "图形色标设置表Entity")
@Data
public class ReportStatisticsColorSetting extends BaseEntity<ReportStatisticsColorSetting> {
    /**
     * 颜色值
     */

    @ApiModelProperty(value = "颜色值", name = "colors")
    private String colors;

    /**
     * 统计类别
     */
    @ApiModelProperty(value = "统计类别", name = "statisticsType")
    private String statisticsType;
}
