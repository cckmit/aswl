package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 图形色标设置表Dto
* @author df
* @date 2021/07/12 16:54
*/

@ApiModel(value = "ReportStatisticsColorSettingDto",description = "图形色标设置表Dto")
@Data
public class ReportStatisticsColorSettingDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 颜色值
    */
    @ApiModelProperty(value = "颜色值",name="colors")
    private String colors;

    /**
     * 统计类别
     */
    @ApiModelProperty(value = "统计类别", name = "statisticsType")
    private String statisticsType;

    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",name="createDate")
    private Date createDate;
    /**
    * 项目ID
    */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;
    /**
    * 系统编号
    */
    @ApiModelProperty(value = "系统编号",name="applicationCode")
    private String applicationCode;
    /**
    * SAAS租户编码
    */
    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;
}
