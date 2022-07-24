package com.aswl.as.ibrs.api.module;
import com.aswl.as.common.core.persistence.BaseEntity;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* 产品中心表Entity
* @author hfx
* @date 2020-03-06 10:28
*/

@ApiModel(value = "ContentProduct",description = "产品中心表Entity")
@Data
public class ContentProduct extends BaseEntity<ContentProduct> {

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
     * 排序
     */

    @ApiModelProperty(value = "排序", name = "sort")
    private Integer sort;
    /**
     * 封面
     */

    @ApiModelProperty(value = "封面", name = "coverImage")
    private String coverImage;
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
     * 其它链接，非本系统的链接
     */
    @ApiModelProperty(value = "其它链接，非本系统的链接", name = "otherUrl")
    private String otherUrl;

}
