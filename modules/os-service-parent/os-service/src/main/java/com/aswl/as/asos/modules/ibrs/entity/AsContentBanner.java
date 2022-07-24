package com.aswl.as.asos.modules.ibrs.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * banner管理
 * </p>
 *
 * @author hfx
 * @since 2020-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsContentBanner对象", description="banner管理")
public class AsContentBanner implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "标题从其它表里面查询出来，不用保存到banner，该属性只作为传递数据使用，没有保存在数据库,如果是外网链接的话，现在也保存到数据库")
//    @TableField(exist = false)
    private String title;

    @ApiModelProperty(value = "banner图")
    private String bannerImage;

    @ApiModelProperty(value = "显示位置")
    private String displayPosition;

    @ApiModelProperty(value = "链接")
    private String contentLink;

    @ApiModelProperty(value = "是否已发布 1-已发布，0为未发布")
    private Integer isRelease;

    @ApiModelProperty(value = "点击量")
    private Integer clicks;

    @ApiModelProperty(value = "修改者")
    private String modifier;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime modifyDate;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @ApiModelProperty(value = "添加时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "删除标记 0:正常;1:删除")
    private Integer delFlag;

    @ApiModelProperty(value = "类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类，4-banner的contentLink放外网链接的种类，前端和APP需要区分")
    private Integer category;

    @ApiModelProperty(value = "内容的唯一标识，有产品中心，常见故障，行业咨询的表的id，下面的category区分，如果category为4，这个保存的是banner自身的Id")
    private String contentId;


}
