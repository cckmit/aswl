package com.aswl.as.ibrs.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 考核统计扣分Entity
 */

@ApiModel(value = "ExamineStatisticsDeduct", description = "考核统计扣分Entity")
@Data
public class ExamineStatisticsDeduct extends BaseEntity<ExamineStatisticsDeduct> {

    /**
     * 考核统计ID
     */
    @ApiModelProperty(value = "examineStatisticsId",name = "")
    private String examineStatisticsId;

    /**
     * 考核指标ID
     */
    @ApiModelProperty(value = "examineBaseLibId",name = "考核指标ID")
    private String examineBaseLibId;

    /**
     *扣分
     */
    @ApiModelProperty(value = "deductScoreTotal",name = "扣分")
    private BigDecimal deductScoreTotal;

    /**
     * 存储时间
     */
    @ApiModelProperty(value = "storeTime",name = "存储时间")
    private Date storeTime;

    /**
     * 扣费
     */
    @ApiModelProperty(value = "deductFeeTotal",name = "扣费")
    private Double deductFeeTotal;
}
