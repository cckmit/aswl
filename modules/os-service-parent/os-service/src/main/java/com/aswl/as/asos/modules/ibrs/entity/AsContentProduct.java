package com.aswl.as.asos.modules.ibrs.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

/**
 * <p>
 * 产品中心表
 * </p>
 *
 * @author hfx
 * @since 2020-03-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsContentProduct对象", description="产品中心表")
public class AsContentProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "内容分类(category_tree表)")
    private String categoryTreeId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "封面")
    private String coverImage;

    @ApiModelProperty(value = "橱窗内容")
    private String showcaseText;

    @ApiModelProperty(value = "内容")
//    @TableField(jdbcType= JdbcType.CLOB)
    private String content;

    @ApiModelProperty(value = "修改者")
    private String modifier;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime modifyDate;

    @ApiModelProperty(value = "创建者")
    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @ApiModelProperty(value = "添加时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "删除标记 0:正常;1:删除")
    private Integer delFlag;

    /**
     * 是否显示到橱窗，1-是，0-不是
     */
    @ApiModelProperty(value = "是否显示到橱窗，1-是，0-不是")
    private Integer isShowcase;

    /**
     * 点击量
     */
    @ApiModelProperty(value = "点击量")
    private Integer clicks ;

    /**
     * 其它链接，非本系统的链接
     */
    @ApiModelProperty(value = "其它链接，非本系统的链接")
    private String otherUrl;

    /**
     * 点赞量
     */
    @ApiModelProperty(value = "点赞量")
    private Integer likes;

    /**
     * 是否已发布 1-已发布，0为未发布
     */
    @ApiModelProperty(value = "是否已发布 1-已发布，0为未发布")
    private Integer isRelease;



    @ApiModelProperty(value = "内容分类名称,只作传递参数和显示使用，不保存到数据库")
    @TableField(exist=false)
    private String nodeName;

    @ApiModelProperty(value = "链接,只作传递参数和显示使用，不保存到数据库")
    @TableField(exist=false)
    private String url;



}
