package com.aswl.as.asos.modules.ibrs.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 通用分类树表，普通的树可以直接用一个type辨别来获取
 * </p>
 *
 * @author hfx
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="AsCategoryTree对象", description="通用分类树表，普通的树可以直接用一个type辨别来获取")
public class AsCategoryTree implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    @TableId(type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "类别，1为产品中心的分类，2-常见故障的分类，3-行业资讯的分类")
    @NotNull(message="类别不能为空")
    private Integer category;

    @ApiModelProperty(value = "名称")
    private String nodeName;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "节点编码")
    private String nodeCode;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty(value = "删除标记 0:正常;1:删除")
    private Boolean delFlag;

    @ApiModelProperty(value = "修改者")
    private String modifier;

    @ApiModelProperty(value = "最后修改时间")
    private LocalDateTime modifyDate;

}
