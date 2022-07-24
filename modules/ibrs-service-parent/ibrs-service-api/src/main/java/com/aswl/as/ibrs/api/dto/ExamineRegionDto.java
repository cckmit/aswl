package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核区域关联表Dto
* @author hfx
* @date 2020-03-19 10:19
*/

@ApiModel(value = "ExamineRegionDto",description = "考核区域关联表Dto")
@Data
public class ExamineRegionDto implements Serializable {

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
    * 区域编码
    */
    @ApiModelProperty(value = "区域编码",name="regionNo")
    private String regionNo;
}
