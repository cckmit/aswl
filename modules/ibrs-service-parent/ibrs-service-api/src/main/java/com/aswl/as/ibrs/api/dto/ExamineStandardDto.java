package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.aswl.as.ibrs.api.module.ExamineItem;
import com.aswl.as.ibrs.api.module.ExamineRegion;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核标准Dto
* @author hfx
* @date 2020-03-19 10:19
*/

@ApiModel(value = "ExamineStandardDto",description = "考核标准Dto")
@Data
public class ExamineStandardDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID主键
    */
    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 考核标准名称
    */
    @ApiModelProperty(value = "考核标准名称",name="title")
    private String title;
    /**
    * 种类(多个用逗号分隔)
    */
    @ApiModelProperty(value = "种类(多个用逗号分隔)",name="kind")
    private String kind;
    /**
    * 考核周期(单位为月)
    */
    @ApiModelProperty(value = "考核周期(单位为月)",name="examinePeriod")
    private Integer examinePeriod;
    /**
    * 是否包含节假日
    */
    @ApiModelProperty(value = "是否包含节假日",name="includeHoliday")
    private Integer includeHoliday;
    /**
    * 是否启用
    */
    @ApiModelProperty(value = "是否启用",name="enable")
    private Integer enable;
    /**
    * 项目ID
    */
    @ApiModelProperty(value = "项目ID",name="projectId")
    private String projectId;
    /**
    * 存储时间
    */
    @ApiModelProperty(value = "存储时间",name="storeTime")
    private Date storeTime;
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

    // 添加对应的层的属性,以下属性不会直接保存到数据库，只作传递数据使用
    //as_examine_region 列表
    private List<ExamineRegion> regionList;

    //as_examine_item 列表
    private List<ExamineItem> itemList;

}
