package com.aswl.as.ibrs.api.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用户点赞表Dto
 *
 * @author hfx
 * @date 2020-03-16 10:26
 */

@ApiModel(value = "ContentLikeDto", description = "用户点赞表Dto")
@Data
public class ContentLikeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识", name = "id")
    private String id;
    /**
     * 类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类
     */
    @ApiModelProperty(value = "类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类，4-banner如果链接是其它的链接的情况", name = "category")
    private Integer category;
    /**
     * 内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分
     */
    @ApiModelProperty(value = "内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分，如果category为4，就是banner表的id", name = "contentId")
    private String contentId;
    /**
     * 创建者（如果取消点赞，直接删除就是，不留del_flag）
     */
    @ApiModelProperty(value = "创建者（如果取消点赞，直接删除就是，不留del_flag）", name = "creator")
    private String creator;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", name = "createDate")
    private Date createDate;
}
