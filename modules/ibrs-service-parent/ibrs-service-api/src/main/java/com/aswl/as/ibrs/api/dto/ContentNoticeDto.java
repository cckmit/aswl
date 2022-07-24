package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 系统消息表Dto
* @author hfx
* @date 2020-03-11 13:28
*/

@ApiModel(value = "ContentNoticeDto",description = "系统消息表Dto")
@Data
public class ContentNoticeDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 唯一标识
    */
    @ApiModelProperty(value = "唯一标识",name="id")
    private String id;
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
    /**
    * 修改者
    */
    @ApiModelProperty(value = "修改者",name="modifier")
    private String modifier;
    /**
    * 修改时间
    */
    @ApiModelProperty(value = "修改时间",name="modifyDate")
    private Date modifyDate;
    /**
    * 创建者
    */
    @ApiModelProperty(value = "创建者",name="creator")
    private String creator;
    /**
    * 创建时间
    */
    @ApiModelProperty(value = "创建时间",name="createDate")
    private Date createDate;
    /**
    * 删除标记 0:正常;1:删除
    */
    @ApiModelProperty(value = "删除标记 0:正常;1:删除",name="delFlag")
    private Integer delFlag;
}
