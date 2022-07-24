package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 考核指标库Entity
* @author hfx
* @date 2020-03-19 10:17
*/

@ApiModel(value = "ExamineBaseLib",description = "考核指标库Entity")
@Data
public class ExamineBaseLib extends BaseEntity<ExamineBaseLib> {

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

}
