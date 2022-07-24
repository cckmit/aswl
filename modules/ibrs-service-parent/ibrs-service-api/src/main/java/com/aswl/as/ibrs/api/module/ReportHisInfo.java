package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 统计报表Entity
* @author df
* @date 2021/07/20 17:28
*/

@ApiModel(value = "ReportHisInfo",description = "统计报表Entity")
@Data
public class ReportHisInfo extends BaseEntity<ReportHisInfo> {
    /**
    * 区域编码
    */

    @ApiModelProperty(value = "区域编码",name="regionCode")
    private String regionCode;
    /**
    * 统计类型
    */

    @ApiModelProperty(value = "统计类型",name="statisType")
    private String statisType;
    /**
    * 统计开始时间
    */

    @ApiModelProperty(value = "统计开始时间",name="statisStartTime")
    private Date statisStartTime;
    /**
    * 统计结束时间
    */

    @ApiModelProperty(value = "统计结束时间",name="statisEndTime")
    private Date statisEndTime;
    /**
    * 文档格式
    */

    @ApiModelProperty(value = "文档格式",name="docFormat")
    private String docFormat;
}
