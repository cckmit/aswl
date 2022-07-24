package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 常见故障表Dto
* @author hfx
* @date 2020-03-06 10:20
*/

@ApiModel(value = "ContentMalfunctionDto",description = "常见故障表Dto")
@Data
public class ContentMalfunctionDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识
     */
    @ApiModelProperty(value = "唯一标识", name = "id")
    private String id;
    /**
     * 内容分类(category_tree表)
     */
    @ApiModelProperty(value = "内容分类(category_tree表)", name = "categoryTreeId")
    private String categoryTreeId;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", name = "title")
    private String title;
    /**
     * 作者
     */
    @ApiModelProperty(value = "作者", name = "author")
    private String author;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序", name = "sort")
    private Integer sort;
    /**
     * 橱窗内容
     */
    @ApiModelProperty(value = "橱窗内容", name = "showcaseText")
    private String showcaseText;
    /**
     * 修改者
     */
    @ApiModelProperty(value = "修改者", name = "modifier")
    private String modifier;
    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间", name = "modifyDate")
    private Date modifyDate;
    /**
     * 创建者
     */
    @ApiModelProperty(value = "创建者", name = "creator")
    private String creator;
    /**
     * 添加时间
     */
    @ApiModelProperty(value = "添加时间", name = "createDate")
    private Date createDate;
    /**
     * 删除标记 0:正常;1:删除
     */
    @ApiModelProperty(value = "删除标记 0:正常;1:删除", name = "delFlag")
    private Integer delFlag;
    /**
     * 是否展示到橱窗
     */
    @ApiModelProperty(value = "是否展示到橱窗", name = "isShowcase")
    private Integer isShowcase;
    /**
     * 点击量
     */
    @ApiModelProperty(value = "点击量", name = "clicks")
    private Integer clicks;
    /**
     * 点赞量
     */
    @ApiModelProperty(value = "点赞量", name = "likes")
    private Integer likes;
    /**
     * 是否已发布 1-已发布，0为未发布
     */
    @ApiModelProperty(value = "是否已发布 1-已发布，0为未发布", name = "isRelease")
    private Integer isRelease;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", name = "content")
    private String content;

    /**
     * 现在的问题热度
     */
    @ApiModelProperty(value = "现在的问题热度", name = "heatCount")
    private Integer heatCount;

    /**
     * 最终的问题热度
     */
    @ApiModelProperty(value = "最终的问题热度", name = "heatMaxCount")
    private Integer heatMaxCount;

    /**
     * 最后的问题热度增加时间
     */
    @ApiModelProperty(value = "最后的问题热度增加时间", name = "lastHeatTime")
    private Date lastHeatTime;

}
