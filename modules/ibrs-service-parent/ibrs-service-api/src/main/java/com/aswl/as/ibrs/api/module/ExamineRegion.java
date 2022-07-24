package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核区域关联表Entity
* @author hfx
* @date 2020-03-19 10:19
*/

@ApiModel(value = "ExamineRegion",description = "考核区域关联表Entity")
@Data
public class ExamineRegion extends BaseEntity<ExamineRegion> {

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
