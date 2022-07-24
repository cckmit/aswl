package com.aswl.as.ibrs.api.module;
import java.util.Date;
import java.util.List;

import com.aswl.as.common.core.persistence.BaseEntity;
import com.aswl.as.ibrs.api.dto.ExamineRegionDto;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核标准Entity
* @author hfx
* @date 2020-03-19 10:19
*/

@ApiModel(value = "ExamineStandard",description = "考核标准Entity")
@Data
public class ExamineStandard extends BaseEntity<ExamineStandard> {

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

    // 添加对应的层的属性,以下属性不会直接保存到数据库，只作传递数据使用
    //as_examine_region 列表
    private List<ExamineRegion> regionList;

    //as_examine_item 列表
    private List<ExamineItem> itemList;

}
