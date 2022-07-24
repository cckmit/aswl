package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 系统消息表Entity
* @author hfx
* @date 2020-03-11 13:28
*/

@ApiModel(value = "ContentNotice",description = "系统消息表Entity")
@Data
public class ContentNotice extends BaseEntity<ContentNotice> {

    /**
    * 内容
    */

    @ApiModelProperty(value = "内容",name="content")
    private String content;
    /**
    * 是否已发布 1-已发布，0-未发布
    */

    @ApiModelProperty(value = "是否已发布 1-已发布，0-未发布",name="isRelease")
    private Integer isRelease;
    /**
    * 发布时间
    */

    @ApiModelProperty(value = "发布时间",name="releaseTime")
    private Date releaseTime;
}
