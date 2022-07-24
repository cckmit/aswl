package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 统计报表Dto
* @author df
* @date 2021/07/20 17:28
*/

@ApiModel(value = "ReportHisInfoDto",description = "统计报表Dto")
@Data
public class ReportHisInfoDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty(value = "主键",name="id")
    private String id;
    /**
    * 项目ID
    */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;
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
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",name="createDate")
    private Date createDate;
    /**
    * 系统编码
    */
    @ApiModelProperty(value = "系统编码",name="applicationCode")
    private String applicationCode;
    /**
    * SAAS租户编码
    */
    @ApiModelProperty(value = "SAAS租户编码",name="tenantCode")
    private String tenantCode;

    /**
     * 查询月份
     */
    @ApiModelProperty(value = "查询月份",name="month")
    private String month;
}
