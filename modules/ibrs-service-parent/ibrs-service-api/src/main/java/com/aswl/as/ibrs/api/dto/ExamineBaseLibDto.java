package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核指标库Dto
* @author hfx
* @date 2020-03-19 10:17
*/

@ApiModel(value = "ExamineBaseLibDto",description = "考核指标库Dto")
@Data
public class ExamineBaseLibDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * ID主键
    */
    @ApiModelProperty(value = "ID主键",name="id")
    private String id;
    /**
    * 标题
    */
    @ApiModelProperty(value = "标题",name="title")
    private String title;
    /**
    * 编码
    */
    @ApiModelProperty(value = "编码",name="code")
    private String code;
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
}
