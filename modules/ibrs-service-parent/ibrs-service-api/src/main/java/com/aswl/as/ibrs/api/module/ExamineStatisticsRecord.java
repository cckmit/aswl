package com.aswl.as.ibrs.api.module;

import com.aswl.as.common.core.persistence.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 考核统计扣分记录Entity
 */
@ApiModel(value = "ExamineStatisticsRecord",description = "考核统计扣分记录Entity")
@Data
public class ExamineStatisticsRecord extends BaseEntity<ExamineStatisticsRecord> {

    /**
     *考核统计ID
     */
    @ApiModelProperty(value = "examineStatisticsId",name = "考核统计ID")
    private String examineStatisticsId;
    /**
     * '考核指标ID'
     */
    @ApiModelProperty(value = "examineBaseLibId",name = "'考核指标ID'")
    private String examineBaseLibId;


    /**
     * 区域编码
     */
    @ApiModelProperty(value = "regionNo",name = "'区域编码'")
    private String regionNo;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "regionName",name = "'区域名称'")
    private String regionName;

    /**
     * 关联ID
     */
    @ApiModelProperty(value = "relateId",name = "关联ID")
    private String  relateId;

    /**
     * 存储时间
     */
    @ApiModelProperty(value = "storeTime",name = "'存储时间'")
    private Date storeTime;
}
