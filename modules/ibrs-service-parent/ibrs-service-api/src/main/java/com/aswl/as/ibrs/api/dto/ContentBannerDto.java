package com.aswl.as.ibrs.api.dto;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
*
* banner管理表Dto
* @author hfx
* @date 2020-03-11 13:28
*/

@ApiModel(value = "ContentBannerDto",description = "banner管理表Dto")
@Data
public class ContentBannerDto implements Serializable {

private static final long serialVersionUID = 1L;

    /**
    * 唯一标识
    */
    @ApiModelProperty(value = "唯一标识",name="id")
    private String id;

    /**
    * banner图
    */
    @ApiModelProperty(value = "banner图",name="bannerImage")
    private String bannerImage;
    /**
    * 显示位置
    */
    @ApiModelProperty(value = "显示位置",name="displayPosition")
    private String displayPosition;
    /**
    * 链接
    */
    @ApiModelProperty(value = "链接",name="contentLink")
    private String contentLink;
    /**
    * 是否已发布 1-已发布，0为未发布
    */
    @ApiModelProperty(value = "是否已发布 1-已发布，0为未发布",name="isRelease")
    private Integer isRelease;
    /**
    * 点击量
    */
    @ApiModelProperty(value = "点击量",name="clicks")
    private Integer clicks;

    /**
     * 点赞量
     */
    @ApiModelProperty(value = "点赞量", name = "likes")
    private Integer likes;

    /**
    * 修改者
    */
    @ApiModelProperty(value = "修改者",name="modifier")
    private String modifier;
    /**
    * 最后修改时间
    */
    @ApiModelProperty(value = "最后修改时间",name="modifyDate")
    private Date modifyDate;
    /**
    * 创建者
    */
    @ApiModelProperty(value = "创建者",name="creator")
    private String creator;
    /**
    * 添加时间
    */
    @ApiModelProperty(value = "添加时间",name="createDate")
    private Date createDate;
    /**
    * 排序
    */
    @ApiModelProperty(value = "排序",name="sort")
    private Integer sort;


    /**
     * 类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类
     */

    @ApiModelProperty(value = "类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类,4-banner的contentLink放外网链接的种类",name="category")
    private Integer category;

    /**
     * 内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分
     */

    @ApiModelProperty(value = "内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分，如果category为4，这个保存的是banner自身的Id",name="contentId")
    private String contentId;

    /**
    * 删除标记 0:正常;1:删除
    */
    @ApiModelProperty(value = "删除标记 0:正常;1:删除",name="delFlag")
    private Integer delFlag;

    /**
     * 标题，标题从其它表里面查询出来，不用保存到banner，该属性只作为传递数据使用，没有保存在数据库
     */
    @ApiModelProperty(value = "标题",name="title")
    private String title;

}
