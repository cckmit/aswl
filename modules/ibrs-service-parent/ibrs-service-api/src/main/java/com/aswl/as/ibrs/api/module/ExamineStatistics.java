package com.aswl.as.ibrs.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 考核统计Entity
 */
@ApiModel(value = "ExamineStatistics", description = "考核统计Entity")
@Data
public class ExamineStatistics extends BaseEntity<ExamineStatistics> {

    /**
     *考核标准ID
     */
    @ApiModelProperty(value = "考核标准ID",name="examineStandardId")
    private String examineStandardId;

    /**
     * 考核年份
     */
    @ApiModelProperty(value = "考核年份",name="examineYear")
    private String examineYear;

    /**
     * 考核月份周期
     */
    @ApiModelProperty(value = "考核月份周期",name="examineMonthPeriod")
    private String examineMonthPeriod;

    /**
     * 存储时间
     */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
}
