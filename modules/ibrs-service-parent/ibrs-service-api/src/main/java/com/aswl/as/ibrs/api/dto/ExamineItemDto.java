package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.aswl.as.ibrs.api.module.ExamineBaseLib;
import com.aswl.as.ibrs.api.module.ExamineDeductRule;
import com.aswl.as.ibrs.api.module.ExamineTimePartConfig;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核项Dto
* @author hfx
* @date 2020-03-19 10:18
*/

@ApiModel(value = "ExamineItemDto",description = "考核项Dto")
@Data
public class ExamineItemDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID主键
    */
    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 考核标准ID
    */
    @ApiModelProperty(value = "考核标准ID",name="examineStandardId")
    private String examineStandardId;
    /**
    * 标题
    */
    @ApiModelProperty(value = "标题",name="title")
    private String title;
    /**
    * 考核基础库ID
    */
    @ApiModelProperty(value = "考核基础库ID",name="examineBaseLibId")
    private String examineBaseLibId;
    /**
    * 是否按时段考核
    */
    @ApiModelProperty(value = "是否按时段考核",name="examineTimePart")
    private Integer examineTimePart;
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


    //---- 下面字段不会直接保存到数据库，只作参数传递使用-----

    @ApiModelProperty(value = "时间部分数据配置",name="timePartConfigList")
    private List<ExamineTimePartConfig> timePartConfigList;

    @ApiModelProperty(value = "扣减规则",name="ruleList")
    private List<ExamineDeductRule> ruleList;

}
