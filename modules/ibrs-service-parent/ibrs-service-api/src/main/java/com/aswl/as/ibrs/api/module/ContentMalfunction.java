package com.aswl.as.ibrs.api.module;
import java.util.Date;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 常见故障表Entity
* @author hfx
* @date 2020-03-06 10:20
*/

@ApiModel(value = "ContentMalfunction",description = "常见故障表Entity")
@Data
public class ContentMalfunction extends BaseEntity<ContentMalfunction> {

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
     * 本字段只作为查询时传递条件使用
     */
    @ApiModelProperty(value = "本字段只作为查询时传递条件使用", name = "queryStr")
    private String queryStr;

    /**
     * 现在的问题热度
     */
    @ApiModelProperty(value = "现在的问题热度", name = "heatCount")
    private Integer heatCount;

    /**
     * 最终的问题热度,从一开始就随机出来
     */
    @ApiModelProperty(value = "最终的问题热度", name = "heatMaxCount")
    private Integer heatMaxCount;

    /**
     * 最后的问题热度增加时间
     */
    @ApiModelProperty(value = "最后的问题热度增加时间", name = "lastHeatTime")
    private Date lastHeatTime;

    /**
     * 其它链接，非本系统的链接
     */
    @ApiModelProperty(value = "其它链接，非本系统的链接", name = "otherUrl")
    private String otherUrl;

}
